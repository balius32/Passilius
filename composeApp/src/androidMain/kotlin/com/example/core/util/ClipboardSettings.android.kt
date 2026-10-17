package com.example.core.util

import android.content.Context

actual object ClipboardSettings {
    private const val PREFS = "passilius_settings"
    private const val KEY_AUTO_CLEAR = "auto_clear_clipboard"

    private var appContext: Context? = null

    fun bind(context: Context) {
        appContext = context.applicationContext
    }

    actual fun isAutoClearEnabled(): Boolean {
        val context = appContext ?: return false
        return context
            .getSharedPreferences(PREFS, Context.MODE_PRIVATE)
            .getBoolean(KEY_AUTO_CLEAR, false)
    }

    actual fun setAutoClearEnabled(enabled: Boolean) {
        val context = appContext ?: return
        context
            .getSharedPreferences(PREFS, Context.MODE_PRIVATE)
            .edit()
            .putBoolean(KEY_AUTO_CLEAR, enabled)
            .apply()
    }
}
