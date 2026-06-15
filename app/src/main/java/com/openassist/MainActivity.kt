package com.openassist

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import com.openassist.data.local.SecureStorage
import com.openassist.ui.about.AboutScreen
import com.openassist.ui.agent.AgentEngineScreen
import com.openassist.ui.assistantpermissions.AssistantPermissionsDashboard
import com.openassist.ui.automation.AutomationCenterScreen
import com.openassist.ui.safety.ActionConfirmationScreen
import com.openassist.ui.safety.SafetySettingsScreen
import com.openassist.ui.screenintelligence.ScreenIntelligenceScreen
import com.openassist.ui.voice.VoiceDownloadsScreen
import com.openassist.ui.voice.VoiceMarketplaceScreen
import com.openassist.ui.voice.VoiceOverlayScreen
import com.openassist.ui.voice.VoicePreviewScreen
import com.openassist.ui.voice.VoiceSettingsScreen
import com.openassist.ui.voice.VoiceStudioScreen
import com.openassist.ui.workspace.ArtifactManagerScreen
import com.openassist.ui.workspace.CodeCanvasScreen
import com.openassist.ui.workspace.DocumentEditorScreen
import com.openassist.ui.workspace.FileViewerScreen
import com.openassist.ui.workspace.ImageStudioScreen
import com.openassist.ui.workspace.OutputConsoleScreen
import com.openassist.ui.workspace.ProjectExplorerScreen
import com.openassist.ui.workspace.PythonRunnerScreen
import com.openassist.ui.workspace.WorkspaceScreen
import com.openassist.ui.workspace.ZipExportCenterScreen
import com.openassist.ui.aimode.AiModeSelectorScreen
import com.openassist.ui.chat.ChatScreen
import com.openassist.ui.downloads.DownloadManagerScreen
import com.openassist.ui.downloads.ModelDownloadScreen
import com.openassist.ui.experience.AgentTaskCenterScreen
import com.openassist.ui.experience.CommandPaletteScreen
import com.openassist.ui.experience.ConnectionsHubScreen
import com.openassist.ui.experience.ContextSidebarScreen
import com.openassist.ui.experience.CorePlatformScreen
import com.openassist.ui.experience.UniversalSearchScreen
import com.openassist.ui.history.ConversationHistoryScreen
import com.openassist.ui.knowledge.KnowledgeBaseScreen
import com.openassist.ui.localmodels.LocalModelsScreen
import com.openassist.ui.mcp.MCPServerScreen
import com.openassist.ui.mcpplatform.DesktopBridgeScreen
import com.openassist.ui.mcpplatform.McpActivityCenterScreen
import com.openassist.ui.mcpplatform.McpConnectionsScreen
import com.openassist.ui.mcpplatform.McpDetailsScreen
import com.openassist.ui.mcpplatform.McpDeveloperCenterScreen
import com.openassist.ui.mcpplatform.McpInstallerScreen
import com.openassist.ui.mcpplatform.McpMarketplaceScreen
import com.openassist.ui.mcpplatform.McpPermissionsScreen
import com.openassist.ui.mcpplatform.McpSecurityDashboardScreen
import com.openassist.ui.model.ModelSelectionScreen
import com.openassist.ui.navigation.OpenAssistDestination
import com.openassist.ui.onboarding.OnboardingScreen
import com.openassist.ui.permissions.PermissionScreen
import com.openassist.ui.settings.SettingsScreen
import com.openassist.ui.splash.SplashScreen
import com.openassist.ui.storage.StorageManagerScreen
import com.openassist.ui.support.AboutOpenAssistScreen
import com.openassist.ui.support.AgentCenterScreen
import com.openassist.ui.support.ContactSupportScreen
import com.openassist.ui.support.ConversationLibraryScreen
import com.openassist.ui.support.FeedbackCenterScreen
import com.openassist.ui.support.HelpCenterScreen
import com.openassist.ui.support.PermissionsCenterScreen
import com.openassist.ui.support.PrivacyCenterScreen
import com.openassist.ui.support.ReportBugScreen
import com.openassist.ui.support.SmartOnboardingScreen
import com.openassist.ui.support.StorageAnalyzerScreen
import com.openassist.ui.support.SupportTrustCenterScreen
import com.openassist.ui.support.TipsRecommendationsScreen
import com.openassist.ui.support.WhatsNewScreen
import com.openassist.ui.tools.ToolApprovalScreen
import com.openassist.viewmodel.ChatViewModel
import com.openassist.viewmodel.SettingsViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val app = application as OpenAssistApp

        val permissionLauncher = registerForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions(),
        ) { results ->
            app.permissionManager.onResult(results)
        }
        app.permissionManager.attachLauncher(permissionLauncher)

        val storage = SecureStorage(this)

        setContent {
            MaterialTheme(colorScheme = lightColorScheme()) {
                val settings: SettingsViewModel = viewModel(factory = settingsFactory(storage))
                val chat: ChatViewModel = viewModel(factory = chatFactory(storage, app))
                OpenAssistNavigation(settings = settings, chat = chat)
            }
        }
    }
}

@Composable
private fun OpenAssistNavigation(settings: SettingsViewModel, chat: ChatViewModel) {
    val apiKey by settings.apiKey.collectAsState()
    var destination by remember { mutableStateOf(OpenAssistDestination.Splash) }

    fun openChatOrOnboarding() {
        destination = if (apiKey.isBlank()) OpenAssistDestination.Onboarding else OpenAssistDestination.Chat
    }

    when (destination) {
        OpenAssistDestination.Splash -> SplashScreen(onContinue = ::openChatOrOnboarding)
        OpenAssistDestination.Onboarding -> OnboardingScreen(onContinue = { destination = OpenAssistDestination.AiModeSelector })
        OpenAssistDestination.Chat -> ChatScreen(
            chatViewModel = chat,
            onSettings = { destination = OpenAssistDestination.Settings },
            onModels = { destination = OpenAssistDestination.ModelSelection },
            onPermissions = { destination = OpenAssistDestination.Permissions },
            onToolApproval = { destination = OpenAssistDestination.ToolApproval },
            onMcpServers = { destination = OpenAssistDestination.MCPServers },
            onHistory = { destination = OpenAssistDestination.ConversationHistory },
            onAbout = { destination = OpenAssistDestination.About },
            onScreenIntelligence = { destination = OpenAssistDestination.ScreenIntelligence },
            onAutomationCenter = { destination = OpenAssistDestination.AutomationCenter },
            onVoiceOverlay = { destination = OpenAssistDestination.VoiceOverlay },
            onVoiceStudio = { destination = OpenAssistDestination.VoiceStudio },
            onWorkspace = { destination = OpenAssistDestination.Workspace },
            onImageStudio = { destination = OpenAssistDestination.ImageStudio },
            onCodeCanvas = { destination = OpenAssistDestination.CodeCanvas },
            onMcpMarketplace = { destination = OpenAssistDestination.McpMarketplace },
            onMcpConnections = { destination = OpenAssistDestination.McpConnections },
            onConnectionsHub = { destination = OpenAssistDestination.ConnectionsHub },
            onDesktopBridge = { destination = OpenAssistDestination.DesktopBridge },
            onAgentEngine = { destination = OpenAssistDestination.AgentEngine },
            onKnowledgeBase = { destination = OpenAssistDestination.KnowledgeBase },
            onUniversalSearch = { destination = OpenAssistDestination.UniversalSearch },
            onCommandPalette = { destination = OpenAssistDestination.CommandPalette },
            onAgentTaskCenter = { destination = OpenAssistDestination.AgentTaskCenter },
            onContextSidebar = { destination = OpenAssistDestination.ContextSidebar },
            onSupportTrust = { destination = OpenAssistDestination.SupportTrustCenter },
        )
        OpenAssistDestination.UniversalSearch -> UniversalSearchScreen(
            onBack = { destination = OpenAssistDestination.Chat },
            onCommandPalette = { destination = OpenAssistDestination.CommandPalette },
        )
        OpenAssistDestination.CommandPalette -> CommandPaletteScreen(
            onBack = { destination = OpenAssistDestination.Chat },
            onNavigate = { destination = it },
        )
        OpenAssistDestination.AgentTaskCenter -> AgentTaskCenterScreen(
            onBack = { destination = OpenAssistDestination.Chat },
            onAgentEngine = { destination = OpenAssistDestination.AgentEngine },
        )
        OpenAssistDestination.ContextSidebar -> ContextSidebarScreen(onBack = { destination = OpenAssistDestination.Chat })
        OpenAssistDestination.ConnectionsHub -> ConnectionsHubScreen(
            onBack = { destination = OpenAssistDestination.Chat },
            onMarketplace = { destination = OpenAssistDestination.McpMarketplace },
            onDesktopBridge = { destination = OpenAssistDestination.DesktopBridge },
            onModels = { destination = OpenAssistDestination.ModelSelection },
        )
        OpenAssistDestination.CorePlatform -> CorePlatformScreen(onBack = { destination = OpenAssistDestination.Settings })
        OpenAssistDestination.Settings -> SettingsScreen(
            viewModel = settings,
            onBack = { destination = OpenAssistDestination.Chat },
            onModelSelection = { destination = OpenAssistDestination.ModelSelection },
            onAiMode = { destination = OpenAssistDestination.AiModeSelector },
            onSafetySettings = { destination = OpenAssistDestination.SafetySettings },
            onAssistantPermissions = { destination = OpenAssistDestination.AssistantPermissions },
            onNavigate = { destination = it },
        )
        OpenAssistDestination.ModelSelection -> ModelSelectionScreen(
            viewModel = settings,
            onBack = { destination = OpenAssistDestination.Settings },
        )
        OpenAssistDestination.Permissions -> PermissionScreen(onBack = { destination = OpenAssistDestination.Chat })
        OpenAssistDestination.ToolApproval -> ToolApprovalScreen(onBack = { destination = OpenAssistDestination.Chat })
        OpenAssistDestination.MCPServers -> MCPServerScreen(onBack = { destination = OpenAssistDestination.Chat })
        OpenAssistDestination.ConversationHistory -> ConversationHistoryScreen(
            onBack = { destination = OpenAssistDestination.Chat },
            onNewChat = { destination = OpenAssistDestination.Chat },
        )
        OpenAssistDestination.About -> AboutScreen(onBack = { destination = OpenAssistDestination.Chat })
        OpenAssistDestination.AiModeSelector -> AiModeSelectorScreen(
            viewModel = settings,
            onBack = { destination = OpenAssistDestination.Chat },
            onLocalModels = { destination = OpenAssistDestination.LocalModels },
        )
        OpenAssistDestination.LocalModels -> LocalModelsScreen(
            viewModel = settings,
            onBack = { destination = OpenAssistDestination.AiModeSelector },
            onDownloadModels = { destination = OpenAssistDestination.ModelDownload },
            onStorage = { destination = OpenAssistDestination.StorageManager },
        )
        OpenAssistDestination.ModelDownload -> ModelDownloadScreen(
            onBack = { destination = OpenAssistDestination.LocalModels },
            onDownloads = { destination = OpenAssistDestination.DownloadManager },
        )
        OpenAssistDestination.DownloadManager -> DownloadManagerScreen(onBack = { destination = OpenAssistDestination.ModelDownload })
        OpenAssistDestination.StorageManager -> StorageManagerScreen(onBack = { destination = OpenAssistDestination.LocalModels })
        OpenAssistDestination.ScreenIntelligence -> ScreenIntelligenceScreen(onBack = { destination = OpenAssistDestination.Chat })
        OpenAssistDestination.ActionConfirmation -> ActionConfirmationScreen(onBack = { destination = OpenAssistDestination.Chat })
        OpenAssistDestination.AutomationCenter -> AutomationCenterScreen(onBack = { destination = OpenAssistDestination.Chat })
        OpenAssistDestination.SafetySettings -> SafetySettingsScreen(onBack = { destination = OpenAssistDestination.Settings })
        OpenAssistDestination.AssistantPermissions -> AssistantPermissionsDashboard(onBack = { destination = OpenAssistDestination.Settings })
        OpenAssistDestination.VoiceOverlay -> VoiceOverlayScreen(onBack = { destination = OpenAssistDestination.Chat })
        OpenAssistDestination.VoiceStudio -> VoiceStudioScreen(
            onBack = { destination = OpenAssistDestination.Chat },
            onMarketplace = { destination = OpenAssistDestination.VoiceMarketplace },
            onDownloads = { destination = OpenAssistDestination.VoiceDownloads },
            onSettings = { destination = OpenAssistDestination.VoiceSettings },
            onPreview = { destination = OpenAssistDestination.VoicePreview },
        )
        OpenAssistDestination.VoiceMarketplace -> VoiceMarketplaceScreen(
            onBack = { destination = OpenAssistDestination.VoiceStudio },
            onPreview = { destination = OpenAssistDestination.VoicePreview },
            onDownloads = { destination = OpenAssistDestination.VoiceDownloads },
        )
        OpenAssistDestination.VoiceDownloads -> VoiceDownloadsScreen(onBack = { destination = OpenAssistDestination.VoiceStudio })
        OpenAssistDestination.VoiceSettings -> VoiceSettingsScreen(
            onBack = { destination = OpenAssistDestination.VoiceStudio },
            onPreview = { destination = OpenAssistDestination.VoicePreview },
        )
        OpenAssistDestination.VoicePreview -> VoicePreviewScreen(
            onBack = { destination = OpenAssistDestination.VoiceStudio },
            onSettings = { destination = OpenAssistDestination.VoiceSettings },
        )
        OpenAssistDestination.Workspace -> WorkspaceScreen(
            onBack = { destination = OpenAssistDestination.Chat },
            onCodeCanvas = { destination = OpenAssistDestination.CodeCanvas },
            onImageStudio = { destination = OpenAssistDestination.ImageStudio },
            onProjectExplorer = { destination = OpenAssistDestination.ProjectExplorer },
            onDocumentEditor = { destination = OpenAssistDestination.DocumentEditor },
            onArtifactManager = { destination = OpenAssistDestination.ArtifactManager },
            onNavigate = { destination = it },
        )
        OpenAssistDestination.ImageStudio -> ImageStudioScreen(onBack = { destination = OpenAssistDestination.Workspace })
        OpenAssistDestination.CodeCanvas -> CodeCanvasScreen(
            onBack = { destination = OpenAssistDestination.Workspace },
            onPythonRunner = { destination = OpenAssistDestination.PythonRunner },
            onConsole = { destination = OpenAssistDestination.OutputConsole },
        )
        OpenAssistDestination.PythonRunner -> PythonRunnerScreen(
            onBack = { destination = OpenAssistDestination.CodeCanvas },
            onConsole = { destination = OpenAssistDestination.OutputConsole },
        )
        OpenAssistDestination.DocumentEditor -> DocumentEditorScreen(onBack = { destination = OpenAssistDestination.Workspace })
        OpenAssistDestination.ProjectExplorer -> ProjectExplorerScreen(
            onBack = { destination = OpenAssistDestination.Workspace },
            onFileViewer = { destination = OpenAssistDestination.FileViewer },
            onZipExport = { destination = OpenAssistDestination.ZipExportCenter },
        )
        OpenAssistDestination.FileViewer -> FileViewerScreen(onBack = { destination = OpenAssistDestination.ProjectExplorer })
        OpenAssistDestination.ArtifactManager -> ArtifactManagerScreen(onBack = { destination = OpenAssistDestination.Workspace })
        OpenAssistDestination.ZipExportCenter -> ZipExportCenterScreen(onBack = { destination = OpenAssistDestination.ProjectExplorer })
        OpenAssistDestination.OutputConsole -> OutputConsoleScreen(onBack = { destination = OpenAssistDestination.PythonRunner })
        OpenAssistDestination.McpMarketplace -> McpMarketplaceScreen(
            onBack = { destination = OpenAssistDestination.Chat },
            onDetails = { destination = OpenAssistDestination.McpDetails },
            onInstaller = { destination = OpenAssistDestination.McpInstaller },
        )
        OpenAssistDestination.McpDetails -> McpDetailsScreen(
            onBack = { destination = OpenAssistDestination.McpMarketplace },
            onPermissions = { destination = OpenAssistDestination.McpPermissions },
        )
        OpenAssistDestination.McpInstaller -> McpInstallerScreen(onBack = { destination = OpenAssistDestination.McpMarketplace })
        OpenAssistDestination.McpPermissions -> McpPermissionsScreen(onBack = { destination = OpenAssistDestination.McpDetails })
        OpenAssistDestination.McpActivityCenter -> McpActivityCenterScreen(onBack = { destination = OpenAssistDestination.Chat })
        OpenAssistDestination.DesktopBridge -> DesktopBridgeScreen(onBack = { destination = OpenAssistDestination.Chat })
        OpenAssistDestination.McpConnections -> McpConnectionsScreen(
            onBack = { destination = OpenAssistDestination.Chat },
            onMarketplace = { destination = OpenAssistDestination.McpMarketplace },
            onDesktopBridge = { destination = OpenAssistDestination.DesktopBridge },
        )
        OpenAssistDestination.McpDeveloperCenter -> McpDeveloperCenterScreen(onBack = { destination = OpenAssistDestination.Chat })
        OpenAssistDestination.McpSecurityDashboard -> McpSecurityDashboardScreen(onBack = { destination = OpenAssistDestination.Chat })
        OpenAssistDestination.AgentEngine -> AgentEngineScreen(onBack = { destination = OpenAssistDestination.Chat })
        OpenAssistDestination.KnowledgeBase -> KnowledgeBaseScreen(
            onBack = { destination = OpenAssistDestination.Chat },
            onNavigate = { destination = it },
        )
        OpenAssistDestination.SupportTrustCenter -> SupportTrustCenterScreen(
            onBack = { destination = OpenAssistDestination.Settings },
            onNavigate = { destination = it },
        )
        OpenAssistDestination.FeedbackCenter -> FeedbackCenterScreen(onBack = { destination = OpenAssistDestination.SupportTrustCenter })
        OpenAssistDestination.ReportBug -> ReportBugScreen(onBack = { destination = OpenAssistDestination.SupportTrustCenter })
        OpenAssistDestination.HelpCenter -> HelpCenterScreen(onBack = { destination = OpenAssistDestination.SupportTrustCenter })
        OpenAssistDestination.AboutOpenAssist -> AboutOpenAssistScreen(onBack = { destination = OpenAssistDestination.SupportTrustCenter })
        OpenAssistDestination.ContactSupport -> ContactSupportScreen(onBack = { destination = OpenAssistDestination.SupportTrustCenter })
        OpenAssistDestination.PrivacyCenter -> PrivacyCenterScreen(onBack = { destination = OpenAssistDestination.SupportTrustCenter })
        OpenAssistDestination.PermissionsCenter -> PermissionsCenterScreen(onBack = { destination = OpenAssistDestination.SupportTrustCenter })
        OpenAssistDestination.ConversationLibrary -> ConversationLibraryScreen(onBack = { destination = OpenAssistDestination.SupportTrustCenter })
        OpenAssistDestination.AgentCenter -> AgentCenterScreen(onBack = { destination = OpenAssistDestination.SupportTrustCenter })
        OpenAssistDestination.WhatsNew -> WhatsNewScreen(onBack = { destination = OpenAssistDestination.SupportTrustCenter })
        OpenAssistDestination.SmartOnboarding -> SmartOnboardingScreen(onBack = { destination = OpenAssistDestination.SupportTrustCenter })
        OpenAssistDestination.TipsRecommendations -> TipsRecommendationsScreen(onBack = { destination = OpenAssistDestination.SupportTrustCenter })
        OpenAssistDestination.StorageAnalyzer -> StorageAnalyzerScreen(onBack = { destination = OpenAssistDestination.SupportTrustCenter })
    }
}

private fun settingsFactory(storage: SecureStorage) =
    object : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T =
            SettingsViewModel(storage) as T
    }

private fun chatFactory(storage: SecureStorage, app: OpenAssistApp) =
    object : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T =
            ChatViewModel(storage, app.toolEngine) as T
    }
