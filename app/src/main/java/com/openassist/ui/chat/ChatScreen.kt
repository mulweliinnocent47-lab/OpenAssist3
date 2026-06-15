package com.openassist.ui.chat

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
import com.openassist.ux.ExperienceCatalog
import com.openassist.viewmodel.ChatViewModel

@Composable
fun ChatScreen(
    chatViewModel: ChatViewModel,
    onSettings: () -> Unit,
    onModels: () -> Unit,
    onPermissions: () -> Unit,
    onToolApproval: () -> Unit,
    onMcpServers: () -> Unit,
    onHistory: () -> Unit,
    onAbout: () -> Unit,
    onScreenIntelligence: () -> Unit,
    onAutomationCenter: () -> Unit,
    onVoiceOverlay: () -> Unit,
    onVoiceStudio: () -> Unit,
    onWorkspace: () -> Unit,
    onImageStudio: () -> Unit,
    onCodeCanvas: () -> Unit,
    onMcpMarketplace: () -> Unit,
    onMcpConnections: () -> Unit,
    onConnectionsHub: () -> Unit,
    onDesktopBridge: () -> Unit,
    onAgentEngine: () -> Unit,
    onKnowledgeBase: () -> Unit,
    onUniversalSearch: () -> Unit,
    onCommandPalette: () -> Unit,
    onAgentTaskCenter: () -> Unit,
    onContextSidebar: () -> Unit,
    onSupportTrust: () -> Unit,
) {
    val state by chatViewModel.state.collectAsState()
    val statusText = when {
        state.pendingToolSummary != null -> "Waiting for approval"
        state.loading -> "Thinking…"
        state.error != null -> "Needs attention"
        else -> "Ready"
    }
    var input by remember { mutableStateOf("") }
    val openDestination: (OpenAssistDestination) -> Unit = { destination ->
        when (destination) {
            OpenAssistDestination.Chat -> Unit
            OpenAssistDestination.ConversationHistory -> onHistory()
            OpenAssistDestination.Workspace -> onWorkspace()
            OpenAssistDestination.KnowledgeBase -> onKnowledgeBase()
            OpenAssistDestination.ConnectionsHub -> onConnectionsHub()
            OpenAssistDestination.Settings -> onSettings()
            OpenAssistDestination.ModelSelection, OpenAssistDestination.AiModeSelector -> onModels()
            OpenAssistDestination.Permissions -> onPermissions()
            OpenAssistDestination.ToolApproval -> onToolApproval()
            OpenAssistDestination.MCPServers -> onMcpServers()
            OpenAssistDestination.About -> onAbout()
            OpenAssistDestination.ScreenIntelligence -> onScreenIntelligence()
            OpenAssistDestination.AutomationCenter -> onAutomationCenter()
            OpenAssistDestination.VoiceOverlay -> onVoiceOverlay()
            OpenAssistDestination.VoiceStudio -> onVoiceStudio()
            OpenAssistDestination.ImageStudio -> onImageStudio()
            OpenAssistDestination.CodeCanvas -> onCodeCanvas()
            OpenAssistDestination.McpMarketplace -> onMcpMarketplace()
            OpenAssistDestination.McpConnections -> onMcpConnections()
            OpenAssistDestination.DesktopBridge -> onDesktopBridge()
            OpenAssistDestination.AgentEngine -> onAgentEngine()
            OpenAssistDestination.UniversalSearch -> onUniversalSearch()
            OpenAssistDestination.CommandPalette -> onCommandPalette()
            OpenAssistDestination.AgentTaskCenter -> onAgentTaskCenter()
            OpenAssistDestination.ContextSidebar -> onContextSidebar()
            OpenAssistDestination.SupportTrustCenter -> onSupportTrust()
            else -> onCommandPalette()
        }
    }
    val navigate: (OpenAssistDestination) -> Unit = openDestination

    PremiumPage("OpenAssist", "Chat, tools, voice, agents, files, and knowledge in one polished command center.", OpenAssistDestination.Chat, navigate, action = { PremiumPill(statusText, onClick = onUniversalSearch) }) {
        PremiumCard(selected = true) {
            Text("Assistant Command Center", color = premiumTextColor(), fontWeight = FontWeight.ExtraBold)
            Spacer(Modifier.height(8.dp))
            Row(Modifier.fillMaxWidth()) {
                PremiumPill("Models", onClick = onModels)
                Spacer(Modifier.width(8.dp))
                PremiumPill("Support & Trust", onClick = onSupportTrust)
            }
            Text("Ask naturally, then approve sensitive actions like calls, SMS, and alarms before OpenAssist touches the device.", color = premiumMutedTextColor())
            state.error?.let { Text("Error: $it", color = premiumTextColor(), fontWeight = FontWeight.Bold) }
        }
        Spacer(Modifier.height(12.dp))
        LazyColumn(Modifier.weight(1f)) {
            item {
                PremiumCard(Modifier.fillMaxWidth(0.82f)) {
                    Text("Assistant", color = premiumMutedTextColor(), fontWeight = FontWeight.Bold)
                    Text("I can plan tasks, generate files, answer from Knowledge, control tools safely, and keep every major feature within 1–2 taps.", color = premiumTextColor())
                }
                Spacer(Modifier.height(12.dp))
                PremiumCard(Modifier.fillMaxWidth(0.72f).padding(start = 90.dp), selected = true) {
                    Text("You", color = premiumMutedTextColor(), fontWeight = FontWeight.Bold)
                    Text("Make this app feel premium and ready for production.", color = premiumTextColor())
                }
                Spacer(Modifier.height(12.dp))
                PremiumCard(Modifier.fillMaxWidth(0.72f).padding(start = 72.dp)) {
                    Text("Tool result", color = premiumMutedTextColor(), fontWeight = FontWeight.Bold)
                    Text("OpenAssist mockups updated successfully.", color = premiumTextColor())
                }
                Spacer(Modifier.height(12.dp))
            }
            items(state.messages) { message ->
                PremiumCard(Modifier.padding(vertical = 4.dp)) {
                    Text(message.role, color = premiumMutedTextColor(), fontWeight = FontWeight.Bold)
                    Text(message.content, color = premiumTextColor())
                }
            }
        }
        PremiumCard(selected = true) {
            Text("Universal Search", color = premiumTextColor(), fontWeight = FontWeight.ExtraBold)
            Text("Search chats, files, projects, knowledge, models, MCP servers, settings, and commands.", color = premiumMutedTextColor())
            Spacer(Modifier.height(8.dp))
            Row(Modifier.fillMaxWidth()) {
                PremiumButton("Search Everything") { onUniversalSearch() }
                Spacer(Modifier.width(12.dp))
                PremiumButton("« Commands »") { onCommandPalette() }
            }
        }
        Spacer(Modifier.height(12.dp))
        PremiumCard {
            Text("Quick Actions", color = premiumTextColor(), fontWeight = FontWeight.ExtraBold)
            ExperienceCatalog.homeQuickActions.take(8).forEach { action ->
                Spacer(Modifier.height(8.dp))
                PremiumCard(selected = false) {
                    Text(action.title, color = premiumTextColor(), fontWeight = FontWeight.Bold)
                    Text(action.description, color = premiumMutedTextColor())
                    Spacer(Modifier.height(6.dp))
                    PremiumPill("Open", onClick = { openDestination(action.destination) })
                }
            }
        }
        Spacer(Modifier.height(12.dp))
        PremiumCard {
            Text("Recent Activity", color = premiumTextColor(), fontWeight = FontWeight.ExtraBold)
            ExperienceCatalog.recentActivity.forEach { activity -> Text("• $activity", color = premiumMutedTextColor()) }
        }
        Spacer(Modifier.height(12.dp))
        state.pendingToolSummary?.let { summary ->
            PremiumCard(selected = true) {
                Text("Confirmation required", color = premiumTextColor(), fontWeight = FontWeight.ExtraBold)
                Text(summary, color = premiumMutedTextColor())
                Spacer(Modifier.height(12.dp))
                Row {
                    PremiumButton("Approve") { chatViewModel.confirmPendingAction() }
                    Spacer(Modifier.width(12.dp))
                    PremiumButton("Cancel") { chatViewModel.cancelPendingAction() }
                }
            }
        }
        PremiumCard(selected = true) {
            Text(if (state.loading) "OpenAssist is working…" else "What can I help with?", color = premiumTextColor(), fontWeight = FontWeight.ExtraBold)
            Spacer(Modifier.height(8.dp))
            Row(Modifier.fillMaxWidth()) {
                OutlinedTextField(input, { input = it }, Modifier.weight(1f), placeholder = { Text("Try: set alarm 07:30, text +15551234567 hello, or call +15551234567") })
                Spacer(Modifier.width(12.dp))
                PremiumButton(if (state.loading) "…" else "Send") { chatViewModel.send(input); input = "" }
            }
        }
    }
}
