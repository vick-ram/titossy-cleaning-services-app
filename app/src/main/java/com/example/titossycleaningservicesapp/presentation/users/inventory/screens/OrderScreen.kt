package com.example.titossycleaningservicesapp.presentation.users.inventory.screens

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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Recycling
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.example.titossycleaningservicesapp.core.CustomProgressIndicator
import com.example.titossycleaningservicesapp.core.showToast
import com.example.titossycleaningservicesapp.core.statusToColor
import com.example.titossycleaningservicesapp.domain.models.ui_models.PurchaseOrder
import com.example.titossycleaningservicesapp.domain.viewmodel.PurchaseOrderViewModel

@Composable
fun OrderScreen(
    navController: NavHostController,
    modifier: Modifier = Modifier,
    paddingValues: PaddingValues
) {
    val context = LocalContext.current
    val purchaseOrderViewModel: PurchaseOrderViewModel = hiltViewModel()
    val purchaseOrderUiState by purchaseOrderViewModel.purchaseOrderDataUiState.collectAsStateWithLifecycle()

    LaunchedEffect(key1 = purchaseOrderViewModel) {
        purchaseOrderViewModel.fetchPurchaseOrders()
    }
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(paddingValues)
    ) {
        when {
            purchaseOrderUiState.isLoading -> Box(
                modifier = modifier.fillMaxSize(),
                contentAlignment = Alignment.Center,
                content = { CustomProgressIndicator(isLoading = true)}
            )
            purchaseOrderUiState.purchaseOrders != null -> {
                purchaseOrderUiState.purchaseOrders?.let { purchaseOrders ->
                    LazyColumn(
                        modifier = modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(purchaseOrders) { purchaseOrder ->
                            PurchaseOrderItemCard(
                                purchaseOrder = purchaseOrder,
                                onViewDetails = { purchaseOrderId ->
                                    navController.navigate("InventoryPurchaseOrderDetails/$purchaseOrderId")
                                }
                            )
                        }
                    }
                }
            }
            purchaseOrderUiState.errorMessage.isNotEmpty() -> {
                showToast(
                    context = context,
                    message = purchaseOrderUiState.errorMessage
                )
                Column(
                    modifier = modifier
                        .fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Icon(
                        modifier = modifier.size(60.dp),
                        imageVector = Icons.Default.Recycling,
                        contentDescription = null
                    )
                    Text(
                        text = "An error occurred",
                        style = MaterialTheme.typography.bodyLarge,
                        modifier = modifier.padding(top = 8.dp)
                    )
                }
            }
        }

    }
}

@Composable
fun PurchaseOrderItemCard(
    modifier: Modifier = Modifier,
    purchaseOrder: PurchaseOrder,
    onViewDetails: (String) -> Unit
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(.5.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface,
            contentColor = MaterialTheme.colorScheme.onSurface
        ),
        shape = MaterialTheme.shapes.medium
    ) {
        Row(
            modifier = modifier
                .fillMaxWidth()
                .padding(top = 16.dp, start = 16.dp, end = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "Order ID:",
                style = MaterialTheme.typography.bodyMedium.copy(
                    color = MaterialTheme.colorScheme.onSurface.copy(.6f)
                )
            )
            Text(
                text = purchaseOrder.purchaseOrderId,
                style = MaterialTheme.typography.bodyMedium.copy(
                    color = MaterialTheme.colorScheme.onSurface,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.End
                )
            )
        }
        Row(
            modifier = modifier
                .fillMaxWidth()
                .padding(top = 4.dp, start = 16.dp, end = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "Employee:",
                style = MaterialTheme.typography.bodyMedium.copy(
                    color = MaterialTheme.colorScheme.onSurface.copy(.6f)
                )
            )
            Text(
                text = purchaseOrder.employee,
                style = MaterialTheme.typography.bodyMedium.copy(
                    color = MaterialTheme.colorScheme.onSurface,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.End
                )
            )
        }

        Row(
            modifier = modifier
                .fillMaxWidth()
                .padding(top = 4.dp, start = 16.dp, end = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "Supplier:",
                style = MaterialTheme.typography.bodyMedium.copy(
                    color = MaterialTheme.colorScheme.onSurface.copy(.6f)
                )
            )
            Text(
                text = purchaseOrder.supplier,
                style = MaterialTheme.typography.bodyMedium.copy(
                    color = MaterialTheme.colorScheme.onSurface,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.End
                )
            )
        }

        Row(
            modifier = modifier
                .fillMaxWidth()
                .padding(top = 4.dp, start = 16.dp, end = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "Order Status:",
                style = MaterialTheme.typography.bodyMedium.copy(
                    color = MaterialTheme.colorScheme.onSurface.copy(.6f)
                )
            )
            Text(
                text = purchaseOrder.orderStatus.name,
                style = MaterialTheme.typography.bodyMedium.copy(
                    color = statusToColor(purchaseOrder.orderStatus),
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.End
                )
            )
        }
        Row(
            modifier = modifier
                .fillMaxWidth()
                .padding(top = 4.dp, bottom = 16.dp, end = 16.dp),
            horizontalArrangement = Arrangement.End,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Button(
                modifier = modifier.wrapContentSize(),
                onClick = { onViewDetails(purchaseOrder.purchaseOrderId) }
            ) {
                Text(text = "View Details")
            }
        }
    }
}

