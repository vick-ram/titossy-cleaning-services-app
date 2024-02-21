package com.example.titossycleaningservicesapp.presentation.users.supplier.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.titossycleaningservicesapp.presentation.users.supplier.screens.ContactScreen
import com.example.titossycleaningservicesapp.presentation.users.supplier.screens.HomeScreen
import com.example.titossycleaningservicesapp.presentation.users.supplier.screens.ProfileScreen
import com.example.titossycleaningservicesapp.presentation.users.supplier.util.NavRoutes

@Composable
fun NavigationGraph(navController: NavHostController) {
    NavHost(navController = navController, startDestination = NavRoutes.Home.route) {
        composable(NavRoutes.Home.route) {
            HomeScreen(navController)
        }
        composable(NavRoutes.Contact.route) {
            ContactScreen(navController)
        }
        composable(NavRoutes.Profile.route) {
            ProfileScreen(navController)
        }
    }
}