package com.example.titossycleaningservicesapp.presentation.users.customer.navigation

import android.util.Log
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.tween
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
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
fun NavigationGraph(
    onSignOut: () -> Unit,
    navController: NavHostController,
    paddingValues: PaddingValues
) {
    NavHost(
        modifier = Modifier.animateContentSize(),
        navController = navController,
        startDestination = CustomerBottomRoutes.Home.route,
        enterTransition = { slideInVertically() }
    ) {
        composable(NavRoutes.Home.route) {
            HomeScreen(navController, paddingValues, onSignOut)
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
    ) {
        composable(
            DetailsRoutes.Details.route + "/{serviceId}",
            enterTransition = { scaleIn(tween(700)) },
            exitTransition = { scaleOut(tween(700)) },
        ) { navBackStackEntry ->
            val serviceId = navBackStackEntry.arguments?.getString("serviceId")
            ServiceDetailsScreen(
                serviceId = serviceId ?: "",
                navController = navController
            )
        }

        composable(DetailsRoutes.BookingDetails.route) {
            BookingDetailsScreen(navController = navController)
        }
        composable(DetailsRoutes.CheckOut.route  + "/{bookingId}") { backStack ->
            val bookingId = backStack.arguments?.getString("bookingId")
            Log.d("BokingID", "Bookingid: $bookingId")
            bookingId?.let {
                CheckOutScreen(bookingId = it, navController = navController)
            }
        }
        composable(DetailsRoutes.Payment.route) {
            PaymentScreen(navController = navController)
        }
        composable(DetailsRoutes.BookingSuccess.route) {
            BookingSuccessScreen(navController = navController)
        }
    }
}