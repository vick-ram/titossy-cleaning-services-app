package com.example.titossycleaningservicesapp.presentation.users.inventory.utils

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ContactSupport
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Home
import androidx.compose.ui.graphics.vector.ImageVector

sealed class NavRoutes(
    val route: String,
    val icon: ImageVector,
    val title: String
) {
    data object Home: NavRoutes(
        route = "home",
        icon = Icons.Default.Home,
        title = "Home"
    )
    data object Contact: NavRoutes(
        route = "contact",
        icon = Icons.AutoMirrored.Filled.ContactSupport,
        title = "Contact"
    )
    data object Profile: NavRoutes(
        route = "profile",
        icon = Icons.Default.AccountCircle,
        title = "Profile"
    )
    data object LogOut: NavRoutes(
        route = "log out",
        icon = Icons.AutoMirrored.Filled.Logout,
        title = "Log out"
    )
}