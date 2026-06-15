package com.openassist.ui.voice

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.openassist.ui.navigation.OpenAssistDestination
import com.openassist.ui.navigation.PremiumButton
import com.openassist.ui.navigation.PremiumCard
import com.openassist.ui.navigation.PremiumPage
import com.openassist.ui.navigation.PremiumPill
import com.openassist.ui.navigation.premiumMutedTextColor
import com.openassist.ui.navigation.premiumTextColor
import com.openassist.voice.VoiceEngine
import com.openassist.voice.VoicePreviewRequest
import com.openassist.voice.VoiceStudioCatalog
import com.openassist.voice.VoiceStudioSettings

@Composable
fun VoiceStudioScreen(
    onBack: () -> Unit,
    onMarketplace: () -> Unit,
    onDownloads: () -> Unit,
    onSettings: () -> Unit,
    onPreview: () -> Unit,
) {
    val settings = VoiceStudioCatalog.defaultSettings
    val preview = VoiceEngine().preview(VoicePreviewRequest("Hello from the wired OpenAssist voice engine.", settings))
    PremiumPage(
        title = "Voice Studio",
        subtitle = "Text to speech uses Android TTS by default, with optional premium cloud and downloadable local voices.",
        selected = OpenAssistDestination.VoiceStudio,
        action = { PremiumPill("Back", onClick = onBack) },
    ) {
        PremiumCard(selected = true) {
            Text("Voice architecture", color = premiumTextColor(), fontWeight = FontWeight.ExtraBold)
            Text(VoiceStudioCatalog.architecture.joinToString("  ↓  "), color = premiumMutedTextColor())
        }
        Spacer(Modifier.height(12.dp))
        PremiumCard {
            Text("Default voice engine", color = premiumTextColor(), fontWeight = FontWeight.ExtraBold)
            Text(settings.selectedProvider.label, color = premiumTextColor(), fontWeight = FontWeight.Bold)
            Text(settings.costAndPrivacySummary, color = premiumMutedTextColor())
            Text(preview.playbackPlan, color = premiumMutedTextColor())
            Text(preview.privacy, color = premiumMutedTextColor())
        }
        Spacer(Modifier.height(12.dp))
        Row(Modifier.fillMaxWidth()) {
            PremiumButton("Preview") { onPreview() }
            Spacer(Modifier.width(12.dp))
            PremiumButton("Settings") { onSettings() }
        }
        Spacer(Modifier.height(12.dp))
        Row(Modifier.fillMaxWidth()) {
            PremiumButton("Marketplace") { onMarketplace() }
            Spacer(Modifier.width(12.dp))
            PremiumButton("Downloads") { onDownloads() }
        }
        Spacer(Modifier.height(12.dp))
        PremiumCard {
            Text("Provider support", color = premiumTextColor(), fontWeight = FontWeight.ExtraBold)
            VoiceStudioCatalog.providers.forEach { provider ->
                val mode = if (provider.isCloud) "Cloud" else "Local/offline"
                val key = if (provider.requiresApiKey) "BYO API key" else "No API key"
                Text("${provider.label} • $mode • $key", color = premiumMutedTextColor())
            }
        }
    }
}

@Composable
fun VoiceMarketplaceScreen(onBack: () -> Unit, onPreview: () -> Unit, onDownloads: () -> Unit) {
    PremiumPage(
        title = "Voice Marketplace",
        subtitle = "Browse Android TTS, OpenAI, ElevenLabs, Google, Azure, and downloadable local voices.",
        selected = OpenAssistDestination.VoiceStudio,
        action = { PremiumPill("Back", onClick = onBack) },
    ) {
        VoiceStudioCatalog.marketplaceVoices.forEach { voice ->
            PremiumCard(Modifier.padding(vertical = 4.dp), selected = voice.id == VoiceStudioCatalog.defaultSettings.selectedVoiceId) {
                Text(voice.name, color = premiumTextColor(), fontWeight = FontWeight.ExtraBold)
                Text("${voice.provider.label} • ${voice.language} • ${voice.gender.label}", color = premiumMutedTextColor())
                Text("${if (voice.supportsOffline) "Offline" else "Cloud"} • Emotion: ${if (voice.supportsEmotion) "Yes" else "No"}", color = premiumMutedTextColor())
            }
        }
        Spacer(Modifier.height(12.dp))
        Row(Modifier.fillMaxWidth()) {
            PremiumButton("Preview voice") { onPreview() }
            Spacer(Modifier.width(12.dp))
            PremiumButton("Manage downloads") { onDownloads() }
        }
    }
}

@Composable
fun VoiceDownloadsScreen(onBack: () -> Unit) {
    PremiumPage(
        title = "Voice Downloads",
        subtitle = "Download local voices for offline text to speech and private on-device playback.",
        selected = OpenAssistDestination.VoiceStudio,
        action = { PremiumPill("Back", onClick = onBack) },
    ) {
        VoiceStudioCatalog.downloads.forEach { download ->
            PremiumCard(Modifier.padding(vertical = 4.dp), selected = download.status == "Installed") {
                Text(download.voice.name, color = premiumTextColor(), fontWeight = FontWeight.ExtraBold)
                Text("${download.voice.language} • ${download.voice.gender.label} • ${download.sizeMb} MB", color = premiumMutedTextColor())
                Text("Status: ${download.status}", color = premiumMutedTextColor())
            }
        }
    }
}

@Composable
fun VoiceSettingsScreen(onBack: () -> Unit, onPreview: () -> Unit) {
    val settings = VoiceStudioSettings()
    PremiumPage(
        title = "Voice Settings",
        subtitle = "Control speed, pitch, emotion, language, local/cloud access, and premium provider API keys.",
        selected = OpenAssistDestination.VoiceStudio,
        action = { PremiumPill("Back", onClick = onBack) },
    ) {
        PremiumCard(selected = true) {
            Text("Current provider", color = premiumTextColor(), fontWeight = FontWeight.ExtraBold)
            Text("${settings.selectedProvider.label} is the default text-to-speech provider.", color = premiumMutedTextColor())
        }
        Spacer(Modifier.height(12.dp))
        PremiumCard {
            Text("Controls", color = premiumTextColor(), fontWeight = FontWeight.ExtraBold)
            Text("Speed: ${settings.speed}x", color = premiumMutedTextColor())
            Text("Pitch: ${settings.pitch}x", color = premiumMutedTextColor())
            Text("Emotion: ${settings.emotion.label}", color = premiumMutedTextColor())
            Text("Language: ${settings.language}", color = premiumMutedTextColor())
        }
        Spacer(Modifier.height(12.dp))
        PremiumCard {
            Text("Premium provider keys", color = premiumTextColor(), fontWeight = FontWeight.ExtraBold)
            Text("Users bring their own OpenAI, ElevenLabs, Google Neural TTS, or Azure Neural TTS API keys before cloud voices are enabled.", color = premiumMutedTextColor())
            Text("Cloud voices allowed: ${settings.cloudVoicesAllowed}", color = premiumMutedTextColor())
        }
        Spacer(Modifier.height(12.dp))
        PremiumButton("Preview current voice") { onPreview() }
    }
}

@Composable
fun VoicePreviewScreen(onBack: () -> Unit, onSettings: () -> Unit) {
    val selectedVoice = VoiceStudioCatalog.marketplaceVoices.first()
    PremiumPage(
        title = "Voice Preview",
        subtitle = "Test text to speech before selecting a voice or spending premium provider credits.",
        selected = OpenAssistDestination.VoiceStudio,
        action = { PremiumPill("Back", onClick = onBack) },
    ) {
        PremiumCard(selected = true) {
            Text(selectedVoice.name, color = premiumTextColor(), fontWeight = FontWeight.ExtraBold)
            Text(selectedVoice.previewText, color = premiumMutedTextColor())
            Text("Provider: ${selectedVoice.provider.label} • Offline: ${selectedVoice.supportsOffline}", color = premiumMutedTextColor())
        }
        Spacer(Modifier.height(12.dp))
        Row(Modifier.fillMaxWidth()) {
            PremiumButton("Play preview") { }
            Spacer(Modifier.width(12.dp))
            PremiumButton("Voice settings") { onSettings() }
        }
    }
}
