package com.openassist.ui.mcpplatform

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.openassist.mcp.BuiltInMcpCatalog
import com.openassist.mcp.McpClientEngine
import com.openassist.mcp.McpPermissionRequest
import com.openassist.mcp.McpServerConfig
import com.openassist.mcp.McpRiskLevel
import com.openassist.mcp.McpSafetyPolicy
import com.openassist.ui.navigation.OpenAssistDestination
import com.openassist.ui.navigation.PremiumButton
import com.openassist.ui.navigation.PremiumCard
import com.openassist.ui.navigation.PremiumColors
import com.openassist.ui.navigation.PremiumPage
import com.openassist.ui.navigation.PremiumPill
import com.openassist.ui.navigation.premiumMutedTextColor
import com.openassist.ui.navigation.premiumTextColor

@Composable
fun McpMarketplaceScreen(onBack: () -> Unit, onDetails: () -> Unit, onInstaller: () -> Unit) {
    PremiumPage("MCP Marketplace", "Browse, search, install, remove, enable, disable, and update MCP servers like apps.", OpenAssistDestination.McpMarketplace, { if (it == OpenAssistDestination.Chat) onBack() }) {
        BuiltInMcpCatalog.officialIntegrations.take(8).forEach { integration ->
            PremiumCard {
                Text(integration.name, color = premiumTextColor(), fontWeight = FontWeight.ExtraBold)
                Text("${integration.category} • v${integration.version} • ★ ${integration.rating}", color = premiumMutedTextColor())
                Text(integration.description, color = premiumMutedTextColor())
                Spacer(Modifier.height(8.dp))
                PremiumPill(if (integration.verified) "Verified" else "Community", color = PremiumColors.Success)
            }
            Spacer(Modifier.height(10.dp))
        }
        Row {
            PremiumButton("Details") { onDetails() }
            Spacer(Modifier.width(12.dp))
            PremiumButton("Installer") { onInstaller() }
        }
    }
}

@Composable
fun McpDetailsScreen(onBack: () -> Unit, onPermissions: () -> Unit) {
    PremiumPage("MCP Details", "View permissions, tools, version, schemas, health, and descriptions.", OpenAssistDestination.McpDetails, { if (it == OpenAssistDestination.McpMarketplace) onBack() }) {
        PremiumCard(selected = true) {
            Text("GitHub MCP Connected", color = premiumTextColor(), fontWeight = FontWeight.ExtraBold)
            BuiltInMcpCatalog.githubTools.forEach { tool ->
                Text("${if (tool.allowedByDefault) "✓" else "✗"} ${tool.name} — ${tool.riskLevel}", color = premiumMutedTextColor())
            }
            Spacer(Modifier.height(12.dp))
            PremiumPill("Manage Permissions", onClick = onPermissions)
        }
    }
}

@Composable
fun McpInstallerScreen(onBack: () -> Unit) {
    PremiumPage("MCP Installer", "Install, update, remove, enable, and disable MCP packages.", OpenAssistDestination.McpInstaller, { if (it == OpenAssistDestination.McpMarketplace) onBack() }) {
        PremiumCard(selected = true) {
            Text("Desktop Bridge MCP", color = premiumTextColor(), fontWeight = FontWeight.ExtraBold)
            Text("Verified package • Version 1.4.2 • Update available", color = premiumMutedTextColor())
            Spacer(Modifier.height(12.dp))
            Row {
                PremiumButton("Install") { }
                Spacer(Modifier.width(12.dp))
                PremiumButton("Disable") { }
            }
        }
    }
}

@Composable
fun McpPermissionsScreen(onBack: () -> Unit) {
    val request = McpPermissionRequest("GitHub MCP", "Create Issue", "Create an issue in the selected repository", "A new issue draft is created after approval", McpRiskLevel.Medium)
    PremiumPage("MCP Permissions", "Allow, deny, allow once, always allow, or revoke MCP tool access later.", OpenAssistDestination.McpPermissions, { if (it == OpenAssistDestination.McpDetails) onBack() }) {
        PremiumCard(selected = true) {
            Text("Permission request", color = premiumTextColor(), fontWeight = FontWeight.ExtraBold)
            Text(McpSafetyPolicy.confirmationCopy(request), color = premiumMutedTextColor())
            Spacer(Modifier.height(12.dp))
            Row {
                PremiumButton("Allow Once") { }
                Spacer(Modifier.width(12.dp))
                PremiumButton("Deny") { onBack() }
            }
        }
    }
}

@Composable
fun McpActivityCenterScreen(onBack: () -> Unit) {
    PremiumPage("MCP Activity Center", "Installed MCPs, tool usage, permission requests, failures, warnings, security events, and execution history.", OpenAssistDestination.McpActivityCenter, { if (it == OpenAssistDestination.Chat) onBack() }) {
        listOf("Installed GitHub MCP", "Permission request: Create Issue", "Warning: Desktop Bridge needs auth", "Security event: Delete Repository denied").forEach {
            PremiumCard { Text(it, color = premiumTextColor(), fontWeight = FontWeight.Bold) }
            Spacer(Modifier.height(10.dp))
        }
    }
}

@Composable
fun DesktopBridgeScreen(onBack: () -> Unit) {
    PremiumPage("Desktop Bridge", "Connect Android OpenAssist to Windows, Linux, and macOS computers for approved workflows.", OpenAssistDestination.DesktopBridge, { if (it == OpenAssistDestination.Chat) onBack() }) {
        BuiltInMcpCatalog.desktopDevices.forEach { device ->
            PremiumCard {
                Text(device.deviceName, color = premiumTextColor(), fontWeight = FontWeight.ExtraBold)
                Text("${device.operatingSystem} • ${device.status} • User: ${device.connectedUser}", color = premiumMutedTextColor())
                Text("Permissions: ${device.permissions.joinToString()}", color = premiumMutedTextColor())
            }
            Spacer(Modifier.height(10.dp))
        }
    }
}

@Composable
fun McpConnectionsScreen(onBack: () -> Unit, onMarketplace: () -> Unit, onDesktopBridge: () -> Unit) {
    val session = McpClientEngine().connect(McpServerConfig("github", "GitHub MCP", "https://api.github.com/mcp"))
    PremiumPage("MCP Connections", "Manage active built-in, custom, community, local network, and remote MCP connections.", OpenAssistDestination.McpConnections, { if (it == OpenAssistDestination.Chat) onBack() }) {
        PremiumCard(selected = true) {
            Text("Connections", color = premiumTextColor(), fontWeight = FontWeight.ExtraBold)
            Text("GitHub, Google Drive, Desktop Bridge, Home Assistant, and custom HTTPS MCP endpoints appear here.", color = premiumMutedTextColor())
            Text("${session.server.displayName}: ${session.status} • ${session.discoveredTools.size} tools", color = premiumMutedTextColor())
            Text(session.lastEvent, color = premiumMutedTextColor())
            Spacer(Modifier.height(12.dp))
            PremiumPill("Open Marketplace", onClick = onMarketplace)
            Spacer(Modifier.height(8.dp))
            PremiumPill("Desktop Bridge", onClick = onDesktopBridge)
        }
    }
}

@Composable
fun McpDeveloperCenterScreen(onBack: () -> Unit) {
    PremiumPage("MCP Developer Center", "Create, test, inspect, and publish community MCP packages.", OpenAssistDestination.McpDeveloperCenter, { if (it == OpenAssistDestination.Chat) onBack() }) {
        PremiumCard(selected = true) {
            Text("Custom MCP server", color = premiumTextColor(), fontWeight = FontWeight.ExtraBold)
            Text("Add endpoints like https://example.com/mcp or http://192.168.1.100:8000/mcp, discover tools, import schemas, test health, and define manual permissions.", color = premiumMutedTextColor())
        }
        Spacer(Modifier.height(12.dp))
        PremiumCard {
            Text("Community examples", color = premiumTextColor(), fontWeight = FontWeight.ExtraBold)
            Text(BuiltInMcpCatalog.communityExamples.joinToString(), color = premiumMutedTextColor())
        }
    }
}

@Composable
fun McpSecurityDashboardScreen(onBack: () -> Unit) {
    PremiumPage("MCP Security Dashboard", "Risk analysis, permissions overview, and security recommendations for MCP tools.", OpenAssistDestination.McpSecurityDashboard, { if (it == OpenAssistDestination.Chat) onBack() }) {
        PremiumCard(selected = true) {
            Text("Sensitive actions always require confirmation", color = premiumTextColor(), fontWeight = FontWeight.ExtraBold)
            McpSafetyPolicy.sensitiveActions.forEach { Text("• $it", color = premiumMutedTextColor()) }
            Spacer(Modifier.height(12.dp))
            PremiumPill("Strict MCP safety enabled", color = PremiumColors.Success)
        }
    }
}
