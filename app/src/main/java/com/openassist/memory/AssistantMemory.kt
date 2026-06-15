package com.openassist.memory

data class MemoryItem(val title: String, val detail: String)

data class AssistantPersonality(
    val name: String = "OpenAssist",
    val tone: String = "helpful, concise, safety-first",
    val voiceChatStyle: String = "small animated overlay instead of opening the full app",
)

class AssistantMemory {
    private val memories = mutableListOf<MemoryItem>()
    var personality: AssistantPersonality = AssistantPersonality()
        private set

    fun remember(title: String, detail: String) {
        memories += MemoryItem(title = title, detail = detail)
    }

    fun updatePersonality(value: AssistantPersonality) {
        personality = value
    }

    fun all(): List<MemoryItem> = memories.toList()
}
