package com.threeblades.kuro

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Intent
import android.media.AudioAttributes
import android.os.Build
import android.os.PowerManager
import android.speech.tts.TextToSpeech
import android.speech.tts.UtteranceProgressListener
import androidx.core.app.NotificationCompat
import java.util.Locale

class KuroSpeakService : Service() {

    private var tts: TextToSpeech? = null
    private var wakeLock: PowerManager.WakeLock? = null
    private var pendingLine: String? = null
    private var ttsReady = false

    override fun onCreate() {
        super.onCreate()
        startForegroundNow()
        acquireWakeLock()
        initTts()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        pendingLine = intent?.getStringExtra(MainActivity.EXTRA_LINE) ?: "Reminder."
        if (ttsReady) speak()
        return START_NOT_STICKY
    }

    private fun initTts() {
        tts = TextToSpeech(this) { status ->
            if (status == TextToSpeech.SUCCESS) {
                tts?.language = Locale("en", "AU")
                tts?.setAudioAttributes(
                    AudioAttributes.Builder()
                        .setUsage(AudioAttributes.USAGE_ASSISTANT)
                        .setContentType(AudioAttributes.CONTENT_TYPE_SPEECH)
                        .build()
                )
                tts?.setOnUtteranceProgressListener(object : UtteranceProgressListener() {
                    override fun onStart(utteranceId: String?) {}
                    override fun onDone(utteranceId: String?) = stopSelfSafely()
                    @Deprecated("Deprecated in Java")
                    override fun onError(utteranceId: String?) = stopSelfSafely()
                })
                ttsReady = true
                if (pendingLine != null) speak()
            } else {
                stopSelfSafely()
            }
        }
    }

    private fun speak() {
        val line = pendingLine ?: return
        tts?.speak(line, TextToSpeech.QUEUE_FLUSH, null, "kuro-${System.currentTimeMillis()}")
    }

    private fun startForegroundNow() {
        val channelId = "kuro_speak"
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val nm = getSystemService(NotificationManager::class.java)
            nm.createNotificationChannel(
                NotificationChannel(channelId, "Kuro speaking", NotificationManager.IMPORTANCE_LOW)
            )
        }
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
        try { wakeLock?.takeIf { it.isHeld }?.release() } catch (_: Throwable) {}
        try { tts?.stop(); tts?.shutdown() } catch (_: Throwable) {}
        stopForeground(STOP_FOREGROUND_REMOVE)
        stopSelf()
    }

    override fun onDestroy() {
        try { wakeLock?.takeIf { it.isHeld }?.release() } catch (_: Throwable) {}
        try { tts?.stop(); tts?.shutdown() } catch (_: Throwable) {}
        super.onDestroy()
    }

    override fun onBind(intent: Intent?) = null
}
