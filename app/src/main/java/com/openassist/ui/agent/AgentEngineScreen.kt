package com.openassist.ui.agent

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
import com.openassist.agent.AgentPlanningEngine
import com.openassist.ui.navigation.OpenAssistDestination
import com.openassist.ui.navigation.PremiumButton
import com.openassist.ui.navigation.PremiumCard
import com.openassist.ui.navigation.PremiumPage
import com.openassist.ui.navigation.PremiumPill
import com.openassist.ui.navigation.premiumMutedTextColor
import com.openassist.ui.navigation.premiumTextColor

@Composable
fun AgentEngineScreen(onBack: () -> Unit) {
    val snapshot = AgentPlanningEngine().plan(AgentEngineCatalog.samplePlan.goal.prompt)
    val plan = snapshot.plan
    PremiumPage(
        title = "Agent Engine",
        subtitle = "Turn goals into plans, tasks, safe tool calls, confirmations, execution, and results.",
        selected = OpenAssistDestination.Chat,
        action = { PremiumPill("Back", onClick = onBack) },
    ) {
        PremiumCard(selected = true) {
            Text("v7 planning loop", color = premiumTextColor(), fontWeight = FontWeight.ExtraBold)
            Spacer(Modifier.height(8.dp))
            Text(AgentEngineCatalog.executionLoop.joinToString("  ↓  "), color = premiumMutedTextColor())
        }
        Spacer(Modifier.height(12.dp))
        PremiumCard {
            Text("Example goal", color = premiumMutedTextColor(), fontWeight = FontWeight.Bold)
            Text(plan.goal.prompt, color = premiumTextColor(), fontWeight = FontWeight.ExtraBold)
            Text("Status: ${snapshot.status.label}", color = premiumMutedTextColor())
            Text("Next action: ${snapshot.nextAction}", color = premiumMutedTextColor())
        }
        Spacer(Modifier.height(12.dp))
        plan.tasks.forEach { task ->
            PremiumCard(Modifier.padding(vertical = 4.dp), selected = task.toolCalls.any { it.requiresConfirmation }) {
                Text("${task.order}. ${task.title}", color = premiumTextColor(), fontWeight = FontWeight.ExtraBold)
                Text(task.description, color = premiumMutedTextColor())
                task.toolCalls.forEach { call ->
                    Spacer(Modifier.height(6.dp))
                    Text("Tool: ${call.toolName} • ${call.riskLevel.name}", color = premiumTextColor(), fontWeight = FontWeight.Bold)
                    Text(call.summary, color = premiumMutedTextColor())
                }
            }
        }
        Spacer(Modifier.height(12.dp))
        PremiumCard {
            Text("Live agent loop", color = premiumTextColor(), fontWeight = FontWeight.ExtraBold)
            Text("Completed tasks: ${snapshot.completedTasks}/${plan.tasks.size}", color = premiumMutedTextColor())
            snapshot.auditTrail.forEach { event -> Text("• $event", color = premiumMutedTextColor()) }
        }
        Spacer(Modifier.height(12.dp))
        Row(Modifier.fillMaxWidth()) {
            PremiumButton("Approve plan") { }
            Spacer(Modifier.width(12.dp))
            PremiumButton("Edit tasks") { }
        }
    }
}
