package com.openassist

import android.app.Application
import com.openassist.tools.AlarmTool
import com.openassist.tools.CallTool
import com.openassist.tools.ContactTool
import com.openassist.tools.DeviceInfoTool
import com.openassist.tools.FileTool
import com.openassist.tools.InstalledAppsTool
import com.openassist.tools.OpenAppTool
import com.openassist.tools.SmsTool
import com.openassist.tools.ToolEngine

/**
 * Application-scoped singletons so that [PermissionManager] and [ToolEngine]
 * survive Activity recreation (rotation, theme change, etc.) without leaking
 * an Activity reference or losing a pending permission-request coroutine.
 *
 * Remember to add android:name=".OpenAssistApp" to <application> in the manifest.
 */
class OpenAssistApp : Application() {

    /**
     * Owns the coroutine continuation that is waiting for a runtime-permission
     * result.  [MainActivity] re-attaches a fresh launcher on every onCreate so
     * the bridge is always connected to the live Activity.
     */
    val permissionManager: PermissionManager by lazy { PermissionManager(this) }

    /** Built once; tools receive applicationContext so there are no Activity leaks. */
    val toolEngine: ToolEngine by lazy {
        ToolEngine(
            tools = listOf(
                DeviceInfoTool(),
                InstalledAppsTool(this),
                OpenAppTool(this),
                AlarmTool(this),
                SmsTool(this),
                ContactTool(this),
                CallTool(this),
                FileTool(this),
            ),
            permissionManager = permissionManager,
        )
    }
}
