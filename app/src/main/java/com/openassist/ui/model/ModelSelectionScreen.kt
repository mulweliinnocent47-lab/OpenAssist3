package com.openassist.ui.model

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.openassist.data.openrouter.OpenRouterFreeModelCatalog
import com.openassist.ui.navigation.OpenAssistDestination
import com.openassist.ui.navigation.PremiumButton
import com.openassist.ui.navigation.PremiumCard
import com.openassist.ui.navigation.PremiumColors
import com.openassist.ui.navigation.PremiumPage
import com.openassist.ui.navigation.PremiumPill
import com.openassist.ui.navigation.premiumMutedTextColor
import com.openassist.ui.navigation.premiumTextColor
import com.openassist.viewmodel.SettingsViewModel

@Composable
fun ModelSelectionScreen(viewModel: SettingsViewModel, onBack: () -> Unit) {
    val savedModel by viewModel.model.collectAsState()
    var selectedModel by remember(savedModel) { mutableStateOf(savedModel.ifBlank { OpenRouterFreeModelCatalog.freeModels.first().id }) }

    PremiumPage("Free OpenRouter Models", "Pick a no-cost OpenRouter model before entering your API key or spending paid credits.", OpenAssistDestination.Chat, { if (it != OpenAssistDestination.Chat) onBack() }) {
        PremiumCard(selected = true) {
            Text("Free model mode", color = premiumTextColor(), fontWeight = FontWeight.ExtraBold)
            Text("These models use OpenRouter :free routes when available. Availability and rate limits are controlled by OpenRouter, so verify pricing in your OpenRouter dashboard before heavy use.", color = premiumMutedTextColor())
        }
        Spacer(Modifier.height(12.dp))
        OpenRouterFreeModelCatalog.freeModels.forEach { model ->
            PremiumCard(Modifier.clickable { selectedModel = model.id }, selected = model.id == selectedModel) {
                Row(Modifier.fillMaxWidth()) {
                    Text(model.displayName, color = premiumTextColor(), fontWeight = FontWeight.ExtraBold, modifier = Modifier.weight(1f))
                    PremiumPill(if (model.free) "Free" else "Paid", color = if (model.free) PremiumColors.Success else PremiumColors.Indigo)
                }
                Text(model.id, color = premiumMutedTextColor())
                Text("${model.provider} • ${model.contextLength?.let { "${it / 1000}k context" } ?: "Context varies"}", color = premiumMutedTextColor())
                Spacer(Modifier.height(8.dp))
                Row(Modifier.fillMaxWidth()) {
                    model.strengths.take(3).forEach { strength ->
                        PremiumPill(strength)
                        Spacer(Modifier.width(8.dp))
                    }
                }
                if (model.id == selectedModel) PremiumPill("Selected", color = PremiumColors.Success)
            }
            Spacer(Modifier.height(12.dp))
        }
        Spacer(Modifier.weight(1f))
        PremiumButton("Save Free Model", Modifier.fillMaxWidth()) { viewModel.saveModel(selectedModel); onBack() }
    }
}
