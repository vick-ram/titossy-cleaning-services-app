package com.example.titossycleaningservicesapp.presentation.users.manager.utils

import androidx.compose.material.icons.Icons
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
    data object Profile: RouteData(
        route = "profile",
        icon = Icons.Default.AccountCircle,
        title = "Profile"
    )
}