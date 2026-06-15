package com.openassist.core

import java.io.FileNotFoundException
import java.net.SocketTimeoutException
import java.net.UnknownHostException

object UserFacingErrors {
    fun chat(error: Throwable): String = when (error) {
        is IllegalArgumentException -> error.message ?: "I need one more setup step before I can answer. Please check your AI mode, model, and API key settings."
        is UnknownHostException -> "I could not reach OpenRouter. Check your internet connection, then try again."
        is SocketTimeoutException -> "The AI provider took too long to respond. Try again, or switch to a faster/free model."
        else -> "Something went wrong while generating the reply. Please try again, or switch models if this keeps happening."
    }

    fun tool(action: String, error: Throwable): String = when (error) {
        is SecurityException -> "I could not $action because Android blocked the permission. Open Settings → Apps → OpenAssist → Permissions, grant the required permission, then try again."
        is android.content.ActivityNotFoundException -> "I could not $action because no compatible app is installed on this device."
        is IllegalArgumentException -> "I could not $action because one of the details was invalid. Please review the number, time, path, or message and try again."
        is FileNotFoundException -> "I could not $action because the file was not found or is no longer available."
        else -> "I could not $action right now. Please review the details and try again."
    }

    fun openRouterEmptyResponse(): String = "OpenRouter returned no assistant message. Try another free model or retry after a moment."

    fun permissionDenied(permissions: List<String>): String =
        "I need ${permissions.joinToString()} permission before I can continue. Grant it in Settings → Apps → OpenAssist → Permissions, then try again."
}
