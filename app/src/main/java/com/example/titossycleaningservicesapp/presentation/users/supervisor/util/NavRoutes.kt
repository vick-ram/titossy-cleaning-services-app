package com.example.titossycleaningservicesapp.presentation.users.supervisor.util

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.AddToHomeScreen
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.automirrored.filled.Message
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.ui.graphics.vector.ImageVector

sealed class NavRoutes(val route: String, val title: String, val icon: ImageVector) {
    data object Home : NavRoutes("home", "Home", Icons.AutoMirrored.Filled.AddToHomeScreen)
    data object Contact : NavRoutes("contact", "Contact", Icons.AutoMirrored.Filled.Message)
    data object Profile : NavRoutes("profile", "Profile", Icons.Filled.AccountCircle)
    data object LogOut : NavRoutes("logout", "LogOut", Icons.AutoMirrored.Filled.Logout)
}

sealed class BookingRoutes(val route: String) {
    data object AllBookings: BookingRoutes(route = "all_bookings")
    data object PendingBookings: BookingRoutes(route = "pending_bookings")
    data object ApprovedBookings: BookingRoutes(route = "approved_bookings")
    data object CompletedBookings: BookingRoutes(route = "completed_bookings")
}
