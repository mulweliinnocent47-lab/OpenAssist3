package com.openassist.tools

import android.Manifest
import android.content.ContentUris
import android.content.Context
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import androidx.annotation.RequiresApi
import com.openassist.core.UserFacingErrors
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.put
import kotlinx.serialization.json.putJsonArray
import kotlinx.serialization.json.putJsonObject
import java.io.File

/**
 * Lists or reads files from the device's Downloads folder.
 *
 * ## Android version strategy
 *
 * | API | Storage access | Permission required |
 * |-----|---------------|---------------------|
 * | 26–28 | `File` API on public Downloads path | `READ_EXTERNAL_STORAGE` (runtime) |
 * | 29–32 | `MediaStore.Downloads` | `READ_EXTERNAL_STORAGE` (runtime) |
 * | 33+   | `MediaStore.Downloads` | none — non-media Downloads are ungated |
 *
 * On API 33+ the system returns only files visible to this app without a
 * special permission, which in practice means files created by this app and
 * files the user explicitly shared.  There is no permission that grants
 * arbitrary read access to all Downloads on API 33+ (SAF is the intended
 * path for that use-case, which requires a user-driven picker).
 */
class FileTool(private val context: Context) : Tool {
    override val name        = "file_operations"
    override val description =
        "Lists files in the Downloads folder, or reads a text file from it. " +
        "On Android 13+ only files visible to this app are accessible."
    override val sensitive   = false

    // READ_EXTERNAL_STORAGE was removed in API 33 (TIRAMISU).
    // On API 33+ MediaStore.Downloads needs no permission for non-media files.
    override val requiredPermissions: List<String>
        get() = if (Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU) {
            listOf(Manifest.permission.READ_EXTERNAL_STORAGE)
        } else {
            emptyList()
        }

    override val parameterSchema: JsonObject = buildJsonObject {
        put("type", "object")
        putJsonObject("properties") {
            putJsonObject("action") {
                put("type", "string")
                put("description", "'list' to enumerate files, 'read' to return a file's text content")
            }
            putJsonObject("path") {
                put("type", "string")
                put("description",
                    "On Android ≤ 12: path relative to Downloads ('' = root, 'docs/note.txt'). " +
                    "On Android 13+: exact file name within Downloads (e.g. 'note.txt')."
                )
            }
        }
        putJsonArray("required") {
            add(JsonPrimitive("action"))
        }
    }

    override suspend fun run(arguments: Map<String, String>): ToolResult {
        val action = arguments["action"]?.lowercase()?.trim()
            ?: return ToolResult(name, "Missing 'action'. Use 'list' or 'read'.")
        val path = arguments["path"]?.trim('/', ' ') ?: ""

        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            runWithMediaStore(action, path)
        } else {
            runWithFileApi(action, path)
        }
    }

    // ── API 29+ — MediaStore.Downloads ───────────────────────────────────────

    @RequiresApi(Build.VERSION_CODES.Q)
    private fun runWithMediaStore(action: String, path: String): ToolResult {
        return when (action) {
            "list"  -> listWithMediaStore(path)
            "read"  -> readWithMediaStore(path)
            else    -> ToolResult(name, "Unknown action '$action'. Use 'list' or 'read'.")
        }
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    private fun listWithMediaStore(subDir: String): ToolResult {
        val collection = MediaStore.Downloads.getContentUri(MediaStore.VOLUME_EXTERNAL)
        val projection = arrayOf(
            MediaStore.Downloads.DISPLAY_NAME,
            MediaStore.Downloads.SIZE,
            MediaStore.Downloads.RELATIVE_PATH,
        )

        // RELATIVE_PATH looks like "Download/" or "Download/docs/".
        val (selection, selectionArgs) = if (subDir.isBlank()) {
            null to null
        } else {
            "${MediaStore.Downloads.RELATIVE_PATH} LIKE ?" to
                arrayOf("Download/$subDir%")
        }

        val cursor = context.contentResolver.query(
            collection, projection, selection, selectionArgs,
            "${MediaStore.Downloads.DISPLAY_NAME} ASC",
        ) ?: return ToolResult(name, "Could not query Downloads.")

        val lines = mutableListOf<String>()
        cursor.use { c ->
            val nameCol = c.getColumnIndexOrThrow(MediaStore.Downloads.DISPLAY_NAME)
            val sizeCol = c.getColumnIndexOrThrow(MediaStore.Downloads.SIZE)
            while (c.moveToNext() && lines.size < 100) {
                val fname = c.getString(nameCol) ?: continue
                val fsize = c.getLong(sizeCol)
                lines.add("$fname  (${formatSize(fsize)})")
            }
        }

        return if (lines.isEmpty()) {
            ToolResult(name, "No files found in Downloads${if (subDir.isNotBlank()) "/$subDir" else ""}.")
        } else {
            ToolResult(name, lines.joinToString("\n"))
        }
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    private fun readWithMediaStore(fileName: String): ToolResult {
        if (fileName.isBlank()) {
            return ToolResult(name, "Provide a file name in the 'path' argument (e.g. 'note.txt').")
        }

        val collection = MediaStore.Downloads.getContentUri(MediaStore.VOLUME_EXTERNAL)
        val projection = arrayOf(
            MediaStore.Downloads._ID,
            MediaStore.Downloads.DISPLAY_NAME,
            MediaStore.Downloads.SIZE,
        )
        val selection     = "${MediaStore.Downloads.DISPLAY_NAME} = ?"
        val selectionArgs = arrayOf(fileName)

        val cursor = context.contentResolver.query(
            collection, projection, selection, selectionArgs, null,
        ) ?: return ToolResult(name, "Could not query Downloads.")

        val contentUri = cursor.use { c ->
            if (!c.moveToFirst()) return ToolResult(name, "'$fileName' not found in Downloads.")
            val idCol   = c.getColumnIndexOrThrow(MediaStore.Downloads._ID)
            val sizeCol = c.getColumnIndexOrThrow(MediaStore.Downloads.SIZE)
            val size    = c.getLong(sizeCol)
            if (size > FILE_SIZE_LIMIT) {
                return ToolResult(name, "'$fileName' is too large (${formatSize(size)}; limit ${formatSize(FILE_SIZE_LIMIT)}).")
            }
            ContentUris.withAppendedId(collection, c.getLong(idCol))
        }

        return try {
            val text = context.contentResolver.openInputStream(contentUri)
                ?.bufferedReader()
                ?.use { it.readText() }
                ?: return ToolResult(name, "Could not open '$fileName'.")
            ToolResult(name, text)
        } catch (e: Exception) {
            ToolResult(name, UserFacingErrors.tool("read $fileName", e))
        }
    }

    // ── API 26–28 — File API ─────────────────────────────────────────────────

    private fun runWithFileApi(action: String, path: String): ToolResult {
        val baseDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
            ?: return ToolResult(name, "External storage is unavailable.")
        val target = if (path.isBlank()) baseDir else File(baseDir, path)

        // Prevent path-traversal.
        if (!target.canonicalPath.startsWith(baseDir.canonicalPath)) {
            return ToolResult(name, "Access denied: path escapes the Downloads directory.")
        }

        return when (action) {
            "list" -> {
                if (!target.exists())      return ToolResult(name, "Not found: $path")
                if (!target.isDirectory)  return ToolResult(name, "'${target.name}' is a file. Use action='read'.")
                val entries = target.listFiles()
                    ?.sortedWith(compareBy({ !it.isDirectory }, { it.name }))
                    ?: emptyList()
                if (entries.isEmpty()) {
                    ToolResult(name, "Directory is empty.")
                } else {
                    ToolResult(name, entries.joinToString("\n") { f ->
                        if (f.isDirectory) "[dir]  ${f.name}/"
                        else               "       ${f.name}  (${formatSize(f.length())})"
                    })
                }
            }
            "read" -> {
                if (!target.exists())    return ToolResult(name, "File not found: $path")
                if (target.isDirectory) return ToolResult(name, "'${target.name}' is a directory. Use action='list'.")
                if (target.length() > FILE_SIZE_LIMIT) {
                    return ToolResult(name, "File too large (${formatSize(target.length())}; limit ${formatSize(FILE_SIZE_LIMIT)}).")
                }
                try { ToolResult(name, target.readText()) }
                catch (e: Exception) { ToolResult(name, UserFacingErrors.tool("read ${target.name}", e)) }
            }
            else -> ToolResult(name, "Unknown action '$action'. Use 'list' or 'read'.")
        }
    }

    // ── Helpers ───────────────────────────────────────────────────────────────

    private fun formatSize(bytes: Long): String = when {
        bytes < 1_024L       -> "$bytes B"
        bytes < 1_048_576L   -> "%.1f KB".format(bytes / 1_024.0)
        else                 -> "%.1f MB".format(bytes / 1_048_576.0)
    }

    companion object {
        private const val FILE_SIZE_LIMIT = 50_000L  // 50 KB
    }
}
