package com.example.titossycleaningservicesapp.presentation.users.inventory.navigation

import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Menu
import androidx.compose.material.icons.rounded.MoreVert
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.titossycleaningservicesapp.domain.viewmodel.EmployeeViewModel
import com.example.titossycleaningservicesapp.domain.viewmodel.MainViewModel
import com.example.titossycleaningservicesapp.presentation.users.inventory.utils.NavRoutes
import com.example.titossycleaningservicesapp.presentation.utils.DrawerUserInfo
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InventoryNavigationDrawer(
    signOutInventoryManager: () -> Unit,
    mainViewModel: MainViewModel,
    employeeViewModel: EmployeeViewModel
) {
    val navController = rememberNavController()
    val scope = rememberCoroutineScope()
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    var inventoryInfo by remember { mutableStateOf<Pair<String, String>?>(null) }
    val drawerItems = listOf(
        NavRoutes.Home,
        NavRoutes.Orders,
        NavRoutes.Profile,
    )
    val currentRoute by navController.currentBackStackEntryAsState()
    val showBar = drawerItems.any { it.route == currentRoute?.destination?.route }

    LaunchedEffect(inventoryInfo) {
        val info = mainViewModel.readUserFromToken()
        inventoryInfo = info
    }

    ModalNavigationDrawer(
        gesturesEnabled = showBar,
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet {
                DrawerUserInfo(
                    name = inventoryInfo?.first ?: "",
                    email = inventoryInfo?.second ?: ""
                )
                drawerItems.forEach { item ->
                    NavigationDrawerItem(
                        label = { Text(text = item.title) },
                        selected = false,
                        onClick = {
                            navController.navigate(item.route)
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
                if (showBar) {
                    CenterAlignedTopAppBar(
                        title = {
                            when (currentRoute?.destination?.route) {
                                NavRoutes.Home.route -> {
                                    Text(
                                        text = "Inventory",
                                        style = MaterialTheme.typography.titleMedium.copy(
                                            fontSize = 20.sp,
                                            fontWeight = FontWeight.W700
                                        )
                                    )
                                }

                                NavRoutes.Orders.route -> {
                                    Text(
                                        text = "Purchase Orders",
                                        style = MaterialTheme.typography.titleMedium
                                    )
                                }

                                NavRoutes.Profile.route -> {}
                            }
                        },
                        navigationIcon = {
                            IconButton(onClick = {
                                scope.launch {
                                    drawerState.apply {
                                        if (isClosed) open() else close()
                                    }
                                }
                            }) {
                                Icon(
                                    modifier = Modifier.size(32.dp),
                                    imageVector = Icons.Rounded.Menu,
                                    contentDescription = "manu"
                                )
                            }
                        },
                        actions = {
                            IconButton(onClick = signOutInventoryManager) {
                                Icon(
                                    modifier = Modifier.size(32.dp),
                                    imageVector = Icons.Rounded.MoreVert,
                                    contentDescription = "more"
                                )
                            }
                        }
                    )
                }
            },
        ) { paddingValues ->
            InventoryNavigationGraph(
                navController = navController,
                paddingValues = paddingValues,
                mainViewModel = mainViewModel,
                employeeViewModel = employeeViewModel
            )
        }
    }
}