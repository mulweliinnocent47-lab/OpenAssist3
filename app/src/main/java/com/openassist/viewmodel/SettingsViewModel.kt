package com.openassist.viewmodel

import androidx.lifecycle.ViewModel
import com.openassist.data.ai.AiMode
import com.openassist.data.local.SecureStorage

class SettingsViewModel(private val storage: SecureStorage) : ViewModel() {
    val apiKey = storage.apiKey
    val model = storage.model
    val aiMode = storage.aiMode
    val localModel = storage.localModel
    val hybridThreshold = storage.hybridThreshold

    fun saveApiKey(value: String) = storage.saveApiKey(value)
    fun clearApiKey() = storage.clearApiKey()
    fun saveModel(value: String) = storage.saveModel(value)
    fun saveAiMode(value: AiMode) = storage.saveAiMode(value)
    fun saveLocalModel(value: String) = storage.saveLocalModel(value)
    fun saveHybridThreshold(value: Int) = storage.saveHybridThreshold(value)
    fun reset() = storage.reset()
}
