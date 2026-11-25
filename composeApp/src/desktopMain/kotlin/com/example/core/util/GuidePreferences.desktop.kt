package com.example.core.util

import java.util.prefs.Preferences

actual object GuidePreferences {
    private val preferences = Preferences.userRoot().node("com.example.passilius.guide")

    actual fun completedVersion(): Int = preferences.getInt("completed_version", 0)

    actual fun complete(version: Int) {
        preferences.putInt("completed_version", version)
    }
}
