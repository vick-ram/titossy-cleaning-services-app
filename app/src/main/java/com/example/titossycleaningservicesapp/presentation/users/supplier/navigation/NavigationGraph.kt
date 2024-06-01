package com.example.titossycleaningservicesapp.presentation.users.supplier.navigation

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.pager.PagerState
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.titossycleaningservicesapp.presentation.users.supplier.screens.ContactScreen
import com.example.titossycleaningservicesapp.presentation.users.supplier.screens.HomeScreen
import com.example.titossycleaningservicesapp.presentation.users.supplier.screens.ProfileScreen
import com.example.titossycleaningservicesapp.presentation.users.supplier.util.NavRoutes

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun NavigationGraph(navController: NavHostController, paddingValues: PaddingValues, pagerState: PagerState) {
    NavHost(navController = navController, startDestination = NavRoutes.Home.route) {
        composable(NavRoutes.Home.route) {
            HomeScreen(navController, paddingValues, pagerState)
        }
        composable(NavRoutes.Contact.route) {
            ContactScreen(navController)
        }
        composable(NavRoutes.Profile.route) {
            ProfileScreen(navController, paddingValues, pagerState)
        }
    }
}