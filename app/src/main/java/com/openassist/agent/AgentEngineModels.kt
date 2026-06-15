package com.openassist.agent

import com.openassist.safety.RiskLevel
import com.openassist.workspace.ArtifactType

/**
 * Describes the v7 Agent Engine loop: goal -> plan -> tasks -> tool calls -> confirmation -> execution -> result.
 */
data class AgentGoal(
    val id: String,
    val prompt: String,
    val status: AgentRunStatus = AgentRunStatus.Draft,
)

enum class AgentRunStatus(val label: String) {
    Draft("Draft"),
    Planning("Planning"),
    WaitingForApproval("Waiting for approval"),
    Executing("Executing"),
    Completed("Completed"),
    Cancelled("Cancelled"),
}

enum class AgentTaskStatus(val label: String) {
    Planned("Planned"),
    NeedsConfirmation("Needs confirmation"),
    Approved("Approved"),
    Running("Running"),
    Done("Done"),
    Blocked("Blocked"),
}

data class AgentToolCall(
    val toolName: String,
    val summary: String,
    val requiresConfirmation: Boolean = true,
    val riskLevel: RiskLevel = RiskLevel.Low,
)

data class AgentTask(
    val order: Int,
    val title: String,
    val description: String,
    val status: AgentTaskStatus = AgentTaskStatus.Planned,
    val toolCalls: List<AgentToolCall> = emptyList(),
    val outputArtifactType: ArtifactType? = null,
)

data class AgentPlan(
    val goal: AgentGoal,
    val tasks: List<AgentTask>,
) {
    val requiresConfirmation: Boolean = tasks.any { task -> task.toolCalls.any { it.requiresConfirmation } }
}

object AgentEngineCatalog {
    val executionLoop = listOf("Goal", "Plan", "Tasks", "Tool Calls", "Confirmation", "Execution", "Result")

    val samplePlan = AgentPlan(
        goal = AgentGoal("research-local-ai", "Research local AI models and create a report", AgentRunStatus.WaitingForApproval),
        tasks = listOf(
            AgentTask(
                order = 1,
                title = "Search model sources",
                description = "Search Hugging Face and local model catalogs for small GGUF-capable models.",
                status = AgentTaskStatus.NeedsConfirmation,
                toolCalls = listOf(AgentToolCall("MCP: Hugging Face", "Search model cards for mobile-friendly local AI models.")),
            ),
            AgentTask(
                order = 2,
                title = "Compare candidates",
                description = "Compare size, license, context window, speed, and Android feasibility.",
            ),
            AgentTask(
                order = 3,
                title = "Create markdown report",
                description = "Draft a structured report with recommendations and tradeoffs.",
                outputArtifactType = ArtifactType.Document,
            ),
            AgentTask(
                order = 4,
                title = "Save to Workspace",
                description = "Save the report under workspace/documents/local_ai_models.md.",
                toolCalls = listOf(AgentToolCall("Workspace", "Create or update the markdown report artifact.")),
                outputArtifactType = ArtifactType.Document,
            ),
            AgentTask(
                order = 5,
                title = "Export PDF after approval",
                description = "Ask for confirmation before exporting a PDF copy.",
                status = AgentTaskStatus.NeedsConfirmation,
                toolCalls = listOf(AgentToolCall("Workspace Export", "Export workspace report as PDF after approval.")),
                outputArtifactType = ArtifactType.Document,
            ),
        ),
    )

    fun buildDraftPlan(goalText: String): AgentPlan = samplePlan.copy(
        goal = AgentGoal("custom-goal", goalText.ifBlank { samplePlan.goal.prompt }, AgentRunStatus.Planning),
    )
}

data class AgentRunSnapshot(
    val plan: AgentPlan,
    val status: AgentRunStatus,
    val completedTasks: Int,
    val nextAction: String,
    val auditTrail: List<String>,
)

class AgentPlanningEngine {
    fun plan(goalText: String): AgentRunSnapshot {
        val plan = AgentEngineCatalog.buildDraftPlan(goalText)
        return AgentRunSnapshot(
            plan = plan,
            status = if (plan.requiresConfirmation) AgentRunStatus.WaitingForApproval else AgentRunStatus.Executing,
            completedTasks = 0,
            nextAction = if (plan.requiresConfirmation) "Request user approval for ${confirmationCount(plan)} tool call(s)." else "Start safe task execution.",
            auditTrail = listOf("Goal accepted", "Plan generated", "Risk scan complete"),
        )
    }

    fun approve(plan: AgentPlan): AgentRunSnapshot {
        val approvedTasks = plan.tasks.map { task ->
            val newStatus = if (task.status == AgentTaskStatus.NeedsConfirmation) AgentTaskStatus.Approved else task.status
            task.copy(status = newStatus)
        }
        val approvedPlan = plan.copy(goal = plan.goal.copy(status = AgentRunStatus.Executing), tasks = approvedTasks)
        return AgentRunSnapshot(
            plan = approvedPlan,
            status = AgentRunStatus.Executing,
            completedTasks = approvedTasks.count { it.status == AgentTaskStatus.Done },
            nextAction = approvedTasks.firstOrNull { it.status in setOf(AgentTaskStatus.Planned, AgentTaskStatus.Approved) }?.title ?: "Finalize result",
            auditTrail = listOf("Plan approved", "Execution queue created", "Progress monitor active"),
        )
    }

    private fun confirmationCount(plan: AgentPlan): Int = plan.tasks.sumOf { task -> task.toolCalls.count { it.requiresConfirmation } }
}
