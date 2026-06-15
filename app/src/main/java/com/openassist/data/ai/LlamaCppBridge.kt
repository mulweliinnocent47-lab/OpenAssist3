package com.openassist.data.ai

import java.io.File

interface LlamaRuntime {
    suspend fun generate(modelFile: File, request: LocalLlmRequest, config: LocalLlmConfig): String
}

class LlamaRuntimeUnavailable : LlamaRuntime {
    override suspend fun generate(modelFile: File, request: LocalLlmRequest, config: LocalLlmConfig): String {
        error(
            "Local AI runtime is not installed. Add the llama.cpp Android native runner, place ${modelFile.name} in ${modelFile.parent}, and retry.",
        )
    }
}

class LlamaCppBridge(private val runtime: LlamaRuntime = LlamaRuntimeUnavailable()) {
    suspend fun complete(modelRoot: File, config: LocalLlmConfig, request: LocalLlmRequest): LocalLlmResult {
        val modelFile = File(modelRoot, config.modelFile)
        require(modelFile.exists()) { "Local model not found: ${modelFile.path}. Download the model before using Local AI." }
        val normalized = request.copy(prompt = request.prompt.trim().ifBlank { "Hello" })
        val text = runtime.generate(modelFile, normalized, config)
        return LocalLlmResult(text = text, provider = "llama.cpp", modelPath = modelFile.path, offline = true)
    }
}
