package com.example.titossycleaningservicesapp.presentation.users.customer.screens

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SearchBar
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.semantics.isTraversalGroup
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import com.example.titossycleaningservicesapp.domain.models.ui_models.Service
import com.example.titossycleaningservicesapp.domain.viewmodel.ServiceViewModel
import com.example.titossycleaningservicesapp.presentation.users.customer.utils.CustomServiceCard
import com.example.titossycleaningservicesapp.presentation.users.customer.utils.DetailsRoutes
import com.example.titossycleaningservicesapp.presentation.utils.RootNavRoutes

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    navController: NavHostController,
    paddingValues: PaddingValues,
    onSignOut: ()-> Unit
) {
    var searchText by remember { mutableStateOf("") }
    var expanded by remember { mutableStateOf(false) }
    var active by remember { mutableStateOf(false) }
    val serviceViewModel: ServiceViewModel = hiltViewModel()
    val context = LocalContext.current
    val serviceState by serviceViewModel.serviceState.collectAsState()

    Box(modifier = Modifier.fillMaxSize()) {
        Column {
            Box(
                modifier = Modifier
                    .semantics { isTraversalGroup = true }
                    .zIndex(1f)
                    .fillMaxWidth()
            ) {
                SearchBar(
                    modifier = Modifier
                        .fillMaxWidth()
                        .align(Alignment.TopCenter)
                        .padding(16.dp),
                    query = searchText,
                    onQueryChange = {
                        searchText = it
                        serviceViewModel.searchServices(it)
                    },
                    onSearch = { serviceViewModel.searchServices(searchText) },
                    active = active,
                    onActiveChange = { active = !active },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = null
                        )
                    },
                    trailingIcon = {
                        if (active) {
                            Icon(
                                imageVector = Icons.Default.Close,
                                contentDescription = null,
                                modifier = Modifier.clickable {
                                    if (searchText.isNotEmpty()) {
                                        searchText = ""
                                    } else {
                                        active = !active
                                    }
                                }
                            )
                        } else {
                            IconButton(onClick = { expanded = true }) {
                                Icon(
                                    imageVector = Icons.Default.MoreVert,
                                    contentDescription = null,
                                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }

                            DropdownMenu(
                                expanded = expanded,
                                onDismissRequest = { expanded = false }
                            ) {
                                DropdownMenuItem(
                                    text = { Text(text = "FAQs") },
                                    onClick = { }
                                )
                                DropdownMenuItem(
                                    text = { Text(text = "Help") },
                                    onClick = { }
                                )
                                DropdownMenuItem(
                                    text = { Text(text = "Logout") },
                                    onClick = {
                                        onSignOut()
                                    }
                                )
                            }
                        }
                    },
                    placeholder = { Text(text = "Search service") }
                ) {
                    LazyColumn(
                        modifier = Modifier.fillMaxWidth(),
                        contentPadding = PaddingValues(),
                        verticalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        items(serviceState.services) { service ->
                            ListItem(
                                modifier = Modifier.clickable {
                                    navController.popBackStack()
                                    navController.navigate(
                                        DetailsRoutes.Details.route + "/${service.id}"
                                    )
                                },
                                headlineContent = {
                                    SearchServiceCard(service)
                                    Text(text = service.name)
                                }
                            )
                        }
                    }
                }
            }

            when {
                serviceState.isLoading -> CircularProgressIndicator(
                    modifier = Modifier.align(
                        Alignment.CenterHorizontally
                    )
                )

                serviceState.services.isNotEmpty() -> {
                    LazyColumn(
                        modifier = Modifier,
                        contentPadding = paddingValues,
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(serviceState.services) { service ->
                            CustomServiceCard(
                                service = service,
                                onServiceClick = {
                                    navController.navigate(
                                        DetailsRoutes.Details.route + "/${it.id}"
                                    )
                                }
                            )
                        }
                    }
                }

                serviceState.error.isNotEmpty() -> {
                    Toast.makeText(context, serviceState.error, Toast.LENGTH_LONG).show()
                }
            }
        }
    }
}

@Composable
fun SearchServiceCard(
    service: Service
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        elevation = CardDefaults.cardElevation()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = rememberAsyncImagePainter(model = service.image),
                contentDescription = null,
                modifier = Modifier
                    .size(100.dp)
                    .padding(end = 4.dp)
            )
            Column {
                Text(text = service.name)
                Spacer(modifier = Modifier.height(4.dp))
                Text(text = service.description, maxLines = 1, overflow = TextOverflow.Ellipsis)
            }
        }
    }
}

