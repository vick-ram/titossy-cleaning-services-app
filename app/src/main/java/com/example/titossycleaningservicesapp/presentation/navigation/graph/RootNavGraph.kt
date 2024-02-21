package com.example.titossycleaningservicesapp.presentation.navigation.graph

import androidx.compose.runtime.Composable
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import com.example.titossycleaningservicesapp.presentation.auth.signIn.EmployeesSignIn
import com.example.titossycleaningservicesapp.presentation.auth.signIn.SignInScreen
import com.example.titossycleaningservicesapp.presentation.auth.signUp.SignUpScreen
import com.example.titossycleaningservicesapp.presentation.auth.signUp.SignUpSuccessful
import com.example.titossycleaningservicesapp.presentation.users.navigation.usersNavigation
import com.example.titossycleaningservicesapp.presentation.utils.Authentication
import com.example.titossycleaningservicesapp.presentation.utils.RootNavRoutes
import com.example.titossycleaningservicesapp.ui.OnBoardingScreen

@Composable
fun RootNavGraph(navController: NavHostController) {
    NavHost(
        route = RootNavRoutes.ROOT.route,
        navController = navController,
        startDestination = RootNavRoutes.ONBOARDING.route
    ) {
        composable(RootNavRoutes.ONBOARDING.route){
            OnBoardingScreen(navController)
        }
        authNavGraph(navController)

        usersNavigation()
    }
}

fun NavGraphBuilder.authNavGraph(navController: NavHostController) {
    navigation(
        route = RootNavRoutes.AUTH.route,
        startDestination = Authentication.LOGIN.route
    ) {
        composable(Authentication.LOGIN.route) {
            SignInScreen(
                navController = navController,
                toSignUpScreen = { navController.navigate(Authentication.SIGNUP.route) }
            )
        }
        composable(Authentication.SIGNUP.route) {
            SignUpScreen(
                navController = navController,
                backToLogin = { navController.navigate(Authentication.LOGIN.route) },
            )
        }
        composable("success") {
            SignUpSuccessful(navController = navController)
        }

        composable(Authentication.EMPLOYEE.route){
            EmployeesSignIn(navController)
        }
    }
}
