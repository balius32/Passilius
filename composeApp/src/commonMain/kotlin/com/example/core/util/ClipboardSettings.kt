package com.example.core.util

expect object ClipboardSettings {
    fun isAutoClearEnabled(): Boolean
    fun setAutoClearEnabled(enabled: Boolean)
}
