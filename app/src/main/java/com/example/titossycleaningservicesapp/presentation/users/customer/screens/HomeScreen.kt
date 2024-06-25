package com.example.titossycleaningservicesapp.presentation.users.customer.screens

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.semantics.isTraversalGroup
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.example.titossycleaningservicesapp.core.CustomProgressIndicator
import com.example.titossycleaningservicesapp.domain.viewmodel.ServiceViewModel
import com.example.titossycleaningservicesapp.presentation.users.customer.utils.CustomServiceCard
import com.example.titossycleaningservicesapp.presentation.users.customer.utils.DetailsRoutes

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    navController: NavHostController,
    paddingValues: PaddingValues,
    onSignOut: () -> Unit
) {
    val context = LocalContext.current
    var searchText by rememberSaveable { mutableStateOf("") }
    var expanded by rememberSaveable { mutableStateOf(false) }
    val serviceViewModel: ServiceViewModel = hiltViewModel()
    val serviceState by serviceViewModel.serviceState.collectAsStateWithLifecycle()
    val focusRequest = remember { FocusRequester() }
    var isFocused by rememberSaveable { mutableStateOf(false) }
    val keyboardController = LocalSoftwareKeyboardController.current
    val focusManager = LocalFocusManager.current


    Box(modifier = Modifier.fillMaxSize()) {
        Column {
            Box(
                modifier = Modifier
                    .semantics { isTraversalGroup = true }
                    .zIndex(1f)
                    .fillMaxWidth()
            ) {
                OutlinedTextField(
                    value = searchText,
                    onValueChange = {
                        searchText = it
                        if (it.isNotEmpty()) {
                            serviceViewModel.searchServices(it)
                        } else {
                            serviceViewModel.fetchServices()
                        }
                        serviceViewModel.searchServices(it)
                    },
                    modifier = Modifier
                        .align(Alignment.TopCenter)
                        .fillMaxWidth()
                        .padding(16.dp)
                        .focusRequester(focusRequest)
                        .onFocusChanged { isFocused = it.isFocused },
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        focusedBorderColor = Color(0xFF007BFF),
                        unfocusedBorderColor = Color.LightGray,
                        cursorColor = Color(0xFF007BFF),
                        disabledBorderColor = Color.LightGray,
                        disabledTextColor = Color.LightGray
                    ),
                    shape = MaterialTheme.shapes.extraLarge,
                    placeholder = { Text(text = "Search service..", color = Color.LightGray) },
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Text,
                        imeAction = ImeAction.Search
                    ),
                    keyboardActions = KeyboardActions(
                        onSearch = {
                            serviceViewModel.searchServices(searchText)
                        }
                    ),
                    textStyle = MaterialTheme.typography.bodySmall,
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Rounded.Search,
                            contentDescription = null
                        )
                    },
                    trailingIcon = {
                        Row {
                            if (isFocused && searchText.isNotEmpty()) {
                                IconButton(
                                    onClick = {
                                        searchText = ""
                                        if (searchText.isEmpty()) {
                                            focusRequest.freeFocus()
                                            serviceViewModel.fetchServices()
                                            keyboardController?.hide()
                                            focusManager.clearFocus()
                                        }
                                    }
                                ) {
                                    Icon(
                                        imageVector = Icons.Rounded.Close,
                                        contentDescription = null,
                                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                            }
                            IconButton(onClick = { expanded = true }) {
                                Icon(
                                    imageVector = Icons.Default.MoreVert,
                                    contentDescription = null,
                                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
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
                                onClick = onSignOut
                            )
                        }
                    }
                )
            }

            when {
                serviceState.isLoading -> Box(
                    modifier = Modifier
                        .fillMaxSize(),
                    contentAlignment = Alignment.Center,
                    content = { CustomProgressIndicator(isLoading = true) }
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

                serviceState.error != null -> {
                    val errorMessage = serviceState.error?.getContentIfNotHandled()
                    errorMessage?.let {
                        Toast.makeText(context, errorMessage, Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }
}
