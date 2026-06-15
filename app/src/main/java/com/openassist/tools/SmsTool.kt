package com.openassist.tools

import android.Manifest
import android.content.Context
import android.telephony.SmsManager
import com.openassist.core.UserFacingErrors
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.put
import kotlinx.serialization.json.putJsonArray
import kotlinx.serialization.json.putJsonObject

class SmsTool(private val context: Context) : Tool {
    override val name        = "send_sms"
    override val description = "Sends an SMS text message to a phone number."
    override val sensitive   = true

    override val requiredPermissions = listOf(Manifest.permission.SEND_SMS)

    override val parameterSchema: JsonObject = buildJsonObject {
        put("type", "object")
        putJsonObject("properties") {
            putJsonObject("phone_number") {
                put("type", "string")
                put("description", "Recipient phone number (digits, +, spaces, or dashes)")
            }
            putJsonObject("message") {
                put("type", "string")
                put("description", "Body of the SMS (long messages are split automatically)")
            }
        }
        putJsonArray("required") {
            add(JsonPrimitive("phone_number"))
            add(JsonPrimitive("message"))
        }
    }

    override suspend fun run(arguments: Map<String, String>): ToolResult {
        val phoneNumber = arguments["phone_number"]?.takeIf { it.isNotBlank() }
            ?: return ToolResult(name, "Missing 'phone_number' argument.")
        val message = arguments["message"]?.takeIf { it.isNotBlank() }
            ?: return ToolResult(name, "Missing 'message' argument.")

        return try {
            @Suppress("DEPRECATION")
            val smsManager: SmsManager = if (android.os.Build.VERSION.SDK_INT >= 31) {
                context.getSystemService(SmsManager::class.java)
            } else {
                SmsManager.getDefault()
            }
            val parts = smsManager.divideMessage(message)
            if (parts.size == 1) {
                smsManager.sendTextMessage(phoneNumber, null, message, null, null)
            } else {
                smsManager.sendMultipartTextMessage(phoneNumber, null, parts, null, null)
            }
            ToolResult(name, "SMS sent to $phoneNumber.")
        } catch (e: Exception) {
            ToolResult(name, UserFacingErrors.tool("send the SMS", e))
        }
    }
}
