package com.openassist.core

import java.time.Instant

enum class PlatformEventType(val label: String) {
    MemoryUpdated("Memory Updated"),
    FileCreated("File Created"),
    ProjectCreated("Project Created"),
    McpInstalled("MCP Installed"),
    AgentStarted("Agent Started"),
    AgentCompleted("Agent Completed"),
    ModelDownloaded("Model Downloaded"),
    PermissionRequested("Permission Requested"),
    VoiceChanged("Voice Changed"),
    SearchPerformed("Search Performed"),
}

data class PlatformEvent(
    val type: PlatformEventType,
    val source: CoreEngineId,
    val summary: String,
    val metadata: Map<String, String> = emptyMap(),
    val timestamp: Instant = Instant.now(),
)

interface PlatformEventSubscriber {
    val engineId: CoreEngineId
    fun onEvent(event: PlatformEvent)
}

class PlatformEventBus {
    private val subscribers = mutableMapOf<PlatformEventType, MutableList<PlatformEventSubscriber>>()
    private val eventLog = mutableListOf<PlatformEvent>()

    fun subscribe(type: PlatformEventType, subscriber: PlatformEventSubscriber) {
        subscribers.getOrPut(type) { mutableListOf() }.add(subscriber)
    }

    fun publish(event: PlatformEvent) {
        eventLog += event
        subscribers[event.type].orEmpty().forEach { it.onEvent(event) }
    }

    fun recentEvents(limit: Int = 50): List<PlatformEvent> = eventLog.takeLast(limit)
}

object PlatformEventCatalog {
    val examples = listOf(
        PlatformEvent(PlatformEventType.MemoryUpdated, CoreEngineId.Memory, "User preference remembered."),
        PlatformEvent(PlatformEventType.FileCreated, CoreEngineId.Workspace, "Created workspace/documents/report.md."),
        PlatformEvent(PlatformEventType.ProjectCreated, CoreEngineId.Workspace, "Created OpenAssist Project."),
        PlatformEvent(PlatformEventType.McpInstalled, CoreEngineId.Mcp, "Installed GitHub MCP."),
        PlatformEvent(PlatformEventType.AgentStarted, CoreEngineId.Agent, "Research local AI models."),
        PlatformEvent(PlatformEventType.AgentCompleted, CoreEngineId.Agent, "Report saved to Workspace."),
        PlatformEvent(PlatformEventType.ModelDownloaded, CoreEngineId.Ai, "Downloaded local GGUF model."),
    )
}
