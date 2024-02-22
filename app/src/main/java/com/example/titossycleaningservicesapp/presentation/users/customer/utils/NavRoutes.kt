package com.example.titossycleaningservicesapp.presentation.users.customer.utils

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ContactSupport
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.QuestionMark
import androidx.compose.material.icons.filled.Today
import androidx.compose.ui.graphics.vector.ImageVector
import com.example.titossycleaningservicesapp.presentation.utils.RootNavRoutes

sealed class NavRoutes(val route: String, val title: String, val icon: ImageVector) {

    data object Home : NavRoutes("home", "Home", Icons.Default.Home)
    data object Bookings : NavRoutes("bookings", "Bookings", Icons.Default.Today)
    data object FAQs : NavRoutes("faqs", "FAQs", Icons.Default.QuestionMark)
    data object Contact : NavRoutes("contact", "Contact", Icons.AutoMirrored.Default.ContactSupport)
    data object About : NavRoutes("about", "About", Icons.AutoMirrored.Default.ContactSupport)
    data object Profile : NavRoutes("profile", "Profile", Icons.Default.AccountCircle)
    data object LogOut :
        NavRoutes(RootNavRoutes.AUTH.route, "LogOut", Icons.AutoMirrored.Default.Logout)
}