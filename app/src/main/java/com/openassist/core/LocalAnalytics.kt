package com.openassist.core

data class LocalUsageMetric(
    val name: String,
    val count: Int,
    val scope: String,
)

object LocalAnalyticsCatalog {
    val trackedLocallyOnly = listOf(
        LocalUsageMetric("Most Used Features", 0, "Device only"),
        LocalUsageMetric("Most Used Models", 0, "Device only"),
        LocalUsageMetric("Most Used MCPs", 0, "Device only"),
        LocalUsageMetric("Most Used Commands", 0, "Device only"),
        LocalUsageMetric("Workspace Activity", 0, "Device only"),
        LocalUsageMetric("Agent Activity", 0, "Device only"),
    )

    val privacyRule = "Analytics stay local and are never uploaded automatically."
}
