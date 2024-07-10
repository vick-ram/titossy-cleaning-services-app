package com.example.titossycleaningservicesapp.presentation.users.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.example.titossycleaningservicesapp.domain.viewmodel.EmployeeViewModel
import com.example.titossycleaningservicesapp.domain.viewmodel.MainViewModel
import com.example.titossycleaningservicesapp.domain.viewmodel.PaymentViewModel
import com.example.titossycleaningservicesapp.domain.viewmodel.SupplierViewModel
import com.example.titossycleaningservicesapp.presentation.users.cleaner.navigation.CleanerNavigationDrawer
import com.example.titossycleaningservicesapp.presentation.users.customer.navigation.CustomerBottomNavigation
import com.example.titossycleaningservicesapp.presentation.users.finance.navigation.FinanceNavigationDrawer
import com.example.titossycleaningservicesapp.presentation.users.inventory.navigation.InventoryNavigationDrawer
import com.example.titossycleaningservicesapp.presentation.users.manager.navigation.ManagerNavigationDrawer
import com.example.titossycleaningservicesapp.presentation.users.supervisor.navigation.SupervisorNavigationDrawer
import com.example.titossycleaningservicesapp.presentation.users.supplier.navigation.SupplierNavigationDrawer
import com.example.titossycleaningservicesapp.presentation.utils.RootNavRoutes
import com.example.titossycleaningservicesapp.presentation.utils.UserRoutes

fun NavGraphBuilder.usersNavigation(
    signOut: () -> Unit,
    startDestination: String,
    mainViewModel: MainViewModel,
    employeeViewModel: EmployeeViewModel,
    supplierAuthViewModel: SupplierViewModel,
    paymentViewModel: PaymentViewModel
) {
    navigation(
        route = RootNavRoutes.HOME.route,
        startDestination = startDestination
    ) {
        composable(UserRoutes.Manager.route) {
            ManagerNavigationDrawer(
                signOutManager = signOut,
                mainViewModel = mainViewModel,
                employeeViewModel = employeeViewModel,
                supplierViewModel = supplierAuthViewModel
            )
        }
        composable(UserRoutes.Inventory.route) {
            InventoryNavigationDrawer(
                signOutInventoryManager = signOut,
                mainViewModel = mainViewModel,
                employeeViewModel = employeeViewModel,
                /*dialog = dialog,
                openDialog = openDialog*/
            )
        }
        composable(UserRoutes.Finance.route) {
            FinanceNavigationDrawer(
                signOutFinanceManager = signOut,
                mainViewModel = mainViewModel,
                employeeViewModel = employeeViewModel,
                paymentViewModel = paymentViewModel
            )
        }
        composable(UserRoutes.Supervisor.route) {
            SupervisorNavigationDrawer(
                signOutSupervisor = signOut,
                mainViewModel = mainViewModel,
                employeeViewModel = employeeViewModel
            )
        }
        composable(UserRoutes.Cleaner.route) {
            CleanerNavigationDrawer(
                signOutUser = signOut,
                mainViewModel = mainViewModel,
                employeeViewModel = employeeViewModel
            )
        }
        composable(UserRoutes.Supplier.route) {
            SupplierNavigationDrawer(
                signOutSupplier = signOut,
                mainViewModel = mainViewModel,
                supplierAuthViewModel = supplierAuthViewModel
            )
        }
        composable(UserRoutes.Customer.route) {
            CustomerBottomNavigation(
                onSignOut = signOut
            )
        }
    }
}
