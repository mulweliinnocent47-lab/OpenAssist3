package com.openassist.ui.safety

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.openassist.safety.AssistantBehaviorRules
import com.openassist.safety.PermissionLevel
import com.openassist.ui.navigation.OpenAssistDestination
import com.openassist.ui.navigation.PremiumCard
import com.openassist.ui.navigation.PremiumColors
import com.openassist.ui.navigation.PremiumPage
import com.openassist.ui.navigation.PremiumPill
import com.openassist.ui.navigation.premiumMutedTextColor
import com.openassist.ui.navigation.premiumTextColor

@Composable
fun SafetySettingsScreen(onBack: () -> Unit) {
    PremiumPage("AI Safety Settings", "All confirmation controls are enabled by default.", OpenAssistDestination.SafetySettings, { if (it == OpenAssistDestination.Chat) onBack() }) {
        listOf(
            "Require confirmation for messages",
            "Require confirmation for calls",
            "Require confirmation for file actions",
            "Require confirmation for automation",
            "Require confirmation for app control",
            "Require confirmation for external sharing",
        ).forEach { setting ->
            PremiumCard {
                Text("✓ $setting", color = premiumTextColor(), fontWeight = FontWeight.ExtraBold)
                Text("Enabled by default to keep the user in control.", color = premiumMutedTextColor())
            }
            Spacer(Modifier.height(10.dp))
        }
        PremiumCard {
            Text("Permission levels", color = premiumTextColor(), fontWeight = FontWeight.ExtraBold)
            PermissionLevel.values().forEach { Text("${it.label}: ${it.description}", color = premiumMutedTextColor()) }
            Spacer(Modifier.height(12.dp))
            PremiumPill("Advanced file access is off", color = PremiumColors.Warning)
        }
        Spacer(Modifier.height(10.dp))
        PremiumCard {
            Text("Behavior rules", color = premiumTextColor(), fontWeight = FontWeight.ExtraBold)
            Text("Allowed: ${AssistantBehaviorRules.allowed.joinToString()}", color = premiumMutedTextColor())
            Text("Blocked without confirmation: ${AssistantBehaviorRules.blockedWithoutConfirmation.joinToString()}", color = premiumMutedTextColor())
        }
    }
}
