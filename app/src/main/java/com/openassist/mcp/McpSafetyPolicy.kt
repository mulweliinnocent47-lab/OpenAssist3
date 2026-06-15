package com.openassist.mcp

object McpSafetyPolicy {
    val sensitiveActions = listOf(
        "Deleting files", "Sending emails", "Sending messages", "Running commands", "Executing scripts", "Making purchases", "Modifying repositories",
    )

    fun confirmationCopy(request: McpPermissionRequest): String = """
        Tool Name: ${request.toolName}
        MCP Name: ${request.mcpName}
        Requested Action: ${request.requestedAction}
        Expected Result: ${request.expectedResult}
        Risk Level: ${request.riskLevel}

        OpenAssist can prepare this MCP action, but execution requires Approve or Cancel from the user.
    """.trimIndent()
}
