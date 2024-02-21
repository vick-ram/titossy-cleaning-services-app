package com.example.titossycleaningservicesapp.presentation.users.cleaner.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.titossycleaningservicesapp.presentation.users.cleaner.screens.AboutScreen
import com.example.titossycleaningservicesapp.presentation.users.cleaner.util.NavRoutes
import com.example.titossycleaningservicesapp.presentation.users.cleaner.screens.HomeScreen
import com.example.titossycleaningservicesapp.presentation.users.cleaner.screens.ProfileScreen

@Composable
fun NavigationGraph(navController: NavHostController) {
    NavHost(navController = navController, startDestination = NavRoutes.Home.routes) {
        composable(NavRoutes.Home.routes) {
            HomeScreen(navController)
        }
        composable(NavRoutes.About.routes) {
            AboutScreen(navController)
        }
        composable(NavRoutes.Profile.routes) {
            ProfileScreen(navController)
        }
    }
}