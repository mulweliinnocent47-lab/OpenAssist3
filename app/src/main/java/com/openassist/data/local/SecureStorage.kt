package com.openassist.data.local

import android.content.Context
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import com.openassist.data.ai.AiMode
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class SecureStorage(context: Context) {
    private val prefs = EncryptedSharedPreferences.create(
        context,
        "openassist_secure_prefs",
        MasterKey.Builder(context).setKeyScheme(MasterKey.KeyScheme.AES256_GCM).build(),
        EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
        EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM,
    )

    private val _apiKey = MutableStateFlow(prefs.getString(KEY_API_KEY, "").orEmpty())
    private val _model = MutableStateFlow(prefs.getString(KEY_MODEL, DEFAULT_MODEL) ?: DEFAULT_MODEL)
    private val _aiMode = MutableStateFlow(AiMode.valueOf(prefs.getString(KEY_AI_MODE, AiMode.Local.name) ?: AiMode.Local.name))
    private val _localModel = MutableStateFlow(prefs.getString(KEY_LOCAL_MODEL, DEFAULT_LOCAL_MODEL) ?: DEFAULT_LOCAL_MODEL)
    private val _hybridThreshold = MutableStateFlow(prefs.getInt(KEY_HYBRID_THRESHOLD, DEFAULT_HYBRID_THRESHOLD))

    val apiKey: StateFlow<String> = _apiKey
    val model: StateFlow<String> = _model
    val aiMode: StateFlow<AiMode> = _aiMode
    val localModel: StateFlow<String> = _localModel
    val hybridThreshold: StateFlow<Int> = _hybridThreshold

    fun saveApiKey(value: String) {
        prefs.edit().putString(KEY_API_KEY, value.trim()).apply()
        _apiKey.value = value.trim()
    }

    fun clearApiKey() = saveApiKey("")

    fun saveModel(value: String) {
        prefs.edit().putString(KEY_MODEL, value.trim()).apply()
        _model.value = value.trim()
    }

    fun saveAiMode(value: AiMode) {
        prefs.edit().putString(KEY_AI_MODE, value.name).apply()
        _aiMode.value = value
    }

    fun saveLocalModel(value: String) {
        prefs.edit().putString(KEY_LOCAL_MODEL, value).apply()
        _localModel.value = value
    }

    fun saveHybridThreshold(value: Int) {
        prefs.edit().putInt(KEY_HYBRID_THRESHOLD, value).apply()
        _hybridThreshold.value = value
    }

    fun reset() {
        prefs.edit().clear().apply()
        _apiKey.value = ""
        _model.value = DEFAULT_MODEL
        _aiMode.value = AiMode.Local
        _localModel.value = DEFAULT_LOCAL_MODEL
        _hybridThreshold.value = DEFAULT_HYBRID_THRESHOLD
    }

    companion object {
        const val DEFAULT_MODEL = "openai/gpt-4o-mini"
        const val DEFAULT_LOCAL_MODEL = "smollm2-135m"
        const val DEFAULT_HYBRID_THRESHOLD = 240
        private const val KEY_API_KEY = "openrouter_api_key"
        private const val KEY_MODEL = "openrouter_model"
        private const val KEY_AI_MODE = "ai_mode"
        private const val KEY_LOCAL_MODEL = "local_model"
        private const val KEY_HYBRID_THRESHOLD = "hybrid_threshold"
    }
}
