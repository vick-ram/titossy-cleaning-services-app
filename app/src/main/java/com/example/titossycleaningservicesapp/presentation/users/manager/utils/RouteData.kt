package com.example.titossycleaningservicesapp.presentation.users.manager.utils

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.LocalMall
import androidx.compose.material.icons.filled.LocalShipping
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

    data object Supplier: RouteData(
        route = "managerSuppliers",
        icon = Icons.Default.LocalShipping,
        title = "Supplier"
    )

    data object Orders: RouteData(
        route = "managerOrders",
        icon = Icons.Default.LocalMall,
        title = "Orders"
    )
    data object Profile: RouteData(
        route = "profile",
        icon = Icons.Default.AccountCircle,
        title = "Profile"
    )
}