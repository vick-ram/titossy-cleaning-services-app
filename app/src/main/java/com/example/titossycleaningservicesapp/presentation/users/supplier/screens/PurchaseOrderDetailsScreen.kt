package com.example.titossycleaningservicesapp.presentation.users.supplier.screens

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ChevronLeft
import androidx.compose.material3.Button
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.example.titossycleaningservicesapp.core.showToast
import com.example.titossycleaningservicesapp.domain.models.OrderStatus
import com.example.titossycleaningservicesapp.domain.models.ui_models.PurchaseOrder
import com.example.titossycleaningservicesapp.domain.models.ui_models.PurchaseOrderItem
import com.example.titossycleaningservicesapp.domain.viewmodel.PurchaseOrderViewModel
import com.example.titossycleaningservicesapp.presentation.utils.NavigationIcon

@Composable
fun PurchaseOrderDetailsScreen(
    modifier: Modifier = Modifier,
    purchaseOrderId: String,
    paddingValues: PaddingValues,
    navController: NavHostController
) {
    val context = LocalContext.current
    val purchaseOrderViewModel: PurchaseOrderViewModel = hiltViewModel()
    val purchaseOrderUiState by purchaseOrderViewModel.purchaseOrderDataUiState.collectAsStateWithLifecycle()

    val purchaseOrder =
        purchaseOrderUiState.purchaseOrders?.find { it.purchaseOrderId == purchaseOrderId }
    val purchaseOrderStatus by purchaseOrderViewModel.purchaseOrderStatus.collectAsStateWithLifecycle()

    LaunchedEffect(purchaseOrderStatus) {
        when {
            purchaseOrderStatus.isLoading -> return@LaunchedEffect
            purchaseOrderStatus.successMessage.isNotEmpty() -> {
                showToast(
                    context = context,
                    length = Toast.LENGTH_LONG,
                    message = purchaseOrderStatus.successMessage
                )
                navController.navigateUp()
            }

            purchaseOrderStatus.errorMessage.isNotEmpty() -> {
                showToast(
                    context = context,
                    message = purchaseOrderStatus.errorMessage
                )
            }
        }
    }
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(paddingValues)
    ) {
        Row(
            modifier = modifier
                .fillMaxWidth()
                .padding(top = 2.dp, bottom = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            NavigationIcon(
                icon = Icons.Rounded.ChevronLeft,
                onClick = {
                    navController.navigateUp()
                }
            )
            Text(
                text = "Purchase Order Details",
                style = MaterialTheme.typography.titleMedium
            )
        }

        LazyColumn(
            modifier = modifier
                .fillMaxWidth()
        ) {
            item {
                purchaseOrder?.let {
                    PurchaseOrderScreen(purchaseOrder = it)
                }
            }
            item {
                Row(
                    modifier = modifier
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center
                ) {
                    if (purchaseOrder?.orderStatus == OrderStatus.APPROVED) {
                        Button(
                            modifier = Modifier
                                .fillMaxWidth(.7f)
                                .padding(bottom = 16.dp),
                            onClick = {
                                purchaseOrderViewModel.updateOrderStatus(
                                    id = purchaseOrderId,
                                    status = OrderStatus.ACKNOWLEDGED.name
                                )
                            },
                            contentPadding = PaddingValues(16.dp),
                            shape = MaterialTheme.shapes.extraSmall
                        ) {
                            Text(text = "Acknowledge Order")
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun PurchaseOrderScreen(purchaseOrder: PurchaseOrder) {
    Column(
        modifier = Modifier
            .padding(16.dp)
    ) {
        OrderRow(
            "Order ID",
            purchaseOrder.purchaseOrderId
        )
        OrderRow(
            "To",
            "Titossy Cleaning Services"
        )
        OrderRow(
            "From",
            purchaseOrder.supplier
        )
        OrderRow(
            "Expected Date",
            purchaseOrder.formattedDate
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "Items:",
            style = MaterialTheme.typography.titleLarge
        )
        Spacer(modifier = Modifier.height(8.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Product",
                style = MaterialTheme.typography.bodyMedium.copy(
                    fontWeight = FontWeight.SemiBold
                )
            )
            Text(
                text = "Qty",
                style = MaterialTheme.typography.bodyMedium.copy(
                    fontWeight = FontWeight.SemiBold
                )
            )
            Text(
                text = "Unit Price",
                style = MaterialTheme.typography.bodyMedium.copy(
                    fontWeight = FontWeight.SemiBold
                )
            )
            Text(
                text = "Subtotal",
                style = MaterialTheme.typography.bodyMedium.copy(
                    fontWeight = FontWeight.SemiBold
                )
            )
        }
        HorizontalDivider()
        if (purchaseOrder.purchaseOrderItems != null) {
            purchaseOrder.purchaseOrderItems.forEach { item ->
                PurchaseOrderItemRow(item)
            }
        } else {
            Text(
                text = "No items found",
                style = MaterialTheme.typography.bodyMedium
            )
        }
        HorizontalDivider()
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Total Amount:",
                style = MaterialTheme.typography.bodyMedium
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text(
                text = purchaseOrder.formattedAmount,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Composable
fun PurchaseOrderItemRow(purchaseOrderItem: PurchaseOrderItem) {
    Row(
        modifier = Modifier
            .padding(vertical = 8.dp)
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = purchaseOrderItem.product,
            style = MaterialTheme.typography.bodyMedium
        )
        Text(
            text = "X ${purchaseOrderItem.quantity}",
            style = MaterialTheme.typography.bodyMedium
        )
        Text(
            text = purchaseOrderItem.unitPrice.toPlainString(),
            style = MaterialTheme.typography.bodyMedium
        )
        Text(
            text = purchaseOrderItem.subtotal.toPlainString(),
            style = MaterialTheme.typography.bodyMedium
        )
    }
}

@Composable
fun OrderRow(label: String, value: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "$label: ",
            style = MaterialTheme.typography.bodyMedium.copy(
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = .6f)
            )
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = value,
            style = MaterialTheme.typography.bodyMedium
        )
    }
}


