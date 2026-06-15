package com.openassist.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.openassist.core.UserFacingErrors
import com.openassist.data.ai.AiMode
import com.openassist.data.ai.HybridProvider
import com.openassist.data.ai.LocalLlmProvider
import com.openassist.data.local.SecureStorage
import com.openassist.data.openrouter.OpenRouterMessage
import com.openassist.data.openrouter.OpenRouterRepository
import com.openassist.tools.ToolEngine
import com.openassist.tools.ToolRequest
import com.openassist.ui.chat.ChatMessage
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonObject
import java.util.Locale
import kotlinx.serialization.json.jsonPrimitive

data class ChatUiState(
    /** Messages shown in the UI (user + final assistant text only). */
    val messages: List<ChatMessage> = emptyList(),
    /**
     * Full conversation as sent to the API — includes assistant tool-call
     * turns and tool-result turns that are invisible in the UI.
     */
    val apiHistory: List<OpenRouterMessage> = emptyList(),
    val loading: Boolean = false,
    val error: String? = null,
    val pendingToolRequest: ToolRequest? = null,
    val pendingToolSummary: String? = null,
)

class ChatViewModel(
    private val storage: SecureStorage,
    private val toolEngine: ToolEngine,
    private val repository: OpenRouterRepository = OpenRouterRepository(),
    private val localProvider: LocalLlmProvider = LocalLlmProvider(),
    private val hybridProvider: HybridProvider = HybridProvider(),
) : ViewModel() {

    private val _state = MutableStateFlow(ChatUiState())
    val state: StateFlow<ChatUiState> = _state.asStateFlow()

    fun send(content: String) {
        if (content.isBlank()) return
        val trimmed = content.trim()

        // Optimistically append the user bubble.
        val userDisplay = ChatMessage(role = "user", content = trimmed)
        val userApiMsg  = OpenRouterMessage(role = "user", content = trimmed)
        val nextMessages    = _state.value.messages + userDisplay
        val nextApiHistory  = _state.value.apiHistory + userApiMsg

        _state.value = _state.value.copy(
            messages   = nextMessages,
            apiHistory = nextApiHistory,
            loading    = true,
            error      = null,
        )

        detectDeviceToolRequest(trimmed)?.let { request ->
            val summary = buildConfirmationSummary(
                request = request,
                safetyMessage = "OpenAssist recognized this as a device action. Review the details, then approve to execute it with Android permissions.",
            )
            _state.value = _state.value.copy(
                messages = nextMessages + ChatMessage(role = "assistant", content = summary),
                pendingToolRequest = request,
                pendingToolSummary = summary,
                loading = false,
                error = null,
            )
            return
        }

        viewModelScope.launch {
            runCatching { runAgentLoop(nextApiHistory) }
                .onSuccess { (newHistory, reply) ->
                    _state.value = _state.value.copy(
                        messages = _state.value.messages + ChatMessage(
                            role    = "assistant",
                            content = reply.ifBlank { "(No response)" },
                        ),
                        apiHistory = newHistory,
                        loading    = false,
                    )
                }
                .onFailure { err ->
                    _state.value = _state.value.copy(loading = false, error = UserFacingErrors.chat(err))
                }
        }
    }


    fun confirmPendingAction() {
        val pending = _state.value.pendingToolRequest ?: return
        _state.value = _state.value.copy(loading = true, error = null)

        viewModelScope.launch {
            runCatching { toolEngine.execute(pending, confirmed = true) }
                .onSuccess { result ->
                    _state.value = _state.value.copy(
                        messages = _state.value.messages + ChatMessage(
                            role = "tool",
                            content = result.output,
                        ),
                        apiHistory = _state.value.apiHistory + OpenRouterMessage(
                            role = "tool",
                            content = result.output,
                        ),
                        pendingToolRequest = null,
                        pendingToolSummary = null,
                        loading = false,
                    )
                }
                .onFailure { err ->
                    _state.value = _state.value.copy(loading = false, error = UserFacingErrors.chat(err))
                }
        }
    }

    fun cancelPendingAction() {
        val pending = _state.value.pendingToolRequest ?: return
        val cancelMessage = "Canceled pending action: ${pending.name}."
        _state.value = _state.value.copy(
            messages = _state.value.messages + ChatMessage(role = "assistant", content = cancelMessage),
            apiHistory = _state.value.apiHistory + OpenRouterMessage(role = "assistant", content = cancelMessage),
            pendingToolRequest = null,
            pendingToolSummary = null,
            loading = false,
        )
    }
    // ── Agent loop ────────────────────────────────────────────────────────────

    /**
     * Drives the tool-calling loop:
     *
     * 1. Send messages + tool definitions to the API.
     * 2. If the model wants to call tools → execute them, append results, repeat.
     * 3. Stop when the model emits a plain text response (no tool_calls).
     *
     * Guards against runaway loops with [maxIterations].
     */
    private suspend fun runAgentLoop(
        initialHistory: List<OpenRouterMessage>,
    ): Pair<List<OpenRouterMessage>, String> {
        when (storage.aiMode.value) {
            AiMode.Local -> {
                val prompt = initialHistory.lastOrNull { it.role == "user" }?.content.orEmpty()
                val reply = localProvider.generate(prompt)
                return Pair(initialHistory + OpenRouterMessage(role = "assistant", content = reply), reply)
            }
            AiMode.Hybrid -> {
                val prompt = initialHistory.lastOrNull { it.role == "user" }?.content.orEmpty()
                if (prompt.length <= storage.hybridThreshold.value) {
                    val reply = hybridProvider.generate(prompt)
                    return Pair(initialHistory + OpenRouterMessage(role = "assistant", content = reply), reply)
                }
            }
            AiMode.Cloud -> Unit
        }

        require(storage.apiKey.value.isNotBlank()) { "OpenRouter API key is required for Cloud AI or complex Hybrid AI requests." }

        val tools = toolEngine.toolDefinitions()
        var history = initialHistory
        var iterations = 0
        val maxIterations = 10

        while (iterations++ < maxIterations) {
            val choice = repository.chat(
                apiKey   = storage.apiKey.value,
                model    = storage.model.value,
                messages = history,
                tools    = tools,
            )
            val assistantMsg = choice.message

            if (!assistantMsg.toolCalls.isNullOrEmpty()) {
                // ── Tool-call turn ────────────────────────────────────────────
                history = history + assistantMsg  // keep assistant msg in history

                val toolResultMsgs = assistantMsg.toolCalls.map { toolCall ->
                    val args   = parseArguments(toolCall.function.arguments)
                    val request = ToolRequest(toolCall.function.name, args)
                    val result = toolEngine.execute(
                        request   = request,
                        confirmed = false,
                    )
                    if (result.requiresConfirmation) {
                        val summary = buildConfirmationSummary(request, result.output)
                        _state.value = _state.value.copy(
                            pendingToolRequest = request,
                            pendingToolSummary = summary,
                        )
                        return Pair(
                            history,
                            summary,
                        )
                    }
                    OpenRouterMessage(
                        role       = "tool",
                        content    = result.output,
                        toolCallId = toolCall.id,
                    )
                }
                history = history + toolResultMsgs
            } else {
                // ── Final text response ───────────────────────────────────────
                history = history + assistantMsg
                return Pair(history, assistantMsg.content.orEmpty())
            }
        }

        // Should only happen if the model calls tools non-stop for maxIterations.
        return Pair(history, "Agent reached the maximum number of tool-call iterations.")
    }

    // ── Helpers ───────────────────────────────────────────────────────────────


    private fun detectDeviceToolRequest(content: String): ToolRequest? {
        val normalized = content.trim()
        parseAlarmRequest(normalized)?.let { return it }
        parseSmsRequest(normalized)?.let { return it }
        parseCallRequest(normalized)?.let { return it }
        return null
    }

    private fun parseAlarmRequest(content: String): ToolRequest? {
        val lower = content.lowercase(Locale.US)
        if (!lower.contains("alarm")) return null
        val timeMatch = Regex("""\b([01]?\d|2[0-3])[:.]([0-5]\d)\b""").find(content) ?: return null
        val label = content.substringAfter(timeMatch.value, "").replace(Regex("""^(for|called|named|label(?:ed)?|to)\s+""", RegexOption.IGNORE_CASE), "").trim()
        return ToolRequest(
            name = "set_alarm",
            arguments = buildMap {
                put("hour", timeMatch.groupValues[1].toInt().toString())
                put("minute", timeMatch.groupValues[2].toInt().toString())
                if (label.isNotBlank()) put("label", label)
            },
        )
    }

    private fun parseSmsRequest(content: String): ToolRequest? {
        val match = Regex("""(?i)\b(?:send\s+)?(?:sms|text)\s+(?:to\s+)?([+()0-9 .-]{3,})\s*[:,-]?\s+(.+)""").find(content) ?: return null
        val phone = match.groupValues[1].trim()
        val message = match.groupValues[2].trim()
        if (message.isBlank()) return null
        return ToolRequest("send_sms", mapOf("phone_number" to phone, "message" to message))
    }

    private fun parseCallRequest(content: String): ToolRequest? {
        val match = Regex("""(?i)\b(?:call|phone|dial)\s+([+()0-9 .-]{3,})\s*$""").find(content) ?: return null
        return ToolRequest("make_call", mapOf("phone_number" to match.groupValues[1].trim()))
    }

    private fun buildConfirmationSummary(request: ToolRequest, safetyMessage: String): String {
        val details = request.arguments.entries.joinToString("\n") { (key, value) ->
            "${key.replace('_', ' ').replaceFirstChar { it.uppercase() }}: $value"
        }.ifBlank { "No extra details provided." }
        return """
            Action Summary
            Tool: ${request.name}
            $details

            $safetyMessage

            Approve to continue, or cancel to keep OpenAssist from performing this action.
        """.trimIndent()
    }
    /**
     * Parses the JSON argument string emitted by the model into a flat
     * [Map<String, String>].  Non-primitive values are serialised to their
     * JSON representation so tools always receive a usable string.
     */
    private fun parseArguments(jsonString: String): Map<String, String> = try {
        Json.decodeFromString<JsonObject>(jsonString).mapValues { (_, v) ->
            try { v.jsonPrimitive.content } catch (_: Exception) { v.toString() }
        }
    } catch (_: Exception) {
        emptyMap()
    }
}
