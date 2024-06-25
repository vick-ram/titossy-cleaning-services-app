package com.example.titossycleaningservicesapp.presentation.users.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.example.titossycleaningservicesapp.domain.viewmodel.MainViewModel
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
    onHomeClick: () -> Unit
) {
    navigation(
        route = RootNavRoutes.HOME.route,
        startDestination = startDestination
    ) {
        composable(UserRoutes.Manager.route) {
            ManagerNavigationDrawer(signOut)
        }
        composable(UserRoutes.Inventory.route) {
            InventoryNavigationDrawer(signOut)
        }
        composable(UserRoutes.Finance.route) {
            FinanceNavigationDrawer(signOut)
        }
        composable(UserRoutes.Supervisor.route) {
            SupervisorNavigationDrawer(signOut, mainViewModel)
        }
        composable(UserRoutes.Cleaner.route) {
            CleanerNavigationDrawer(signOut)
        }
        composable(UserRoutes.Supplier.route) {
            SupplierNavigationDrawer(signOut, mainViewModel)
        }
        composable(UserRoutes.Customer.route) {
            CustomerBottomNavigation(onSignOut = signOut, onHomeClick = onHomeClick)
        }
    }
}
