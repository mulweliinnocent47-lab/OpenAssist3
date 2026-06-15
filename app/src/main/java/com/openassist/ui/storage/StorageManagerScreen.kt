package com.openassist.ui.storage

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.openassist.ui.navigation.OpenAssistDestination
import com.openassist.ui.navigation.PremiumCard
import com.openassist.ui.navigation.PremiumColors
import com.openassist.ui.navigation.PremiumPage
import com.openassist.ui.navigation.PremiumPill
import com.openassist.ui.navigation.premiumMutedTextColor
import com.openassist.ui.navigation.premiumTextColor

@Composable
fun StorageManagerScreen(onBack: () -> Unit) {
    PremiumPage("Storage", "Manage OpenAssist local AI files.", OpenAssistDestination.Settings, { if (it == OpenAssistDestination.Chat) onBack() }) {
        PremiumCard {
            Text("Device storage", color = premiumTextColor(), fontWeight = FontWeight.ExtraBold)
            LinearProgressIndicator(progress = { 0.42f }, modifier = Modifier.fillMaxWidth())
            Text("AI storage used: 1.34 GB • Cache: 128 MB • Downloads: 640 MB", color = premiumMutedTextColor())
        }
        Spacer(Modifier.height(12.dp))
        listOf("OpenAssist/models/" to "1.34 GB", "OpenAssist/cache/" to "128 MB", "OpenAssist/downloads/" to "640 MB", "OpenAssist/chat_history/" to "24 MB").forEach { (path, size) ->
            PremiumCard {
                Text(path, color = premiumTextColor(), fontWeight = FontWeight.Bold)
                Text(size, color = premiumMutedTextColor())
            }
            Spacer(Modifier.height(12.dp))
        }
        PremiumPill("Clear cache", color = PremiumColors.Danger)
        Spacer(Modifier.height(8.dp))
        PremiumPill("Delete unused models", color = PremiumColors.Danger)
    }
}
