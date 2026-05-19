package com.threeblades.kuro

import android.content.Context

object ReminderState {
    private const val PREFS = "kuro_state"
    private const val KEY_ID = "active_id"
    private const val KEY_LABEL = "active_label"
    private const val KEY_PERSISTENCE = "active_persistence"
    private const val KEY_NAG_COUNT = "active_nag_count"
    private const val KEY_ACK = "active_ack"

    const val PERSIST_GENTLE = 1
    const val PERSIST_FIRM = 2
    const val PERSIST_STUBBORN = 3

    fun maxNagsFor(persistence: Int): Int = when (persistence) {
        PERSIST_GENTLE -> 0
        PERSIST_FIRM -> 3
        PERSIST_STUBBORN -> 99
        else -> 0
    }

    fun start(ctx: Context, id: String, label: String, persistence: Int) {
        ctx.prefs().edit()
            .putString(KEY_ID, id)
            .putString(KEY_LABEL, label)
            .putInt(KEY_PERSISTENCE, persistence)
            .putInt(KEY_NAG_COUNT, 0)
            .putBoolean(KEY_ACK, false)
            .apply()
    }

    fun isActive(ctx: Context): Boolean =
        ctx.prefs().getString(KEY_ID, null) != null

    fun isAcknowledged(ctx: Context): Boolean =
        ctx.prefs().getBoolean(KEY_ACK, false)

    fun label(ctx: Context): String =
        ctx.prefs().getString(KEY_LABEL, "") ?: ""

    fun persistence(ctx: Context): Int =
        ctx.prefs().getInt(KEY_PERSISTENCE, PERSIST_GENTLE)

    fun nagCount(ctx: Context): Int =
        ctx.prefs().getInt(KEY_NAG_COUNT, 0)

    fun incrementNag(ctx: Context): Int {
        val n = nagCount(ctx) + 1
        ctx.prefs().edit().putInt(KEY_NAG_COUNT, n).apply()
        return n
    }

    fun acknowledge(ctx: Context) {
        ctx.prefs().edit().putBoolean(KEY_ACK, true).apply()
    }

    fun clear(ctx: Context) {
        ctx.prefs().edit()
            .remove(KEY_ID)
            .remove(KEY_LABEL)
            .remove(KEY_PERSISTENCE)
            .remove(KEY_NAG_COUNT)
            .remove(KEY_ACK)
            .apply()
    }

    private fun Context.prefs() = getSharedPreferences(PREFS, Context.MODE_PRIVATE)
}
