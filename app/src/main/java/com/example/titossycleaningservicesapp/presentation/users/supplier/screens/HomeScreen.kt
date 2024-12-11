package com.example.titossycleaningservicesapp.presentation.users.supplier.screens

import android.graphics.drawable.Icon
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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Recycling
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.example.titossycleaningservicesapp.core.CustomProgressIndicator
import com.example.titossycleaningservicesapp.core.SmallSearchField
import com.example.titossycleaningservicesapp.core.showToast
import com.example.titossycleaningservicesapp.core.statusToColor
import com.example.titossycleaningservicesapp.domain.models.OrderStatus
import com.example.titossycleaningservicesapp.domain.models.ui_models.PurchaseOrder
import com.example.titossycleaningservicesapp.domain.viewmodel.PurchaseOrderViewModel
import kotlinx.coroutines.delay

@Composable
fun HomeScreen(
    navController: NavHostController,
    paddingValues: PaddingValues,
) {
    val context = LocalContext.current
    val purchaseOrderViewModel: PurchaseOrderViewModel = hiltViewModel()
    val purchaseOrderUiState by purchaseOrderViewModel.purchaseOrderDataUiState.collectAsStateWithLifecycle()
    var search by rememberSaveable { mutableStateOf("") }
    val purchaseOrderStatus by purchaseOrderViewModel.purchaseOrderStatus.collectAsStateWithLifecycle()


    LaunchedEffect(purchaseOrderViewModel) {
        purchaseOrderViewModel.fetchPurchaseOrders()
    }

    LaunchedEffect(key1 = purchaseOrderStatus) {
        when {
            purchaseOrderStatus.isLoading -> delay(100L)
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
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues)
            .padding(horizontal = 16.dp)
    ) {

        SmallSearchField(
            modifier = Modifier
                .align(Alignment.CenterHorizontally),
            value = search,
            onValueChange = { search = it },
            width = 1f,
            shape = MaterialTheme.shapes.extraLarge,
            height = 56.dp,
            iconSize = 32.dp
        )
        Spacer(modifier = Modifier.height(8.dp))
        when {
            purchaseOrderUiState.isLoading -> Box(
                modifier = Modifier
                    .fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CustomProgressIndicator(isLoading = true)
            }

            purchaseOrderUiState.purchaseOrders != null -> {
                val filteredOrders = purchaseOrderUiState.purchaseOrders?.filter {
                    it.orderStatus.name.contains(
                        search,
                        ignoreCase = true
                    )
                }
                if (filteredOrders.isNullOrEmpty()) {
                    EmptyHomeScreen()
                } else {
                    LazyColumn(
                        modifier = Modifier.fillMaxWidth(),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(filteredOrders) { purchaseOrder ->
                            PurchaseOrderCard(
                                purchaseOrder = purchaseOrder,
                                navController = navController,
                            )
                        }
                    }
                }
    /*            purchaseOrderUiState.purchaseOrders?.filter {
                    it.orderStatus.name.contains(
                        search,
                        ignoreCase = true
                    )
                }?.let { purchaseOrders ->
                    LazyColumn(
                        modifier = Modifier.fillMaxWidth(),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(purchaseOrders) { purchaseOrder ->
                            PurchaseOrderCard(
                                purchaseOrder = purchaseOrder,
                                navController = navController,
                            )
                        }
                    }
                }*/
            }

            purchaseOrderUiState.errorMessage.isNotEmpty() -> {
                showToast(
                    context = context,
                    message = purchaseOrderUiState.errorMessage
                )
                Column(
                    modifier = Modifier
                        .fillMaxSize(),
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = "Oops... something went wrong",
                        style = MaterialTheme.typography.bodyLarge
                    )
                }
            }

            else -> {
                EmptyHomeScreen()
            }
        }
    }
}

@Composable
fun PurchaseOrderCard(
    purchaseOrder: PurchaseOrder,
    navController: NavHostController
) {
    val buttonText =
        if (purchaseOrder.orderStatus == OrderStatus.COMPLETED) "Print Receipt" else "View Details"
    val navigateTo =
        if (purchaseOrder.orderStatus == OrderStatus.COMPLETED) "receiptScreen/${purchaseOrder.purchaseOrderId}" else "purchaseOrderDetails/${purchaseOrder.purchaseOrderId}"
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "Purchase Order #${purchaseOrder.purchaseOrderId}",
                    style = MaterialTheme.typography.titleMedium.copy(
                        color = MaterialTheme.colorScheme.onSurface
                    ),
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Supplier: ${purchaseOrder.supplier}",
                    style = MaterialTheme.typography.bodyMedium
                )
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Status: ",
                        style = MaterialTheme.typography.bodyMedium
                    )
                    Text(
                        text = purchaseOrder.orderStatus.name,
                        color = statusToColor(purchaseOrder.orderStatus),
                        style = MaterialTheme.typography.bodyMedium.copy(
                            fontWeight = FontWeight.Bold
                        )
                    )

                }
            }
            OutlinedButton(
                onClick = { navController.navigate(navigateTo) },
                modifier = Modifier.wrapContentSize()
            ) {
                Text(text = buttonText)
            }
        }
    }
}

@Composable
fun EmptyHomeScreen(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            modifier = modifier.size(64.dp),
            imageVector = Icons.Outlined.Recycling,
            contentDescription = null
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "No purchase orders available",
            style = MaterialTheme.typography.bodyMedium
        )
    }
}


