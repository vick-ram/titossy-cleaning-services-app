package com.example.titossycleaningservicesapp.presentation.users.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.example.titossycleaningservicesapp.presentation.users.cleaner.navigation.CleanerNavigationDrawer
import com.example.titossycleaningservicesapp.presentation.users.customer.navigation.CustomerNavigationDrawer
import com.example.titossycleaningservicesapp.presentation.users.finance.navigation.FinanceNavigationDrawer
import com.example.titossycleaningservicesapp.presentation.users.manager.navigation.SideNavigation
import com.example.titossycleaningservicesapp.presentation.users.supervisor.navigation.SupervisorNavigationDrawer
import com.example.titossycleaningservicesapp.presentation.users.supplier.navigation.SupplierNavigationDrawer
import com.example.titossycleaningservicesapp.presentation.utils.RootNavRoutes
import com.example.titossycleaningservicesapp.presentation.utils.UserRoutes

fun NavGraphBuilder.usersNavigation() {

    navigation(
        route = RootNavRoutes.HOME.route,
        startDestination = UserRoutes.Customer.route
    ) {
        composable(UserRoutes.Manager.route) {
            SideNavigation()
        }
        composable(UserRoutes.Finance.route) {
            FinanceNavigationDrawer()
        }
        composable(UserRoutes.Supervisor.route) {
            SupervisorNavigationDrawer()
        }
        composable(UserRoutes.Cleaner.route) {
            CleanerNavigationDrawer()
        }
        composable(UserRoutes.Supplier.route) {
            SupplierNavigationDrawer()
        }
        composable(UserRoutes.Customer.route) {
            CustomerNavigationDrawer()
        }
    }
}
