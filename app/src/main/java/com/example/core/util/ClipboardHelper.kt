package com.example.core.util

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.os.Build
import android.os.PersistableBundle
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

object ClipboardHelper {

    fun copyToClipboard(
        context: Context,
        label: String,
        text: String,
        isSensitive: Boolean = true,
        autoClearSeconds: Int = 30
    ) {
        val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val clip = ClipData.newPlainText(label, text)

        if (isSensitive && Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            val bundle = PersistableBundle().apply {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    putBoolean("android.content.extra.IS_SENSITIVE", true)
                }
            }
            clip.description.extras = bundle
        }

        clipboard.setPrimaryClip(clip)

        // Sensitive auto-clear coroutine
        if (isSensitive && autoClearSeconds > 0) {
            CoroutineScope(Dispatchers.Main).launch {
                delay(autoClearSeconds * 1000L)
                val currentClip = clipboard.primaryClip
                if (currentClip != null && currentClip.itemCount > 0) {
                    val currentText = currentClip.getItemAt(0).text?.toString()
                    if (currentText == text) {
                        clipboard.setPrimaryClip(ClipData.newPlainText("", ""))
                    }
                }
            }
        }
    }
}
