package com.example.titossycleaningservicesapp.presentation.navigation.rootGraph

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.titossycleaningservicesapp.core.CustomProgressIndicator
import com.example.titossycleaningservicesapp.data.local.datastore.DataStoreKeys
import com.example.titossycleaningservicesapp.domain.viewmodel.CustomerViewModel
import com.example.titossycleaningservicesapp.domain.viewmodel.EmployeeViewModel
import com.example.titossycleaningservicesapp.domain.viewmodel.MainViewModel
import com.example.titossycleaningservicesapp.domain.viewmodel.PaymentViewModel
import com.example.titossycleaningservicesapp.domain.viewmodel.SupplierViewModel
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
    dataStoreKeys: DataStoreKeys,
    paddingValues: PaddingValues
) {

    val employeeViewModel: EmployeeViewModel = hiltViewModel()
    val supplierViewModel: SupplierViewModel = hiltViewModel()
    val customerViewModel: CustomerViewModel = hiltViewModel()
    val paymentViewModel: PaymentViewModel = hiltViewModel()

    var userType by rememberSaveable { mutableStateOf<String?>(null) }
    var isLoading by rememberSaveable { mutableStateOf(true) }

    LaunchedEffect(key1 = Unit) {
        userType = dataStoreKeys.getUserTypeFromDataStore()
        isLoading = false

    }
    val usersStartDestination = when (userType) {
        "MANAGER" -> UserRoutes.Manager.route
        "INVENTORY" -> UserRoutes.Inventory.route
        "FINANCE" -> UserRoutes.Finance.route
        "SUPERVISOR" -> UserRoutes.Supervisor.route
        "CLEANER" -> UserRoutes.Cleaner.route
        "SUPPLIER" -> UserRoutes.Supplier.route
        else -> UserRoutes.Customer.route
    }

    if (isLoading) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CustomProgressIndicator(isLoading = isLoading)
        }
    } else {
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
            authNavGraph(
                navController = navController,
                customerViewModel = customerViewModel,
                paddingValues = paddingValues
            )
            usersNavigation(
                signOut = {
                    mainViewModel.signOut()
                    navController.navigate(Authentication.LOGIN.route) {
                        popUpTo(RootNavRoutes.HOME.route) {
                            inclusive = true
                        }
                    }
                },
                startDestination = usersStartDestination,
                mainViewModel = mainViewModel,
                employeeViewModel = employeeViewModel,
                supplierAuthViewModel = supplierViewModel,
                paymentViewModel = paymentViewModel,
            )
        }
    }
}
