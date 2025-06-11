package com.example.titossycleaningservicesapp.presentation.users.manager.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.titossycleaningservicesapp.domain.viewmodel.EmployeeViewModel
import com.example.titossycleaningservicesapp.domain.viewmodel.MainViewModel
import com.example.titossycleaningservicesapp.domain.viewmodel.SupplierViewModel
import com.example.titossycleaningservicesapp.presentation.users.manager.screens.HomeScreen
import com.example.titossycleaningservicesapp.presentation.users.manager.screens.ProfileScreen
import com.example.titossycleaningservicesapp.presentation.users.manager.screens.ManagerOrdersScreen
import com.example.titossycleaningservicesapp.presentation.users.manager.utils.RouteData
import com.example.titossycleaningservicesapp.presentation.users.manager.screens.SuppliersScreen

@Composable
fun SideNavigationGraph(
    navController: NavHostController,
    paddingValues: PaddingValues,
    mainViewModel: MainViewModel,
    employeeViewModel: EmployeeViewModel,
    supplierViewModel: SupplierViewModel
){
    NavHost(navController = navController, startDestination = RouteData.Home.route){
        composable(RouteData.Home.route){
            HomeScreen(
                navController = navController,
                paddingValues = paddingValues,
                supplierViewModel = supplierViewModel
            )
        }
        composable(RouteData.Profile.route) {
            ProfileScreen(
                paddingValues = paddingValues,
                mainViewModel = mainViewModel,
                employeeViewModel = employeeViewModel
            )
        }
        composable(RouteData.Supplier.route) {
            SuppliersScreen(
                navController = navController,
                supplierViewModel = supplierViewModel,
                paddingValues = paddingValues
            )
        }
        composable(RouteData.Orders.route) {
            ManagerOrdersScreen(
                paddingValues = paddingValues,
                navController = navController
            )
        }
    }
}