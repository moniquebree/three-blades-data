package com.threeblades.kuro

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Intent
import android.media.AudioAttributes
import android.media.AudioFocusRequest
import android.media.AudioManager
import android.os.Bundle
import android.os.PowerManager
import android.speech.tts.TextToSpeech
import android.speech.tts.UtteranceProgressListener
import androidx.core.app.NotificationCompat

class KuroSpeakService : Service() {

    private var tts: TextToSpeech? = null
    private var wakeLock: PowerManager.WakeLock? = null
    private var pendingLine: String? = null
    private var ttsReady = false
    private var focusRequest: AudioFocusRequest? = null
    private var triedFallback = false

    override fun onCreate() {
        super.onCreate()
        startForegroundNow()
        acquireWakeLock()
        initTts(GOOGLE_TTS_PACKAGE)
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        pendingLine = intent?.getStringExtra(MainActivity.EXTRA_LINE) ?: "Reminder."
        if (ttsReady) speak()
        return START_NOT_STICKY
    }

    private fun mediaAttributes(): AudioAttributes =
        AudioAttributes.Builder()
            .setUsage(AudioAttributes.USAGE_MEDIA)
            .setContentType(AudioAttributes.CONTENT_TYPE_SPEECH)
            .build()

    private fun initTts(enginePackage: String?) {
        tts?.shutdown()
        tts = TextToSpeech(this, { status ->
            if (status == TextToSpeech.SUCCESS) {
                tts?.setAudioAttributes(mediaAttributes())
                tts?.setOnUtteranceProgressListener(object : UtteranceProgressListener() {
                    override fun onStart(utteranceId: String?) {}
                    override fun onDone(utteranceId: String?) = stopSelfSafely()
                    @Deprecated("Deprecated in Java")
                    override fun onError(utteranceId: String?) = stopSelfSafely()
                })
                ttsReady = true
                if (pendingLine != null) speak()
            } else if (!triedFallback && enginePackage != null) {
                triedFallback = true
                initTts(null)
            } else {
                stopSelfSafely()
            }
        }, enginePackage)
    }

    private fun requestMediaFocus() {
        val am = getSystemService(AUDIO_SERVICE) as AudioManager
        val req = AudioFocusRequest.Builder(AudioManager.AUDIOFOCUS_GAIN_TRANSIENT)
            .setAudioAttributes(mediaAttributes())
            .build()
        focusRequest = req
        am.requestAudioFocus(req)
    }

    private fun releaseMediaFocus() {
        val am = getSystemService(AUDIO_SERVICE) as AudioManager
        focusRequest?.let { am.abandonAudioFocusRequest(it) }
        focusRequest = null
    }

    private fun speak() {
        val line = pendingLine ?: return
        requestMediaFocus()
        val params = Bundle().apply {
            putString(TextToSpeech.Engine.KEY_PARAM_STREAM, AudioManager.STREAM_MUSIC.toString())
            putFloat(TextToSpeech.Engine.KEY_PARAM_VOLUME, 1.0f)
        }
        tts?.speak(line, TextToSpeech.QUEUE_FLUSH, params, "kuro-${System.currentTimeMillis()}")
    }

    private fun startForegroundNow() {
        val channelId = "kuro_speak"
        val nm = getSystemService(NotificationManager::class.java)
        nm.createNotificationChannel(
            NotificationChannel(channelId, "Kuro speaking", NotificationManager.IMPORTANCE_LOW)
        )
        val n: Notification = NotificationCompat.Builder(this, channelId)
            .setContentTitle("Kuro")
            .setContentText("Speaking a reminder…")
            .setSmallIcon(android.R.drawable.ic_menu_compass)
            .setOngoing(true)
            .build()
        startForeground(1, n)
    }

    private fun acquireWakeLock() {
        val pm = getSystemService(POWER_SERVICE) as PowerManager
        wakeLock = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "kuro:speak").apply {
            setReferenceCounted(false)
            acquire(30_000)
        }
    }

    private fun stopSelfSafely() {
        try { releaseMediaFocus() } catch (_: Throwable) {}
        try { wakeLock?.takeIf { it.isHeld }?.release() } catch (_: Throwable) {}
        try { tts?.stop(); tts?.shutdown() } catch (_: Throwable) {}
        stopForeground(STOP_FOREGROUND_REMOVE)
        stopSelf()
    }

    override fun onDestroy() {
        try { releaseMediaFocus() } catch (_: Throwable) {}
        try { wakeLock?.takeIf { it.isHeld }?.release() } catch (_: Throwable) {}
        try { tts?.stop(); tts?.shutdown() } catch (_: Throwable) {}
        super.onDestroy()
    }

    override fun onBind(intent: Intent?) = null

    companion object {
        private const val GOOGLE_TTS_PACKAGE = "com.google.android.tts"
    }
}
