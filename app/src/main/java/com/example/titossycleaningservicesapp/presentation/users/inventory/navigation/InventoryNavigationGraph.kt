package com.example.titossycleaningservicesapp.presentation.users.inventory.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.titossycleaningservicesapp.presentation.users.inventory.screens.ContactScreen
import com.example.titossycleaningservicesapp.presentation.users.inventory.screens.HomeScreen
import com.example.titossycleaningservicesapp.presentation.users.inventory.screens.ProfileScreen
import com.example.titossycleaningservicesapp.presentation.users.inventory.utils.NavRoutes

@Composable
fun InventoryNavigationGraph(navController: NavHostController, modifier: Modifier) {
    NavHost(navController = navController, startDestination = NavRoutes.Home.route) {
        composable(NavRoutes.Home.route){
            HomeScreen(navController, modifier)
        }
        composable(NavRoutes.Contact.route){
            ContactScreen(navController, modifier)
        }
        composable(NavRoutes.Profile.route){
            ProfileScreen(navController, modifier)
        }
    }
}