package com.openassist.data.ai

data class LocalModelInfo(
    val id: String,
    val name: String,
    val version: String,
    val description: String,
    val fileName: String,
    val downloadSize: String,
    val requiredRam: String,
    val installed: Boolean = false,
    val active: Boolean = false,
    val verified: Boolean = false,
)

object LocalModelCatalog {
    val recommended = listOf(
        LocalModelInfo("smollm2-135m", "SmolLM2 135M", "GGUF Q4", "Tiny offline assistant for quick replies.", "smollm2-135m-q4.gguf", "92 MB", "512 MB"),
        LocalModelInfo("smollm2-360m", "SmolLM2 360M", "GGUF Q4", "Balanced small model for lightweight chat.", "smollm2-360m-q4.gguf", "230 MB", "1 GB"),
        LocalModelInfo("smollm2-1.7b", "SmolLM2 1.7B", "GGUF Q4", "Better reasoning while staying phone friendly.", "smollm2-1.7b-q4.gguf", "1.1 GB", "3 GB"),
        LocalModelInfo("gemma-2b", "Gemma 2B", "GGUF Q4", "Google Gemma class model for local workflows.", "gemma-2b-q4.gguf", "1.6 GB", "4 GB"),
        LocalModelInfo("gemma-3-4b", "Gemma 3 4B", "GGUF Q4", "Higher quality local model for newer devices.", "gemma-3-4b-q4.gguf", "2.8 GB", "6 GB"),
        LocalModelInfo("phi-4-mini", "Phi-4 Mini", "GGUF Q4", "Compact reasoning model for hybrid workflows.", "phi-4-mini-q4.gguf", "2.4 GB", "6 GB"),
    )

    val installedDemo = recommended.take(2).mapIndexed { index, model ->
        model.copy(installed = true, active = index == 0, verified = true)
    }
}
