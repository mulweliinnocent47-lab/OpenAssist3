package com.openassist.ui.permissions

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
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
fun PermissionScreen(onBack: () -> Unit) {
    val permissions = listOf("SMS" to "Enabled", "Calls" to "Ask", "Contacts" to "Ask", "Files" to "Enabled", "Notifications" to "Enabled")
    PremiumPage("Permissions", "Control what the assistant can access.", OpenAssistDestination.Settings, { if (it == OpenAssistDestination.Chat) onBack() }) {
        permissions.forEach { (name, status) ->
            PremiumCard {
                Text(name, color = premiumTextColor(), fontWeight = FontWeight.ExtraBold)
                Text("Grant access for this capability", color = premiumMutedTextColor())
                PremiumPill(status, color = if (status == "Enabled") PremiumColors.Success else PremiumColors.Indigo)
            }
            Spacer(Modifier.height(12.dp))
        }
    }
}
