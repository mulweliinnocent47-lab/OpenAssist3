package com.openassist.data.downloads

import android.app.DownloadManager
import android.content.Context
import android.net.Uri
import android.os.Environment
import com.openassist.data.ai.LocalModelCatalog

data class ModelDownloadRequest(
    val modelName: String,
    val url: String,
    val fileName: String,
)

data class ModelDownloadTicket(val id: Long, val modelName: String, val destination: String)

class ModelDownloadManager(private val context: Context) {
    private val manager = context.getSystemService(DownloadManager::class.java)

    fun enqueue(request: ModelDownloadRequest): ModelDownloadTicket {
        require(request.url.startsWith("https://")) { "Model downloads must use HTTPS." }
        val download = DownloadManager.Request(Uri.parse(request.url))
            .setTitle("OpenAssist model: ${request.modelName}")
            .setDescription("Downloading GGUF model for offline Local AI")
            .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
            .setAllowedOverMetered(true)
            .setAllowedOverRoaming(false)
            .setDestinationInExternalFilesDir(context, Environment.DIRECTORY_DOWNLOADS, "models/${request.fileName}")
        val id = manager.enqueue(download)
        return ModelDownloadTicket(id, request.modelName, "Android/data/${context.packageName}/files/Download/models/${request.fileName}")
    }

    fun catalogRequest(modelName: String): ModelDownloadRequest {
        val model = LocalModelCatalog.recommended.firstOrNull { it.name.equals(modelName, ignoreCase = true) }
            ?: error("Unknown local model: $modelName")
        val fileName = model.name.lowercase().replace(Regex("[^a-z0-9]+"), "-").trim('-') + ".gguf"
        val url = "https://huggingface.co/models?search=${Uri.encode(model.name)}"
        return ModelDownloadRequest(model.name, url, fileName)
    }
}
