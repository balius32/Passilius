package com.example.presentation.sync

import androidx.compose.runtime.Composable

/**
 * Returns a lambda that launches the platform QR scanner.
 * On desktop, scanning is unsupported (no-op / error via onUnsupported).
 */
@Composable
expect fun rememberSyncQrScanner(
    onResult: (String) -> Unit,
    onUnsupported: () -> Unit = {}
): () -> Unit
