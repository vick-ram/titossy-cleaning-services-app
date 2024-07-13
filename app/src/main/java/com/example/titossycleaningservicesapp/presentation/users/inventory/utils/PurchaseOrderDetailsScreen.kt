package com.example.titossycleaningservicesapp.presentation.users.inventory.utils

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronLeft
import androidx.compose.material3.Button
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.titossycleaningservicesapp.core.showToast
import com.example.titossycleaningservicesapp.domain.models.OrderStatus
import com.example.titossycleaningservicesapp.domain.models.ui_models.PurchaseOrderItem
import com.example.titossycleaningservicesapp.domain.viewmodel.PurchaseOrderViewModel

@Composable
fun InventoryPurchaseOrderDetailsScreen(
    modifier: Modifier = Modifier,
    paddingValues: PaddingValues,
    purchaseOrderId: String,
    onBack: () -> Unit
) {
    val context = LocalContext.current
    val purchaseOrderViewModel: PurchaseOrderViewModel = hiltViewModel()
    val purchaseOrderUiState by purchaseOrderViewModel.purchaseOrderDataUiState.collectAsStateWithLifecycle()
    val purchaseOrder =
        purchaseOrderUiState.purchaseOrders?.find { it.purchaseOrderId == purchaseOrderId }
    val purchaseOrderStatus by purchaseOrderViewModel.purchaseOrderStatus.collectAsStateWithLifecycle()

    LaunchedEffect(purchaseOrderStatus, purchaseOrderViewModel) {
        when {
            purchaseOrderStatus.isLoading -> return@LaunchedEffect
            purchaseOrderStatus.successMessage.isNotEmpty() -> {
                showToast(
                    context = context,
                    message = purchaseOrderStatus.successMessage
                )
                purchaseOrderViewModel.fetchPurchaseOrders()
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
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Row(
            modifier = modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onBack) {
                Icon(
                    modifier = modifier.size(32.dp),
                    imageVector = Icons.Default.ChevronLeft,
                    contentDescription = null
                )
            }
            Spacer(modifier = modifier.width(8.dp))
            Text(
                "Purchase Order Details",
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(bottom = 8.dp)
            )
        }
        HorizontalDivider()
        DetailRow("Order ID:  ", purchaseOrder?.purchaseOrderId ?: "")
        DetailRow("Employee:  ", purchaseOrder?.employee ?: "")
        DetailRow("Supplier:  ", purchaseOrder?.supplier ?: "")
        DetailRow("Expected Date:  ", purchaseOrder?.formattedDate ?: "")
        DetailRow(
            "Total Amount:  ",
            "Kshs. ${purchaseOrder?.totalAmount?.toPlainString()}"
        )
        DetailRow("Order Status:  ", purchaseOrder?.orderStatus?.name ?: "")
        HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))
        Text(
            "Items",
            style = MaterialTheme.typography.titleSmall,
            modifier = Modifier.padding(vertical = 8.dp)
        )
        LazyColumn {
            purchaseOrder?.purchaseOrderItems?.let { items ->
                items(items) {
                    PurchaseOrderItemRow(purchaseOrderItem = it)
                }
            }
        }
        HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))
        if (purchaseOrder?.orderStatus == OrderStatus.ACKNOWLEDGED) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .systemBarsPadding()
                    .padding(bottom = 8.dp),
                horizontalArrangement = Arrangement.End
            ) {
                Button(onClick = {
                    purchaseOrderViewModel.updateOrderStatus(
                        id = purchaseOrderId,
                        status = OrderStatus.RECEIVED.name
                    )
                }) {
                    Text("Receive Order")
                }
            }
        }
    }
}

@Composable
fun PurchaseOrderItemRow(
    purchaseOrderItem: PurchaseOrderItem,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier.padding(8.dp)) {
        Text(
            "Product:  ${purchaseOrderItem.product}",
            style = MaterialTheme.typography.bodyMedium
        )
        Text(
            "Quantity:  ${purchaseOrderItem.quantity}",
            style = MaterialTheme.typography.bodyMedium
        )
        Text(
            "Unit Price:  Kshs. ${purchaseOrderItem.unitPrice.toPlainString()}",
            style = MaterialTheme.typography.bodyMedium
        )
        Text(
            "Subtotal:  Kshs. ${purchaseOrderItem.subtotal.toPlainString()}",
            style = MaterialTheme.typography.bodyMedium
        )
    }
}

@Composable
fun DetailRow(
    label: String,
    value: String,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.padding(vertical = 2.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(label, style = MaterialTheme.typography.bodyMedium)
        Text(value, style = MaterialTheme.typography.bodyMedium)
    }
}