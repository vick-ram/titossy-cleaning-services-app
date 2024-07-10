package com.example.titossycleaningservicesapp.presentation.users.supervisor.util

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.AddToHomeScreen
import androidx.compose.material.icons.automirrored.filled.Help
import androidx.compose.material.icons.automirrored.filled.Message
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Help
import androidx.compose.material.icons.filled.Home
import androidx.compose.ui.graphics.vector.ImageVector

sealed class NavRoutes(val route: String, val title: String, val icon: ImageVector) {
    data object Home : NavRoutes("home", "Home", Icons.Filled.Home)
    data object Contact : NavRoutes("contact", "Contact", Icons.AutoMirrored.Filled.Help)
    data object Profile : NavRoutes("profile", "Profile", Icons.Filled.AccountCircle)
}

sealed class BookingRoutes(val route: String) {
    data object AllBookings: BookingRoutes(route = "all_bookings")
    data object PendingBookings: BookingRoutes(route = "pending_bookings")
    data object ApprovedBookings: BookingRoutes(route = "approved_bookings")
    data object InProgressBookings: BookingRoutes(route = "inProgress_bookings")
    data object CompletedBookings: BookingRoutes(route = "completed_bookings")
    data object CancelledBookings: BookingRoutes(route = "cancelled_bookings")
}
