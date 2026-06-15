package com.openassist.tools

import android.Manifest
import android.content.Context
import android.content.Intent
import android.net.Uri
import com.openassist.core.UserFacingErrors
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.put
import kotlinx.serialization.json.putJsonArray
import kotlinx.serialization.json.putJsonObject

class CallTool(private val context: Context) : Tool {
    override val name        = "make_call"
    override val description = "Makes a phone call to the specified number."
    override val sensitive   = true

    override val requiredPermissions = listOf(Manifest.permission.CALL_PHONE)

    override val parameterSchema: JsonObject = buildJsonObject {
        put("type", "object")
        putJsonObject("properties") {
            putJsonObject("phone_number") {
                put("type", "string")
                put("description", "Phone number to dial (digits, +, spaces, or dashes)")
            }
        }
        putJsonArray("required") {
            add(JsonPrimitive("phone_number"))
        }
    }

    override suspend fun run(arguments: Map<String, String>): ToolResult {
        val phoneNumber = arguments["phone_number"]?.takeIf { it.isNotBlank() }
            ?: return ToolResult(name, "Missing 'phone_number' argument.")

        return try {
            val intent = Intent(Intent.ACTION_CALL, Uri.parse("tel:$phoneNumber")).apply {
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            }
            context.startActivity(intent)
            ToolResult(name, "Calling $phoneNumber…")
        } catch (e: Exception) {
            ToolResult(name, UserFacingErrors.tool("start the phone call", e))
        }
    }
}
