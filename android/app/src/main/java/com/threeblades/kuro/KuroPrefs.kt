package com.threeblades.kuro

import android.content.Context

object KuroPrefs {
    private const val PREFS = "kuro_user_prefs"
    private const val KEY_PERSISTENCE = "user_persistence"

    fun persistence(ctx: Context): Int =
        ctx.prefs().getInt(KEY_PERSISTENCE, ReminderState.PERSIST_FIRM)

    fun setPersistence(ctx: Context, value: Int) {
        ctx.prefs().edit().putInt(KEY_PERSISTENCE, value).apply()
    }

    private fun Context.prefs() = getSharedPreferences(PREFS, Context.MODE_PRIVATE)
}
