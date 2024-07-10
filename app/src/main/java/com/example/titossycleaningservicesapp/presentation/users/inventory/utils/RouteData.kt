package com.example.titossycleaningservicesapp.presentation.users.inventory.utils

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.LocalMall
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
    data object Orders: NavRoutes(
        route = "orders",
        icon = Icons.Filled.LocalMall,
        title = "Orders"
    )
    data object Profile: NavRoutes(
        route = "profile",
        icon = Icons.Default.AccountCircle,
        title = "Profile"
    )
}