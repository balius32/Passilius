package com.example.core.biometric

import android.content.Context

actual object BiometricSettings {
    private const val PREFS = "passilius_settings"
    private const val KEY_UNLOCK = "biometric_unlock_enabled"

    private var appContext: Context? = null

    fun bind(context: Context) {
        appContext = context.applicationContext
    }

    actual fun isUnlockEnabled(): Boolean {
        val context = appContext ?: return true
        return context
            .getSharedPreferences(PREFS, Context.MODE_PRIVATE)
            .getBoolean(KEY_UNLOCK, true)
    }

    actual fun setUnlockEnabled(enabled: Boolean) {
        val context = appContext ?: return
        context
            .getSharedPreferences(PREFS, Context.MODE_PRIVATE)
            .edit()
            .putBoolean(KEY_UNLOCK, enabled)
            .apply()
    }
}
