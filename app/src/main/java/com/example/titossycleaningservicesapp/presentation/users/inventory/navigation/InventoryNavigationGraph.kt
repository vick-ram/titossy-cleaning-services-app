package com.example.titossycleaningservicesapp.presentation.users.inventory.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.titossycleaningservicesapp.presentation.users.inventory.screens.ContactScreen
import com.example.titossycleaningservicesapp.presentation.users.inventory.screens.HomeScreen
import com.example.titossycleaningservicesapp.presentation.users.inventory.screens.ProfileScreen
import com.example.titossycleaningservicesapp.presentation.users.inventory.utils.NavRoutes
import com.example.titossycleaningservicesapp.presentation.users.inventory.utils.PurchaseOrderScreen

@Composable
fun InventoryNavigationGraph(navController: NavHostController, paddingValues: PaddingValues) {
    NavHost(
        navController = navController,
        startDestination = NavRoutes.Home.route
    ) {
        composable(NavRoutes.Home.route){
            HomeScreen(
                navController = navController, paddingValues = paddingValues
            )
        }
        composable(NavRoutes.Contact.route){
            ContactScreen(navController)
        }
        composable(NavRoutes.Profile.route){
            ProfileScreen(navController)
        }

        composable(
            route = "purchaseOrder/{productId}/{quantity}",
            arguments = listOf(
                navArgument("productId") { type = NavType.StringType },
                navArgument("quantity") { type = NavType.StringType }
            )
        ){backStackEntry ->
            val productId = backStackEntry.arguments?.getString("productId") ?: ""
            val quantity = backStackEntry.arguments?.getString("quantity") ?: ""
            PurchaseOrderScreen(
                paddingValues = paddingValues,
                navController = navController,
                productId = productId,
                quantity = quantity
            )
        }
    }
}