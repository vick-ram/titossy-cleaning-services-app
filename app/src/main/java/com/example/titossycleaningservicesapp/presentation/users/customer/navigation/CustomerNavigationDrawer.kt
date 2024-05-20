package com.example.titossycleaningservicesapp.presentation.users.customer.navigation

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarDefaults
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberBottomSheetScaffoldState
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.titossycleaningservicesapp.domain.viewmodel.CustomerAuthViewModel
import com.example.titossycleaningservicesapp.presentation.users.customer.utils.CustomerBottomRoutes
import com.example.titossycleaningservicesapp.presentation.users.customer.utils.NavRoutes
import com.example.titossycleaningservicesapp.presentation.utils.DrawerUserInfo
import kotlinx.coroutines.launch

@Preview(showBackground = true, showSystemUi = true)
@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun CustomerNavigationDrawer(signOutUser: (() -> Unit)? = null) {
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    val navController = rememberNavController()
    var expanded by remember { mutableStateOf(false) }
    val viewModel: CustomerAuthViewModel = hiltViewModel()
    val customerState by viewModel.customerUiState.collectAsState()
    val user = customerState.customer?.fullName ?: "User"

    val context = LocalContext.current
    val drawerItems = listOf(
        NavRoutes.Home,
        NavRoutes.Bookings,
        NavRoutes.FAQs,
        NavRoutes.Contact,
        NavRoutes.About,
        NavRoutes.Profile,
    )

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet {
                DrawerUserInfo(name = "", email = "")
                drawerItems.forEach { item ->
                    NavigationDrawerItem(
                        label = { Text(text = item.title) },
                        selected = false,
                        onClick = {
                            navController.navigate(item.route) {
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
        },
        gesturesEnabled = true
    ) {
        Scaffold(
            topBar = {
                //val currentRoute =
                navController.currentBackStackEntryAsState().value?.destination?.route
                //if (currentRoute != NavRoutes.Profile.route && currentRoute?.startsWith("serviceDetails/") == false) {
                TopAppBar(
                    title = { },
                    navigationIcon = {
                        IconButton(
                            onClick = {
                                scope.launch {
                                    drawerState.apply {
                                        if (isClosed) open() else close()
                                    }
                                }
                            },
                        ) {
                            Icon(
                                imageVector = Icons.Default.Menu,
                                contentDescription = "menu"
                            )
                        }
                    },
                    actions = {
                        IconButton(onClick = {
                            expanded = true
                            //signOutUser?.invoke()
                        }) {
                            Icon(
                                imageVector = Icons.Default.MoreVert,
                                contentDescription = "search",
                            )
                            DropdownMenu(
                                expanded = expanded,
                                onDismissRequest = { expanded = false }
                            ) {
                                DropdownMenuItem(
                                    text = { Text(text = "Help") },
                                    onClick = { /*TODO*/ }
                                )
                                DropdownMenuItem(
                                    text = { Text(text = "FAQs") },
                                    onClick = { /*TODO*/ }
                                )
                                DropdownMenuItem(
                                    text = { Text(text = "Settings") },
                                    onClick = { /*TODO*/ }
                                )
                                DropdownMenuItem(
                                    text = { Text(text = "Log out") },
                                    onClick = { signOutUser?.invoke() }
                                )
                            }
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = MaterialTheme.colorScheme.surface,
                        navigationIconContentColor = MaterialTheme.colorScheme.onSurface,
                        actionIconContentColor = MaterialTheme.colorScheme.onSurfaceVariant,
                        titleContentColor = MaterialTheme.colorScheme.onSurface
                    )
                )
                //}
            },
        ) { innerPadding ->
            NavigationGraph(
                navController, innerPadding
            )
        }
    }
}


@Composable
fun CustomerBottomNavigation() {
    val navController = rememberNavController()
    val bottomItems = CustomerBottomRoutes.entries
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination?.route
    val selected by remember { mutableStateOf(false) }

    val showBottomBar = bottomItems.any { it.route == currentDestination }


    Scaffold(
        bottomBar = {
            if (showBottomBar) {
                NavigationBar(
                    modifier = Modifier.navigationBarsPadding(),
                    containerColor = Color.Transparent,
                    contentColor = MaterialTheme.colorScheme.onSurface
                    //windowInsets = WindowInsets.navigationBars
                ) {
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
                                //if (selected) {
                                    Text(text = item.title)
                                //}
                            },
                            alwaysShowLabel = selected,
                            colors = NavigationBarItemDefaults.colors()
                        )
                    }
                }
            }
        }
    ) { innerPadding ->
        NavigationGraph(navController = navController, paddingValues = innerPadding)
    }
}























