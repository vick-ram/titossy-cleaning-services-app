package com.example.titossycleaningservicesapp.presentation.users.supervisor.navigation

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.MoreVert
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
import com.example.titossycleaningservicesapp.domain.viewmodel.MainViewModel
import com.example.titossycleaningservicesapp.presentation.users.supervisor.util.NavRoutes
import com.example.titossycleaningservicesapp.presentation.utils.DrawerUserInfo
import com.example.titossycleaningservicesapp.presentation.utils.NavigationIcon
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun SupervisorNavigationDrawer(
    signOutSupervisor: () -> Unit,
    mainViewModel: MainViewModel
) {
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    val navController = rememberNavController()
    val drawerItems = listOf(
        NavRoutes.Home,
        NavRoutes.Contact,
        NavRoutes.Profile,
    )
    val currentRoute = navController.currentBackStackEntryAsState().value?.destination?.route
    val showAppBar = drawerItems.any { it.route == currentRoute }
    var supervisorInfo by rememberSaveable { mutableStateOf<Pair<String, String>?>(null) }
    var expanded by rememberSaveable { mutableStateOf(false) }


    LaunchedEffect(key1 = supervisorInfo) {
        supervisorInfo = mainViewModel.readUserFromToken()
    }

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet {
                DrawerUserInfo(
                    name = supervisorInfo?.first ?: "",
                    email = supervisorInfo?.second ?: ""
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
                if (showAppBar) {
                    CenterAlignedTopAppBar(
                        title = {
                            when (currentRoute) {
                                NavRoutes.Home.route -> {
                                    Text(
                                        text = "Bookings",
                                        style = MaterialTheme.typography.headlineSmall
                                    )
                                }

                                NavRoutes.Contact.route -> {
                                    Text(
                                        text = "Contact",
                                        style = MaterialTheme.typography.headlineSmall
                                    )
                                }

                                NavRoutes.Profile.route -> {
                                    Text(
                                        text = "Profile",
                                        style = MaterialTheme.typography.headlineSmall
                                    )
                                }
                            }
                        },
                        navigationIcon = {
                            NavigationIcon(
                                icon = Icons.Rounded.Menu,
                                onClick = {
                                    scope.launch {
                                        drawerState.apply {
                                            if (isClosed) open() else close()
                                        }
                                    }
                                }
                            )
                        },
                        actions = {
                            Box(modifier = Modifier) {
                                NavigationIcon(
                                    icon = Icons.Rounded.MoreVert,
                                    onClick = { expanded = !expanded }
                                )
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
                                        onClick = signOutSupervisor
                                    )
                                }
                            }
                        }
                    )
                }
            }
        ) { innerPadding ->
            NavigationGraph(
                navController = navController,
                paddingValues = innerPadding
            )
        }
    }
}