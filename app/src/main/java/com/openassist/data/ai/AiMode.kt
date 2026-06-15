package com.openassist.data.ai

enum class AiMode(val label: String, val description: String) {
    Cloud("Cloud AI", "Uses OpenRouter with your API key and any OpenRouter model."),
    Local("Local AI", "Runs downloaded GGUF models fully offline on this Android device."),
    Hybrid("Hybrid AI", "Routes simple prompts locally and complex prompts to OpenRouter."),
}
