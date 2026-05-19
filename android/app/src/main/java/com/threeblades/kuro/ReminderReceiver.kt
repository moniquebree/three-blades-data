package com.threeblades.kuro

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.core.content.ContextCompat

class ReminderReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        val line = intent.getStringExtra(MainActivity.EXTRA_LINE) ?: "Reminder."
        val serviceIntent = Intent(context, KuroSpeakService::class.java).apply {
            putExtra(MainActivity.EXTRA_LINE, line)
        }
        ContextCompat.startForegroundService(context, serviceIntent)
    }
}
