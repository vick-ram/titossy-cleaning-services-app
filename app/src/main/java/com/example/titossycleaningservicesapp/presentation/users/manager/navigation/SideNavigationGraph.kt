package com.example.titossycleaningservicesapp.presentation.users.manager.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.titossycleaningservicesapp.presentation.users.manager.screens.Dashboard
import com.example.titossycleaningservicesapp.presentation.users.manager.screens.HomeScreen
import com.example.titossycleaningservicesapp.presentation.users.manager.screens.InvoiceScreen
import com.example.titossycleaningservicesapp.presentation.users.manager.screens.LogOut
import com.example.titossycleaningservicesapp.presentation.users.manager.screens.Notifications
import com.example.titossycleaningservicesapp.presentation.users.manager.screens.ProfileScreen
import com.example.titossycleaningservicesapp.presentation.users.manager.utils.RouteData

@Composable
fun SideNavigationGraph(navController: NavHostController, modifier: Modifier){
    NavHost(navController = navController, startDestination = RouteData.Dashboard.route){
        composable(RouteData.Dashboard.route){
            Dashboard(navController = navController,modifier)
        }
        composable(RouteData.Home.route){
            HomeScreen(navController = navController, modifier)
        }
        composable(RouteData.Profile.route){
            ProfileScreen(navController, modifier)
        }
        composable(RouteData.Notifications.route){
            Notifications(navController, modifier)
        }
        composable(RouteData.Invoices.route){
            InvoiceScreen(navController, modifier)
        }
        composable(RouteData.LogOut.route){
            LogOut(navController, modifier)
        }
    }
}