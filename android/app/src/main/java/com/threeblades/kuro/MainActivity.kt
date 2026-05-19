package com.threeblades.kuro

import android.Manifest
import android.app.AlarmManager
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

class MainActivity : AppCompatActivity() {

    private lateinit var status: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        status = findViewById(R.id.status)

        requestNotificationPermissionIfNeeded()

        findViewById<Button>(R.id.btnTestSingle).setOnClickListener {
            if (!ensureExactAlarmsAllowed()) return@setOnClickListener
            startReminder(
                label = "Single-speak test — milestone one still works",
                persistence = ReminderState.PERSIST_GENTLE,
                delayMs = 60_000L,
                statusText = "Single speak armed. Speaks in 60s, no nag."
            )
        }

        findViewById<Button>(R.id.btnTestNag).setOnClickListener {
            if (!ensureExactAlarmsAllowed()) return@setOnClickListener
            startReminder(
                label = "the test nag",
                persistence = ReminderState.PERSIST_FIRM,
                delayMs = 30_000L,
                statusText = "Nag test armed (firm). Fires in 30s, repeats every 30s — up to 4 times — unless you tap 'I heard you'."
            )
        }

        findViewById<Button>(R.id.btnAck).setOnClickListener {
            if (!ReminderState.isActive(this)) {
                status.text = "Nothing to acknowledge."
                return@setOnClickListener
            }
            ReminderState.acknowledge(this)
            AlarmScheduler.cancel(this)
            ReminderState.clear(this)
            status.text = "Acknowledged. Kuro will pipe down."
            Toast.makeText(this, "Acknowledged.", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onResume() {
        super.onResume()
        refreshStatus()
    }

    private fun refreshStatus() {
        if (!ReminderState.isActive(this)) {
            status.text = "Ready. Pick a test above."
            return
        }
        val label = ReminderState.label(this)
        val nag = ReminderState.nagCount(this)
        val maxNags = ReminderState.maxNagsFor(ReminderState.persistence(this))
        status.text = if (maxNags == 0) {
            "Active: \"$label\" (single speak, no nag)."
        } else {
            "Active: \"$label\" — fired $nag of up to ${maxNags + 1} times."
        }
    }

    private fun startReminder(label: String, persistence: Int, delayMs: Long, statusText: String) {
        val id = "r-${System.currentTimeMillis()}"
        ReminderState.start(this, id, label, persistence)
        AlarmScheduler.scheduleAt(this, System.currentTimeMillis() + delayMs)
        status.text = statusText
        Toast.makeText(this, "Armed. Lock the tablet to test.", Toast.LENGTH_SHORT).show()
    }

    private fun ensureExactAlarmsAllowed(): Boolean {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            val am = getSystemService(Context.ALARM_SERVICE) as AlarmManager
            if (!am.canScheduleExactAlarms()) {
                Toast.makeText(
                    this,
                    "Allow exact alarms for Kuro, then come back and tap again.",
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
}
