package com.example.presentation.sync

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
expect fun SyncQrCamera(
    onResult: (String) -> Unit,
    modifier: Modifier = Modifier
)
