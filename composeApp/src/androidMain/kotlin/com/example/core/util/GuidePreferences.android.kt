package com.example.core.util

import android.content.Context

actual object GuidePreferences {
    private lateinit var context: Context

    fun bind(context: Context) {
        this.context = context.applicationContext
    }

    private val preferences
        get() = context.getSharedPreferences("passilius_guide", Context.MODE_PRIVATE)

    actual fun completedVersion(): Int = preferences.getInt("completed_version", 0)

    actual fun complete(version: Int) {
        preferences.edit().putInt("completed_version", version).apply()
    }
}
