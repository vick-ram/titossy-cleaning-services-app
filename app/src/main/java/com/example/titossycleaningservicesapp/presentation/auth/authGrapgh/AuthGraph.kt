package com.example.titossycleaningservicesapp.presentation.auth.authGrapgh

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import com.example.titossycleaningservicesapp.presentation.auth.signIn.EmployeesSignIn
import com.example.titossycleaningservicesapp.presentation.auth.signIn.SignInScreen
import com.example.titossycleaningservicesapp.presentation.auth.signUp.SignUpScreen
import com.example.titossycleaningservicesapp.presentation.auth.signUp.SignUpSuccessful
import com.example.titossycleaningservicesapp.presentation.utils.Authentication
import com.example.titossycleaningservicesapp.presentation.utils.RootNavRoutes

fun NavGraphBuilder.authNavGraph(
    navController: NavHostController,
    onboardingCompleted: () -> Unit = {}
) {
    navigation(
        route = RootNavRoutes.AUTH.route,
        startDestination = Authentication.LOGIN.route
    ) {
        composable(Authentication.LOGIN.route) {
            SignInScreen(
                navController = navController,
                toSignUpScreen = { navController.navigate(Authentication.SIGNUP.route) },
                onboardingCompleted = onboardingCompleted
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

        composable(Authentication.EMPLOYEE.route) {
            EmployeesSignIn(navController)
        }
    }
}