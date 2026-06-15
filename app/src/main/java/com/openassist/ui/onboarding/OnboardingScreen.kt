package com.openassist.ui.onboarding

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.openassist.ui.navigation.PremiumButton
import com.openassist.ui.navigation.PremiumCard
import com.openassist.ui.navigation.PremiumColors
import com.openassist.ui.navigation.PremiumPage
import com.openassist.ui.navigation.PremiumPill
import com.openassist.ui.navigation.premiumMutedTextColor
import com.openassist.ui.navigation.premiumTextColor

@Composable
fun OnboardingScreen(onContinue: () -> Unit) {
    PremiumPage("Before You Start", "OpenAssist can use cloud AI, local models, and sensitive device tools. Review the safety basics first.") {
        PremiumCard(selected = true) {
            PremiumPill("Important", color = PremiumColors.Danger)
            Spacer(Modifier.height(12.dp))
            Text("API costs are your responsibility", color = premiumTextColor(), fontWeight = FontWeight.ExtraBold)
            Text("Cloud providers such as OpenRouter may charge your account or apply rate limits. OpenAssist will let you choose free OpenRouter models, but free availability can change and heavy usage may still require checking your provider dashboard.", color = premiumMutedTextColor())
        }
        Spacer(Modifier.height(12.dp))
        PremiumCard(selected = true) {
            Text("Local key storage", color = premiumTextColor(), fontWeight = FontWeight.ExtraBold)
            Text("Your API key is stored only on this device using Android encrypted storage. OpenAssist does not ship your key to its own server, but anyone with device access may still be able to use configured providers from this app.", color = premiumMutedTextColor())
        }
        Spacer(Modifier.height(12.dp))
        PremiumCard {
            Text("Sensitive actions require approval", color = premiumTextColor(), fontWeight = FontWeight.ExtraBold)
            listOf("Calls", "SMS messages", "Alarms", "Files", "MCP tools").forEach {
                Spacer(Modifier.height(8.dp))
                PremiumPill(it)
            }
            Spacer(Modifier.height(12.dp))
            Text("OpenAssist asks for confirmation and Android permissions before executing sensitive actions.", color = premiumMutedTextColor())
        }
        Spacer(Modifier.weight(1f))
        PremiumButton("I Understand — Continue", Modifier.fillMaxWidth(), onContinue)
    }
}
