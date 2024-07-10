package com.example.titossycleaningservicesapp.presentation.users.inventory.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.titossycleaningservicesapp.domain.viewmodel.EmployeeViewModel
import com.example.titossycleaningservicesapp.domain.viewmodel.MainViewModel
import com.example.titossycleaningservicesapp.presentation.users.inventory.screens.HomeScreen
import com.example.titossycleaningservicesapp.presentation.users.inventory.screens.OrderScreen
import com.example.titossycleaningservicesapp.presentation.users.inventory.screens.ProfileScreen
import com.example.titossycleaningservicesapp.presentation.users.inventory.utils.InventoryPurchaseOrderDetailsScreen
import com.example.titossycleaningservicesapp.presentation.users.inventory.utils.NavRoutes
import com.example.titossycleaningservicesapp.presentation.users.inventory.utils.PurchaseOrderScreen

@Composable
fun InventoryNavigationGraph(
    navController: NavHostController,
    paddingValues: PaddingValues,
    mainViewModel: MainViewModel,
    employeeViewModel: EmployeeViewModel
) {
    NavHost(
        navController = navController,
        startDestination = NavRoutes.Home.route
    ) {
        composable(NavRoutes.Home.route) {
            HomeScreen(
                navController = navController,
                paddingValues = paddingValues,
            )
        }
        composable(NavRoutes.Orders.route) {
            OrderScreen(
                navController = navController,
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

        composable(
            route = "purchaseOrder"
        ) {
            PurchaseOrderScreen(
                paddingValues = paddingValues,
                navController = navController,
            )
        }
        composable("InventoryPurchaseOrderDetails/{purchaseOrderId}") { backStackEntry ->
            val purchaseOrderId = backStackEntry.arguments?.getString("purchaseOrderId")
            InventoryPurchaseOrderDetailsScreen(
                paddingValues = paddingValues,
                purchaseOrderId = purchaseOrderId ?: "",
                onBack = { navController.navigateUp() },
            )
        }
    }
}