package com.openassist.voice

import android.content.Context
import android.content.Intent
import android.speech.RecognizerIntent
import android.speech.tts.TextToSpeech
import java.util.Locale

class AndroidVoiceController(context: Context) : TextToSpeech.OnInitListener {
    private var ready = false
    private val tts = TextToSpeech(context.applicationContext, this)

    override fun onInit(status: Int) {
        ready = status == TextToSpeech.SUCCESS
        if (ready) tts.language = Locale.getDefault()
    }

    fun speak(text: String): Boolean {
        if (!ready || text.isBlank()) return false
        tts.speak(text.take(4_000), TextToSpeech.QUEUE_FLUSH, null, "openassist-${System.currentTimeMillis()}")
        return true
    }

    fun stop() = tts.stop()
    fun shutdown() = tts.shutdown()

    fun speechInputIntent(prompt: String = "Speak to OpenAssist"): Intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply {
        putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
        putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault())
        putExtra(RecognizerIntent.EXTRA_PROMPT, prompt)
    }
}
