package com.openassist.ui.automation

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.openassist.safety.AssistantAction
import com.openassist.safety.RiskLevel
import com.openassist.ui.navigation.OpenAssistDestination
import com.openassist.ui.navigation.PremiumCard
import com.openassist.ui.navigation.PremiumPage
import com.openassist.ui.navigation.PremiumPill
import com.openassist.ui.navigation.premiumMutedTextColor
import com.openassist.ui.navigation.premiumTextColor

@Composable
fun AutomationCenterScreen(onBack: () -> Unit) {
    val pending = AssistantAction(app = "WhatsApp", action = "Send Voice Message", recipient = "Yos", content = "I will arrive at 5 PM", riskLevel = RiskLevel.High)
    PremiumPage("Automation Center", "Review pending, completed, and canceled assistant actions.", OpenAssistDestination.AutomationCenter, { if (it == OpenAssistDestination.Chat) onBack() }) {
        PremiumCard(selected = true) {
            Text("Pending action", color = premiumTextColor(), fontWeight = FontWeight.ExtraBold)
            Text("${pending.action} in ${pending.app}", color = premiumMutedTextColor())
            Text("Recipient: ${pending.recipient}", color = premiumMutedTextColor())
            Text("Content: ${pending.content}", color = premiumMutedTextColor())
            Spacer(Modifier.height(10.dp))
            PremiumPill("Needs final confirmation")
        }
        Spacer(Modifier.height(12.dp))
        PremiumCard {
            Text("History", color = premiumTextColor(), fontWeight = FontWeight.ExtraBold)
            Text("Completed and canceled automations will appear here with timestamps and risk levels.", color = premiumMutedTextColor())
        }
    }
}
