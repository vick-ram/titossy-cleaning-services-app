package com.example.titossycleaningservicesapp.presentation.users.customer.utils

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ContactSupport
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.QuestionMark
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Today
import androidx.compose.material.icons.outlined.CalendarMonth
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.ui.graphics.vector.ImageVector

sealed class NavRoutes(val route: String, val title: String, val icon: ImageVector) {

    data object Home : NavRoutes("home", "Home", Icons.Default.Home)
    data object Bookings : NavRoutes("bookings", "Bookings", Icons.Default.Today)
    data object FAQs : NavRoutes("faqs", "FAQs", Icons.Default.QuestionMark)
    data object Contact : NavRoutes("contact", "Contact", Icons.AutoMirrored.Default.ContactSupport)
    data object About : NavRoutes("about", "About", Icons.AutoMirrored.Default.ContactSupport)
    data object Profile : NavRoutes("profile", "Profile", Icons.Default.AccountCircle)
}

enum class CustomerBottomRoutes(
    val route: String,
    val title: String,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector
) {
    Home(
        route = "home",
        title = "Home",
        selectedIcon = Icons.Filled.Home,
        unselectedIcon = Icons.Outlined.Home
    ),
    Bookings(
        route = "bookings",
        title = "Bookings",
        selectedIcon = Icons.Filled.CalendarMonth,
        unselectedIcon = Icons.Outlined.CalendarMonth
    ),
    Settings(
        route = "settings",
        title = "Settings",
        selectedIcon = Icons.Filled.Settings,
        unselectedIcon = Icons.Outlined.Settings
    )
}

enum class DetailsRoute {
    DETAILS
}

sealed class DetailsRoutes(val route: String) {
    data object Details : DetailsRoutes("serviceDetails")
    data object BookingDetails: DetailsRoutes("bookingDetails")
    data object CheckOut : DetailsRoutes("checkOut")
    data object BookingSuccess: DetailsRoutes("bookingSuccess")
}