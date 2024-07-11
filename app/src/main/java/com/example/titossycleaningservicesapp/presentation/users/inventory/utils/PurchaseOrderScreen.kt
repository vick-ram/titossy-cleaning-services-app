package com.example.titossycleaningservicesapp.presentation.users.inventory.utils

import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.example.titossycleaningservicesapp.core.CustomProgressIndicator
import com.example.titossycleaningservicesapp.domain.models.ui_models.Supplier
import com.example.titossycleaningservicesapp.domain.viewmodel.ProductViewModel
import com.example.titossycleaningservicesapp.domain.viewmodel.PurchaseOrderViewModel
import com.example.titossycleaningservicesapp.domain.viewmodel.SupplierViewModel
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PurchaseOrderScreen(
    modifier: Modifier = Modifier,
    paddingValues: PaddingValues,
    navController: NavHostController,
) {

    val context = LocalContext.current

    var expanded by rememberSaveable { mutableStateOf(false) }
    var showDateDialog by rememberSaveable { mutableStateOf(false) }
    var selectedSupplier by remember { mutableStateOf<Supplier?>(null) }
    val productViewModel: ProductViewModel = hiltViewModel()
    val productCartUiState by productViewModel.productCartUiState.collectAsStateWithLifecycle()
    val supplierViewModel: SupplierViewModel = hiltViewModel()
    val supplierState by supplierViewModel.supplierUiState.collectAsStateWithLifecycle()
    val purchaseOrderViewModel: PurchaseOrderViewModel = hiltViewModel()
    val datePickerState = rememberDatePickerState()
    val confirmEnabled = remember { derivedStateOf { datePickerState.selectedDateMillis != null } }
    var selectedDate by remember { mutableStateOf(LocalDate.now()) }
    val purchaseUiState by purchaseOrderViewModel.purchaseOrderDataUiState.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        supplierViewModel.fetchSuppliers()
        productViewModel.fetchProductCart()
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(paddingValues)
            .padding(16.dp)
    ) {
        Text(
            modifier = modifier.align(Alignment.CenterHorizontally),
            text = "Purchase Order",
            style = MaterialTheme.typography.titleMedium.copy(
                fontSize = 18.sp
            )
        )
        Spacer(modifier = Modifier.height(8.dp))
        Row(
            modifier = modifier,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Order Date: ",
                style = MaterialTheme.typography.bodyLarge.copy()
            )
            Box(
                modifier = modifier
                    .wrapContentSize()
                    .padding(8.dp)
                    .border(
                        BorderStroke(
                            color = MaterialTheme.colorScheme.secondaryContainer,
                            width = 1.dp
                        ), MaterialTheme.shapes.small
                    )
                    .background(MaterialTheme.colorScheme.secondaryContainer)
                    .clip(MaterialTheme.shapes.small)
                    .clickable { showDateDialog = true },
            ) {
                Text(
                    modifier = modifier
                        .padding(4.dp),
                    text = selectedDate.toLocalFormattedDate(),
                    style = MaterialTheme.typography.bodyLarge.copy(
                        color = MaterialTheme.colorScheme.onSecondaryContainer
                    )
                )
            }
        }
        Spacer(modifier = Modifier.height(8.dp))

        CartTableHeader()
        Spacer(modifier = Modifier.height(8.dp))
        when {
            productCartUiState.products != null -> {
                LazyColumn(
                    modifier = modifier
                        .fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    productCartUiState.products?.let { products ->
                        items(products) { product ->
                            PurchaseOrderTableData(product = product)
                        }
                    }
                }
            }

            productCartUiState.isLoading -> CustomProgressIndicator(isLoading = true)
            productCartUiState.errorMessage.isNotEmpty() -> {
                Toast.makeText(
                    context,
                    productCartUiState.errorMessage,
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedButton(
            modifier = modifier
                .align(Alignment.End)
                .wrapContentSize(),
            onClick = {
                navController.navigate(NavRoutes.Home.route)
            }
        ) {
            Text(text = "Add Item")
        }
        Spacer(modifier = Modifier.height(8.dp))


        Row(
            modifier = modifier
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                modifier = modifier
                    .padding(end = 16.dp),
                text = "Select Supplier",
                style = MaterialTheme.typography.bodyLarge.copy()
            )
            when {
                supplierState.suppliers != null -> {
                    ExposedDropdownMenuBox(
                        modifier = modifier
                            .weight(1f),
                        expanded = expanded,
                        onExpandedChange = { expanded = !expanded }
                    ) {

                        TextField(
                            value = selectedSupplier?.fullName ?: "",
                            onValueChange = { },
                            label = { },
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp)
                                .menuAnchor(),
                            trailingIcon = {
                                ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
                            },
                            colors = ExposedDropdownMenuDefaults.textFieldColors(),
                            readOnly = true
                        )
                        ExposedDropdownMenu(
                            expanded = expanded,
                            onDismissRequest = { expanded = false }
                        ) {
                            supplierState.suppliers?.let { suppliers ->
                                suppliers.forEach { supplier ->
                                    DropdownMenuItem(
                                        text = { Text(text = supplier.fullName) },
                                        onClick = {
                                            selectedSupplier = supplier
                                            expanded = false
                                        }
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
        LoadingButton(
            modifier = modifier
                .fillMaxWidth(.6f)
                .align(Alignment.CenterHorizontally),
            isLoading = purchaseUiState.isLoading,
            onClick = {
                purchaseOrderViewModel.createPurchaseOrder(
                    supplierId = selectedSupplier?.id.toString(),
                    expectedDate = selectedDate
                )
            },
            text = "Create Purchase Order"
        )
    }

    if (showDateDialog) {
        DatePickerDialog(
            onDismissRequest = { showDateDialog = false },
            confirmButton = {
                TextButton(
                    onClick = {
                        selectedDate =
                            datePickerState.selectedDateMillis?.toLocalDate() ?: LocalDate.now()
                        showDateDialog = false
                    },
                    enabled = confirmEnabled.value
                ) {
                    Text(text = "Ok")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = { showDateDialog = false }
                ) {
                    Text(text = "Cancel")
                }
            },

            ) {
            DatePicker(
                state = datePickerState
            )
        }
    }
}

fun LocalDate.toLocalFormattedDate(): String {
    val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy", Locale.ENGLISH)
    return this.format(formatter)
}

fun Long.toLocalDate(): LocalDate {
    return Instant.ofEpochMilli(this)
        .atZone(ZoneId.systemDefault())
        .toLocalDate()
}

@Composable
fun LoadingButton(
    modifier: Modifier = Modifier,
    isLoading: Boolean = false,
    onClick: () -> Unit,
    text: String
) {
    Button(
        modifier = modifier,
        onClick = onClick,
        shape = MaterialTheme.shapes.extraSmall,
        contentPadding = PaddingValues(16.dp)
    ) {
        if (isLoading) {
            CircularProgressIndicator()
        } else {
            Text(text = text)
        }
    }
}
