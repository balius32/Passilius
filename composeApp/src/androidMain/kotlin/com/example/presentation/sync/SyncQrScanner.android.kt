package com.example.presentation.sync

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import com.journeyapps.barcodescanner.ScanContract
import com.journeyapps.barcodescanner.ScanOptions

@Composable
actual fun rememberSyncQrScanner(
    onResult: (String) -> Unit,
    onUnsupported: () -> Unit
): () -> Unit {
    val launcher = rememberLauncherForActivityResult(ScanContract()) { result ->
        val text = result.contents
        if (!text.isNullOrBlank()) onResult(text)
    }
    return remember(launcher) {
        {
            val options = ScanOptions()
                .setDesiredBarcodeFormats(ScanOptions.QR_CODE)
                .setPrompt("Scan Passilius sync QR")
                .setBeepEnabled(false)
                .setOrientationLocked(true)
            launcher.launch(options)
        }
    }
}
