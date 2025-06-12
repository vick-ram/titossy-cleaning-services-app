package com.example.titossycleaningservicesapp.presentation.users.inventory.utils

import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.example.titossycleaningservicesapp.core.CustomProgressIndicator
import com.example.titossycleaningservicesapp.core.showToast
import com.example.titossycleaningservicesapp.domain.models.ui_models.Supplier
import com.example.titossycleaningservicesapp.domain.viewmodel.MainViewModel
import com.example.titossycleaningservicesapp.domain.viewmodel.ProductViewModel
import com.example.titossycleaningservicesapp.domain.viewmodel.PurchaseOrderViewModel
import com.example.titossycleaningservicesapp.domain.viewmodel.SupplierViewModel
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PurchaseOrderScreen(
    modifier: Modifier = Modifier,
    paddingValues: PaddingValues,
    navController: NavHostController,
    supplierId: String,
) {

    val context = LocalContext.current
    var showDateDialog by rememberSaveable { mutableStateOf(false) }
    val productViewModel: ProductViewModel = hiltViewModel()
    val productCartUiState by productViewModel.productCartUiState.collectAsStateWithLifecycle()
    val purchaseOrderViewModel: PurchaseOrderViewModel = hiltViewModel()
    val datePickerState = rememberDatePickerState()
    val confirmEnabled = remember { derivedStateOf { datePickerState.selectedDateMillis != null } }
    var selectedDate by remember { mutableStateOf(LocalDate.now()) }
    val purchaseOrderStatus by purchaseOrderViewModel.purchaseOrderStatus.collectAsStateWithLifecycle()

    val isButtonEnabled = remember(selectedDate) {
        (selectedDate.isEqual(LocalDate.now()) || selectedDate.isAfter(
            LocalDate.now()
        ))
    }

    LaunchedEffect(Unit) {
        productViewModel.fetchProductCart()
    }

    LaunchedEffect(purchaseOrderStatus) {
        when {
            purchaseOrderStatus.successMessage.isNotEmpty() -> {
                showToast(
                    context = context,
                    length = Toast.LENGTH_LONG,
                    message = purchaseOrderStatus.successMessage
                )
                navController.navigate(NavRoutes.Orders.route)
            }

            purchaseOrderStatus.errorMessage.isNotEmpty() -> {
                showToast(
                    context = context,
                    message = purchaseOrderStatus.errorMessage
                )
            }
        }
    }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(8.dp),
        contentPadding = paddingValues
    ) {
        item {
            Row(
                modifier = modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp),
                horizontalArrangement = Arrangement.Center
            ) {
                Text(
                    modifier = modifier,
                    text = "Purchase Order",
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontSize = 18.sp
                    )
                )
            }
        }
        item {
            Row(
                modifier = modifier.padding(bottom = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Order Date: ",
                    style = MaterialTheme.typography.bodyMedium
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
        }
        item { CartTableHeader(modifier = modifier.padding(bottom = 8.dp)) }
        when {
            productCartUiState.products != null -> {
                productCartUiState.products?.let { products ->
                    items(products) { product ->
                        PurchaseOrderTableData(product = product)
                    }
                }
            }

            productCartUiState.isLoading -> {
                item {
                    Row(
                        modifier = modifier
                            .fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center
                    ) {
                        CustomProgressIndicator(isLoading = true)
                    }
                }
            }

            productCartUiState.errorMessage.isNotEmpty() -> {
                showToast(
                    context = context,
                    message = productCartUiState.errorMessage
                )
            }
        }
        item {
            Row(
                modifier = modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                horizontalArrangement = Arrangement.End
            ) {
                OutlinedButton(
                    modifier = modifier
                        .wrapContentSize(),
                    onClick = {
                        navController.navigate(NavRoutes.Home.route)
                    }
                ) {
                    Text(text = "Add Item")
                }
            }
        }
        item {
            Row(
                modifier = modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                LoadingButton(
                    modifier = modifier
                        .fillMaxWidth(.6f),
                    isLoading = !purchaseOrderStatus.isLoading,
                    onClick = {
                        purchaseOrderViewModel.createPurchaseOrder(
                            supplierId = supplierId,
                            expectedDate = selectedDate
                        )
                    },
                    text = "Create Purchase Order",
                    enabled = isButtonEnabled
                )
            }
        }
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
    val formatter = DateTimeFormatter.ISO_LOCAL_DATE
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
    text: String,
    enabled: Boolean
) {
    Button(
        modifier = modifier,
        onClick = onClick,
        shape = MaterialTheme.shapes.extraSmall,
        contentPadding = PaddingValues(16.dp),
        enabled = enabled
    ) {
        if (isLoading) {
            CircularProgressIndicator(color = Color.White)
        } else {
            Text(text = text)
        }
    }
}
