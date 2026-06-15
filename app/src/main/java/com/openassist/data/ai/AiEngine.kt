package com.openassist.data.ai

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File

interface AiProvider {
    val name: String
    suspend fun generate(prompt: String): String
}

data class LocalLlmConfig(
    val modelFile: String = "tinyllama-1.1b-chat.Q4_K_M.gguf",
    val contextTokens: Int = 2048,
    val threads: Int = 4,
    val temperature: Float = 0.7f,
)

data class LocalLlmRequest(val prompt: String, val systemPrompt: String = "You are OpenAssist running locally on Android.")

data class LocalLlmResult(val text: String, val provider: String, val modelPath: String, val offline: Boolean = true)

data class OpenRouterProviderConfig(
    val apiKey: String,
    val model: String,
)

class OpenRouterProvider(
    private val repository: com.openassist.data.openrouter.OpenRouterRepository = com.openassist.data.openrouter.OpenRouterRepository(),
    private val config: OpenRouterProviderConfig? = null,
) : AiProvider {
    override val name = "OpenRouter Provider"

    override suspend fun generate(prompt: String): String {
        val current = requireNotNull(config) { "OpenRouter provider requires an API key and model before generation." }
        val choice = repository.chat(
            apiKey = current.apiKey,
            model = current.model,
            messages = listOf(com.openassist.data.openrouter.OpenRouterMessage(role = "user", content = prompt)),
        )
        return choice.message.content.orEmpty().ifBlank { "OpenRouter returned an empty response." }
    }
}

class LocalLlmProvider(
    private val config: LocalLlmConfig = LocalLlmConfig(),
    private val modelRoot: File = File("OpenAssist/models"),
    private val bridge: LlamaCppBridge = LlamaCppBridge(),
) : AiProvider {
    override val name = "Local LLM Provider"

    override suspend fun generate(prompt: String): String = complete(LocalLlmRequest(prompt)).text

    suspend fun complete(request: LocalLlmRequest): LocalLlmResult = withContext(Dispatchers.Default) {
        bridge.complete(modelRoot, config, request)
    }

    fun modelPath(modelFile: String = config.modelFile): String = File(modelRoot, modelFile).path

    fun isModelInstalled(modelFile: String = config.modelFile): Boolean = File(modelRoot, modelFile).exists()

    fun llamaCppCommand(prompt: String): List<String> = listOf(
        "llama-cli",
        "-m", modelPath(),
        "-c", config.contextTokens.toString(),
        "-t", config.threads.toString(),
        "--temp", config.temperature.toString(),
        "-p", prompt,
    )
}

class HybridProvider(
    private val localProvider: LocalLlmProvider = LocalLlmProvider(),
    private val cloudProvider: OpenRouterProvider = OpenRouterProvider(),
) : AiProvider {
    override val name = "Hybrid Provider"
    override suspend fun generate(prompt: String): String =
        if (prompt.length < 240) localProvider.generate(prompt) else cloudProvider.generate(prompt)
}
