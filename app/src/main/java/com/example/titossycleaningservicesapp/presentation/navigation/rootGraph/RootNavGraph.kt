package com.example.titossycleaningservicesapp.presentation.navigation.rootGraph

import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.titossycleaningservicesapp.data.local.datastore.DataStoreKeys
import com.example.titossycleaningservicesapp.domain.viewmodel.MainViewModel
import com.example.titossycleaningservicesapp.presentation.auth.authGraph.authNavGraph
import com.example.titossycleaningservicesapp.presentation.ui.OnBoardingScreen
import com.example.titossycleaningservicesapp.presentation.users.navigation.usersNavigation
import com.example.titossycleaningservicesapp.presentation.utils.Authentication
import com.example.titossycleaningservicesapp.presentation.utils.RootNavRoutes
import com.example.titossycleaningservicesapp.presentation.utils.UserRoutes

@Composable
fun RootNavGraph(
    navController: NavHostController,
    mainViewModel: MainViewModel,
    startDestination: String,
    dataStoreKeys: DataStoreKeys
) {

    var userType by rememberSaveable { mutableStateOf<String?>(null) }

    LaunchedEffect(key1 = Unit) {
        userType = dataStoreKeys.getUserTypeFromDataStore()
    }
    val usersStartDestination = when(userType) {
        "MANAGER" -> UserRoutes.Manager.route
        "INVENTORY" -> UserRoutes.Inventory.route
        "FINANCE" -> UserRoutes.Finance.route
        "SUPERVISOR" -> UserRoutes.Supervisor.route
        "CLEANER" -> UserRoutes.Cleaner.route
        "SUPPLIER" -> UserRoutes.Supplier.route
        else -> UserRoutes.Customer.route
    }

    NavHost(
        route = RootNavRoutes.ROOT.route,
        navController = navController,
        startDestination = startDestination,
        enterTransition = { fadeIn(animationSpec = tween(2000)) },
        exitTransition = { fadeOut(animationSpec = tween(200)) }
    ) {
        composable(RootNavRoutes.ONBOARDING.route) {
            OnBoardingScreen(
                onBoardingCompleted = {
                    mainViewModel.setOnBoardingCompleted()
                    navController.popBackStack()
                    navController.navigate(RootNavRoutes.AUTH.route)
                }
            )
        }
        authNavGraph(navController)
        usersNavigation(
            signOut = {
                mainViewModel.signOut()
                navController.popBackStack()
                navController.navigate(Authentication.LOGIN.route)
            },
            startDestination = usersStartDestination,
            mainViewModel = mainViewModel
        )
    }
}
