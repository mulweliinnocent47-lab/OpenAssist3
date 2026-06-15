package com.openassist.ui.localmodels

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.openassist.data.ai.LocalModelCatalog
import com.openassist.ui.navigation.OpenAssistDestination
import com.openassist.ui.navigation.PremiumCard
import com.openassist.ui.navigation.PremiumColors
import com.openassist.ui.navigation.PremiumPage
import com.openassist.ui.navigation.PremiumPill
import com.openassist.ui.navigation.premiumMutedTextColor
import com.openassist.ui.navigation.premiumTextColor
import com.openassist.viewmodel.SettingsViewModel

@Composable
fun LocalModelsScreen(viewModel: SettingsViewModel, onBack: () -> Unit, onDownloadModels: () -> Unit, onStorage: () -> Unit) {
    val activeModel by viewModel.localModel.collectAsState()
    PremiumPage("Local Models", "Installed offline GGUF models for llama.cpp Android.", OpenAssistDestination.Settings, { if (it == OpenAssistDestination.Chat) onBack() }) {
        LocalModelCatalog.installedDemo.map { it.copy(active = it.id == activeModel) }.forEach { model ->
            PremiumCard(selected = model.active) {
                Text(model.name, color = premiumTextColor(), fontWeight = FontWeight.ExtraBold)
                Text("${model.version} • ${model.downloadSize} • RAM ${model.requiredRam}", color = premiumMutedTextColor())
                Text("OpenAssist/models/${model.fileName}", color = premiumMutedTextColor())
                PremiumPill(if (model.active) "Active model" else "Switch model", color = if (model.active) PremiumColors.Success else PremiumColors.Indigo, onClick = { viewModel.saveLocalModel(model.id) })
                Spacer(Modifier.height(8.dp))
                PremiumPill("Delete model", color = PremiumColors.Danger)
            }
            Spacer(Modifier.height(12.dp))
        }
        PremiumPill("Download Hugging Face models", color = PremiumColors.Blue, onClick = onDownloadModels)
        Spacer(Modifier.height(8.dp))
        PremiumPill("Storage manager", onClick = onStorage)
    }
}
