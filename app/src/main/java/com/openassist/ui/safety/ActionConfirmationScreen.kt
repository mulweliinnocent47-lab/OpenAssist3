package com.openassist.ui.safety

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.openassist.safety.AssistantAction
import com.openassist.safety.RiskLevel
import com.openassist.ui.navigation.OpenAssistDestination
import com.openassist.ui.navigation.PremiumButton
import com.openassist.ui.navigation.PremiumCard
import com.openassist.ui.navigation.PremiumColors
import com.openassist.ui.navigation.PremiumPage
import com.openassist.ui.navigation.PremiumPill
import com.openassist.ui.navigation.premiumMutedTextColor
import com.openassist.ui.navigation.premiumTextColor

@Composable
fun ActionConfirmationScreen(onBack: () -> Unit) {
    val action = AssistantAction(app = "WhatsApp", action = "Send Voice Message", recipient = "Yos", content = "I will arrive at 5 PM", riskLevel = RiskLevel.High)
    PremiumPage("Action Confirmation", "OpenAssist never performs sensitive actions without your approval.", OpenAssistDestination.ActionConfirmation, { if (it == OpenAssistDestination.Chat) onBack() }) {
        PremiumCard(selected = true) {
            Text("Action Summary", color = premiumTextColor(), fontWeight = FontWeight.ExtraBold)
            Text("App: ${action.app}", color = premiumMutedTextColor())
            Text("Action: ${action.action}", color = premiumMutedTextColor())
            Text("Recipient: ${action.recipient}", color = premiumMutedTextColor())
            Text("Content: ${action.content}", color = premiumMutedTextColor())
            Spacer(Modifier.height(12.dp))
            PremiumPill("Risk: ${action.riskLevel}", color = PremiumColors.Warning)
        }
        Spacer(Modifier.height(12.dp))
        Row {
            PremiumButton("Approve") { onBack() }
            Spacer(Modifier.width(12.dp))
            PremiumButton("Cancel") { onBack() }
        }
    }
}
