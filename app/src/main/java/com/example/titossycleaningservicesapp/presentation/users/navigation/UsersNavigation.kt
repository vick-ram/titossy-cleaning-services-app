package com.example.titossycleaningservicesapp.presentation.users.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.example.titossycleaningservicesapp.domain.viewmodel.AuthViewModel
import com.example.titossycleaningservicesapp.presentation.users.cleaner.screens.CleanerMainScreen
import com.example.titossycleaningservicesapp.presentation.users.customer.navigation.CustomerMainScreen
import com.example.titossycleaningservicesapp.presentation.users.finance.screens.FinanceMainScreen
import com.example.titossycleaningservicesapp.presentation.users.manager.navigation.SideNavigation
import com.example.titossycleaningservicesapp.presentation.users.supervisor.screen.SupervisorMainScreen
import com.example.titossycleaningservicesapp.presentation.users.supplier.screens.SupplierMainScreen
import com.example.titossycleaningservicesapp.presentation.utils.RootNavRoutes
import com.example.titossycleaningservicesapp.presentation.utils.UserRoutes

fun NavGraphBuilder.usersNavigation(navController: NavHostController){

    navigation(
        route = RootNavRoutes.HOME.route,
        startDestination = UserRoutes.Customer.route
    ){
        composable(UserRoutes.Manager.route){
            SideNavigation()
        }
        composable(UserRoutes.Finance.route){
            FinanceMainScreen(navController)
        }
        composable(UserRoutes.Supervisor.route){
            SupervisorMainScreen(navController)
        }
        composable(UserRoutes.Cleaner.route){
            CleanerMainScreen(navController)
        }
        composable(UserRoutes.Supplier.route){
            SupplierMainScreen(navController)
        }
        composable(UserRoutes.Customer.route){
            CustomerMainScreen()
        }
    }
}
