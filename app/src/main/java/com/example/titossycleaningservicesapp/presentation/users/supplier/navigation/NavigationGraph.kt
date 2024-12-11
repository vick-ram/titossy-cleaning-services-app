package com.example.titossycleaningservicesapp.presentation.users.supplier.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.titossycleaningservicesapp.domain.viewmodel.MainViewModel
import com.example.titossycleaningservicesapp.domain.viewmodel.SupplierViewModel
import com.example.titossycleaningservicesapp.presentation.users.supplier.screens.ContactScreen
import com.example.titossycleaningservicesapp.presentation.users.supplier.screens.HomeScreen
import com.example.titossycleaningservicesapp.presentation.users.supplier.screens.ProfileScreen
import com.example.titossycleaningservicesapp.presentation.users.supplier.screens.PurchaseOrderDetailsScreen
import com.example.titossycleaningservicesapp.presentation.users.supplier.screens.SupplierReceiptScreen
import com.example.titossycleaningservicesapp.presentation.users.supplier.screens.SupplierReceiptScreenWithPrint
import com.example.titossycleaningservicesapp.presentation.users.supplier.util.NavRoutes

@Composable
fun NavigationGraph(
    navController: NavHostController,
    paddingValues: PaddingValues,
    mainViewModel: MainViewModel,
    supplierViewModel: SupplierViewModel
) {
    NavHost(
        navController = navController,
        startDestination = NavRoutes.Home.route
    ) {
        composable(NavRoutes.Home.route) {
            HomeScreen(navController, paddingValues)
        }
        composable(NavRoutes.Contact.route) {
            ContactScreen(navController, paddingValues)
        }
        composable(NavRoutes.Profile.route) {
            ProfileScreen(
                paddingValues = paddingValues,
                mainViewModel = mainViewModel,
                supplierViewModel = supplierViewModel
            )
        }
        composable("purchaseOrderDetails/{purchaseOrderId}") { backStackEntry ->
            val purchaseOrderId = backStackEntry.arguments?.getString("purchaseOrderId").toString()
            PurchaseOrderDetailsScreen(
                purchaseOrderId = purchaseOrderId,
                paddingValues = paddingValues,
                navController = navController
            )
        }
        composable("receiptScreen/{purchaseOrderId}") { backStackEntry ->
            val purchaseOrderId = backStackEntry.arguments?.getString("purchaseOrderId").toString()
            SupplierReceiptScreenWithPrint(
                purchaseOrderId = purchaseOrderId,
                paddingValues = paddingValues
            )
        }
    }
}