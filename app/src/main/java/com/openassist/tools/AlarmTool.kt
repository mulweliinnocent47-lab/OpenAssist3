package com.openassist.tools

import android.content.Context
import android.content.Intent
import android.provider.AlarmClock
import com.openassist.core.UserFacingErrors
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.putJsonArray
import kotlinx.serialization.json.putJsonObject
import kotlinx.serialization.json.put

class AlarmTool(private val context: Context) : Tool {
    override val name = "set_alarm"
    override val description = "Sets an alarm on the device's clock app."
    override val sensitive = true

    override val parameterSchema: JsonObject = buildJsonObject {
        put("type", "object")
        putJsonObject("properties") {
            putJsonObject("hour") {
                put("type", "integer")
                put("description", "Hour of the alarm in 24-hour format (0–23)")
            }
            putJsonObject("minute") {
                put("type", "integer")
                put("description", "Minute of the alarm (0–59)")
            }
            putJsonObject("label") {
                put("type", "string")
                put("description", "Optional alarm label / message")
            }
        }
        putJsonArray("required") {
            add(JsonPrimitive("hour"))
            add(JsonPrimitive("minute"))
        }
    }

    override suspend fun run(arguments: Map<String, String>): ToolResult {
        val hour = arguments["hour"]?.toIntOrNull()
            ?: return ToolResult(name, "Missing or invalid 'hour' (expected 0–23).")
        val minute = arguments["minute"]?.toIntOrNull()
            ?: return ToolResult(name, "Missing or invalid 'minute' (expected 0–59).")

        if (hour !in 0..23) return ToolResult(name, "'hour' must be between 0 and 23.")
        if (minute !in 0..59) return ToolResult(name, "'minute' must be between 0 and 59.")

        val label = arguments["label"]
        val intent = Intent(AlarmClock.ACTION_SET_ALARM).apply {
            putExtra(AlarmClock.EXTRA_HOUR, hour)
            putExtra(AlarmClock.EXTRA_MINUTES, minute)
            putExtra(AlarmClock.EXTRA_SKIP_UI, true)
            if (!label.isNullOrBlank()) putExtra(AlarmClock.EXTRA_MESSAGE, label)
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }

        return try {
            context.startActivity(intent)
            val time = "%02d:%02d".format(hour, minute)
            ToolResult(name, "Alarm set for $time${if (!label.isNullOrBlank()) " — $label" else ""}.")
        } catch (e: Exception) {
            ToolResult(name, UserFacingErrors.tool("set the alarm", e))
        }
    }
}
