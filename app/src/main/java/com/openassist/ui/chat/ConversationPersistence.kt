package com.openassist.ui.chat

import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

@Serializable
data class PersistedChatMessage(val role: String, val content: String)

@Serializable
data class ConversationSnapshot(
    val id: String,
    val title: String,
    val updatedAtMillis: Long,
    val messages: List<PersistedChatMessage>,
)

object ConversationPersistence {
    private val json = Json { ignoreUnknownKeys = true; encodeDefaults = true }

    fun encode(messages: List<ChatMessage>): String {
        val title = messages.firstOrNull { it.role == "user" }?.content?.take(60).orEmpty().ifBlank { "New conversation" }
        val snapshot = ConversationSnapshot(
            id = "conversation-current",
            title = title,
            updatedAtMillis = System.currentTimeMillis(),
            messages = messages.map { PersistedChatMessage(it.role, it.content) },
        )
        return json.encodeToString(snapshot)
    }

    fun decode(raw: String): List<ChatMessage> = raw.takeIf { it.isNotBlank() }?.let {
        json.decodeFromString<ConversationSnapshot>(it).messages.map { message -> ChatMessage(message.role, message.content) }
    }.orEmpty()
}
