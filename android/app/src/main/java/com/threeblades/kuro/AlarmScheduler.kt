package com.threeblades.kuro

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent

object AlarmScheduler {
    private const val REQ_CODE = 42

    fun scheduleAt(ctx: Context, triggerAtMillis: Long) {
        val am = ctx.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val pending = pendingIntent(ctx, create = true)
        am.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, triggerAtMillis, pending!!)
    }

    fun cancel(ctx: Context) {
        val am = ctx.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val pending = pendingIntent(ctx, create = false) ?: return
        am.cancel(pending)
        pending.cancel()
    }

    private fun pendingIntent(ctx: Context, create: Boolean): PendingIntent? {
        val intent = Intent(ctx, ReminderReceiver::class.java)
        val flags = if (create)
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        else
            PendingIntent.FLAG_NO_CREATE or PendingIntent.FLAG_IMMUTABLE
        return PendingIntent.getBroadcast(ctx, REQ_CODE, intent, flags)
    }
}
