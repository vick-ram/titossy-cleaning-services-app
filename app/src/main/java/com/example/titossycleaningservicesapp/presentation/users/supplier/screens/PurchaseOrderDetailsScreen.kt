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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.rounded.ChevronLeft
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
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
import com.example.titossycleaningservicesapp.core.statusToColor
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

            Spacer(modifier = Modifier.width(16.dp))
            Text(
                text = "Purchase Order Details",
                style = MaterialTheme.typography.titleLarge
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
        // Purchase Order Header
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp)
        ) {
            Column(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth()
            ) {
                Text(
                    text = "Purchase Order #${purchaseOrder.purchaseOrderId}",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(8.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(text = "Status: ", style = MaterialTheme.typography.bodyLarge)
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = purchaseOrder.orderStatus.name,
                        color = statusToColor(purchaseOrder.orderStatus),
                        style = MaterialTheme.typography.bodyLarge
                    )
                }
            }
        }

        Text(
            text = "Order Details",
            style = MaterialTheme.typography.headlineSmall
        )
        Spacer(modifier = Modifier.height(8.dp))
        OrderRow("Employee", purchaseOrder.employee)
        OrderRow("Supplier", purchaseOrder.supplier)
        OrderRow("Expected Date", purchaseOrder.formattedDate)
        Spacer(modifier = Modifier.height(16.dp))

        // Items
        Text(
            text = "Items",
            style = MaterialTheme.typography.headlineSmall
        )
        Spacer(modifier = Modifier.height(8.dp))
        if (purchaseOrder.purchaseOrderItems != null) {
            purchaseOrder.purchaseOrderItems.forEach { item ->
                PurchaseOrderItemRow(item)
            }
        } else {
            Text(
                text = "No items found",
                style = MaterialTheme.typography.bodyLarge
            )
        }
        Spacer(modifier = Modifier.height(16.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.End
        ) {
            Text(
                text = "Total Amount: ",
                style = MaterialTheme.typography.bodyLarge
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text(
                text = purchaseOrder.formattedAmount,
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Bold
            )
        }
        Spacer(modifier = Modifier.height(16.dp))
    }
}

@Composable
fun PurchaseOrderItemRow(purchaseOrderItem: PurchaseOrderItem) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = purchaseOrderItem.product,
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "Quantity: ${purchaseOrderItem.quantity}",
                    style = MaterialTheme.typography.bodyLarge
                )
                Text(
                    text = "Unit Price: ${purchaseOrderItem.formattedUnitPrice}",
                    style = MaterialTheme.typography.bodyLarge
                )
                Text(
                    text = "Subtotal: ${purchaseOrderItem.formattedSubtotal}",
                    style = MaterialTheme.typography.bodyLarge
                )
            }
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                contentDescription = "Details",
                modifier = Modifier.size(24.dp)
            )
        }
    }
}

@Composable
fun OrderRow(label: String, value: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = "$label: ", style = MaterialTheme.typography.bodyLarge)
        Spacer(modifier = Modifier.width(4.dp))
        Text(text = value, style = MaterialTheme.typography.bodyLarge)
    }
}

