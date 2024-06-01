package com.example.titossycleaningservicesapp.presentation.users.cleaner.util

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Help
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Home
import androidx.compose.ui.graphics.vector.ImageVector

sealed class CleanerNavRoutes(val routes: String, val title: String, val icon: ImageVector) {
    data object Home : CleanerNavRoutes("home", "Home", Icons.Default.Home)
    data object About : CleanerNavRoutes("about", "About", Icons.AutoMirrored.Filled.Help)
    data object Profile : CleanerNavRoutes("profile", "Profile", Icons.Default.AccountCircle)
    data object LogOut : CleanerNavRoutes("logout", "LogOut", Icons.AutoMirrored.Filled.Logout)

}