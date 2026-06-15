package com.openassist.core

import com.openassist.ui.navigation.OpenAssistDestination

enum class CoreEngineId(val label: String) {
    Ai("AI Engine"),
    Agent("Agent Engine"),
    Memory("Memory Engine"),
    Workspace("Workspace Engine"),
    Knowledge("Knowledge Engine"),
    Mcp("MCP Engine"),
    AndroidAutomation("Android Automation Engine"),
    Voice("Voice Engine"),
    Search("Search Engine"),
    Notification("Notification Engine"),
}

enum class EngineCapability(val label: String) {
    Chat("Chat"),
    ToolCalling("Tool Calling"),
    Reasoning("Reasoning"),
    StructuredOutputs("Structured Outputs"),
    AgentTasks("Agent Tasks"),
    SemanticSearch("Semantic Search"),
    BackgroundJobs("Background Jobs"),
    AuditLogging("Audit Logging"),
    Confirmation("Confirmation"),
}

data class CoreEngineDefinition(
    val id: CoreEngineId,
    val responsibility: String,
    val capabilities: List<String>,
    val destination: OpenAssistDestination? = null,
)

object CorePlatformRegistry {
    val engines = listOf(
        CoreEngineDefinition(
            CoreEngineId.Ai,
            "OpenRouter, local GGUF models, hybrid mode, model selection, context assembly, tool calling, and token management.",
            listOf("OpenRouter", "Local GGUF Models", "Future Providers", "Chat", "Tool Calling", "Reasoning", "Structured Outputs", "Agent Tasks"),
            OpenAssistDestination.AiModeSelector,
        ),
        CoreEngineDefinition(
            CoreEngineId.Agent,
            "Goal → Plan → Tasks → Tool Calls → Confirmation → Execution → Result.",
            listOf("Task Queue", "Task History", "Task Approval", "Task Retry", "Task Cancellation", "Task Monitoring", "Multi-step Reasoning", "Progress Tracking"),
            OpenAssistDestination.AgentTaskCenter,
        ),
        CoreEngineDefinition(
            CoreEngineId.Memory,
            "Conversation memory, long-term memory, preferences, projects, goals, contacts, and learning patterns.",
            listOf("Memory Search", "Categories", "Editing", "Export", "Backup", "Encryption"),
        ),
        CoreEngineDefinition(
            CoreEngineId.Workspace,
            "Projects, code, documents, images, artifacts, archives, downloads, import, export, and file management.",
            listOf("Version Tracking", "Export", "Import", "File Management", "Project Management"),
            OpenAssistDestination.Workspace,
        ),
        CoreEngineDefinition(
            CoreEngineId.Knowledge,
            "PDF analysis, document analysis, image understanding, code understanding, collections, semantic search, Q&A, and summaries.",
            listOf("PDF Analysis", "Document Analysis", "Image Understanding", "Code Understanding", "Semantic Search", "Question Answering", "Knowledge Summaries"),
            OpenAssistDestination.KnowledgeBase,
        ),
        CoreEngineDefinition(
            CoreEngineId.Mcp,
            "MCP discovery, permissions, marketplace, Desktop Bridge, tool execution, registry, monitoring, and security validation.",
            listOf("Discovery", "Permissions", "Marketplace", "Desktop Bridge", "Tool Execution", "Registry", "Connection Monitoring", "Security Validation"),
            OpenAssistDestination.ConnectionsHub,
        ),
        CoreEngineDefinition(
            CoreEngineId.AndroidAutomation,
            "Open apps, notifications, accessibility actions, file access, system information, screen understanding, permissions, and confirmations.",
            listOf("Open Apps", "Notifications", "Accessibility", "File Access", "System Info", "Screen Understanding", "Permission Handling", "Action Confirmations"),
            OpenAssistDestination.AutomationCenter,
        ),
        CoreEngineDefinition(
            CoreEngineId.Voice,
            "Speech-to-text, text-to-speech, voice providers, downloads, settings, profiles, and future wake word support.",
            listOf("Speech-to-Text", "Text-to-Speech", "Voice Providers", "Voice Downloads", "Voice Settings", "Voice Profiles", "Wake Word (Future)"),
            OpenAssistDestination.VoiceStudio,
        ),
        CoreEngineDefinition(
            CoreEngineId.Search,
            "Single merged search layer for chats, memory, workspace, knowledge, MCPs, models, projects, files, commands, and settings.",
            listOf("Unified Ranking", "Command Results", "Settings Results", "File Results", "Model Results", "Connection Results"),
            OpenAssistDestination.UniversalSearch,
        ),
        CoreEngineDefinition(
            CoreEngineId.Notification,
            "Agent progress, downloads, model updates, workspace events, MCP events, permission requests, desktop status, and background tasks.",
            listOf("Agent Progress", "Downloads", "Model Updates", "Workspace Events", "MCP Events", "Permission Requests", "Desktop Bridge Status", "Background Tasks"),
        ),
    )

    fun engine(id: CoreEngineId): CoreEngineDefinition = engines.first { it.id == id }
}
