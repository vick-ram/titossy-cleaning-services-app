package com.example.titossycleaningservicesapp.presentation.users.supervisor.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.titossycleaningservicesapp.domain.viewmodel.EmployeeViewModel
import com.example.titossycleaningservicesapp.domain.viewmodel.MainViewModel
import com.example.titossycleaningservicesapp.presentation.users.supervisor.screen.AllBookingsScreen
import com.example.titossycleaningservicesapp.presentation.users.supervisor.screen.ApprovedBookingsScreen
import com.example.titossycleaningservicesapp.presentation.users.supervisor.screen.CancelledBookings
import com.example.titossycleaningservicesapp.presentation.users.supervisor.screen.CompletedBookingsScreen
import com.example.titossycleaningservicesapp.presentation.users.supervisor.screen.ContactScreen
import com.example.titossycleaningservicesapp.presentation.users.supervisor.screen.FeedbackScreen
import com.example.titossycleaningservicesapp.presentation.users.supervisor.screen.HomeScreen
import com.example.titossycleaningservicesapp.presentation.users.supervisor.screen.InProgressBooking
import com.example.titossycleaningservicesapp.presentation.users.supervisor.screen.PendingBookingsScreen
import com.example.titossycleaningservicesapp.presentation.users.supervisor.screen.ProfileScreen
import com.example.titossycleaningservicesapp.presentation.users.supervisor.util.BookingRoutes
import com.example.titossycleaningservicesapp.presentation.users.supervisor.util.NavRoutes
import com.example.titossycleaningservicesapp.presentation.users.supervisor.util.SupervisorBookingDetails

@Composable
fun NavigationGraph(
    navController: NavHostController,
    paddingValues: PaddingValues,
    mainViewModel: MainViewModel,
    employeeViewModel: EmployeeViewModel
) {
    NavHost(
        navController = navController,
        startDestination = NavRoutes.Home.route
    ) {
        composable(NavRoutes.Home.route) {
            HomeScreen(
                navController = navController,
                paddingValues = paddingValues
            )
        }
        composable(NavRoutes.Contact.route) {
            ContactScreen(
                navController = navController,
                paddingValues = paddingValues
            )
        }
        composable(NavRoutes.Feedback.route) {
            FeedbackScreen(paddingValues)
        }
        composable(NavRoutes.Profile.route) {
            ProfileScreen(
                paddingValues = paddingValues,
                mainViewModel = mainViewModel,
                employeeViewModel = employeeViewModel
            )
        }
        composable(BookingRoutes.AllBookings.route) {
            AllBookingsScreen(navController = navController)
        }
        composable(BookingRoutes.PendingBookings.route) {
            PendingBookingsScreen(navController = navController)
        }
        composable(BookingRoutes.ApprovedBookings.route) {
            ApprovedBookingsScreen(navController = navController)
        }
        composable(BookingRoutes.InProgressBookings.route) {
            InProgressBooking(navController = navController)
        }
        composable(BookingRoutes.CompletedBookings.route) {
            CompletedBookingsScreen(navController = navController)
        }
        composable(BookingRoutes.CancelledBookings.route) {
            CancelledBookings(navController = navController)
        }
        composable("supplierBookingDetails/{bookingId}") { backStackEntry ->
            val bookingId = backStackEntry.arguments?.getString("bookingId").toString()
            SupervisorBookingDetails(
                bookingId = bookingId,
                navController = navController,
                paddingValues = paddingValues
            )
        }
    }
}

