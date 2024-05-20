package com.example.titossycleaningservicesapp.presentation.navigation.rootGraph

import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.titossycleaningservicesapp.domain.viewmodel.CustomerAuthViewModel
import com.example.titossycleaningservicesapp.domain.viewmodel.EmployeeViewModel
import com.example.titossycleaningservicesapp.domain.viewmodel.MainViewModel
import com.example.titossycleaningservicesapp.domain.viewmodel.SupplierAuthViewModel
import com.example.titossycleaningservicesapp.presentation.auth.authGraph.authNavGraph
import com.example.titossycleaningservicesapp.presentation.ui.OnBoardingScreen
import com.example.titossycleaningservicesapp.presentation.users.navigation.usersNavigation
import com.example.titossycleaningservicesapp.presentation.utils.Authentication
import com.example.titossycleaningservicesapp.presentation.utils.RootNavRoutes
import kotlinx.coroutines.launch

@Composable
fun RootNavGraph(
    navController: NavHostController,
    mainViewModel: MainViewModel,
    startDestination: String
) {
    val authViewModel: CustomerAuthViewModel = hiltViewModel()
    val employeeAuthViewModel: EmployeeViewModel = hiltViewModel()
    val supplierAuthViewModel: SupplierAuthViewModel = hiltViewModel()
    val scope = rememberCoroutineScope()
    NavHost(
        route = RootNavRoutes.ROOT.route,
        navController = navController,
        startDestination = startDestination
    ) {
        composable(RootNavRoutes.ONBOARDING.route) {
            OnBoardingScreen(
                onBoardingCompleted = {
                    mainViewModel.setOnBoardingCompleted()
                    navController.popBackStack()
                    navController.navigate(RootNavRoutes.AUTH.route)
                }
            )
        }
        authNavGraph(navController)
        usersNavigation(
            signOutCustomer = {
                authViewModel.signOut()
                navController.navigate(Authentication.LOGIN.route)
            },
            signOutCleaner = {
                scope.launch { }
                navController.navigate(Authentication.LOGIN.route)
            },
            signOutManager = {
                scope.launch {  }
                navController.navigate(Authentication.LOGIN.route)
            },
            signOutSupplier = {
                scope.launch {  }
                navController.navigate(Authentication.LOGIN.route)
            },
            signOutSupervisor = {
                scope.launch {  }
                navController.navigate(Authentication.LOGIN.route)
            },
            signOutFinance = {
                scope.launch { }
                navController.navigate(Authentication.LOGIN.route)
            },
            signOutInventory = {
                scope.launch {  }
                navController.navigate(Authentication.LOGIN.route)
            },
        )
    }
}
