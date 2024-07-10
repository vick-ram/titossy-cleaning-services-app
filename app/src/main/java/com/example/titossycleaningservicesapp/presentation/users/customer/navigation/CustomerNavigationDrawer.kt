package com.example.titossycleaningservicesapp.presentation.users.customer.navigation

import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.titossycleaningservicesapp.presentation.users.customer.utils.CustomerBottomRoutes

@Composable
fun CustomerBottomNavigation(
    onSignOut: () -> Unit
) {
    val navController = rememberNavController()
    val bottomItems = CustomerBottomRoutes.entries
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination?.route
    val selected by remember { mutableStateOf(false) }

    val showBottomBar = bottomItems.any { it.route == currentDestination }

    Scaffold(
        bottomBar = {
            if (showBottomBar) {
                NavigationBar {
                    bottomItems.forEach { item ->
                        NavigationBarItem(
                            selected = currentDestination == item.route,
                            onClick = {
                                navController.navigate(item.route) {
                                    popUpTo(navController.graph.findStartDestination().id) {
                                        saveState = true
                                    }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            },
                            icon = {
                                if (selected) {
                                    Icon(
                                        imageVector = item.selectedIcon,
                                        contentDescription = item.title
                                    )
                                } else {
                                    Icon(
                                        imageVector = item.unselectedIcon,
                                        contentDescription = item.title
                                    )
                                }
                            },
                            label = {
                                Text(text = item.title)
                            },
                            alwaysShowLabel = selected,
                            colors = NavigationBarItemDefaults.colors()
                        )
                    }
                }
            }
        }
    ) { innerPadding ->
        NavigationGraph(
            onSignOut = onSignOut,
            navController = navController,
            paddingValues = innerPadding
        )
    }
}























