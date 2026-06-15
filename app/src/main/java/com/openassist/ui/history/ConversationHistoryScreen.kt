package com.openassist.ui.history

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
import com.openassist.ui.navigation.PremiumPage
import com.openassist.ui.navigation.PremiumPill
import com.openassist.ui.navigation.premiumMutedTextColor
import com.openassist.ui.navigation.premiumTextColor

@Composable
fun ConversationHistoryScreen(onBack: () -> Unit, onNewChat: () -> Unit) {
    val conversations = listOf(
        Triple("Open WhatsApp", "1 min ago", "Chat"),
        Triple("Device info", "12 min ago", "Tool"),
        Triple("Plan my study schedule", "1 hour ago", "Chat"),
        Triple("OpenAssist settings", "Yesterday", "Settings"),
        Triple("Test the API key", "2 days ago", "Tool"),
    )
    PremiumPage("History", "Saved locally on this device.", OpenAssistDestination.ConversationHistory, { if (it == OpenAssistDestination.Chat) onBack() }) {
        conversations.forEach { (title, time, type) ->
            PremiumCard {
                Text(title, color = premiumTextColor(), fontWeight = FontWeight.ExtraBold)
                Text(time, color = premiumMutedTextColor())
                PremiumPill(type)
            }
            Spacer(Modifier.height(12.dp))
        }
        Spacer(Modifier.weight(1f))
        PremiumButton("New Chat", Modifier.fillMaxWidth(), onNewChat)
    }
}
