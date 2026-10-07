package com.example.core.util

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.statusBars
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun rememberScreenContentPadding(
    horizontal: Dp = 20.dp,
    topExtra: Dp = 20.dp,
    bottom: Dp = 120.dp
): PaddingValues {
    val statusBars = WindowInsets.statusBars.asPaddingValues()
    val navigationBars = WindowInsets.navigationBars.asPaddingValues()
    return PaddingValues(
        start = horizontal,
        end = horizontal,
        top = statusBars.calculateTopPadding() + topExtra,
        bottom = navigationBars.calculateBottomPadding() + bottom
    )
}
