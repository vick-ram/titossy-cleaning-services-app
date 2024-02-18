package com.example.titossycleaningservicesapp.presentation.users.manager.utils

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Dashboard
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.RequestQuote
import androidx.compose.ui.graphics.vector.ImageVector

sealed class RouteData(
    val route: String,
    val icon: ImageVector,
    val title: String
) {
    data object Dashboard: RouteData(
        route = "dashboard",
        icon = Icons.Default.Dashboard,
        title = "Dashboard"
    )
    data object Home: RouteData(
        route = "home",
        icon = Icons.Default.Home,
        title = "Home"
    )
    data object Profile: RouteData(
        route = "profile",
        icon = Icons.Default.Person,
        title = "Profile"
    )
    data object Notifications: RouteData(
        route = "notifications",
        icon = Icons.Default.Notifications,
        title = "notifications"
    )
    data object Invoices: RouteData(
        route = "invoices",
        icon = Icons.Default.RequestQuote,
        title = "Invoices"
    )
    data object LogOut: RouteData(
        route = "log out",
        icon = Icons.Default.Logout,
        title = "Log out"
    )
}