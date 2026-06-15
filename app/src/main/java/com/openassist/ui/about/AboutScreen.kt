package com.openassist.ui.about

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.openassist.ui.navigation.LogoMark
import com.openassist.ui.navigation.OpenAssistDestination
import com.openassist.ui.navigation.PremiumCard
import com.openassist.ui.navigation.PremiumPage
import com.openassist.ui.navigation.premiumMutedTextColor
import com.openassist.ui.navigation.premiumTextColor

@Composable
fun AboutScreen(onBack: () -> Unit) {
    PremiumPage("About", "Open-source friendly, premium by design.", OpenAssistDestination.Settings, { if (it == OpenAssistDestination.Chat) onBack() }) {
        PremiumCard {
            Row(verticalAlignment = Alignment.CenterVertically) {
                LogoMark()
                Spacer(Modifier.width(16.dp))
                androidx.compose.foundation.layout.Column {
                    Text("OpenAssist", color = premiumTextColor(), fontWeight = FontWeight.ExtraBold)
                    Text("Version 1.0.0", color = premiumMutedTextColor())
                    Text("Built for productivity, MCP workflows, and model choice.", color = premiumMutedTextColor())
                }
            }
        }
        Spacer(Modifier.height(36.dp))
        listOf("Support & Trust Center", "Privacy Center", "Help Center", "Send Feedback").forEach {
            PremiumCard { Text("$it  ›", color = premiumTextColor(), fontWeight = FontWeight.Bold) }
            Spacer(Modifier.height(12.dp))
        }
    }
}
