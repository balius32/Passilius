package com.example.core.util

expect object ClipboardHelper {
    fun copy(
        text: String,
        label: String = "",
        isSensitive: Boolean = true,
        autoClearSeconds: Int = 30
    )
}
