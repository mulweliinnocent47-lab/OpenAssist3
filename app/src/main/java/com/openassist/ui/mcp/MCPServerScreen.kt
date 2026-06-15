package com.openassist.ui.mcp

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
fun MCPServerScreen(onBack: () -> Unit) {
    val servers = listOf(
        Triple("Local PC", "Terminal + files", "Connected"),
        Triple("Figma Assistant", "Design handoff", "Offline"),
        Triple("Repository API", "GitHub data", "On"),
        Triple("Notion Sync", "Knowledge base", "On"),
    )
    PremiumPage("MCP Servers", "Connect external tools to OpenAssist.", OpenAssistDestination.MCPServers, { if (it == OpenAssistDestination.Chat) onBack() }, action = { PremiumPill("+ Add Server", color = PremiumColors.Blue) }) {
        servers.forEach { (name, detail, status) ->
            PremiumCard(selected = status == "Connected") {
                Text(name, color = premiumTextColor(), fontWeight = FontWeight.ExtraBold)
                Text(detail, color = premiumMutedTextColor())
                PremiumPill(status, color = if (status == "Connected") PremiumColors.Success else PremiumColors.Indigo)
            }
            Spacer(Modifier.height(12.dp))
        }
    }
}
