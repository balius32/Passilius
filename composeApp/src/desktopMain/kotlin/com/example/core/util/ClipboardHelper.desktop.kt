package com.example.core.util

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.awt.Toolkit
import java.awt.datatransfer.StringSelection

actual object ClipboardHelper {

    private var autoClearJob: Job? = null

    actual fun copy(
        text: String,
        label: String,
        isSensitive: Boolean,
        autoClearSeconds: Int
    ) {
        val clipboard = Toolkit.getDefaultToolkit().systemClipboard
        clipboard.setContents(StringSelection(text), null)

        autoClearJob?.cancel()
        if (isSensitive && autoClearSeconds > 0) {
            autoClearJob = CoroutineScope(Dispatchers.Default).launch {
                delay(autoClearSeconds * 1000L)
                val current = runCatching {
                    clipboard.getData(java.awt.datatransfer.DataFlavor.stringFlavor) as? String
                }.getOrNull()
                if (current == text) {
                    clipboard.setContents(StringSelection(""), null)
                }
            }
        }
    }
}
