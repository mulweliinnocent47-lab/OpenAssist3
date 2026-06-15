package com.openassist.ui.settings

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.layout.height
import androidx.compose.ui.Modifier
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.material3.Text
import com.openassist.ui.navigation.OpenAssistDestination
import com.openassist.ui.navigation.PremiumCard
import com.openassist.ui.navigation.PremiumPage
import com.openassist.ui.navigation.PremiumPill
import com.openassist.ui.navigation.PremiumColors
import com.openassist.ui.navigation.premiumMutedTextColor
import com.openassist.ui.navigation.premiumTextColor
import com.openassist.viewmodel.SettingsViewModel

@Composable
fun SettingsScreen(viewModel: SettingsViewModel, onBack: () -> Unit, onModelSelection: () -> Unit = {}, onAiMode: () -> Unit = {}, onSafetySettings: () -> Unit = {}, onAssistantPermissions: () -> Unit = {}, onNavigate: (OpenAssistDestination) -> Unit = {}) {
    val savedModel by viewModel.model.collectAsState()
    val navigate: (OpenAssistDestination) -> Unit = {
        when (it) {
            OpenAssistDestination.Chat -> onBack()
            OpenAssistDestination.Settings -> Unit
            else -> onNavigate(it)
        }
    }
    PremiumPage("Settings", "Manage your app, models, privacy, and security.", OpenAssistDestination.Settings, navigate) {
        val rows = listOf(
            Triple("API key", "Securely stored and encrypted.", "Change"),
            Triple("AI mode", "Cloud, local offline, or hybrid routing.", "Choose"),
            Triple("OpenRouter model", savedModel, "Choose"),
            Triple("Local models", "Downloaded Hugging Face GGUF models.", "Manage"),
            Triple("Theme", "Premium white Android 16 style.", "Auto"),
            Triple("History", "Saved on device", "Open"),
            Triple("AI Safety Settings", "All sensitive actions require confirmation by default.", "Edit"),
            Triple("Assistant Permissions", "Accessibility, notifications, screen capture, and file access status.", "Review"),
            Triple("Support & Trust", "Feedback, bug reports, privacy, permissions, help, conversations, agents, onboarding, tips, and storage.", "Open"),
            Triple("Privacy", "Your data stays under your control.", "Review"),
            Triple("Voice", "Android TTS by default with optional cloud voices.", "Open"),
            Triple("Storage", "Manage downloads, artifacts, and cache.", "Open"),
            Triple("Core Platform", "Engines, events, context, background tasks, security, and analytics.", "Open"),
            Triple("Developer Options", "MCP, command, and automation diagnostics.", "Open"),
            Triple("Accessibility", "Assistant gesture, voice overlay, and screen access.", "Open"),
        )
        LazyColumn(Modifier.weight(1f)) {
            items(rows) { (title, detail, action) ->
                PremiumCard {
                    Text(title, color = premiumTextColor(), fontWeight = FontWeight.ExtraBold)
                    Text(detail, color = premiumMutedTextColor())
                    Spacer(Modifier.height(8.dp))
                    PremiumPill(action, onClick = when (title) {
                        "OpenRouter model" -> onModelSelection
                        "AI mode", "Local models" -> onAiMode
                        "AI Safety Settings" -> onSafetySettings
                        "Assistant Permissions" -> onAssistantPermissions
                        "Support & Trust", "Privacy", "History", "Storage" -> { onNavigate(OpenAssistDestination.SupportTrustCenter) }
                        "Core Platform" -> { onNavigate(OpenAssistDestination.CorePlatform) }
                        else -> null
                    })
                }
                Spacer(Modifier.height(12.dp))
            }
            item {
                PremiumCard {
                    Text("Security status", color = premiumTextColor(), fontWeight = FontWeight.ExtraBold)
                    Text("Permissions and keys are protected with local encryption.", color = premiumMutedTextColor())
                    Spacer(Modifier.height(12.dp))
                    PremiumPill("Protected", color = PremiumColors.Success)
                }
            }
        }
    }
}
