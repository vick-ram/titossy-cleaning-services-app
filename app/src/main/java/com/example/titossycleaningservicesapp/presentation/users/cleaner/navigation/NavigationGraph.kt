package com.example.titossycleaningservicesapp.presentation.users.cleaner.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.titossycleaningservicesapp.presentation.users.cleaner.screens.AboutScreen
import com.example.titossycleaningservicesapp.presentation.users.cleaner.util.CleanerNavRoutes
import com.example.titossycleaningservicesapp.presentation.users.cleaner.screens.HomeScreen
import com.example.titossycleaningservicesapp.presentation.users.cleaner.screens.ProfileScreen

@Composable
fun NavigationGraph(navController: NavHostController) {
    NavHost(navController = navController, startDestination = CleanerNavRoutes.Home.routes) {
        composable(CleanerNavRoutes.Home.routes) {
            HomeScreen(navController)
        }
        composable(CleanerNavRoutes.About.routes) {
            AboutScreen(navController)
        }
        composable(CleanerNavRoutes.Profile.routes) {
            ProfileScreen(navController)
        }
    }
}