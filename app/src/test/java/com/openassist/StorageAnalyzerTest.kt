package com.openassist

import com.openassist.storage.StorageReport
import com.openassist.storage.StorageBucket
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class StorageAnalyzerTest {
    @Test
    fun reportTotalsBucketsAndExportsText() {
        val report = StorageReport(
            listOf(
                StorageBucket("Workspace", 10, "/workspace"),
                StorageBucket("Cache", 5, "/cache"),
            ),
        )

        assertEquals(15, report.totalBytes)
        assertTrue(report.exportText().contains("Workspace"))
    }
}
