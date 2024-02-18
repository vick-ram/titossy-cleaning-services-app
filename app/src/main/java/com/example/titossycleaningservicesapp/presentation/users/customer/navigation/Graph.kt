package com.example.titossycleaningservicesapp.presentation.users.customer.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.ContactSupport
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.outlined.CalendarMonth
import androidx.compose.material.icons.outlined.ContactSupport
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Person
import androidx.compose.ui.graphics.vector.ImageVector

sealed class Graph(
    val route: String,
    val title: String,
    val iconSelected: ImageVector,
    val iconUnselected: ImageVector
) {
    data object Home : Graph(
        route = "home",
        title = "Home",
        iconSelected = Icons.Filled.Home,
        iconUnselected = Icons.Outlined.Home
    )
    data object Bookings : Graph(
        route = "bookings",
        title = "Bookings",
        iconSelected = Icons.Filled.CalendarMonth,
        iconUnselected = Icons.Outlined.CalendarMonth
    )
    data object Support : Graph(
        route = "support",
        title = "Support",
        iconSelected = Icons.Filled.ContactSupport,
        iconUnselected = Icons.Outlined.ContactSupport
    )
    data object Profile : Graph(
        route = "profile",
        title = "Profile",
        iconSelected = Icons.Filled.Person,
        iconUnselected = Icons.Outlined.Person
    )
}