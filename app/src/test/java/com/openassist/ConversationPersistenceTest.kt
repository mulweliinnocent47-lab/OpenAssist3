package com.openassist

import com.openassist.ui.chat.ChatMessage
import com.openassist.ui.chat.ConversationPersistence
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class ConversationPersistenceTest {
    @Test
    fun roundTripPreservesMessages() {
        val raw = ConversationPersistence.encode(
            listOf(
                ChatMessage("user", "Hello"),
                ChatMessage("assistant", "Hi there"),
            ),
        )

        val restored = ConversationPersistence.decode(raw)

        assertEquals(2, restored.size)
        assertEquals("user", restored[0].role)
        assertEquals("Hello", restored[0].content)
    }

    @Test
    fun blankSnapshotDecodesToEmptyList() {
        assertTrue(ConversationPersistence.decode("").isEmpty())
    }
}
