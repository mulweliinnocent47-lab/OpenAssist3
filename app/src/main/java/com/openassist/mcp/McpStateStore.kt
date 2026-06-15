package com.openassist.mcp

import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

@Serializable
data class PersistedMcpConnection(val id: String, val displayName: String, val endpoint: String, val enabled: Boolean, val status: String)

@Serializable
data class PersistedMcpPermission(val serverId: String, val toolName: String, val decision: String, val updatedAtMillis: Long)

class McpStateStore(private val read: (String) -> String?, private val write: (String, String) -> Unit) {
    private val json = Json { ignoreUnknownKeys = true; encodeDefaults = true }

    fun connections(): List<PersistedMcpConnection> = read(KEY_CONNECTIONS)?.takeIf { it.isNotBlank() }?.let { json.decodeFromString<List<PersistedMcpConnection>>(it) }.orEmpty()

    fun saveConnection(session: McpClientSession) {
        val item = PersistedMcpConnection(session.server.id, session.server.displayName, session.server.endpoint, session.server.enabled, session.status.name)
        val merged = (connections().filterNot { it.id == item.id } + item).sortedBy { it.displayName }
        write(KEY_CONNECTIONS, json.encodeToString(merged))
    }

    fun permissions(): List<PersistedMcpPermission> = read(KEY_PERMISSIONS)?.takeIf { it.isNotBlank() }?.let { json.decodeFromString<List<PersistedMcpPermission>>(it) }.orEmpty()

    fun savePermission(serverId: String, toolName: String, decision: McpPermissionDecision) {
        val item = PersistedMcpPermission(serverId, toolName, decision.name, System.currentTimeMillis())
        val merged = permissions().filterNot { it.serverId == serverId && it.toolName == toolName } + item
        write(KEY_PERMISSIONS, json.encodeToString(merged))
    }

    companion object {
        private const val KEY_CONNECTIONS = "mcp_connections"
        private const val KEY_PERMISSIONS = "mcp_permissions"
    }
}
