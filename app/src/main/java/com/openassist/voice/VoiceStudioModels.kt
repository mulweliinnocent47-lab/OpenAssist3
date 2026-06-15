package com.openassist.voice

/**
 * v7 Voice Studio architecture: Speech Input -> AI Model -> Voice Engine -> Selected Voice Provider -> Audio Output.
 */
enum class VoiceProviderType(val label: String, val isCloud: Boolean, val requiresApiKey: Boolean) {
    AndroidTts("Android TTS", isCloud = false, requiresApiKey = false),
    OpenAiTts("OpenAI TTS", isCloud = true, requiresApiKey = true),
    ElevenLabs("ElevenLabs", isCloud = true, requiresApiKey = true),
    GoogleNeuralTts("Google Neural TTS", isCloud = true, requiresApiKey = true),
    AzureNeuralTts("Azure Neural TTS", isCloud = true, requiresApiKey = true),
    LocalDownloadable("Local Downloadable Voices", isCloud = false, requiresApiKey = false),
}

enum class VoiceGender(val label: String) {
    Male("Male"),
    Female("Female"),
    Neutral("Neutral"),
}

enum class VoiceEmotion(val label: String) {
    Neutral("Neutral"),
    Friendly("Friendly"),
    Calm("Calm"),
    Excited("Excited"),
    Professional("Professional"),
    Storytelling("Storytelling"),
}

data class VoiceProfile(
    val id: String,
    val name: String,
    val provider: VoiceProviderType,
    val language: String,
    val gender: VoiceGender,
    val supportsOffline: Boolean,
    val supportsEmotion: Boolean,
    val previewText: String = "Hello, I am OpenAssist. This is your voice preview.",
)

data class VoiceStudioSettings(
    val selectedProvider: VoiceProviderType = VoiceProviderType.AndroidTts,
    val selectedVoiceId: String = "android-default",
    val speed: Float = 1.0f,
    val pitch: Float = 1.0f,
    val emotion: VoiceEmotion = VoiceEmotion.Neutral,
    val language: String = "English (US)",
    val cloudVoicesAllowed: Boolean = false,
) {
    val costAndPrivacySummary: String = if (selectedProvider.isCloud) {
        "Cloud voice: requires user API key, may use paid provider credits, and sends text to the selected provider."
    } else {
        "Local voice: uses Android TTS or downloaded voices by default for better privacy and cost control."
    }
}

data class VoiceDownload(
    val voice: VoiceProfile,
    val sizeMb: Int,
    val status: String,
)

object VoiceStudioCatalog {
    val architecture = listOf("Speech Input", "AI Model", "Voice Engine", "Selected Voice Provider", "Audio Output")

    val providers = VoiceProviderType.values().toList()

    val marketplaceVoices = listOf(
        VoiceProfile("android-default", "Android System Default", VoiceProviderType.AndroidTts, "English (US)", VoiceGender.Neutral, supportsOffline = true, supportsEmotion = false),
        VoiceProfile("android-female-us", "Android US Female", VoiceProviderType.AndroidTts, "English (US)", VoiceGender.Female, supportsOffline = true, supportsEmotion = false),
        VoiceProfile("openai-alloy", "Alloy", VoiceProviderType.OpenAiTts, "English (US)", VoiceGender.Neutral, supportsOffline = false, supportsEmotion = true),
        VoiceProfile("eleven-rachel", "Rachel", VoiceProviderType.ElevenLabs, "English (US)", VoiceGender.Female, supportsOffline = false, supportsEmotion = true),
        VoiceProfile("google-neural-male", "Google Neural Male", VoiceProviderType.GoogleNeuralTts, "English (US)", VoiceGender.Male, supportsOffline = false, supportsEmotion = true),
        VoiceProfile("azure-aria", "Azure Aria", VoiceProviderType.AzureNeuralTts, "English (US)", VoiceGender.Female, supportsOffline = false, supportsEmotion = true),
        VoiceProfile("local-nova", "Local Nova", VoiceProviderType.LocalDownloadable, "English (US)", VoiceGender.Female, supportsOffline = true, supportsEmotion = true),
        VoiceProfile("local-orion", "Local Orion", VoiceProviderType.LocalDownloadable, "Spanish (ES)", VoiceGender.Male, supportsOffline = true, supportsEmotion = true),
    )

    val downloads = listOf(
        VoiceDownload(marketplaceVoices[6], sizeMb = 142, status = "Available"),
        VoiceDownload(marketplaceVoices[7], sizeMb = 156, status = "Available"),
        VoiceDownload(marketplaceVoices[0], sizeMb = 0, status = "Installed"),
    )

    val defaultSettings = VoiceStudioSettings()

    val supportedLanguages = listOf("English (US)", "English (UK)", "Spanish (ES)", "French (FR)", "German (DE)", "Japanese (JP)")
}

data class VoicePreviewRequest(val text: String, val settings: VoiceStudioSettings = VoiceStudioCatalog.defaultSettings)

data class VoicePreviewResult(val provider: VoiceProviderType, val voiceId: String, val playbackPlan: String, val privacy: String)

class VoiceEngine {
    fun preview(request: VoicePreviewRequest): VoicePreviewResult {
        val safeText = request.text.ifBlank { VoiceStudioCatalog.marketplaceVoices.first().previewText }.take(280)
        val settings = request.settings
        val privacy = if (settings.selectedProvider.isCloud && settings.cloudVoicesAllowed) {
            "Cloud synthesis enabled with user-managed provider credentials."
        } else {
            "Local Android/downloaded voice synthesis; text stays on device."
        }
        return VoicePreviewResult(
            provider = settings.selectedProvider,
            voiceId = settings.selectedVoiceId,
            playbackPlan = "Speak ${safeText.length} characters at ${settings.speed}x speed, ${settings.pitch}x pitch, ${settings.emotion.label} emotion.",
            privacy = privacy,
        )
    }
}
