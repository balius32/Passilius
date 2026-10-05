package com.example.presentation.navigation

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable

@Serializable
data object VaultRoute : NavKey

@Serializable
data object GeneratorRoute : NavKey

@Serializable
data object SettingsRoute : NavKey

@Serializable
data object CategoriesRoute : NavKey
