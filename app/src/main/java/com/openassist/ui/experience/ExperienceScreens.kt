package com.openassist.ui.experience

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.openassist.agent.AgentEngineCatalog
import com.openassist.ui.navigation.OpenAssistDestination
import com.openassist.ui.navigation.PremiumButton
import com.openassist.ui.navigation.PremiumCard
import com.openassist.ui.navigation.PremiumPage
import com.openassist.ui.navigation.PremiumPill
import com.openassist.ui.navigation.premiumMutedTextColor
import com.openassist.ui.navigation.premiumTextColor
import com.openassist.ux.ExperienceCatalog

@Composable
fun UniversalSearchScreen(onBack: () -> Unit, onCommandPalette: () -> Unit) {
    PremiumPage(
        title = "Search Everything",
        subtitle = "Find chats, workspace files, projects, knowledge, memory, documents, images, models, MCP servers, settings, and commands.",
        selected = OpenAssistDestination.Chat,
        action = { PremiumPill("Back", onClick = onBack) },
    ) {
        PremiumCard(selected = true) {
            Text("Search Everything...", color = premiumTextColor(), fontWeight = FontWeight.ExtraBold)
            Text("Try OpenAssist, Gemma, Python, GitHub, or School Notes.", color = premiumMutedTextColor())
        }
        Spacer(Modifier.height(12.dp))
        ExperienceCatalog.universalSearchGroups.forEach { group ->
            PremiumCard(Modifier.padding(vertical = 4.dp)) {
                Text(group.title, color = premiumTextColor(), fontWeight = FontWeight.ExtraBold)
                Text(group.examples.joinToString(" • "), color = premiumMutedTextColor())
            }
        }
        Spacer(Modifier.height(12.dp))
        PremiumButton("Open Command Palette") { onCommandPalette() }
    }
}

@Composable
fun CommandPaletteScreen(onBack: () -> Unit, onNavigate: (OpenAssistDestination) -> Unit) {
    PremiumPage(
        title = "Command Palette",
        subtitle = "Run OpenAssist commands from a floating button, swipe gesture, keyboard shortcut, or voice command.",
        selected = OpenAssistDestination.Chat,
        action = { PremiumPill("Back", onClick = onBack) },
    ) {
        PremiumCard(selected = true) {
            Text("« Search Command »", color = premiumTextColor(), fontWeight = FontWeight.ExtraBold)
            Text("Inspired by VS Code for fast access to every major function.", color = premiumMutedTextColor())
        }
        Spacer(Modifier.height(12.dp))
        ExperienceCatalog.commands.forEach { command ->
            PremiumCard(Modifier.padding(vertical = 4.dp)) {
                Text("«${command.command}»", color = premiumTextColor(), fontWeight = FontWeight.ExtraBold)
                Text(command.description, color = premiumMutedTextColor())
                Spacer(Modifier.height(6.dp))
                PremiumPill("Run", onClick = { onNavigate(command.destination) })
            }
        }
    }
}

@Composable
fun AgentTaskCenterScreen(onBack: () -> Unit, onAgentEngine: () -> Unit) {
    val plan = AgentEngineCatalog.samplePlan
    PremiumPage(
        title = "Agent Task Center",
        subtitle = "Make every agent goal, plan, tool call, confirmation request, and result visible.",
        selected = OpenAssistDestination.Chat,
        action = { PremiumPill("Back", onClick = onBack) },
    ) {
        PremiumCard(selected = true) {
            Text("Goal: ${plan.goal.prompt}", color = premiumTextColor(), fontWeight = FontWeight.ExtraBold)
            Text("Progress: 2 done • 1 running • 2 queued", color = premiumMutedTextColor())
        }
        Spacer(Modifier.height(12.dp))
        plan.tasks.forEachIndexed { index, task ->
            val marker = when (index) {
                0, 1 -> "✓"
                2 -> "⏳"
                else -> "○"
            }
            PremiumCard(Modifier.padding(vertical = 4.dp), selected = index == 2) {
                Text("$marker ${task.title}", color = premiumTextColor(), fontWeight = FontWeight.ExtraBold)
                Text(task.description, color = premiumMutedTextColor())
                if (task.toolCalls.isNotEmpty()) Text("Tools: ${task.toolCalls.joinToString { it.toolName }}", color = premiumMutedTextColor())
            }
        }
        Spacer(Modifier.height(12.dp))
        PremiumButton("Open Agent Engine") { onAgentEngine() }
    }
}

@Composable
fun ConnectionsHubScreen(onBack: () -> Unit, onMarketplace: () -> Unit, onDesktopBridge: () -> Unit, onModels: () -> Unit) {
    PremiumPage(
        title = "Connections",
        subtitle = "Manage OpenRouter, local models, MCP marketplace, desktop bridge, connected services, and installed integrations.",
        selected = OpenAssistDestination.ConnectionsHub,
        action = { PremiumPill("Back", onClick = onBack) },
    ) {
        ExperienceCatalog.connectionSections.forEach { section ->
            PremiumCard(Modifier.padding(vertical = 4.dp)) {
                Text(section, color = premiumTextColor(), fontWeight = FontWeight.ExtraBold)
                Text("Install, update, remove, manage permissions, and monitor status.", color = premiumMutedTextColor())
            }
        }
        Spacer(Modifier.height(12.dp))
        Row(Modifier.fillMaxWidth()) {
            PremiumButton("MCP Marketplace") { onMarketplace() }
            Spacer(Modifier.width(12.dp))
            PremiumButton("Desktop Bridge") { onDesktopBridge() }
        }
        Spacer(Modifier.height(12.dp))
        PremiumButton("Browse Models") { onModels() }
    }
}

@Composable
fun ContextSidebarScreen(onBack: () -> Unit) {
    PremiumPage(
        title = "Context Sidebar",
        subtitle = "A slide-out status panel for model, memory, agent, workspace, MCP, desktop, and storage state.",
        selected = OpenAssistDestination.Chat,
        action = { PremiumPill("Back", onClick = onBack) },
    ) {
        ExperienceCatalog.contextStatuses.forEach { status ->
            PremiumCard(Modifier.padding(vertical = 4.dp)) {
                Text(status.label, color = premiumTextColor(), fontWeight = FontWeight.ExtraBold)
                Text(status.value, color = premiumMutedTextColor())
            }
        }
    }
}
