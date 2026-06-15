package com.openassist.core

import java.time.Instant

enum class SensitiveActionType(val label: String) {
    Message("Message"),
    Call("Call"),
    FileChange("File Change"),
    Payment("Payment"),
    ExternalSharing("External Sharing"),
    CommandExecution("Command Execution"),
    McpAction("MCP Action"),
}

data class SecurityCheckResult(
    val permissionCheck: Boolean,
    val safetyCheck: Boolean,
    val confirmationCheck: Boolean,
    val executionCheck: Boolean,
) {
    val allowed: Boolean = permissionCheck && safetyCheck && confirmationCheck && executionCheck
}

data class AuditLogEntry(
    val actionType: SensitiveActionType,
    val engineId: CoreEngineId,
    val summary: String,
    val result: SecurityCheckResult,
    val timestamp: Instant = Instant.now(),
)

object SecurityLayer {
    fun evaluateSensitiveAction(
        actionType: SensitiveActionType,
        hasPermission: Boolean,
        passesSafety: Boolean,
        userConfirmed: Boolean,
    ): SecurityCheckResult = SecurityCheckResult(
        permissionCheck = hasPermission,
        safetyCheck = passesSafety,
        confirmationCheck = userConfirmed,
        executionCheck = hasPermission && passesSafety && userConfirmed,
    )

    val sensitiveActions = SensitiveActionType.values().toList()
}
