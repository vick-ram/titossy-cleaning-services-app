package com.example.titossycleaningservicesapp.presentation.navigation.rootGraph

import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.titossycleaningservicesapp.presentation.auth.authGrapgh.authNavGraph
import com.example.titossycleaningservicesapp.presentation.users.navigation.usersNavigation
import com.example.titossycleaningservicesapp.presentation.utils.RootNavRoutes
import com.example.titossycleaningservicesapp.ui.OnBoardingScreen

@Composable
fun RootNavGraph(navController: NavHostController) {
    var isLoading by remember { mutableStateOf(false) }
    NavHost(
        route = RootNavRoutes.ROOT.route,
        navController = navController,
        startDestination = RootNavRoutes.ONBOARDING.route
    ) {
        composable(RootNavRoutes.ONBOARDING.route) {
            OnBoardingScreen(navController, onboardingStarted = { isLoading = true })
        }
        authNavGraph(navController, onboardingCompleted = { isLoading = false })

        usersNavigation()
    }

    if (isLoading) {
        CircularProgressIndicator()
    }
}
