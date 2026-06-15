package com.openassist.data.openrouter

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonObject

// ── Tool definitions (sent to the API) ──────────────────────────────────────

@Serializable
data class ToolFunction(
    val name: String,
    val description: String,
    val parameters: JsonObject,
)

@Serializable
data class ToolDefinition(
    val type: String = "function",
    val function: ToolFunction,
)

// ── Tool-call response fields (received from the API) ────────────────────────

@Serializable
data class ToolCallFunction(
    val name: String,
    val arguments: String, // raw JSON string, e.g. {"hour":7,"minute":30}
)

@Serializable
data class ToolCall(
    val id: String,
    val type: String = "function",
    val function: ToolCallFunction,
)

// ── Messages ─────────────────────────────────────────────────────────────────

/** Unified message type for all roles: user, assistant, system, tool. */
@Serializable
data class OpenRouterMessage(
    val role: String,
    /** Null when the assistant message contains only tool_calls. */
    val content: String? = null,
    /** Present on assistant messages that invoke one or more tools. */
    @SerialName("tool_calls") val toolCalls: List<ToolCall>? = null,
    /** Present on role="tool" messages; matches ToolCall.id. */
    @SerialName("tool_call_id") val toolCallId: String? = null,
)

// ── Request / Response ───────────────────────────────────────────────────────

@Serializable
data class ChatRequest(
    val model: String,
    val messages: List<OpenRouterMessage>,
    /** Omitted (null) when there are no tools so the API behaves normally. */
    val tools: List<ToolDefinition>? = null,
)

@Serializable
data class ChatResponse(val choices: List<Choice> = emptyList())

@Serializable
data class Choice(
    val message: OpenRouterMessage,
    @SerialName("finish_reason") val finishReason: String? = null,
)

// ── Model catalogue ──────────────────────────────────────────────────────────

@Serializable
data class ModelsResponse(val data: List<ModelInfo> = emptyList())

@Serializable
data class ModelInfo(
    val id: String,
    val name: String? = null,
    @SerialName("context_length") val contextLength: Int? = null,
)

@Serializable
data class OpenRouterModelOption(
    val id: String,
    val displayName: String,
    val provider: String,
    val contextLength: Int? = null,
    val free: Boolean = false,
    val strengths: List<String> = emptyList(),
)

object OpenRouterFreeModelCatalog {
    val freeModels = listOf(
        OpenRouterModelOption("deepseek/deepseek-r1:free", "DeepSeek R1 Free", "DeepSeek", 163_840, true, listOf("Reasoning", "Coding", "Long context")),
        OpenRouterModelOption("deepseek/deepseek-chat-v3-0324:free", "DeepSeek Chat V3 Free", "DeepSeek", 163_840, true, listOf("General chat", "Fast drafts")),
        OpenRouterModelOption("meta-llama/llama-3.3-70b-instruct:free", "Llama 3.3 70B Instruct Free", "Meta", 131_072, true, listOf("Open model", "Instruction following")),
        OpenRouterModelOption("google/gemini-2.0-flash-exp:free", "Gemini 2.0 Flash Experimental Free", "Google", 1_048_576, true, listOf("Fast", "Large context", "Multimodal-ready")),
        OpenRouterModelOption("qwen/qwen-2.5-coder-32b-instruct:free", "Qwen 2.5 Coder 32B Free", "Qwen", 32_768, true, listOf("Code", "Refactors", "Debugging")),
        OpenRouterModelOption("mistralai/mistral-7b-instruct:free", "Mistral 7B Instruct Free", "Mistral AI", 32_768, true, listOf("Lightweight", "Quick answers")),
    )

    fun byId(id: String): OpenRouterModelOption? = freeModels.firstOrNull { it.id == id }
}
