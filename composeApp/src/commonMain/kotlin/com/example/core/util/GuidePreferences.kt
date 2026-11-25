package com.example.core.util

expect object GuidePreferences {
    fun completedVersion(): Int
    fun complete(version: Int)
}
