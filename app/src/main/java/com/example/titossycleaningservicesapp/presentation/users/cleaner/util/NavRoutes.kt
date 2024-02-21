package com.example.titossycleaningservicesapp.presentation.users.cleaner.util

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Help
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Home
import androidx.compose.ui.graphics.vector.ImageVector

sealed class NavRoutes(val routes: String, val title: String, val icon: ImageVector) {
    data object Home : NavRoutes("home", "Home", Icons.Default.Home)
    data object About : NavRoutes("about", "About", Icons.AutoMirrored.Filled.Help)
    data object Profile : NavRoutes("profile", "Profile", Icons.Default.AccountCircle)
    data object LogOut : NavRoutes("logout", "LogOut", Icons.AutoMirrored.Filled.Logout)

}