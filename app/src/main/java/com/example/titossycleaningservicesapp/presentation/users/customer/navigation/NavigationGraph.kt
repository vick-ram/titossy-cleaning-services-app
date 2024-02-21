package com.example.titossycleaningservicesapp.presentation.users.customer.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.titossycleaningservicesapp.presentation.users.customer.screens.AboutScreen
import com.example.titossycleaningservicesapp.presentation.users.customer.screens.BookingsScreen
import com.example.titossycleaningservicesapp.presentation.users.customer.screens.ContactScreen
import com.example.titossycleaningservicesapp.presentation.users.customer.screens.FAQsScreen
import com.example.titossycleaningservicesapp.presentation.users.customer.screens.HomeScreen
import com.example.titossycleaningservicesapp.presentation.users.customer.screens.ProfileScreen
import com.example.titossycleaningservicesapp.presentation.users.customer.utils.NavRoutes

@Composable
fun NavigationGraph(navController: NavHostController){
    NavHost(navController = navController, startDestination = NavRoutes.Home.route){
        composable(NavRoutes.Home.route){
            HomeScreen(navController)
        }
        composable(NavRoutes.Bookings.route){
            BookingsScreen(navController)
        }
        composable(NavRoutes.FAQs.route){
            FAQsScreen(navController)
        }
        composable(NavRoutes.Contact.route){
            ContactScreen(navController)
        }
        composable(NavRoutes.About.route){
            AboutScreen(navController)
        }
        composable(NavRoutes.Profile.route){
            ProfileScreen(navController)
        }
    }
}