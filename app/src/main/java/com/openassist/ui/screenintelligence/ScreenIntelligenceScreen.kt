package com.openassist.ui.screenintelligence

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.openassist.screen.ScreenIntelligenceService
import com.openassist.ui.navigation.OpenAssistDestination
import com.openassist.ui.navigation.PremiumCard
import com.openassist.ui.navigation.PremiumColors
import com.openassist.ui.navigation.PremiumPage
import com.openassist.ui.navigation.PremiumPill
import com.openassist.ui.navigation.premiumMutedTextColor
import com.openassist.ui.navigation.premiumTextColor

@Composable
fun ScreenIntelligenceScreen(onBack: () -> Unit) {
    val service = ScreenIntelligenceService()
    val insight = service.summarizeVisibleScreen("OCR preview will appear here after screen capture permission is approved.")
    PremiumPage("Screen Intelligence", "Read, explain, summarize, and answer questions about the visible screen.", OpenAssistDestination.ScreenIntelligence, { if (it == OpenAssistDestination.Chat) onBack() }) {
        PremiumCard {
            Text("Privacy warning", color = premiumTextColor(), fontWeight = FontWeight.ExtraBold)
            Text(insight.privacyWarning, color = premiumMutedTextColor())
            Spacer(Modifier.height(12.dp))
            PremiumPill("Activation requires consent", color = PremiumColors.Warning)
        }
        Spacer(Modifier.height(12.dp))
        PremiumCard {
            Text("Screen summary", color = premiumTextColor(), fontWeight = FontWeight.ExtraBold)
            Text(insight.summary, color = premiumMutedTextColor())
            Spacer(Modifier.height(8.dp))
            Text("OCR extraction", color = premiumTextColor(), fontWeight = FontWeight.Bold)
            Text(insight.visibleText, color = premiumMutedTextColor())
        }
        Spacer(Modifier.height(12.dp))
        PremiumCard {
            Text("Requirements", color = premiumTextColor(), fontWeight = FontWeight.ExtraBold)
            service.permissionChecklist().forEach { Text("• $it", color = premiumMutedTextColor()) }
        }
    }
}
