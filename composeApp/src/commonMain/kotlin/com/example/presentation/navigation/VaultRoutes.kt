package com.example.presentation.navigation

sealed interface VaultDestination

data object VaultRoute : VaultDestination
data object GeneratorRoute : VaultDestination
data object SettingsRoute : VaultDestination
data object CategoriesRoute : VaultDestination
data object SyncRoute : VaultDestination
