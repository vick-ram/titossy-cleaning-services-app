package com.example.titossycleaningservicesapp.presentation.users.manager.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.rounded.MoreVert
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
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
import com.example.titossycleaningservicesapp.domain.viewmodel.SupplierViewModel
import com.example.titossycleaningservicesapp.presentation.users.manager.utils.RouteData
import com.example.titossycleaningservicesapp.presentation.utils.DrawerUserInfo
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ManagerNavigationDrawer(
    signOutManager: () -> Unit,
    mainViewModel: MainViewModel,
    employeeViewModel: EmployeeViewModel,
    supplierViewModel: SupplierViewModel,
) {
    val navController = rememberNavController()
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    val drawerItems = listOf(
        RouteData.Home,
        RouteData.Supplier,
        RouteData.Orders,
        RouteData.Profile
    )
    val currentDestination by navController.currentBackStackEntryAsState()
    val showSheet = drawerItems.any { it.route == currentDestination?.destination?.route }
    var managerInfo by rememberSaveable { mutableStateOf<Pair<String, String>?>(null) }
    var expanded by rememberSaveable { mutableStateOf(false) }

    LaunchedEffect(key1 = managerInfo) {
        managerInfo = mainViewModel.readUserFromToken()
    }
    ModalNavigationDrawer(
        gesturesEnabled = showSheet,
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet {
                DrawerUserInfo(
                    name = managerInfo?.first ?: "",
                    email = managerInfo?.second ?: ""
                )
                LazyColumn(
                    contentPadding = PaddingValues(8.dp)
                ) {
                    items(drawerItems) { item ->
                        NavigationDrawerItem(
                            label = {
                                Text(text = item.title)
                            },
                            selected = false,
                            onClick = {
                                navController.navigate(item.route)
                                scope.launch {
                                    drawerState.close()
                                }
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
        }
    ) {
        Scaffold(
            topBar = {
                if (showSheet) {
                    TopAppBar(
                        title = {
                            if (currentDestination?.destination?.route == RouteData.Home.route) {
                                Text(text = "DashBoard")
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
                            Box(modifier = Modifier) {
                                IconButton(onClick = { expanded = !expanded }) {
                                    Icon(
                                        modifier = Modifier.size(32.dp),
                                        imageVector = Icons.Rounded.MoreVert,
                                        contentDescription = "more"
                                    )
                                }
                                DropdownMenu(
                                    expanded = expanded,
                                    onDismissRequest = { expanded = false }
                                ) {
                                    DropdownMenuItem(
                                        text = { Text(text = "notifications") },
                                        onClick = { /*TODO*/ }
                                    )
                                    HorizontalDivider(
                                        modifier = Modifier.fillMaxWidth(),
                                        thickness = 1.dp,
                                        color = androidx.compose.ui.graphics.Color.Gray
                                    )
                                    DropdownMenuItem(
                                        text = { Text(text = "logout") },
                                        onClick = signOutManager
                                    )
                                }
                            }
                        }
                    )
                }
            }
        ) { paddingValues ->
            SideNavigationGraph(
                navController = navController,
                paddingValues = paddingValues,
                mainViewModel = mainViewModel,
                employeeViewModel = employeeViewModel,
                supplierViewModel = supplierViewModel
            )
        }
    }
}
