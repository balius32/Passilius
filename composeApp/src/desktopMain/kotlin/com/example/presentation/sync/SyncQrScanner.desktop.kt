package com.example.presentation.sync

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember

@Composable
actual fun rememberSyncQrScanner(
    onResult: (String) -> Unit,
    onUnsupported: () -> Unit
): () -> Unit = remember(onUnsupported) { onUnsupported }
