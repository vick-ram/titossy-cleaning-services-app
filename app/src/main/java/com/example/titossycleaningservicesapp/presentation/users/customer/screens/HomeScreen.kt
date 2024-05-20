package com.example.titossycleaningservicesapp.presentation.users.customer.screens

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.windowInsetsBottomHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.titossycleaningservicesapp.domain.viewmodel.ServiceViewModel
import com.example.titossycleaningservicesapp.presentation.users.customer.utils.CustomServiceCard
import com.example.titossycleaningservicesapp.presentation.users.customer.utils.DetailsRoutes

@Composable
fun HomeScreen(
    navController: NavHostController,
    paddingValues: PaddingValues,
) {
    var searchText by remember { mutableStateOf("") }
    var expanded by remember { mutableStateOf(false) }
    val serviceViewModel: ServiceViewModel = hiltViewModel()
    val context = LocalContext.current
    val serviceState by serviceViewModel.serviceState.collectAsState()

    when {
        serviceState.isLoading -> {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        }

        serviceState.services.isNotEmpty() -> {
            Column(
                modifier = Modifier
                    .fillMaxSize()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 26.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Absolute.SpaceBetween
                ) {

                    Box(
                        modifier = Modifier
                            .size(50.dp)
                            .clip(RoundedCornerShape(50))
                    ) {
                        Icon(
                            imageVector = Icons.Default.AccountCircle,
                            contentDescription = null,
                            modifier = Modifier.fillMaxSize()
                        )
                    }

                    IconButton(
                        onClick = { expanded = true },
                        colors = IconButtonDefaults.iconButtonColors(
                            containerColor = MaterialTheme.colorScheme.surfaceContainer,
                            contentColor = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    ) {
                        Icon(
                            imageVector = Icons.Default.MoreVert,
                            contentDescription = null,
                        )

                        DropdownMenu(
                            expanded = expanded,
                            onDismissRequest = { expanded = false }
                        ) {
                            DropdownMenuItem(
                                text = { Text(text = "FAQs") },
                                onClick = { /*TODO*/ }
                            )
                            DropdownMenuItem(
                                text = { Text(text = "Help") },
                                onClick = { /*TODO*/ }
                            )
                            DropdownMenuItem(
                                text = { Text(text = "Logout") },
                                onClick = { /*TODO*/ }
                            )
                        }
                    }

                }

                Spacer(modifier = Modifier.height(16.dp))

                OutlinedTextField(
                    value = searchText,
                    onValueChange = { searchText = it },
                    modifier = Modifier
                        .fillMaxWidth(0.8f),
                    placeholder = { Text(text = "Search for services") },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Filled.Search,
                            contentDescription = "search"
                        )
                    },
                    shape = RoundedCornerShape(50),
                    singleLine = true,
                    maxLines = 1,
                    keyboardActions = KeyboardActions(
                        onSend = { serviceViewModel.searchServices(searchText) }
                    ),
                    keyboardOptions = KeyboardOptions(
                        imeAction = ImeAction.Search,
                        autoCorrect = true,
                        keyboardType = KeyboardType.Text
                    ),
                    colors = TextFieldDefaults.colors(
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent,
                        disabledIndicatorColor = Color.Transparent,
                        cursorColor = MaterialTheme.colorScheme.primary
                    )

                )

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    modifier = Modifier.padding(start = 8.dp),
                    text = "Services",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(8.dp))

                LazyColumn(
                    modifier = Modifier
                        .statusBarsPadding()
                        .consumeWindowInsets(paddingValues)
                        .weight(1f)
                        .fillMaxWidth(),
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
                    item {
                        Spacer(
                            modifier = Modifier.windowInsetsBottomHeight(
                                WindowInsets.systemBars
                            )
                        )
                    }
                }
            }
        }

        serviceState.error.isNotEmpty() -> {
            Toast.makeText(
                context,
                serviceState.error,
                Toast.LENGTH_LONG
            ).show()
        }
    }
}


