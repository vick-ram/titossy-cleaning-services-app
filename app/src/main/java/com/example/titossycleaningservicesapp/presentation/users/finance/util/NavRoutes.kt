package com.example.titossycleaningservicesapp.presentation.users.finance.util

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.LocalMall
import androidx.compose.ui.graphics.vector.ImageVector

sealed class NavRoutes(val route: String, val title: String, val icon: ImageVector) {

    data object Home : NavRoutes("home", "Home", Icons.Default.Home)
    data object Orders : NavRoutes("orders", "Orders", Icons.Filled.LocalMall)
    data object Profile : NavRoutes("profile", "Profile", Icons.Default.AccountCircle)
}