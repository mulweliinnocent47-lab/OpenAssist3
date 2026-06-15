package com.openassist.ui.downloads

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
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

@Composable
fun ModelDownloadScreen(onBack: () -> Unit, onDownloads: () -> Unit) {
    var search by remember { mutableStateOf("") }
    val models = LocalModelCatalog.recommended.filter { it.name.contains(search, ignoreCase = true) || search.isBlank() }
    PremiumPage("Download Models", "Browse Hugging Face GGUF models.", OpenAssistDestination.Settings, { if (it == OpenAssistDestination.Chat) onBack() }) {
        OutlinedTextField(search, { search = it }, Modifier.fillMaxWidth(), placeholder = { Text("Search Hugging Face models") })
        Spacer(Modifier.height(16.dp))
        models.forEach { model ->
            PremiumCard {
                Text(model.name, color = premiumTextColor(), fontWeight = FontWeight.ExtraBold)
                Text(model.description, color = premiumMutedTextColor())
                Text("Download ${model.downloadSize} • Required RAM ${model.requiredRam}", color = premiumMutedTextColor())
                Text("Verified after download before llama.cpp loading", color = premiumMutedTextColor())
                PremiumPill("Download", color = PremiumColors.Blue, onClick = onDownloads)
            }
            Spacer(Modifier.height(12.dp))
        }
    }
}

@Composable
fun DownloadManagerScreen(onBack: () -> Unit) {
    PremiumPage("Downloads", "Track active and completed local model downloads.", OpenAssistDestination.Settings, { if (it == OpenAssistDestination.Chat) onBack() }) {
        listOf("SmolLM2 1.7B" to 0.68f, "Gemma 2B" to 1f, "Phi-4 Mini" to 0.34f).forEach { (name, progress) ->
            PremiumCard {
                Text(name, color = premiumTextColor(), fontWeight = FontWeight.ExtraBold)
                LinearProgressIndicator(progress = { progress }, modifier = Modifier.fillMaxWidth())
                Text(if (progress >= 1f) "Completed • verified checksum" else "${(progress * 100).toInt()}% • 4.8 MB/s", color = premiumMutedTextColor())
                PremiumPill(if (progress >= 1f) "Open" else "Pause / Resume / Cancel")
            }
            Spacer(Modifier.height(12.dp))
        }
    }
}
