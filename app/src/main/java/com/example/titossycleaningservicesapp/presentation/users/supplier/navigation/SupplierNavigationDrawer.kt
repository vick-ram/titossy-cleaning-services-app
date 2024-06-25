package com.example.titossycleaningservicesapp.presentation.users.supplier.navigation

import android.annotation.SuppressLint
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material.icons.rounded.Menu
import androidx.compose.material.icons.rounded.Search
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.titossycleaningservicesapp.domain.viewmodel.MainViewModel
import com.example.titossycleaningservicesapp.presentation.users.supplier.util.NavRoutes
import com.example.titossycleaningservicesapp.presentation.utils.DrawerUserInfo
import com.example.titossycleaningservicesapp.presentation.utils.NavigationIcon
import kotlinx.coroutines.launch

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun SupplierNavigationDrawer(
    signOutSupplier: () -> Unit,
    mainViewModel: MainViewModel
) {
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    val navController = rememberNavController()
    val drawerItems = listOf(
        NavRoutes.Home,
        NavRoutes.Contact,
        NavRoutes.Profile
    )
    val pagerState = rememberPagerState(
        initialPage = 0,
        pageCount = { 2 }
    )
    val currentDestination = navController.currentBackStackEntryAsState().value?.destination?.route
    val supplier = rememberSaveable { mutableStateOf<Pair<String, String>?>(null) }
    var active by rememberSaveable { mutableStateOf(false) }
    var search by rememberSaveable { mutableStateOf("") }
    val showTopAppBar = drawerItems.any { it.route == currentDestination }
    val focusManager = LocalFocusManager.current
    val keyboardController = LocalSoftwareKeyboardController.current
    var expanded by rememberSaveable { mutableStateOf(false) }

    LaunchedEffect(supplier) {
        supplier.value = mainViewModel.readUserFromToken()
    }

    ModalNavigationDrawer(
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
                            AnimatedVisibility(visible = !active) {
                                when (currentDestination) {
                                    NavRoutes.Home.route -> Text(text = "Purchase Orders")
                                    NavRoutes.Contact.route -> Text(text = "Contact")
                                    NavRoutes.Profile.route -> Text(text = "Profile")
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
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.fillMaxHeight()
                            ) {
                                if (active) {
                                    BasicTextField(
                                        value = search,
                                        onValueChange = { search = it },
                                        modifier = Modifier
                                            .fillMaxWidth(.7f)
                                            .height(40.dp)
                                            .clip(RoundedCornerShape(50))
                                            .background(MaterialTheme.colorScheme.primaryContainer)
                                            .padding(horizontal = 16.dp, vertical = 8.dp),
                                        decorationBox = { innerTextField ->
                                            Row(
                                                modifier = Modifier.fillMaxWidth(),
                                                verticalAlignment = Alignment.CenterVertically
                                            ) {
                                                Icon(
                                                    imageVector = Icons.Rounded.Search,
                                                    contentDescription = "search",
                                                    modifier = Modifier.size(24.dp)
                                                )
                                                Box(modifier = Modifier.weight(1f)) {
                                                    if (search.isEmpty()) {
                                                        Text(
                                                            text = "Search..",
                                                            style = MaterialTheme.typography.bodyMedium.copy(
                                                                color = MaterialTheme.colorScheme.onPrimaryContainer.copy(
                                                                    0.5f
                                                                )
                                                            )
                                                        )
                                                    }
                                                    innerTextField()
                                                }
                                                if (search.isNotEmpty()) {
                                                    IconButton(onClick = { search = "" }) {
                                                        Icon(
                                                            imageVector = Icons.Rounded.Close,
                                                            contentDescription = "clear search",
                                                            modifier = Modifier.size(24.dp)
                                                        )
                                                    }
                                                }
                                            }
                                        },
                                        keyboardOptions = KeyboardOptions(
                                            imeAction = ImeAction.Search
                                        ),
                                        keyboardActions = KeyboardActions(
                                            onSearch = {
                                                focusManager.clearFocus()
                                                keyboardController?.hide()
                                            }
                                        ),
                                        singleLine = true,
                                        textStyle = MaterialTheme.typography.bodyLarge.copy(
                                            color = MaterialTheme.colorScheme.onPrimaryContainer
                                        )
                                    )
                                } else {
                                    IconButton(onClick = { active = true }) {
                                        Icon(
                                            imageVector = Icons.Rounded.Search,
                                            contentDescription = null,
                                            modifier = Modifier.size(32.dp)
                                        )
                                    }
                                }
                                Box(modifier = Modifier){
                                    NavigationIcon(
                                        icon = Icons.Default.MoreVert,
                                        onClick = { expanded = !expanded }
                                    )
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
                pagerState = pagerState
            )
        }
    }
}
