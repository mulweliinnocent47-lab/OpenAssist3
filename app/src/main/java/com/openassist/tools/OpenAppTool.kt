package com.openassist.tools

import android.content.Context
import android.content.Intent
import com.openassist.core.UserFacingErrors
import android.content.pm.PackageManager
import android.os.Build
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.put
import kotlinx.serialization.json.putJsonArray
import kotlinx.serialization.json.putJsonObject

/**
 * Launches an installed app by package name.
 *
 * [getLaunchIntentForPackage] works for packages that are visible to this app
 * through the `<queries>` block in the manifest (MAIN/LAUNCHER apps), which
 * covers every app the user can see on their home screen — no
 * QUERY_ALL_PACKAGES needed.
 */
class OpenAppTool(private val context: Context) : Tool {
    override val name        = "open_app"
    override val description =
        "Launches an installed app by its package name (e.g. com.spotify.music). " +
        "Use installed_apps to find the correct package name first."
    override val sensitive   = true

    override val requiredPermissions = emptyList<String>()

    override val parameterSchema: JsonObject = buildJsonObject {
        put("type", "object")
        putJsonObject("properties") {
            putJsonObject("package") {
                put("type", "string")
                put("description", "Android package name, e.g. 'com.android.chrome'")
            }
        }
        putJsonArray("required") {
            add(JsonPrimitive("package"))
        }
    }

    override suspend fun run(arguments: Map<String, String>): ToolResult {
        val packageName = arguments["package"]?.takeIf { it.isNotBlank() }
            ?: return ToolResult(name, "Missing 'package' argument.")

        // Confirm the package is actually visible (covers API 30+ package visibility).
        val pm             = context.packageManager
        val launchIntent   = pm.getLaunchIntentForPackage(packageName)
            ?: return ToolResult(name, "No launchable activity found for '$packageName'. " +
                "Use installed_apps to verify the exact package name.")

        launchIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        return try {
            context.startActivity(launchIntent)
            ToolResult(name, "Launched $packageName.")
        } catch (e: Exception) {
            ToolResult(name, UserFacingErrors.tool("open $packageName", e))
        }
    }
}
