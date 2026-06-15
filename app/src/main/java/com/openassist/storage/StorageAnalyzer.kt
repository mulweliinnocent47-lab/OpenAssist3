package com.openassist.storage

import android.content.Context
import java.io.File

private fun File.sizeBytes(): Long = if (!exists()) 0L else if (isFile) length() else walkTopDown().filter { it.isFile }.sumOf { it.length() }

data class StorageBucket(val name: String, val bytes: Long, val path: String)

data class StorageReport(val buckets: List<StorageBucket>) {
    val totalBytes: Long = buckets.sumOf { it.bytes }
    fun exportText(): String = buildString {
        appendLine("OpenAssist Storage Report")
        buckets.forEach { appendLine("${it.name}: ${it.bytes} bytes — ${it.path}") }
        appendLine("Total: $totalBytes bytes")
    }
}

class StorageAnalyzer(private val context: Context) {
    fun scan(): StorageReport {
        val files = context.filesDir
        return StorageReport(
            listOf(
                StorageBucket("Workspace Storage", File(files, "workspace").sizeBytes(), File(files, "workspace").path),
                StorageBucket("Knowledge Storage", File(files, "knowledge").sizeBytes(), File(files, "knowledge").path),
                StorageBucket("Memory Storage", File(files, "memory").sizeBytes(), File(files, "memory").path),
                StorageBucket("Downloaded Models", File(files, "models").sizeBytes(), File(files, "models").path),
                StorageBucket("Generated Images", File(files, "images").sizeBytes(), File(files, "images").path),
                StorageBucket("Documents", File(files, "documents").sizeBytes(), File(files, "documents").path),
                StorageBucket("Cache", context.cacheDir.sizeBytes(), context.cacheDir.path),
            ),
        )
    }

    fun deleteCache(): Long {
        val before = context.cacheDir.sizeBytes()
        context.cacheDir.deleteRecursively()
        context.cacheDir.mkdirs()
        return before
    }
}
