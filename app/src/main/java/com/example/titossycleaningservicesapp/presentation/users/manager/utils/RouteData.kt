package com.example.titossycleaningservicesapp.presentation.users.manager.utils

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ContactSupport
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Home
import androidx.compose.ui.graphics.vector.ImageVector

sealed class RouteData(
    val route: String,
    val icon: ImageVector,
    val title: String
) {
    data object Home: RouteData(
        route = "home",
        icon = Icons.Default.Home,
        title = "Home"
    )
    data object Contact: RouteData(
        route = "contact",
        icon = Icons.AutoMirrored.Filled.ContactSupport,
        title = "Contact"
    )
    data object Profile: RouteData(
        route = "profile",
        icon = Icons.Default.AccountCircle,
        title = "Profile"
    )
    data object LogOut: RouteData(
        route = "log out",
        icon = Icons.AutoMirrored.Filled.Logout,
        title = "Log out"
    )
}