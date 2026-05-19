package com.threeblades.kuro

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.core.content.ContextCompat

class ReminderReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        if (!ReminderState.isActive(context)) return
        if (ReminderState.isAcknowledged(context)) {
            ReminderState.clear(context)
            return
        }
        ContextCompat.startForegroundService(
            context,
            Intent(context, KuroSpeakService::class.java)
        )
    }
}
