package com.example.titossycleaningservicesapp.presentation.auth.authGraph

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.example.titossycleaningservicesapp.presentation.auth.signIn.EmployeesSignIn
import com.example.titossycleaningservicesapp.presentation.auth.signIn.CustomerSignInScreen
import com.example.titossycleaningservicesapp.presentation.auth.signUp.CustomerSignUpScreen
import com.example.titossycleaningservicesapp.presentation.auth.signUp.SignUpSuccessful
import com.example.titossycleaningservicesapp.presentation.utils.Authentication
import com.example.titossycleaningservicesapp.presentation.utils.RootNavRoutes

fun NavGraphBuilder.authNavGraph(
    navController: NavHostController,
) {
    navigation(
        route = RootNavRoutes.AUTH.route,
        startDestination = Authentication.LOGIN.route
    ) {
        composable(Authentication.LOGIN.route) {
            CustomerSignInScreen(
                navController = navController,
                toSignUpScreen = { navController.navigate(Authentication.SIGNUP.route) }
            )
        }
        composable(Authentication.SIGNUP.route) {
            CustomerSignUpScreen(
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
