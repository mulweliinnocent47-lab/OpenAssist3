package com.openassist.ui.assistantpermissions

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
fun AssistantPermissionsDashboard(onBack: () -> Unit) {
    val rows = listOf(
        "Accessibility status" to "Required for app control and must be approved by the user.",
        "Notification access" to "Optional read-only context after user approval.",
        "Screen capture permission" to "Required for visible-screen understanding.",
        "File access status" to "Read-only by default; modifications require advanced access and confirmation.",
        "Hold Home assistant role" to "Use Android assistant settings to launch the OpenAssist voice overlay.",
    )
    PremiumPage("Assistant Permissions", "Control every capability OpenAssist can use.", OpenAssistDestination.AssistantPermissions, { if (it == OpenAssistDestination.Chat) onBack() }) {
        rows.forEach { (title, detail) ->
            PremiumCard {
                Text(title, color = premiumTextColor(), fontWeight = FontWeight.ExtraBold)
                Text(detail, color = premiumMutedTextColor())
                Spacer(Modifier.height(8.dp))
                PremiumPill("Needs user approval", color = PremiumColors.Warning)
            }
            Spacer(Modifier.height(10.dp))
        }
    }
}
