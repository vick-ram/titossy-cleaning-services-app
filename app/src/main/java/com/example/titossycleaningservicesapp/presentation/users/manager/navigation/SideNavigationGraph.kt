package com.example.titossycleaningservicesapp.presentation.users.manager.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.titossycleaningservicesapp.presentation.users.manager.screens.ContactScreen
import com.example.titossycleaningservicesapp.presentation.users.manager.screens.HomeScreen
import com.example.titossycleaningservicesapp.presentation.users.manager.screens.ProfileScreen
import com.example.titossycleaningservicesapp.presentation.users.manager.utils.RouteData

@Composable
fun SideNavigationGraph(navController: NavHostController, modifier: Modifier){
    NavHost(navController = navController, startDestination = RouteData.Home.route){
        composable(RouteData.Home.route){
            HomeScreen(navController = navController, modifier)
        }
        composable(RouteData.Contact.route){
            ContactScreen(navController = navController, modifier)
        }
        composable(RouteData.Profile.route) {
            ProfileScreen(navController, modifier)
        }
    }
}