package com.example.titossycleaningservicesapp.presentation.users.cleaner.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.titossycleaningservicesapp.domain.viewmodel.EmployeeViewModel
import com.example.titossycleaningservicesapp.domain.viewmodel.MainViewModel
import com.example.titossycleaningservicesapp.presentation.users.cleaner.screens.AboutScreen
import com.example.titossycleaningservicesapp.presentation.users.cleaner.screens.HomeScreen
import com.example.titossycleaningservicesapp.presentation.users.cleaner.screens.ProfileScreen
import com.example.titossycleaningservicesapp.presentation.users.cleaner.util.CleanerNavRoutes

@Composable
fun NavigationGraph(
    navController: NavHostController,
    paddingValues: PaddingValues,
    mainViewModel: MainViewModel,
    employeeViewModel: EmployeeViewModel
) {
    NavHost(
        navController = navController,
        startDestination = CleanerNavRoutes.Home.routes,
    ) {
        composable(CleanerNavRoutes.Home.routes) {
            HomeScreen(
                navController = navController,
                paddingValues = paddingValues
            )
        }
        composable(CleanerNavRoutes.About.routes) {
            AboutScreen(
                navController = navController,
                paddingValues = paddingValues
            )
        }
        composable(CleanerNavRoutes.Profile.routes) {
            ProfileScreen(
                paddingValues = paddingValues,
                mainViewModel = mainViewModel,
                employeeViewModel = employeeViewModel
            )
        }
    }
}