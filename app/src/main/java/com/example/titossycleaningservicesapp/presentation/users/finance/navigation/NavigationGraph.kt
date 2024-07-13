package com.example.titossycleaningservicesapp.presentation.users.finance.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.titossycleaningservicesapp.domain.viewmodel.EmployeeViewModel
import com.example.titossycleaningservicesapp.domain.viewmodel.MainViewModel
import com.example.titossycleaningservicesapp.domain.viewmodel.PaymentViewModel
import com.example.titossycleaningservicesapp.presentation.users.finance.screens.HomeScreen
import com.example.titossycleaningservicesapp.presentation.users.finance.screens.ProfileScreen
import com.example.titossycleaningservicesapp.presentation.users.finance.screens.PurchaseOrdersScreen
import com.example.titossycleaningservicesapp.presentation.users.finance.util.CustomerIncomeScreen
import com.example.titossycleaningservicesapp.presentation.users.finance.util.NavRoutes

@Composable
fun NavigationGraph(
    navController: NavHostController,
    paddingValues: PaddingValues,
    mainViewModel: MainViewModel,
    employeeViewModel: EmployeeViewModel,
    paymentViewModel: PaymentViewModel
) {
    NavHost(
        navController = navController,
        startDestination = NavRoutes.Home.route
    ) {
        composable(NavRoutes.Home.route) {
            HomeScreen(
                navController = navController,
                paymentViewModel = paymentViewModel,
                paddingValues = paddingValues
                )
        }
        composable(NavRoutes.Orders.route) {
            PurchaseOrdersScreen(
                paddingValues = paddingValues
            )
        }
        composable(NavRoutes.Profile.route) {
            ProfileScreen(
                paddingValues = paddingValues,
                mainViewModel = mainViewModel,
                employeeViewModel = employeeViewModel
            )
        }
        composable("CustomerPayment") {
            CustomerIncomeScreen(
                paymentViewModel = paymentViewModel
            )
        }
    }
}