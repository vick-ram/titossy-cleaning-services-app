package com.example.titossycleaningservicesapp.presentation.users.supplier.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Menu
import androidx.compose.material.icons.rounded.MoreVert
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.titossycleaningservicesapp.domain.viewmodel.MainViewModel
import com.example.titossycleaningservicesapp.domain.viewmodel.SupplierViewModel
import com.example.titossycleaningservicesapp.presentation.users.supplier.util.NavRoutes
import com.example.titossycleaningservicesapp.presentation.utils.DrawerUserInfo
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SupplierNavigationDrawer(
    signOutSupplier: () -> Unit,
    mainViewModel: MainViewModel,
    supplierAuthViewModel: SupplierViewModel
) {
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    val navController = rememberNavController()
    val drawerItems = listOf(
        NavRoutes.Home,
        NavRoutes.Contact,
        NavRoutes.Profile
    )
    val currentDestination = navController.currentBackStackEntryAsState().value?.destination?.route
    val supplier = rememberSaveable { mutableStateOf<Pair<String, String>?>(null) }
    val showTopAppBar = drawerItems.any { it.route == currentDestination }
    var expanded by rememberSaveable { mutableStateOf(false) }

    LaunchedEffect(supplier) {
        supplier.value = mainViewModel.readUserFromToken()
    }

    ModalNavigationDrawer(
        gesturesEnabled = showTopAppBar,
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet {
                DrawerUserInfo(
                    name = "SUPPLIER",
                    email = supplier.value?.second ?: ""
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
                if (showTopAppBar) {
                    CenterAlignedTopAppBar(
                        title = {
                            when (currentDestination) {
                                NavRoutes.Home.route -> Text(text = "Purchase Orders")
                                NavRoutes.Contact.route -> Text(text = "Contact")
                                NavRoutes.Profile.route -> Text(text = "My Profile")
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
                                    contentDescription = "menu"
                                )
                            }
                        },
                        actions = {
                            Box(modifier = Modifier) {
                                IconButton(onClick = {
                                    expanded = !expanded
                                }) {
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
                                        text = { Text(text = "settings") },
                                        onClick = {}
                                    )
                                    DropdownMenuItem(
                                        text = { Text(text = "logout") },
                                        onClick = signOutSupplier
                                    )
                                }
                            }
                        },
                        colors = TopAppBarDefaults.topAppBarColors(
                            containerColor = Color.Transparent,
                            titleContentColor = MaterialTheme.colorScheme.onSurface,
                            actionIconContentColor = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    )
                }
            }
        ) { paddingValues ->
            NavigationGraph(
                navController = navController,
                paddingValues = paddingValues,
                mainViewModel = mainViewModel,
                supplierViewModel = supplierAuthViewModel
            )
        }
    }
}
