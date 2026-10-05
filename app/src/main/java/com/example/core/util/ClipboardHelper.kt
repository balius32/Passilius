package com.example.core.util

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.os.Build
import android.os.PersistableBundle
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

object ClipboardHelper {

    private const val ExtraIsSensitive = "android.content.extra.IS_SENSITIVE"

    private var autoClearJob: Job? = null

    fun copyToClipboard(
        context: Context,
        label: String = "",
        text: String,
        isSensitive: Boolean = true,
        autoClearSeconds: Int = 30
    ) {
        val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        // Empty label reduces what OEM clipboard banners display.
        val clip = ClipData.newPlainText(label.ifBlank { " " }, text)

        if (isSensitive) {
            // Hides clipboard content preview in Android 13+ system copy UI.
            clip.description.extras = PersistableBundle().apply {
                putBoolean(ExtraIsSensitive, true)
            }
        }

        clipboard.setPrimaryClip(clip)

        autoClearJob?.cancel()
        if (isSensitive && autoClearSeconds > 0) {
            autoClearJob = CoroutineScope(Dispatchers.Main).launch {
                delay(autoClearSeconds * 1000L)
                clearIfStillMatching(clipboard, text)
            }
        }
    }

    private fun clearIfStillMatching(clipboard: ClipboardManager, text: String) {
        val currentClip = clipboard.primaryClip
        if (currentClip == null || currentClip.itemCount == 0) return
        val currentText = currentClip.getItemAt(0).text?.toString() ?: return
        if (currentText != text) return

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            clipboard.clearPrimaryClip()
        } else {
            clipboard.setPrimaryClip(ClipData.newPlainText("", ""))
        }
    }
}
