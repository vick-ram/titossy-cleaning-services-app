package com.example.titossycleaningservicesapp.presentation.users.supervisor.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.titossycleaningservicesapp.presentation.users.supervisor.screen.AllBookingsScreen
import com.example.titossycleaningservicesapp.presentation.users.supervisor.screen.ApprovedBookingsScreen
import com.example.titossycleaningservicesapp.presentation.users.supervisor.screen.CompletedBookingsScreen
import com.example.titossycleaningservicesapp.presentation.users.supervisor.screen.ContactScreen
import com.example.titossycleaningservicesapp.presentation.users.supervisor.screen.HomeScreen
import com.example.titossycleaningservicesapp.presentation.users.supervisor.screen.PendingBookingsScreen
import com.example.titossycleaningservicesapp.presentation.users.supervisor.screen.ProfileScreen
import com.example.titossycleaningservicesapp.presentation.users.supervisor.util.BookingRoutes
import com.example.titossycleaningservicesapp.presentation.users.supervisor.util.NavRoutes

@Composable
fun NavigationGraph(navController: NavHostController, paddingValues: PaddingValues) {
    NavHost(navController = navController, startDestination = NavRoutes.Home.route) {
        composable(NavRoutes.Home.route) {
            HomeScreen(navController, paddingValues)
        }
        composable(NavRoutes.Contact.route) {
            ContactScreen(navController, paddingValues)
        }
        composable(NavRoutes.Profile.route) {
            ProfileScreen(navController, paddingValues)
        }
        composable(BookingRoutes.AllBookings.route) {
            AllBookingsScreen()
        }
        composable(BookingRoutes.PendingBookings.route) {
            PendingBookingsScreen()
        }
        composable(BookingRoutes.ApprovedBookings.route) {
            ApprovedBookingsScreen()
        }
        composable(BookingRoutes.CompletedBookings.route) {
            CompletedBookingsScreen()
        }
    }
}
