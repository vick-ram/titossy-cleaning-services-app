package com.example.titossycleaningservicesapp.presentation.users.supplier.util

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ContactSupport
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.outlined.Dashboard
import androidx.compose.material.icons.outlined.Recycling
import androidx.compose.ui.graphics.vector.ImageVector

sealed class NavRoutes(val route: String, val title: String, val icon: ImageVector) {

    data object Products : NavRoutes("products", "Products", Icons.Outlined.Dashboard)
    data object Home : NavRoutes("home", "Home", Icons.Default.Home)
    data object Contact : NavRoutes("contact", "Contact", Icons.AutoMirrored.Default.ContactSupport)
    data object Profile : NavRoutes("profile", "Profile", Icons.Default.AccountCircle)
}