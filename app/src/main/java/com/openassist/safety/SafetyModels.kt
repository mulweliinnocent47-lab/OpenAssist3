package com.openassist.safety

enum class PermissionLevel(val label: String, val description: String) {
    Safe("Level 1 · Safe", "Read screen, open apps, and read notifications after user approval."),
    Interactive("Level 2 · Interactive", "Prepare messages, calls, sharing, and app interactions with confirmation."),
    Advanced("Level 3 · Advanced", "File modifications, system automation, and accessibility actions with explicit approval."),
}

enum class RiskLevel { Low, Medium, High }

data class SafetySettings(
    val confirmMessages: Boolean = true,
    val confirmCalls: Boolean = true,
    val confirmFileActions: Boolean = true,
    val confirmAutomation: Boolean = true,
    val confirmAppControl: Boolean = true,
    val confirmExternalSharing: Boolean = true,
    val advancedFileAccess: Boolean = false,
    val permissionLevel: PermissionLevel = PermissionLevel.Safe,
)

data class AssistantAction(
    val app: String,
    val action: String,
    val recipient: String? = null,
    val content: String? = null,
    val riskLevel: RiskLevel = RiskLevel.Medium,
    val permissionLevel: PermissionLevel = PermissionLevel.Interactive,
    val sensitive: Boolean = true,
)

object AssistantBehaviorRules {
    val allowed = listOf("Read", "Explain", "Summarize", "Suggest", "Prepare actions")
    val blockedWithoutConfirmation = listOf(
        "Delete files",
        "Send messages",
        "Make calls",
        "Modify files",
        "Post content",
        "Change settings",
        "Access contacts",
        "Open external links",
    )

    fun requiresConfirmation(action: AssistantAction, settings: SafetySettings = SafetySettings()): Boolean {
        if (!action.sensitive) return false
        return when {
            action.action.contains("message", ignoreCase = true) -> settings.confirmMessages
            action.action.contains("call", ignoreCase = true) -> settings.confirmCalls
            action.action.contains("file", ignoreCase = true) -> settings.confirmFileActions
            action.action.contains("share", ignoreCase = true) -> settings.confirmExternalSharing
            action.action.contains("open", ignoreCase = true) -> settings.confirmAppControl
            else -> settings.confirmAutomation
        }
    }
}
