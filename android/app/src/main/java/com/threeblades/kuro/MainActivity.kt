package com.threeblades.kuro

import android.Manifest
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val button = findViewById<Button>(R.id.btnTest)
        val status = findViewById<TextView>(R.id.status)

        requestNotificationPermissionIfNeeded()

        button.setOnClickListener {
            if (!ensureExactAlarmsAllowed()) return@setOnClickListener
            val triggerAt = System.currentTimeMillis() + 60_000L
            scheduleSpeak(triggerAt)
            val fmt = SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(Date(triggerAt))
            status.text = "Armed. Will speak at $fmt — lock the tablet and put it down."
            Toast.makeText(this, "Locked-screen test armed.", Toast.LENGTH_SHORT).show()
        }
    }

    private fun scheduleSpeak(triggerAtMillis: Long) {
        val alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(this, ReminderReceiver::class.java).apply {
            putExtra(EXTRA_LINE, "This is Kuro. The test worked — you heard me with the screen off.")
        }
        val pending = PendingIntent.getBroadcast(
            this, REQ_CODE, intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
        alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, triggerAtMillis, pending)
    }

    private fun ensureExactAlarmsAllowed(): Boolean {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            val am = getSystemService(Context.ALARM_SERVICE) as AlarmManager
            if (!am.canScheduleExactAlarms()) {
                Toast.makeText(
                    this,
                    "Allow exact alarms for Kuro, then come back and tap test.",
                    Toast.LENGTH_LONG
                ).show()
                startActivity(Intent(Settings.ACTION_REQUEST_SCHEDULE_EXACT_ALARM))
                return false
            }
        }
        return true
    }

    private fun requestNotificationPermissionIfNeeded() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS)
                != PackageManager.PERMISSION_GRANTED
            ) {
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(Manifest.permission.POST_NOTIFICATIONS),
                    1
                )
            }
        }
    }

    companion object {
        const val EXTRA_LINE = "line"
        const val REQ_CODE = 42
    }
}
