package com.openassist.tools

import com.openassist.PermissionManager
import com.openassist.core.UserFacingErrors
import com.openassist.data.openrouter.ToolDefinition
import com.openassist.data.openrouter.ToolFunction

class ToolEngine(
    tools: List<Tool>,
    private val permissionManager: PermissionManager,
) {
    private val registry: Map<String, Tool> = tools.associateBy { it.name }

    /**
     * Runs a tool by name, enforcing two gates in order:
     *
     * 1. **Sensitive-action confirmation** — if [Tool.sensitive] is `true`
     *    and [confirmed] is `false`, returns a [ToolResult] with
     *    [ToolResult.requiresConfirmation] set so the caller can ask the
     *    user before retrying with `confirmed = true`.
     *
     * 2. **Runtime permissions** — only after explicit action confirmation,
     *    any missing Android permission listed in [Tool.requiredPermissions]
     *    is requested. If the user denies any permission the call returns a
     *    descriptive [ToolResult] without calling [Tool.run].
     */
    suspend fun execute(request: ToolRequest, confirmed: Boolean = false): ToolResult {
        val tool = registry[request.name]
            ?: return ToolResult(request.name, "Unknown tool: '${request.name}'.")

        // ── Gate 1: sensitive-action confirmation ─────────────────────────────
        // Confirmation is checked before runtime permission requests so the
        // assistant cannot prompt for or use sensitive capabilities until the
        // user has explicitly approved the action summary.
        if (tool.sensitive && !confirmed) {
            return ToolResult(
                name                  = tool.name,
                output                = "Confirmation required before running '${tool.name}' (${tool.permissionLevel.label}). OpenAssist may prepare this action, but the user must approve final execution.",
                requiresConfirmation  = true,
            )
        }

        // ── Gate 2: runtime permissions ───────────────────────────────────────
        val needed = tool.requiredPermissions
        if (needed.isNotEmpty()) {
            val granted = permissionManager.require(*needed.toTypedArray())
            if (!granted) {
                val denied = needed
                    .filter { !permissionManager.hasPermission(it) }
                    .map { it.substringAfterLast('.') }
                return ToolResult(
                    name   = tool.name,
                    output = UserFacingErrors.permissionDenied(denied),
                )
            }
        }

        return tool.run(request.arguments)
    }

    fun availableTools(): List<Tool> = registry.values.toList()

    /** Converts the tool registry into the API's tool-definition format. */
    fun toolDefinitions(): List<ToolDefinition> = registry.values.map { tool ->
        ToolDefinition(
            function = ToolFunction(
                name        = tool.name,
                description = tool.description,
                parameters  = tool.parameterSchema,
            ),
        )
    }
}
