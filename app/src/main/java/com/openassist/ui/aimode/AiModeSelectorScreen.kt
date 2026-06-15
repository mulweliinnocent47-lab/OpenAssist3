package com.openassist.ui.aimode

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.openassist.data.ai.AiMode
import com.openassist.ui.navigation.OpenAssistDestination
import com.openassist.ui.navigation.PremiumCard
import com.openassist.ui.navigation.PremiumPage
import com.openassist.ui.navigation.PremiumPill
import com.openassist.ui.navigation.premiumMutedTextColor
import com.openassist.ui.navigation.premiumTextColor
import com.openassist.viewmodel.SettingsViewModel

@Composable
fun AiModeSelectorScreen(viewModel: SettingsViewModel, onBack: () -> Unit, onLocalModels: () -> Unit) {
    val activeMode by viewModel.aiMode.collectAsState()
    PremiumPage("AI Mode", "Choose cloud, offline, or hybrid intelligence.", OpenAssistDestination.Settings, { if (it == OpenAssistDestination.Chat) onBack() }) {
        AiMode.values().forEach { mode ->
            PremiumCard(Modifier.clickable { viewModel.saveAiMode(mode) }, selected = mode == activeMode) {
                Text(mode.label, color = premiumTextColor(), fontWeight = FontWeight.ExtraBold)
                Text(mode.description, color = premiumMutedTextColor())
                Text(
                    when (mode) {
                        AiMode.Cloud -> "Advantages: largest models. Limitation: requires API key and network."
                        AiMode.Local -> "Advantages: private and offline. Limitation: depends on device RAM."
                        AiMode.Hybrid -> "Advantages: balances speed, privacy, and quality. Limitation: needs routing preferences."
                    },
                    color = premiumMutedTextColor(),
                )
                if (mode == activeMode) PremiumPill("Active mode")
            }
            Spacer(Modifier.height(12.dp))
        }
        PremiumPill("Manage local models", onClick = onLocalModels)
    }
}
