package com.example.titossycleaningservicesapp.presentation.auth.authGraph

import androidx.compose.foundation.layout.PaddingValues
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.example.titossycleaningservicesapp.domain.viewmodel.CustomerViewModel
import com.example.titossycleaningservicesapp.presentation.auth.signIn.EmployeesSignIn
import com.example.titossycleaningservicesapp.presentation.auth.signIn.CustomerSignInScreen
import com.example.titossycleaningservicesapp.presentation.auth.signIn.SupplierSignInScreen
import com.example.titossycleaningservicesapp.presentation.auth.signUp.CustomerSignUpScreen
import com.example.titossycleaningservicesapp.presentation.auth.signUp.SupplierSignUpScreen
import com.example.titossycleaningservicesapp.presentation.auth.utils.ApprovalScreen
import com.example.titossycleaningservicesapp.presentation.auth.utils.SupplierApprovalScreen
import com.example.titossycleaningservicesapp.presentation.utils.Authentication
import com.example.titossycleaningservicesapp.presentation.utils.RootNavRoutes

fun NavGraphBuilder.authNavGraph(
    navController: NavHostController,
    customerViewModel: CustomerViewModel,
    paddingValues: PaddingValues
) {
    navigation(
        route = RootNavRoutes.AUTH.route,
        startDestination = Authentication.LOGIN.route
    ) {
        composable(Authentication.LOGIN.route) {
            CustomerSignInScreen(
                navController = navController,
                toSignUpScreen = { navController.navigate(Authentication.SIGNUP.route) },
                customerViewModel = customerViewModel,
                paddingValues = paddingValues
            )
        }
        composable(Authentication.SIGNUP.route) {
            CustomerSignUpScreen(
                navController = navController,
                backToLogin = { navController.navigate(Authentication.LOGIN.route) },
                paddingValues = paddingValues
            )
        }
        composable(Authentication.SUPPLIER_SIGNUP.route) {
            SupplierSignUpScreen(navController = navController, paddingValues)
        }

        composable(Authentication.SUPPLIER.route) {
            SupplierSignInScreen(
                navController = navController,
                paddingValues = paddingValues
            )
        }

        composable(Authentication.EMPLOYEE.route) {
            EmployeesSignIn(navController, paddingValues)
        }
        composable(Authentication.APPROVAL.route) {
            ApprovalScreen(navController)
        }
        composable(Authentication.SUPPLIER_APPROVAL.route) {
            SupplierApprovalScreen(navController)
        }
    }
}
