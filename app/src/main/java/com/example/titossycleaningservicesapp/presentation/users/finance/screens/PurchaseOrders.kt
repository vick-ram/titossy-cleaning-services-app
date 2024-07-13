package com.example.titossycleaningservicesapp.presentation.users.finance.screens

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.titossycleaningservicesapp.core.CustomProgressIndicator
import com.example.titossycleaningservicesapp.core.CustomSearchField
import com.example.titossycleaningservicesapp.core.showToast
import com.example.titossycleaningservicesapp.core.statusToColor
import com.example.titossycleaningservicesapp.domain.models.OrderStatus
import com.example.titossycleaningservicesapp.domain.models.PaymentMethod
import com.example.titossycleaningservicesapp.domain.models.ui_models.PurchaseOrder
import com.example.titossycleaningservicesapp.domain.viewmodel.PaymentViewModel
import com.example.titossycleaningservicesapp.domain.viewmodel.PurchaseOrderViewModel

@Composable
fun PurchaseOrdersScreen(
    modifier: Modifier = Modifier,
    paddingValues: PaddingValues
) {
    val context = LocalContext.current
    val purchaseOrderViewModel: PurchaseOrderViewModel = hiltViewModel()
    val purchaseOrderUiState by purchaseOrderViewModel.purchaseOrderDataUiState.collectAsStateWithLifecycle()
    val paymentViewModel: PaymentViewModel = hiltViewModel()
    val paymentStatusUiState by paymentViewModel.supplierPaymentStatusUiState.collectAsStateWithLifecycle()
    var query by rememberSaveable { mutableStateOf("") }

    LaunchedEffect(purchaseOrderViewModel) {
        purchaseOrderViewModel.fetchPurchaseOrders()
    }
    
    LaunchedEffect(key1 = paymentStatusUiState) {
        when {
            paymentStatusUiState.isLoading -> return@LaunchedEffect
            paymentStatusUiState.successMessage.isNotEmpty() -> {
                showToast(
                    context = context,
                    length = Toast.LENGTH_LONG,
                    message = paymentStatusUiState.successMessage
                )
            }
            paymentStatusUiState.errorMessage.isNotEmpty() -> {
                showToast(
                    context = context,
                    message = paymentStatusUiState.errorMessage
                )
            }
        }
    }
    
    when {
        purchaseOrderUiState.isLoading -> {
            Box(
                modifier = modifier.fillMaxSize(),
                contentAlignment = Alignment.Center,
                content = { CustomProgressIndicator(isLoading = true)}
            )
        }
        purchaseOrderUiState.purchaseOrders != null -> {
            Column(
                modifier = modifier
                    .fillMaxSize()
                    .padding(paddingValues)
            ) {
                CustomSearchField(
                    modifier = modifier
                        .align(Alignment.CenterHorizontally)
                        .padding(bottom = 8.dp),
                    value = query,
                    onValueChange = { query = it }
                )
                LazyColumn(
                    modifier = modifier
                        .fillMaxWidth()
                ) {
                    purchaseOrderUiState.purchaseOrders?.filter {
                        it.orderStatus == OrderStatus.RECEIVED
                                || it.orderStatus.name.contains(query, ignoreCase = true)
                                || it.supplier.contains(query, ignoreCase = true)
                                || it.purchaseOrderId.contains(query, ignoreCase = true)
                    }?.let { purchaseOrders ->
                        items(purchaseOrders) {purchaseOrder ->
                            PurchaseOrderCard(
                                purchaseOrder = purchaseOrder,
                                onClick = { order, method ->
                                    paymentViewModel.paySupplier(
                                        orderId = order.purchaseOrderId,
                                        method = method.name
                                    )
                                },
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
private fun PurchaseOrderCard(
    modifier: Modifier = Modifier,
    purchaseOrder: PurchaseOrder,
    onClick: (PurchaseOrder, PaymentMethod) -> Unit,
    onApprove: (PurchaseOrder,OrderStatus) -> Unit
) {
    val buttonText = when (purchaseOrder.orderStatus) {
        OrderStatus.REVIEWED -> "Approve"
        OrderStatus.RECEIVED -> "Pay"
        else -> ""
    }
    val buttonAction: () -> Unit = when(purchaseOrder.orderStatus) {
        OrderStatus.REVIEWED -> {
            { onApprove(purchaseOrder,OrderStatus.APPROVED) }}
        OrderStatus.RECEIVED -> {
            { onClick(purchaseOrder, PaymentMethod.CARD) } }
        else -> { {} }
    }
    Card(
        modifier = modifier.padding(16.dp),
        shape = MaterialTheme.shapes.medium,
        elevation = CardDefaults.cardElevation(0.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainer,
            contentColor = MaterialTheme.colorScheme.onSurface
        )
    ) {
        RowData(
            modifier = modifier.padding(top = 16.dp),
            label = "Purchase Order",
            value = purchaseOrder.purchaseOrderId
        )
        RowData(
            modifier = modifier.padding(top = 8.dp),
            label = "Supplier",
            value = purchaseOrder.supplier
        )
        RowData(
            modifier = modifier.padding(bottom = 16.dp, top = 8.dp),
            label = "Amount",
            value = purchaseOrder.totalAmount.toPlainString()
        )
        RowData(
            modifier = modifier.padding(bottom = 16.dp, top = 8.dp),
            label = "Status",
            value = purchaseOrder.orderStatus.name,
            valueColor = statusToColor(purchaseOrder.orderStatus)
        )
        Row(
            modifier = modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.End
        ) {
            if (buttonText.isNotEmpty()) {
                Button(
                    modifier = modifier
                        .wrapContentSize()
                        .padding(bottom = 4.dp, end = 12.dp),
                    onClick = buttonAction,
                    shape = RoundedCornerShape(
                        topStart = 0.dp,
                        topEnd = 0.dp,
                        bottomEnd = 15.dp,
                        bottomStart = 0.dp
                    )
                ) {
                    Text(
                        text = buttonText
                    )
                }
            }
        }
    }
}

@Composable
private fun RowData(
    modifier: Modifier = Modifier,
    label: String,
    value: String,
    valueColor: Color = MaterialTheme.colorScheme.onSurface
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodyMedium.copy(
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
            )
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodyMedium.copy(
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.End,
                color = valueColor
            )
        )
    }
}