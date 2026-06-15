package com.openassist.ux

import com.openassist.ui.navigation.OpenAssistDestination

enum class PrimaryTab(val label: String, val destination: OpenAssistDestination) {
    Home("Home", OpenAssistDestination.Chat),
    Workspace("Workspace", OpenAssistDestination.Workspace),
    Knowledge("Knowledge", OpenAssistDestination.KnowledgeBase),
    Connections("Connections", OpenAssistDestination.ConnectionsHub),
    Settings("Settings", OpenAssistDestination.Settings),
}

data class QuickAction(val title: String, val description: String, val destination: OpenAssistDestination)

data class SearchResultGroup(val title: String, val examples: List<String>)

data class CommandAction(val command: String, val destination: OpenAssistDestination, val description: String)

data class ContextSidebarStatus(val label: String, val value: String)

object ExperienceCatalog {
    val homeQuickActions = listOf(
        QuickAction("Generate Image", "Create images in Image Studio.", OpenAssistDestination.ImageStudio),
        QuickAction("Analyze Screen", "Summarize the visible screen.", OpenAssistDestination.ScreenIntelligence),
        QuickAction("Run Python", "Open the sandboxed Python runner.", OpenAssistDestination.PythonRunner),
        QuickAction("Open Workspace", "Browse projects and artifacts.", OpenAssistDestination.Workspace),
        QuickAction("Create Project", "Start a project in Workspace.", OpenAssistDestination.ProjectExplorer),
        QuickAction("Knowledge Search", "Ask My Knowledge.", OpenAssistDestination.KnowledgeBase),
        QuickAction("Desktop Bridge", "Control paired computers safely.", OpenAssistDestination.DesktopBridge),
        QuickAction("MCP Marketplace", "Install integrations.", OpenAssistDestination.McpMarketplace),
    )

    val recentActivity = listOf("OpenAssist Project", "local_ai_models.md", "Screen summary automation", "GitHub MCP action")

    val workspaceCategories = listOf("Projects", "Documents", "Code", "Images", "Archives", "Downloads", "Artifacts")

    val knowledgeSections = listOf("Memory", "Knowledge Base", "Uploaded Files", "Learning Insights", "Saved Research", "Projects")

    val connectionSections = listOf("OpenRouter", "Local Models", "MCP Marketplace", "Desktop Bridge", "Connected Services", "Installed Integrations")

    val settingsSections = listOf("Appearance", "Voice", "Memory", "Permissions", "Privacy", "Storage", "Developer Options", "AI Settings", "Safety Settings", "Accessibility")

    val universalSearchGroups = listOf(
        SearchResultGroup("Chats", listOf("Recent conversations", "Assistant messages")),
        SearchResultGroup("Workspace", listOf("Projects", "Documents", "Images", "Python files")),
        SearchResultGroup("Knowledge", listOf("School Notes", "Research Papers", "Memory")),
        SearchResultGroup("Connections", listOf("GitHub", "OpenRouter", "Desktop Bridge", "MCP Servers")),
        SearchResultGroup("Commands", listOf("Run Python", "Switch Model", "Export Project")),
    )

    val commands = listOf(
        CommandAction("Create Project", OpenAssistDestination.ProjectExplorer, "Create a Workspace project."),
        CommandAction("Generate Image", OpenAssistDestination.ImageStudio, "Open Image Studio."),
        CommandAction("Run Python", OpenAssistDestination.PythonRunner, "Launch sandboxed Python."),
        CommandAction("Summarize Screen", OpenAssistDestination.ScreenIntelligence, "Analyze visible screen content."),
        CommandAction("Connect GitHub", OpenAssistDestination.McpMarketplace, "Find the GitHub MCP integration."),
        CommandAction("Switch Model", OpenAssistDestination.ModelSelection, "Choose an AI model."),
        CommandAction("Open Workspace", OpenAssistDestination.Workspace, "Browse generated artifacts."),
        CommandAction("Browse MCP Marketplace", OpenAssistDestination.McpMarketplace, "Install integrations."),
        CommandAction("New Knowledge Collection", OpenAssistDestination.KnowledgeBase, "Create a My Knowledge collection."),
        CommandAction("Export Project", OpenAssistDestination.ZipExportCenter, "Export a project ZIP."),
        CommandAction("Core Platform", OpenAssistDestination.CorePlatform, "Inspect OpenAssist platform engines."),
        CommandAction("Safety Settings", OpenAssistDestination.SafetySettings, "Review confirmation and safety controls."),
        CommandAction("Assistant Permissions", OpenAssistDestination.AssistantPermissions, "Review Android assistant permissions."),
        CommandAction("MCP Activity", OpenAssistDestination.McpActivityCenter, "Monitor MCP events and tool activity."),
        CommandAction("MCP Developer Center", OpenAssistDestination.McpDeveloperCenter, "Build and test MCP integrations."),
        CommandAction("MCP Security", OpenAssistDestination.McpSecurityDashboard, "Review MCP risks and permissions."),
        CommandAction("Voice Studio", OpenAssistDestination.VoiceStudio, "Configure voice providers and previews."),
        CommandAction("Storage Manager", OpenAssistDestination.StorageManager, "Manage local models, artifacts, and cache."),
        CommandAction("Tool Approval", OpenAssistDestination.ToolApproval, "Review sensitive action approvals."),
    )

    val contextStatuses = listOf(
        ContextSidebarStatus("Current Model", "GPT-5.4 Thinking"),
        ContextSidebarStatus("Memory", "Enabled"),
        ContextSidebarStatus("Agent", "1 plan waiting"),
        ContextSidebarStatus("Workspace", "5 recent artifacts"),
        ContextSidebarStatus("MCP", "3 connected"),
        ContextSidebarStatus("Desktop", "No active device"),
        ContextSidebarStatus("Storage", "Healthy"),
    )
}
