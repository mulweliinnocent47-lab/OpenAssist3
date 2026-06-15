package com.openassist.core

enum class BackgroundTaskType(val label: String) {
    ModelDownload("Model Download"),
    KnowledgeIndexing("Knowledge Indexing"),
    WorkspaceExport("Workspace Export"),
    AgentTask("Agent Task"),
    McpSync("MCP Sync"),
    DesktopBridgeSync("Desktop Bridge Sync"),
    LongRunningJob("Long-running Job"),
}

enum class BackgroundTaskStatus(val label: String) {
    Queued("Queued"),
    Running("Running"),
    Paused("Paused"),
    Completed("Completed"),
    Failed("Failed"),
    Cancelled("Cancelled"),
}

data class BackgroundTask(
    val id: String,
    val type: BackgroundTaskType,
    val title: String,
    val progressPercent: Int = 0,
    val status: BackgroundTaskStatus = BackgroundTaskStatus.Queued,
    val canPause: Boolean = true,
    val canResume: Boolean = true,
    val canCancel: Boolean = true,
    val canRetry: Boolean = true,
)

class BackgroundTaskManager {
    private val tasks = mutableListOf<BackgroundTask>()

    fun enqueue(task: BackgroundTask) {
        tasks += task
    }

    fun allTasks(): List<BackgroundTask> = tasks.toList()

    fun updateStatus(id: String, status: BackgroundTaskStatus, progressPercent: Int? = null) {
        val index = tasks.indexOfFirst { it.id == id }
        if (index >= 0) {
            val current = tasks[index]
            tasks[index] = current.copy(status = status, progressPercent = progressPercent ?: current.progressPercent)
        }
    }
}

object BackgroundTaskCatalog {
    val examples = listOf(
        BackgroundTask("model-gemma", BackgroundTaskType.ModelDownload, "Download Gemma GGUF", 42, BackgroundTaskStatus.Running),
        BackgroundTask("knowledge-school", BackgroundTaskType.KnowledgeIndexing, "Index School Notes", 70, BackgroundTaskStatus.Running),
        BackgroundTask("workspace-export", BackgroundTaskType.WorkspaceExport, "Export OpenAssist Project", 0, BackgroundTaskStatus.Queued),
        BackgroundTask("mcp-sync", BackgroundTaskType.McpSync, "Sync GitHub MCP", 100, BackgroundTaskStatus.Completed),
    )
}
