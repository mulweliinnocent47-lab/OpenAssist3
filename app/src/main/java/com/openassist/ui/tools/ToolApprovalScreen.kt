package com.openassist.ui.tools

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.openassist.ui.navigation.OpenAssistDestination
import com.openassist.ui.navigation.PremiumButton
import com.openassist.ui.navigation.PremiumCard
import com.openassist.ui.navigation.PremiumColors
import com.openassist.ui.navigation.PremiumPage
import com.openassist.ui.navigation.PremiumPill
import com.openassist.ui.navigation.premiumMutedTextColor
import com.openassist.ui.navigation.premiumTextColor

@Composable
fun ToolApprovalScreen(onBack: () -> Unit) {
    PremiumPage("Tool Approval", "Approve or deny a requested action.", OpenAssistDestination.MCPServers, { if (it == OpenAssistDestination.Chat) onBack() }) {
        PremiumCard {
            Text("OpenAssist wants to send an SMS", color = premiumTextColor(), fontWeight = FontWeight.ExtraBold)
            PremiumPill("Dangerous", color = PremiumColors.Danger)
            Spacer(Modifier.height(16.dp))
            listOf("Recipient" to "+27 82 000 0000", "Message" to "Hello, I will be late.", "Reason" to "Requested by the assistant.").forEach { (label, value) ->
                Text(label, color = premiumMutedTextColor(), fontWeight = FontWeight.Bold)
                PremiumCard { Text(value, color = premiumTextColor()) }
                Spacer(Modifier.height(12.dp))
            }
            PremiumCard { Text("This action affects a real-world cost and may reach a contact.", color = premiumTextColor()) }
            Spacer(Modifier.height(16.dp))
            PremiumButton("Allow", Modifier.fillMaxWidth(), onBack)
            Spacer(Modifier.height(8.dp))
            PremiumPill("Deny", onClick = onBack)
        }
    }
}
