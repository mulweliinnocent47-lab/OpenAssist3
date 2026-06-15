package com.openassist.workspace

data class PythonSafetyPolicy(
    val allowFileDeletion: Boolean = false,
    val allowSystemModification: Boolean = false,
    val allowRootAccess: Boolean = false,
    val allowNetwork: Boolean = false,
    val allowBackgroundExecution: Boolean = false,
) {
    fun approvalSummary(request: PythonRunRequest): String = """
        Script Name: ${request.scriptName}
        Estimated Runtime: ${request.estimatedRuntime}
        Resource Usage: ${request.resourceUsage}
        Network Access: ${if (request.networkAllowed && allowNetwork) "Allowed" else "Blocked by default"}
        Restrictions: no file deletion, no system modification, no root access, no background execution without approval.
    """.trimIndent()
}
