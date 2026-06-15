package com.openassist.tools

import android.os.Build
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.put
import kotlinx.serialization.json.putJsonObject

class DeviceInfoTool : Tool {
    override val name = "device_info"
    override val description = "Returns Android version, SDK level, manufacturer, and device model."
    override val sensitive = false

    override val parameterSchema: JsonObject = buildJsonObject {
        put("type", "object")
        putJsonObject("properties") {}
    }

    override suspend fun run(arguments: Map<String, String>) = ToolResult(
        name   = name,
        output = "Android ${Build.VERSION.RELEASE} (SDK ${Build.VERSION.SDK_INT})" +
                 " on ${Build.MANUFACTURER} ${Build.MODEL}",
    )
}
