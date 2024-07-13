package com.example.titossycleaningservicesapp.presentation.users.manager.utils

import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Inbox
import androidx.compose.material.icons.rounded.ChevronLeft
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.example.titossycleaningservicesapp.core.CustomProgressIndicator
import com.example.titossycleaningservicesapp.core.showToast
import com.example.titossycleaningservicesapp.domain.models.OrderStatus
import com.example.titossycleaningservicesapp.domain.models.ui_models.PurchaseOrder
import com.example.titossycleaningservicesapp.domain.viewmodel.PurchaseOrderViewModel
import com.example.titossycleaningservicesapp.presentation.utils.NavigationIcon

@Composable
fun NoPendingOrdersScreen() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Icon(
                imageVector = Icons.Filled.Inbox,
                contentDescription = "No Orders",
                modifier = Modifier.size(70.dp),
                tint = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Text(
                text = "No pending orders available.",
                style = MaterialTheme.typography.headlineSmall.copy(
                    textAlign = TextAlign.Center
                ),
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
fun ManagerOrdersScreen(
    modifier: Modifier = Modifier,
    paddingValues: PaddingValues,
    navController: NavHostController
) {
    val context = LocalContext.current
    val purchaseOrderViewModel: PurchaseOrderViewModel = hiltViewModel()
    val purchaseOrderStatus by purchaseOrderViewModel.purchaseOrderStatus.collectAsStateWithLifecycle()
    val purchaseOrderUiState by purchaseOrderViewModel.purchaseOrderDataUiState.collectAsStateWithLifecycle()

    LaunchedEffect(key1 = purchaseOrderStatus) {
        when {
            purchaseOrderStatus.isLoading -> return@LaunchedEffect
            purchaseOrderStatus.successMessage.isNotEmpty() -> {
                showToast(
                    context = context,
                    length = Toast.LENGTH_LONG,
                    message = purchaseOrderStatus.successMessage
                )
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
                .padding(bottom = 4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            NavigationIcon(icon = Icons.Rounded.ChevronLeft) {
                navController.navigateUp()
            }
            Text(
                text = "Pending Orders",
                style = MaterialTheme.typography.titleMedium
            )
        }
        when {
            purchaseOrderUiState.isLoading -> Box(
                modifier = modifier.fillMaxSize(),
                contentAlignment = Alignment.Center,
                content = { CustomProgressIndicator(isLoading = true) }
            )

            purchaseOrderUiState.purchaseOrders.isNullOrEmpty() -> NoPendingOrdersScreen()

            else -> {
                val pendingOrders = purchaseOrderUiState.purchaseOrders?.filter { it.orderStatus == OrderStatus.PENDING }
                if (pendingOrders.isNullOrEmpty()) {
                    NoPendingOrdersScreen()
                } else {
                    LazyColumn(
                        modifier = modifier.fillMaxWidth()
                    ) {
                        items(pendingOrders) { purchaseOrder ->
                            OrderItemCard(
                                purchaseOrder = purchaseOrder,
                                onApprove = { order, status ->
                                    purchaseOrderViewModel.updateOrderStatus(
                                        id = order.purchaseOrderId,
                                        status = status.name
                                    )
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun OrderItemCard(
    modifier: Modifier = Modifier,
    purchaseOrder: PurchaseOrder,
    onApprove: (PurchaseOrder, OrderStatus) -> Unit
) {
    var visible by rememberSaveable { mutableStateOf(false) }
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp),
        elevation = CardDefaults.cardElevation(4.dp),
        shape = MaterialTheme.shapes.medium,
        onClick = { visible = !visible }
    ) {
        RowData(title = "Order", value = purchaseOrder.purchaseOrderId)
        RowData(title = "Supplier", value = purchaseOrder.supplier)
        RowData(title = "Date", value = purchaseOrder.formattedDate)
        RowData(title = "Amount", value = purchaseOrder.formattedAmount)
        AnimatedVisibility(visible = visible) {
            purchaseOrder.purchaseOrderItems?.forEach {
                RowData(title = it.product, value = it.quantity.toString())
            }
        }
        Row(
            modifier = modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.End
        ) {
            Button(
                modifier = modifier
                    .wrapContentSize()
                    .padding(bottom = 4.dp, end = 12.dp),
                onClick = { onApprove(purchaseOrder, OrderStatus.REVIEWED) },
                shape = RoundedCornerShape(
                    topStart = 0.dp,
                    topEnd = 0.dp,
                    bottomEnd = 15.dp,
                    bottomStart = 0.dp
                )
            ) {
                Text(text = "Review Order")
            }
        }
    }
}

@Composable
private fun RowData(
    modifier: Modifier = Modifier,
    title: String,
    value: String,
    titleStyle: TextStyle = MaterialTheme.typography.titleMedium.copy(
        color = MaterialTheme.colorScheme.onSurface.copy(
            .6f
        )
    ),
    valueStyle: TextStyle = MaterialTheme.typography.bodyMedium
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = title, style = titleStyle)
        Text(text = value, style = valueStyle)
    }
}