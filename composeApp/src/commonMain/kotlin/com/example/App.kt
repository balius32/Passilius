package com.example

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import com.example.core.designsystem.VaultTheme
import com.example.core.platform.LocalPlatformCapabilities
import com.example.core.platform.platformCapabilities
import com.example.di.AppGraph
import com.example.presentation.navigation.VaultApp
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue

@Composable
fun App(appGraph: AppGraph) {
    val categoryIdentities by appGraph.categoryIdentities.collectAsState(emptyMap())
    CompositionLocalProvider(
        com.example.core.localization.LocalCategoryIdentities provides categoryIdentities,
        androidx.compose.ui.platform.LocalLayoutDirection provides
            if (com.example.core.localization.LanguageController.language == "fa") androidx.compose.ui.unit.LayoutDirection.Rtl else androidx.compose.ui.unit.LayoutDirection.Ltr,
        LocalPlatformCapabilities provides platformCapabilities()
    ) {
        VaultTheme {
            VaultApp(
                vaultViewModel = appGraph.vaultViewModel,
                generatorViewModel = appGraph.generatorViewModel,
                manageCategoriesViewModel = appGraph.manageCategoriesViewModel,
                syncViewModel = appGraph.syncViewModel,
                settingsViewModel = appGraph.settingsViewModel
            )
        }
    }
}
