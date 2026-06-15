package com.openassist.screen

import com.openassist.safety.PermissionLevel

data class ScreenInsight(
    val summary: String,
    val visibleText: String,
    val appContext: String,
    val privacyWarning: String = "Screen Intelligence can read visible app content. Activate only when you want OpenAssist to analyze the current screen.",
)

class ScreenIntelligenceService {
    fun permissionChecklist(): List<String> = listOf(
        "Show privacy warning before activation",
        "Request screen capture consent",
        "Verify accessibility service status",
        "Run OCR on visible content only",
        "Summarize context without storing screenshots by default",
    )

    fun summarizeVisibleScreen(visibleText: String, appName: String? = null): ScreenInsight {
        val cleaned = visibleText.ifBlank { "No OCR text has been captured yet." }
        val context = appName?.let { "Visible app: $it" } ?: "Visible app has not been identified yet."
        return ScreenInsight(
            summary = "OpenAssist can explain, summarize, and answer questions about this screen after user-approved capture.",
            visibleText = cleaned,
            appContext = context,
        )
    }

    fun requiredPermissionLevel(): PermissionLevel = PermissionLevel.Safe
}
