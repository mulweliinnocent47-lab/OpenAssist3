package com.openassist.tools

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.put
import kotlinx.serialization.json.putJsonObject

/**
 * Returns a list of user-facing apps (those with a MAIN/LAUNCHER activity).
 *
 * ## Why no QUERY_ALL_PACKAGES
 * `QUERY_ALL_PACKAGES` violates Google Play's permission policy unless the app
 * has a clear device-management use case.  Instead we query for exactly the
 * intent we care about (MAIN + LAUNCHER).  The manifest's `<queries>` block
 * makes those results visible on API 30+ without the broad permission.
 */
class InstalledAppsTool(private val context: Context) : Tool {
    override val name        = "installed_apps"
    override val description =
        "Returns an alphabetical list of installed apps (display name and package name)."
    override val sensitive   = false

    // No runtime permissions required — MAIN/LAUNCHER query is covered by
    // the <queries> element in AndroidManifest.xml.
    override val requiredPermissions = emptyList<String>()

    override val parameterSchema: JsonObject = buildJsonObject {
        put("type", "object")
        putJsonObject("properties") {}
    }

    override suspend fun run(arguments: Map<String, String>): ToolResult {
        val pm     = context.packageManager
        val intent = Intent(Intent.ACTION_MAIN).addCategory(Intent.CATEGORY_LAUNCHER)

        @Suppress("DEPRECATION")
        val resolved = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            pm.queryIntentActivities(intent, PackageManager.ResolveInfoFlags.of(0L))
        } else {
            pm.queryIntentActivities(intent, 0)
        }

        val apps = resolved
            .map { info ->
                val label   = info.loadLabel(pm).toString()
                val pkgName = info.activityInfo.packageName
                "$label ($pkgName)"
            }
            .distinct()
            .sorted()

        return ToolResult(
            name   = name,
            output = apps.joinToString("\n").ifEmpty { "No launchable apps found." },
        )
    }
}
