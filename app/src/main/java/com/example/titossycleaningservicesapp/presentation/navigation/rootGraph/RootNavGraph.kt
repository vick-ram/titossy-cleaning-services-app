package com.example.titossycleaningservicesapp.presentation.navigation.rootGraph

import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.titossycleaningservicesapp.data.local.datastore.DataStoreKeys
import com.example.titossycleaningservicesapp.domain.viewmodel.CustomerAuthViewModel
import com.example.titossycleaningservicesapp.domain.viewmodel.EmployeeViewModel
import com.example.titossycleaningservicesapp.domain.viewmodel.MainViewModel
import com.example.titossycleaningservicesapp.domain.viewmodel.SupplierAuthViewModel
import com.example.titossycleaningservicesapp.presentation.auth.authGraph.authNavGraph
import com.example.titossycleaningservicesapp.presentation.ui.OnBoardingScreen
import com.example.titossycleaningservicesapp.presentation.users.navigation.usersNavigation
import com.example.titossycleaningservicesapp.presentation.utils.Authentication
import com.example.titossycleaningservicesapp.presentation.utils.RootNavRoutes
import com.example.titossycleaningservicesapp.presentation.utils.UserRoutes

@Composable
fun RootNavGraph(
    navController: NavHostController,
    mainViewModel: MainViewModel,
    startDestination: String,
    dataStoreKeys: DataStoreKeys
) {

    val customerAuthViewModel: CustomerAuthViewModel = hiltViewModel()
    val employeeAuthViewModel: EmployeeViewModel = hiltViewModel()
    val supplierViewModel: SupplierAuthViewModel = hiltViewModel()
    var userType by rememberSaveable { mutableStateOf<String?>(null) }
    LaunchedEffect(key1 = dataStoreKeys) {
        userType = dataStoreKeys.getUserTypeFromDataStore()
    }

    val usersStartDestination = when(userType) {
        "MANAGER" -> UserRoutes.Manager.route
        "INVENTORY" -> UserRoutes.Inventory.route
        "FINANCE" -> UserRoutes.Finance.route
        "SUPERVISOR" -> UserRoutes.Supervisor.route
        "CLEANER" -> UserRoutes.Cleaner.route
        "SUPPLIER" -> UserRoutes.Supplier.route
        else -> UserRoutes.Customer.route
    }

    NavHost(
        route = RootNavRoutes.ROOT.route,
        navController = navController,
        startDestination = startDestination,
        enterTransition = { fadeIn(animationSpec = tween(2000)) },
        exitTransition = { fadeOut(animationSpec = tween(200)) }
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
        authNavGraph(navController, customerAuthViewModel)
        usersNavigation(
            signOutCustomer = {
                customerAuthViewModel.signOut()
                navController.popBackStack()
                employeeAuthViewModel.signOut()
                navController.navigate(Authentication.LOGIN.route)
            },
            signOutCleaner = {
                navController.navigate(Authentication.LOGIN.route)
            },
            signOutManager = {
                navController.navigate(Authentication.LOGIN.route)
            },
            signOutSupplier = {
                supplierViewModel.signOut()
                navController.navigate(Authentication.LOGIN.route)
            },
            signOutSupervisor = {
                navController.navigate(Authentication.LOGIN.route)
            },
            signOutFinance = {
                navController.navigate(Authentication.LOGIN.route)
            },
            signOutInventory = {
                navController.navigate(Authentication.LOGIN.route)
            },
            startDestination = usersStartDestination
        )
    }
}
