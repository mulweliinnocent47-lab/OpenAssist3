package com.openassist.tools

import android.Manifest
import android.content.Context
import android.provider.ContactsContract
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.put
import kotlinx.serialization.json.putJsonArray
import kotlinx.serialization.json.putJsonObject

class ContactTool(private val context: Context) : Tool {
    override val name        = "search_contacts"
    override val description = "Searches the device contacts by name and returns matching names and phone numbers."
    override val sensitive   = false

    override val requiredPermissions = listOf(Manifest.permission.READ_CONTACTS)

    override val parameterSchema: JsonObject = buildJsonObject {
        put("type", "object")
        putJsonObject("properties") {
            putJsonObject("name") {
                put("type", "string")
                put("description", "Full or partial name to search for (case-insensitive)")
            }
        }
        putJsonArray("required") {
            add(JsonPrimitive("name"))
        }
    }

    override suspend fun run(arguments: Map<String, String>): ToolResult {
        val query = arguments["name"]?.takeIf { it.isNotBlank() }
            ?: return ToolResult(name, "Missing 'name' argument.")

        val projection = arrayOf(
            ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME,
            ContactsContract.CommonDataKinds.Phone.NUMBER,
        )
        val selection     = "${ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME} LIKE ?"
        val selectionArgs = arrayOf("%$query%")
        val sortOrder     = "${ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME} ASC"

        val cursor = context.contentResolver.query(
            ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
            projection, selection, selectionArgs, sortOrder,
        ) ?: return ToolResult(name, "Could not access contacts.")

        val results = mutableListOf<String>()
        cursor.use { c ->
            val nameCol = c.getColumnIndexOrThrow(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME)
            val numCol  = c.getColumnIndexOrThrow(ContactsContract.CommonDataKinds.Phone.NUMBER)
            while (c.moveToNext() && results.size < 20) {
                val contactName   = c.getString(nameCol) ?: continue
                val contactNumber = c.getString(numCol)  ?: continue
                results.add("$contactName: $contactNumber")
            }
        }

        return if (results.isEmpty()) {
            ToolResult(name, "No contacts found matching \"$query\".")
        } else {
            ToolResult(name, results.joinToString("\n"))
        }
    }
}
