package com.openassist.mcp

enum class McpCategory { Productivity, Developer, Communication, SmartHome, Media, Storage, Desktop, Community, AI }
enum class McpConnectionStatus { Connected, Disconnected, NeedsAuth, Warning }
enum class McpPermissionDecision { Allow, Deny, AllowOnce, AlwaysAllow, RevokeLater }
enum class McpRiskLevel { Low, Medium, High, Critical }

data class McpIntegration(
    val name: String,
    val category: McpCategory,
    val verified: Boolean = true,
    val version: String = "1.0.0",
    val rating: Double = 4.8,
    val status: McpConnectionStatus = McpConnectionStatus.Disconnected,
    val description: String,
)

data class McpToolCapability(
    val name: String,
    val description: String,
    val allowedByDefault: Boolean,
    val riskLevel: McpRiskLevel,
)

data class McpPermissionRequest(
    val mcpName: String,
    val toolName: String,
    val requestedAction: String,
    val expectedResult: String,
    val riskLevel: McpRiskLevel,
)

data class DesktopBridgeDevice(
    val deviceName: String,
    val operatingSystem: String,
    val status: McpConnectionStatus,
    val permissions: List<String>,
    val connectedUser: String,
)

object BuiltInMcpCatalog {
    val officialIntegrations = listOf(
        "GitHub", "Google Drive", "Notion", "Discord", "Telegram", "Slack", "Home Assistant", "Google Calendar", "Gmail",
        "Dropbox", "OneDrive", "Jira", "Linear", "YouTube", "Reddit", "Spotify", "OpenAI", "OpenRouter", "Hugging Face",
    ).mapIndexed { index, name ->
        McpIntegration(
            name = name,
            category = when (name) {
                "GitHub", "Jira", "Linear" -> McpCategory.Developer
                "Discord", "Telegram", "Slack", "Gmail" -> McpCategory.Communication
                "Google Drive", "Dropbox", "OneDrive" -> McpCategory.Storage
                "Home Assistant" -> McpCategory.SmartHome
                "YouTube", "Reddit", "Spotify" -> McpCategory.Media
                "OpenAI", "OpenRouter", "Hugging Face" -> McpCategory.AI
                else -> McpCategory.Productivity
            },
            version = "1.${index}.0",
            description = "$name connection with discovered tools, permissions, and activity logs.",
        )
    }

    val githubTools = listOf(
        McpToolCapability("Read Repository", "Read repository metadata and files.", true, McpRiskLevel.Low),
        McpToolCapability("Create Issue", "Create a GitHub issue after approval.", true, McpRiskLevel.Medium),
        McpToolCapability("Create Repository", "Create a new repository after confirmation.", false, McpRiskLevel.High),
        McpToolCapability("Delete Repository", "Destructive repository deletion; denied by default.", false, McpRiskLevel.Critical),
    )

    val desktopDevices = listOf(
        DesktopBridgeDevice("Mulweli-PC", "Windows", McpConnectionStatus.Connected, listOf("Launch apps", "Transfer files"), "mulweli"),
        DesktopBridgeDevice("Gaming-PC", "Windows", McpConnectionStatus.NeedsAuth, listOf("System status only"), "local"),
        DesktopBridgeDevice("Office-Laptop", "macOS", McpConnectionStatus.Disconnected, listOf("Read files"), "work"),
        DesktopBridgeDevice("Workstation", "Linux", McpConnectionStatus.Warning, listOf("Run scripts requires approval"), "dev"),
    )

    val communityExamples = listOf("Netflix MCP", "Steam MCP", "Minecraft MCP", "Shopify MCP", "WordPress MCP", "X MCP", "TikTok MCP", "Trading MCP", "Crypto MCP", "School MCP", "Company MCP")
}

data class McpServerConfig(
    val id: String,
    val displayName: String,
    val endpoint: String,
    val enabled: Boolean = true,
)

data class McpClientSession(
    val server: McpServerConfig,
    val status: McpConnectionStatus,
    val discoveredTools: List<McpToolCapability>,
    val lastEvent: String,
)

class McpClientEngine {
    fun connect(server: McpServerConfig): McpClientSession {
        val safeEndpoint = server.endpoint.trim()
        val status = when {
            !server.enabled -> McpConnectionStatus.Disconnected
            safeEndpoint.startsWith("https://") || safeEndpoint.startsWith("http://127.0.0.1") || safeEndpoint.startsWith("http://localhost") -> McpConnectionStatus.Connected
            safeEndpoint.startsWith("http://") -> McpConnectionStatus.Warning
            else -> McpConnectionStatus.NeedsAuth
        }
        return McpClientSession(
            server = server,
            status = status,
            discoveredTools = if (server.displayName.contains("GitHub", ignoreCase = true)) BuiltInMcpCatalog.githubTools else emptyList(),
            lastEvent = "Initialized MCP JSON-RPC session for ${server.displayName} with status $status.",
        )
    }

    fun permissionDecision(request: McpPermissionRequest): McpPermissionDecision = when (request.riskLevel) {
        McpRiskLevel.Low -> McpPermissionDecision.Allow
        McpRiskLevel.Medium -> McpPermissionDecision.AllowOnce
        McpRiskLevel.High, McpRiskLevel.Critical -> McpPermissionDecision.Deny
    }
}
