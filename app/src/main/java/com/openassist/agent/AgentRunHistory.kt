package com.openassist.agent

import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

@Serializable
data class AgentRunLog(
    val id: String,
    val goal: String,
    val status: String,
    val createdAtMillis: Long,
    val updatedAtMillis: Long,
    val events: List<String>,
)

class AgentRunHistory(private val read: () -> String?, private val write: (String) -> Unit) {
    private val json = Json { ignoreUnknownKeys = true; encodeDefaults = true }

    fun all(): List<AgentRunLog> = read()?.takeIf { it.isNotBlank() }?.let { json.decodeFromString<List<AgentRunLog>>(it) }.orEmpty()

    fun append(goal: String, status: AgentRunStatus, event: String): AgentRunLog {
        val now = System.currentTimeMillis()
        val log = AgentRunLog("agent-$now", goal, status.name, now, now, listOf(event))
        write(json.encodeToString((all() + log).takeLast(100)))
        return log
    }

    fun addEvent(id: String, status: AgentRunStatus, event: String) {
        val now = System.currentTimeMillis()
        val updated = all().map { log ->
            if (log.id == id) log.copy(status = status.name, updatedAtMillis = now, events = log.events + event) else log
        }
        write(json.encodeToString(updated))
    }
}
