package com.example.core.util

import java.util.prefs.Preferences

actual object ClipboardSettings {
    private const val KEY_AUTO_CLEAR = "auto_clear_clipboard"
    private val prefs: Preferences = Preferences.userRoot().node("com.example.passilius")

    actual fun isAutoClearEnabled(): Boolean = prefs.getBoolean(KEY_AUTO_CLEAR, false)

    actual fun setAutoClearEnabled(enabled: Boolean) {
        prefs.putBoolean(KEY_AUTO_CLEAR, enabled)
        prefs.flush()
    }
}
