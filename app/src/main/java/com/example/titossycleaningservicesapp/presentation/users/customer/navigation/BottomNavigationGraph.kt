package com.example.titossycleaningservicesapp.presentation.users.customer.navigation

import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState

@Composable
fun BottomNavigationGraph(navController: NavHostController){
    val bottomNavigationItems = listOf(
        Graph.Home,
        Graph.Bookings,
        Graph.Support,
        Graph.Profile
    )
    val selected by remember {
        mutableStateOf(false)
    }
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination?.route

    NavigationBar(
        containerColor = MaterialTheme.colorScheme.primaryContainer,
        contentColor = MaterialTheme.colorScheme.onPrimaryContainer
    ) {
        bottomNavigationItems.forEach { screen ->

            NavigationBarItem(
                selected = currentDestination == screen.route,
                onClick = {navController.navigate(screen.route){
                    popUpTo(navController.graph.startDestinationId){
                        inclusive = true
                    }
                } },
                icon = {
                    if (selected){
                        Icon(imageVector = screen.iconSelected, contentDescription = null)
                    }else{
                        Icon(imageVector = screen.iconUnselected, contentDescription = null)
                    }
                },
                label = {
                    Text(text = screen.title)
                }
            )
        }
    }

}