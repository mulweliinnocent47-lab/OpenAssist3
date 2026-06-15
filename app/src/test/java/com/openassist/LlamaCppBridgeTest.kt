package com.openassist

import com.openassist.data.ai.LlamaCppBridge
import com.openassist.data.ai.LocalLlmConfig
import com.openassist.data.ai.LocalLlmRequest
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertTrue
import org.junit.Test
import java.io.File

class LlamaCppBridgeTest {
    @Test
    fun missingModelReturnsActionableError() = runTest {
        val bridge = LlamaCppBridge()
        val result = runCatching {
            bridge.complete(File("/tmp/openassist-test-models"), LocalLlmConfig(modelFile = "missing.gguf"), LocalLlmRequest("hello"))
        }

        assertTrue(result.exceptionOrNull()?.message.orEmpty().contains("Local model not found"))
    }
}
