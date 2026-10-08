package com.example

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import com.example.core.designsystem.VaultTheme
import com.example.core.platform.LocalPlatformCapabilities
import com.example.core.platform.platformCapabilities
import com.example.di.AppGraph
import com.example.presentation.navigation.VaultApp

@Composable
fun App(appGraph: AppGraph) {
    CompositionLocalProvider(
        LocalPlatformCapabilities provides platformCapabilities()
    ) {
        VaultTheme {
            VaultApp(
                vaultViewModel = appGraph.vaultViewModel,
                generatorViewModel = appGraph.generatorViewModel,
                manageCategoriesViewModel = appGraph.manageCategoriesViewModel,
                syncViewModel = appGraph.syncViewModel
            )
        }
    }
}
