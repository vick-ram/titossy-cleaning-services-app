package com.example.titossycleaningservicesapp.presentation.users.supplier.screens

import android.widget.Toast
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.titossycleaningservicesapp.core.CustomProgressIndicator
import com.example.titossycleaningservicesapp.core.splitFullName
import com.example.titossycleaningservicesapp.data.remote.util.AuthEvent
import com.example.titossycleaningservicesapp.domain.viewmodel.MainViewModel
import com.example.titossycleaningservicesapp.domain.viewmodel.SupplierViewModel
import java.util.UUID

@Composable
fun ProfileScreen(
    modifier: Modifier = Modifier,
    paddingValues: PaddingValues,
    mainViewModel: MainViewModel,
    supplierViewModel: SupplierViewModel
) {
    val context = LocalContext.current

    var supplierId by rememberSaveable { mutableStateOf<String?>(null) }
    val supplierState by supplierViewModel.supplierUiState.collectAsStateWithLifecycle()
    var showDialog by rememberSaveable { mutableStateOf(false) }
    val supplier = supplierState.suppliers?.find { it.id.toString() == supplierId }

    val supplierNames = splitFullName(supplier?.fullName ?: "No fullName")

    var firstName by rememberSaveable { mutableStateOf("") }
    var lastName by rememberSaveable { mutableStateOf("") }
    var phone by rememberSaveable { mutableStateOf("") }
    var address by rememberSaveable { mutableStateOf("") }
    var email by rememberSaveable { mutableStateOf("") }
    var password by rememberSaveable { mutableStateOf("") }

    LaunchedEffect(key1 = supplierId) {
        supplierId = mainViewModel.readUserId()
    }

    LaunchedEffect(supplierViewModel, context) {
        supplierViewModel.resultChannel.collect { result ->
            when (result) {
                is AuthEvent.Error -> {
                    Toast.makeText(
                        context,
                        result.message,
                        Toast.LENGTH_LONG
                    ).show()
                }

                is AuthEvent.Loading -> supplierViewModel.isLoading

                is AuthEvent.Success -> {
                    Toast.makeText(
                        context,
                        result.message,
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }

    when {
        supplierState.isLoading -> Box(
            modifier = modifier.fillMaxSize(),
            contentAlignment = Alignment.Center,
            content = { CustomProgressIndicator(isLoading = true) }
        )

        supplierState.suppliers != null -> {
            supplier?.let { sup ->
                Column(
                    modifier = modifier
                        .fillMaxSize()
                        .padding(paddingValues)
                ) {
                    Row(
                        modifier = modifier
                            .fillMaxWidth(),
                        horizontalArrangement = Arrangement.End
                    ) {
                        IconButton(onClick = { showDialog = true }) {
                            Icon(
                                imageVector = Icons.Filled.Edit,
                                contentDescription = null
                            )
                        }
                    }
                    Spacer(modifier = modifier.height(16.dp))
                    SupplierInfoField(
                        label = "Name:",
                        field = sup.fullName
                    )
                    HorizontalDivider()
                    SupplierInfoField(
                        label = "Phone:",
                        field = sup.phone
                    )
                    HorizontalDivider()
                    SupplierInfoField(
                        label = "Email:",
                        field = sup.email
                    )
                    HorizontalDivider()
                    SupplierInfoField(
                        label = "Address:",
                        field = sup.address
                    )
                }
            }
        }

        supplierState.errorMessage.isNotEmpty() -> {
            Toast.makeText(
                context,
                supplierState.errorMessage,
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    if (showDialog) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            confirmButton = {
                TextButton(onClick = {
                    supplierViewModel.update(
                        id = UUID.fromString(supplierId),
                        firstName = firstName,
                        lastName = lastName,
                        phone = phone,
                        address = address,
                        email = email,
                        password = password
                    )
                    showDialog = false }
                ) {
                    Text(text = "Confirm")
                }
            },
            dismissButton = {
                TextButton(onClick = { showDialog = false }) {
                    Text(text = "Cancel")
                }
            },
            title = {
                Text(text = "Edit Profile")
            },
            text = {
                Column(
                    modifier = modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    OutlinedTextField(
                        modifier = modifier.fillMaxWidth(),
                        value = firstName,
                        onValueChange = { firstName = it },
                        shape = MaterialTheme.shapes.medium,
                        textStyle = MaterialTheme.typography.bodyLarge,
                        singleLine = true,
                        placeholder = {
                            Text(
                                text = supplierNames.first,
                                style = MaterialTheme.typography.bodyLarge.copy(
                                    color = MaterialTheme.colorScheme.onSurface.copy(.5f)
                                )
                            )
                        }
                    )
                    OutlinedTextField(
                        modifier = modifier.fillMaxWidth(),
                        value = lastName,
                        onValueChange = { lastName = it },
                        shape = MaterialTheme.shapes.medium,
                        textStyle = MaterialTheme.typography.bodyLarge,
                        singleLine = true,
                        placeholder = {
                            Text(
                                text = supplierNames.second,
                                style = MaterialTheme.typography.bodyLarge.copy(
                                    color = MaterialTheme.colorScheme.onSurface.copy(.5f)
                                )
                            )
                        }
                    )
                    OutlinedTextField(
                        modifier = modifier.fillMaxWidth(),
                        value = phone,
                        onValueChange = { phone = it },
                        shape = MaterialTheme.shapes.medium,
                        textStyle = MaterialTheme.typography.bodyLarge,
                        singleLine = true,
                        placeholder = {
                            Text(
                                text = supplier?.phone ?: "",
                                style = MaterialTheme.typography.bodyLarge.copy(
                                    color = MaterialTheme.colorScheme.onSurface.copy(.5f)
                                )
                            )
                        }
                    )
                    OutlinedTextField(
                        modifier = modifier.fillMaxWidth(),
                        value = address,
                        onValueChange = { address = it },
                        shape = MaterialTheme.shapes.medium,
                        textStyle = MaterialTheme.typography.bodyLarge,
                        singleLine = true,
                        placeholder = {
                            Text(
                                text = supplier?.address ?: "",
                                style = MaterialTheme.typography.bodyLarge.copy(
                                    color = MaterialTheme.colorScheme.onSurface.copy(.5f)
                                )
                            )
                        }
                    )
                    OutlinedTextField(
                        modifier = modifier.fillMaxWidth(),
                        value = email,
                        onValueChange = { email = it },
                        shape = MaterialTheme.shapes.medium,
                        textStyle = MaterialTheme.typography.bodyLarge,
                        singleLine = true,
                        placeholder = {
                            Text(
                                text = supplier?.email ?: "",
                                style = MaterialTheme.typography.bodyLarge.copy(
                                    color = MaterialTheme.colorScheme.onSurface.copy(.5f)
                                )
                            )
                        }
                    )
                    OutlinedTextField(
                        modifier = modifier.fillMaxWidth(),
                        value = password,
                        onValueChange = { password = it },
                        shape = MaterialTheme.shapes.medium,
                        textStyle = MaterialTheme.typography.bodyLarge,
                        singleLine = true,
                        placeholder = {
                            Text(
                                text = "Enter Password",
                                style = MaterialTheme.typography.bodyLarge.copy(
                                    color = MaterialTheme.colorScheme.onSurface.copy(.5f)
                                )
                            )
                        }
                    )
                }
            }
        )
    }
}


@Composable
fun SupplierInfoField(
    modifier: Modifier = Modifier,
    label: String, field: String,
    style: TextStyle = MaterialTheme.typography.bodyLarge
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Text(
            text = label,
            style = style.copy(
                color = MaterialTheme.colorScheme.onSurface,
                fontWeight = FontWeight.SemiBold
            )
        )
        Spacer(modifier = modifier.height(8.dp))

        Text(
            modifier = modifier.padding(start = 16.dp),
            text = field,
            style = style.copy(
                color = MaterialTheme.colorScheme.onSurface.copy(.7f)
            )
        )
    }
}
