package com.openassist

import android.content.Context
import android.content.pm.PackageManager
import androidx.activity.result.ActivityResultLauncher
import androidx.core.content.ContextCompat
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.coroutines.withContext

/**
 * Bridges Android's callback-based runtime-permission API to Kotlin coroutines.
 *
 * ## Lifecycle
 * This object lives in [OpenAssistApp] (application scope) so it outlasts any
 * Activity.  [MainActivity] must call [attachLauncher] in every `onCreate` to
 * wire up a fresh [ActivityResultLauncher]; the old launcher becomes invalid
 * after an Activity is destroyed.
 *
 * ## Thread safety
 * [require] serialises concurrent requests with a [Mutex] so only one system
 * permission dialog is shown at a time.  [onResult] is always called on the
 * main thread by the activity-result infrastructure.
 */
class PermissionManager(private val context: Context) {

    @Volatile
    private var launcher: ActivityResultLauncher<Array<String>>? = null

    private val mutex = Mutex()
    private var pending: CompletableDeferred<Map<String, Boolean>>? = null

    // ── Activity wiring ───────────────────────────────────────────────────────

    /**
     * Called by [MainActivity] after [registerForActivityResult] in every
     * `onCreate`.  Safe to call while a previous request is in flight — the
     * new launcher replaces the old one without cancelling the deferred.
     */
    fun attachLauncher(launcher: ActivityResultLauncher<Array<String>>) {
        this.launcher = launcher
    }

    /**
     * Called from the activity-result callback registered in [MainActivity].
     * Completes any suspended [require] call with the user's decisions.
     */
    fun onResult(results: Map<String, Boolean>) {
        pending?.complete(results)
        pending = null
    }

    // ── Public API ────────────────────────────────────────────────────────────

    fun hasPermission(permission: String): Boolean =
        ContextCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_GRANTED

    /**
     * Returns `true` if every [permissions] string is already granted, or if
     * the user grants them when the system dialog appears.  Returns `false` if
     * any permission is denied or the launcher is not yet attached.
     *
     * Suspends the calling coroutine until the user responds; does not block
     * the main thread.
     */
    suspend fun require(vararg permissions: String): Boolean {
        val missing = permissions.filter { !hasPermission(it) }
        if (missing.isEmpty()) return true

        // Serialise so we never show two dialogs simultaneously.
        return mutex.withLock {
            val deferred = CompletableDeferred<Map<String, Boolean>>()
            pending = deferred

            withContext(Dispatchers.Main) {
                val l = launcher
                    ?: run {
                        deferred.complete(missing.associateWith { false })
                        return@withContext
                    }
                l.launch(missing.toTypedArray())
            }

            val results = deferred.await()
            results.values.all { it }
        }
    }
}
