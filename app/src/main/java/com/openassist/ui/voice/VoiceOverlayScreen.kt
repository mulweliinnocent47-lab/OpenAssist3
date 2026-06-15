package com.openassist.ui.voice

import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.openassist.ui.navigation.OpenAssistDestination
import com.openassist.ui.navigation.PremiumCard
import com.openassist.ui.navigation.PremiumPage
import com.openassist.ui.navigation.PremiumPill
import com.openassist.ui.navigation.premiumMutedTextColor
import com.openassist.ui.navigation.premiumTextColor

@Composable
fun VoiceOverlayScreen(onBack: () -> Unit) {
    val transition = rememberInfiniteTransition(label = "voiceOverlay")
    val pulse by transition.animateFloat(
        initialValue = 0.84f,
        targetValue = 1.12f,
        animationSpec = infiniteRepeatable(animation = tween(900), repeatMode = RepeatMode.Reverse),
        label = "assistantPulse",
    )
    PremiumPage("Voice Overlay", "Hold Home to open a small animated assistant layer without launching the full app.", OpenAssistDestination.VoiceOverlay, { if (it == OpenAssistDestination.Chat) onBack() }) {
        PremiumCard(selected = true) {
            Text("Animated assistant layer", color = premiumTextColor(), fontWeight = FontWeight.ExtraBold)
            Text("Pulse scale: ${"%.2f".format(pulse)}", color = premiumMutedTextColor())
            Text("Designed to behave like a Google Assistant-style overlay for fast voice chat.", color = premiumMutedTextColor())
            Spacer(Modifier.height(12.dp))
            PremiumPill("Listening after user activation")
        }
        Spacer(Modifier.height(12.dp))
        PremiumCard {
            Text("Memory and personality", color = premiumTextColor(), fontWeight = FontWeight.ExtraBold)
            Text("Users can ask OpenAssist to remember preferences and choose a personality while keeping memories stored in-app.", color = premiumMutedTextColor())
        }
    }
}
