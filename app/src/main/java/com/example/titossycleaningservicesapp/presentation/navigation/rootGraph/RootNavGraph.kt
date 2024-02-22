package com.example.titossycleaningservicesapp.presentation.navigation.rootGraph

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.titossycleaningservicesapp.presentation.auth.authGraph.authNavGraph
import com.example.titossycleaningservicesapp.presentation.users.navigation.usersNavigation
import com.example.titossycleaningservicesapp.presentation.utils.RootNavRoutes
import com.example.titossycleaningservicesapp.ui.OnBoardingScreen

@Composable
fun RootNavGraph(navController: NavHostController) {
    NavHost(
        route = RootNavRoutes.ROOT.route,
        navController = navController,
        startDestination = RootNavRoutes.ONBOARDING.route
    ) {
        composable(RootNavRoutes.ONBOARDING.route) {
            OnBoardingScreen(navController)
        }
        authNavGraph(navController)

        usersNavigation()
    }
}
