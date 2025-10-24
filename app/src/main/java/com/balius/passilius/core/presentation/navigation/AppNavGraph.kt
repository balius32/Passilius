package com.balius.passilius.core.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.balius.passilius.features.password.presentation.crud.PasswordScreen
import com.balius.passilius.features.password.presentation.home.presentation.pages.HomeScreen


@Composable
fun AppNavGraph(navController: NavHostController){

    NavHost(
        navController = navController,
        startDestination = Screen.Home.route
    ){
        composable(Screen.Home.route) {
            HomeScreen(navController)
        }
        composable(Screen.Crud.route) {
            PasswordScreen(navController)
        }
    }


}