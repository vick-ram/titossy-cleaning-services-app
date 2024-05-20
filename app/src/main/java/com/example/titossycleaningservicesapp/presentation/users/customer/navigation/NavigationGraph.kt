package com.example.titossycleaningservicesapp.presentation.users.customer.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import com.example.titossycleaningservicesapp.presentation.users.customer.screens.BookingsScreen
import com.example.titossycleaningservicesapp.presentation.users.customer.screens.HomeScreen
import com.example.titossycleaningservicesapp.presentation.users.customer.screens.ServiceDetailsScreen
import com.example.titossycleaningservicesapp.presentation.users.customer.screens.SettingsScreen
import com.example.titossycleaningservicesapp.presentation.users.customer.utils.BookingDetailsScreen
import com.example.titossycleaningservicesapp.presentation.users.customer.utils.BookingSuccessScreen
import com.example.titossycleaningservicesapp.presentation.users.customer.utils.CheckOutScreen
import com.example.titossycleaningservicesapp.presentation.users.customer.utils.CustomerBottomRoutes
import com.example.titossycleaningservicesapp.presentation.users.customer.utils.DetailsRoute
import com.example.titossycleaningservicesapp.presentation.users.customer.utils.DetailsRoutes
import com.example.titossycleaningservicesapp.presentation.users.customer.utils.NavRoutes
import com.example.titossycleaningservicesapp.presentation.users.customer.utils.PaymentScreen

@Composable
fun NavigationGraph(navController: NavHostController, paddingValues: PaddingValues) {
    NavHost(navController = navController, startDestination = CustomerBottomRoutes.Home.route) {
        composable(NavRoutes.Home.route) {
            HomeScreen(navController, paddingValues)
        }
        composable(CustomerBottomRoutes.Bookings.route) {
            BookingsScreen(navController, paddingValues)
        }

        composable(CustomerBottomRoutes.Settings.route) {
            SettingsScreen(navController, paddingValues)
        }

        detailsGraph(navController)
    }
}

fun NavGraphBuilder.detailsGraph(
    navController: NavHostController
) {
    navigation(
        route = DetailsRoute.DETAILS.name,
        startDestination = DetailsRoutes.Details.route
    ){
        composable(
            DetailsRoutes.Details.route +"/{serviceId}"
        ) {navBackStackEntry ->
            val serviceId = navBackStackEntry.arguments?.getString("serviceId")
            ServiceDetailsScreen(
                serviceId = serviceId ?: "",
                onServiceDetailsBack = { navController.popBackStack() },
                navController = navController
            )
        }

        composable(DetailsRoutes.BookingDetails.route){
            BookingDetailsScreen(navController = navController)
        }
        composable(DetailsRoutes.CheckOut.route){
            CheckOutScreen(navController = navController)
        }
        composable(DetailsRoutes.Payment.route){
            PaymentScreen(navController = navController)
        }
        composable(DetailsRoutes.BookingSuccess.route){
            BookingSuccessScreen(navController = navController)
        }
    }
}