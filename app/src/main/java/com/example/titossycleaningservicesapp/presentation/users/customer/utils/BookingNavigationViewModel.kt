package com.example.titossycleaningservicesapp.presentation.users.customer.utils

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.ViewModel
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavHostController

@Composable
inline fun <reified T: ViewModel>NavBackStackEntry.sharedViewModel(
    navController: NavHostController
) : T {
    val navGraphRoute = destination.parent?.route
    val parentEntry = remember(this) {
        navGraphRoute?.let {
            navController.getBackStackEntry(it)
        }
    }
    return hiltViewModel(parentEntry ?: this)
}