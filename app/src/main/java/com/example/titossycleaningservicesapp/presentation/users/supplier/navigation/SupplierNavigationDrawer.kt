package com.example.titossycleaningservicesapp.presentation.users.supplier.navigation

import android.annotation.SuppressLint
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
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
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.navigation.compose.rememberNavController
import com.example.titossycleaningservicesapp.presentation.users.supplier.util.NavRoutes
import com.example.titossycleaningservicesapp.presentation.utils.DrawerUserInfo
import kotlinx.coroutines.launch

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SupplierNavigationDrawer(signOutSupplier: () -> Unit) {
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    val navController = rememberNavController()
    /*val viewModel: CustomerAuthViewModel = hiltViewModel()
    val displayName = viewModel.currentUser?.displayName ?: "supplier name"
    val email = viewModel.currentUser?.email ?: "supplier email"*/
    val drawerItems = listOf(
        NavRoutes.Home,
        NavRoutes.Contact,
        NavRoutes.Profile,
        NavRoutes.LogOut
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
                            if (item.route == "logout") {
                                navController.popBackStack()
                                signOutSupplier()
                            } else {
                                navController.navigate(item.route)
                            }
                            scope.launch { drawerState.close() }
                        },
                        icon = {
                            Icon(imageVector = item.icon, contentDescription = item.title)
                        }
                    )
                }
            }
        }
    ) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text(text = "Supplier") },
                    navigationIcon = {
                        IconButton(
                            onClick = {
                                scope.launch {
                                    drawerState.apply { if (isClosed) open() else close() }
                                }
                            }
                        ) {
                            Icon(imageVector = Icons.Default.Menu, contentDescription = "menu")
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = MaterialTheme.colorScheme.primary,
                        titleContentColor = MaterialTheme.colorScheme.onPrimary
                    )
                )
            }
        ) {
            NavigationGraph(navController)
        }
    }
}