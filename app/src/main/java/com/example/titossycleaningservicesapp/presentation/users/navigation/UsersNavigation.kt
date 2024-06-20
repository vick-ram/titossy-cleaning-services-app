package com.example.titossycleaningservicesapp.presentation.users.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
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
    signOutCustomer: () -> Unit,
    signOutCleaner: () -> Unit,
    signOutManager: () -> Unit,
    signOutSupplier: () -> Unit,
    signOutSupervisor: () -> Unit,
    signOutFinance: () -> Unit,
    signOutInventory: () -> Unit,
    startDestination: String
) {

    navigation(
        route = RootNavRoutes.HOME.route,
        startDestination = startDestination
    ) {
        composable(UserRoutes.Manager.route) {
            ManagerNavigationDrawer(signOutManager)
        }
        composable(UserRoutes.Inventory.route) {
            InventoryNavigationDrawer(signOutInventory)
        }
        composable(UserRoutes.Finance.route) {
            FinanceNavigationDrawer(signOutFinance)
        }
        composable(UserRoutes.Supervisor.route) {
            SupervisorNavigationDrawer(signOutSupervisor)
        }
        composable(UserRoutes.Cleaner.route) {
            CleanerNavigationDrawer(signOutCleaner)
        }
        composable(UserRoutes.Supplier.route) {
            SupplierNavigationDrawer(signOutSupplier)
        }
        composable(UserRoutes.Customer.route) {
            CustomerBottomNavigation(signOutCustomer)
        }
    }
}
