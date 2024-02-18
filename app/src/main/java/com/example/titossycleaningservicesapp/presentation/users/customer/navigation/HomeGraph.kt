package com.example.titossycleaningservicesapp.presentation.users.customer.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.titossycleaningservicesapp.presentation.users.customer.screens.BookingsScreen
import com.example.titossycleaningservicesapp.presentation.users.customer.screens.HomeScreen
import com.example.titossycleaningservicesapp.presentation.users.customer.screens.NotificationsScreen
import com.example.titossycleaningservicesapp.presentation.users.customer.screens.ProfileScreen
import com.example.titossycleaningservicesapp.presentation.utils.RootNavRoutes

@Composable
fun HomeNavGraph(navController: NavHostController){
    NavHost(
        route = RootNavRoutes.HOME.route,
        navController = navController,
        startDestination = Graph.Home.route
    ){
        composable(Graph.Home.route){
            HomeScreen(navController)
        }
        composable(Graph.Bookings.route){
            BookingsScreen(navController)
        }
        composable(Graph.Support.route){
            NotificationsScreen(navController)
        }
        composable(Graph.Profile.route){
            ProfileScreen(navController)
        }
    }
}