package com.example.titossycleaningservicesapp.presentation.users.cleaner.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.rounded.MoreVert
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.titossycleaningservicesapp.domain.viewmodel.EmployeeViewModel
import com.example.titossycleaningservicesapp.domain.viewmodel.MainViewModel
import com.example.titossycleaningservicesapp.presentation.users.cleaner.util.CleanerNavRoutes
import com.example.titossycleaningservicesapp.presentation.utils.DrawerUserInfo
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CleanerNavigationDrawer(
    signOutUser: () -> Unit,
    mainViewModel: MainViewModel,
    employeeViewModel: EmployeeViewModel
) {
    val navController = rememberNavController()
    val scope = rememberCoroutineScope()
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val drawerItems = listOf(
        CleanerNavRoutes.Home,
        CleanerNavRoutes.Profile
    )
    var expanded by rememberSaveable { mutableStateOf(false) }
    var cleaner by rememberSaveable { mutableStateOf<Pair<String, String>?>(null) }
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination?.route
    val show = drawerItems.any { it.routes == currentDestination }

    LaunchedEffect(key1 = cleaner) {
        cleaner = mainViewModel.readUserFromToken()
    }

    ModalNavigationDrawer(
        gesturesEnabled = show,
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet {
                DrawerUserInfo(
                    name = cleaner?.first ?: "",
                    email = cleaner?.second ?: ""
                )
                drawerItems.forEach { item ->
                    NavigationDrawerItem(
                        label = { Text(text = item.title) },
                        selected = false,
                        onClick = {
                            navController.navigate(item.routes) {
                                popUpTo(navController.graph.startDestinationId) {
                                    inclusive = true
                                    saveState = true
                                }
                                launchSingleTop = true
                            }
                            scope.launch { drawerState.close() }
                        },
                        icon = {
                            Icon(
                                imageVector = item.icon,
                                contentDescription = item.title
                            )
                        }
                    )
                }
            }
        }
    ) {
        Scaffold(
            topBar = {
                if (show) {
                    CenterAlignedTopAppBar(
                        title = {
                            when(currentDestination) {
                                CleanerNavRoutes.Home.routes -> {
                                    Text(text = "Booking Schedules")
                                }
                                CleanerNavRoutes.Profile.routes -> {}
                            }
                        },
                        navigationIcon = {
                            IconButton(
                                onClick = {
                                    scope.launch {
                                        drawerState.apply {
                                            if (isClosed) open() else close()
                                        }
                                    }
                                }
                            ) {
                                Icon(
                                    modifier = Modifier.size(32.dp),
                                    imageVector = Icons.Default.Menu,
                                    contentDescription = "menu"
                                )
                            }
                        },
                        actions = {
                            if (currentDestination == CleanerNavRoutes.Home.routes) {
                                Box(modifier = Modifier){
                                    IconButton(
                                        onClick = {expanded = !expanded  }
                                    ) {
                                        Icon(
                                            modifier = Modifier.size(32.dp),
                                            imageVector = Icons.Rounded.MoreVert,
                                            contentDescription = null
                                        )
                                    }

                                    DropdownMenu(
                                        expanded = expanded,
                                        onDismissRequest = { expanded = false }
                                    ) {
                                        DropdownMenuItem(
                                            text = { Text(text = "Settings") },
                                            onClick = { /*TODO*/ }
                                        )
                                        DropdownMenuItem(
                                            text = { Text(text = "logout") },
                                            onClick = signOutUser
                                        )
                                    }
                                }
                            }
                        }
                    )
                }
            }
        ) { paddingValues ->
            NavigationGraph(
                navController = navController,
                paddingValues = paddingValues,
                mainViewModel = mainViewModel,
                employeeViewModel = employeeViewModel
            )
        }
    }
}
