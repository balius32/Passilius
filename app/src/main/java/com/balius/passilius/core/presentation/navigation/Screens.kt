package com.balius.passilius.core.presentation.navigation

sealed class Screen(val route: String) {

    object Home : Screen("home")
    object Crud : Screen("crud")

}
