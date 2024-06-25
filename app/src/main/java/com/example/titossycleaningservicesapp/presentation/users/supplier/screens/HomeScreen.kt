package com.example.titossycleaningservicesapp.presentation.users.supplier.screens

import android.widget.Toast
import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.example.titossycleaningservicesapp.core.CustomProgressIndicator
import com.example.titossycleaningservicesapp.domain.models.OrderStatus
import com.example.titossycleaningservicesapp.domain.models.ui_models.PurchaseOrder
import com.example.titossycleaningservicesapp.domain.viewmodel.PurchaseOrderViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun HomeScreen(
    navController: NavHostController,
    paddingValues: PaddingValues,
    pagerState: PagerState
) {
    val context = LocalContext.current
    val purchaseOrderViewModel: PurchaseOrderViewModel = hiltViewModel()
    val purchaseOrderUiState by purchaseOrderViewModel.purchaseOrderDataUiState.collectAsStateWithLifecycle()

    LaunchedEffect(purchaseOrderViewModel) {
        purchaseOrderViewModel.fetchPurchaseOrders()
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(paddingValues)
            .padding(horizontal = 16.dp)
    ) {

        val tabTitles = listOf("ALL", "DELIVERED")
        val scope = rememberCoroutineScope()

        TabRow(selectedTabIndex = pagerState.currentPage) {
            tabTitles.forEachIndexed { index, title ->
                Tab(
                    text = {
                        Text(
                            text = title,
                            style = MaterialTheme.typography.titleSmall.copy(
                                color = if (pagerState.currentPage == index) {
                                    MaterialTheme.colorScheme.primary
                                } else {
                                    MaterialTheme.colorScheme.onSurface
                                },
                                fontWeight = FontWeight.Bold
                            )
                        )
                    },
                    selected = pagerState.currentPage == index,
                    onClick = {
                        scope.launch {
                            pagerState.animateScrollToPage(
                                page = index,
                                animationSpec = tween(2000)
                            )
                        }
                    }
                )
            }
        }
        when {
            purchaseOrderUiState.isLoading -> Box(
                modifier = Modifier
                    .fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CustomProgressIndicator(isLoading = true)
            }

            purchaseOrderUiState.purchaseOrders != null -> {
                HorizontalPager(
                    state = pagerState,
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth()
                ) { page ->
                    when (page) {
                        0 -> AllPurchaseOrdersContent(
                            purchaseOrders = purchaseOrderUiState.purchaseOrders!!,
                            onPurchaseOrderItemClick = {
                                navController.navigate("purchaseOrderDetails" + "/" + it.purchaseOrderId)
                            },
                            onStatusClick = {
                                purchaseOrderViewModel.updateOrderStatus(
                                    id = it.purchaseOrderId,
                                    status = OrderStatus.PROCESSING.name
                                )
                            }
                        )

                        1 -> CompletedOrdersContent(
                            purchaseOrders = purchaseOrderUiState.purchaseOrders!!.filter {
                                it.orderStatus == OrderStatus.DELIVERED
                            },
                            onPurchaseOrderItemClick = {},
                            onStatusClick = {}
                        )
                    }
                }
            }

            purchaseOrderUiState.errorMessage.isNotEmpty() -> {
                Toast.makeText(
                    context,
                    purchaseOrderUiState.errorMessage,
                    Toast.LENGTH_SHORT
                ).show()
            }

        }
    }
}

@Composable
fun CompletedOrdersContent(
    modifier: Modifier = Modifier,
    purchaseOrders: List<PurchaseOrder>,
    onPurchaseOrderItemClick: (PurchaseOrder) -> Unit,
    onStatusClick: (PurchaseOrder) -> Unit
) {
    Column(
        modifier = modifier
            .fillMaxSize()
    ) {
        LazyColumn(
            modifier = modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
        ) {
            items(purchaseOrders) { purchaseOrder ->
                PurchaseOrderCard(
                    purchaseOrder = purchaseOrder,
                    onDetailsClick = onPurchaseOrderItemClick,
                    onStatusClick = onStatusClick
                )
            }
        }
    }
}

@Composable
fun AllPurchaseOrdersContent(
    modifier: Modifier = Modifier,
    purchaseOrders: List<PurchaseOrder>,
    onPurchaseOrderItemClick: (PurchaseOrder) -> Unit,
    onStatusClick: (PurchaseOrder) -> Unit
) {
    Column(
        modifier = modifier
            .fillMaxSize()
    ) {
        LazyColumn {
            items(purchaseOrders) { purchaseOrder ->
                PurchaseOrderCard(
                    purchaseOrder = purchaseOrder,
                    onDetailsClick = onPurchaseOrderItemClick,
                    onStatusClick = onStatusClick
                )
            }
        }
    }
}


@Composable
fun PurchaseOrderCard(
    purchaseOrder: PurchaseOrder,
    onDetailsClick: (PurchaseOrder) -> Unit,
    onStatusClick: (PurchaseOrder) -> Unit
) {
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
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Supplier: ${purchaseOrder.supplier}",
                    style = MaterialTheme.typography.bodyLarge
                )
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Status: ",
                        style = MaterialTheme.typography.bodyLarge
                    )
                    TextButton(
                        onClick = { onStatusClick(purchaseOrder) }
                    ) {
                        Text(
                            text = purchaseOrder.orderStatus.name,
                            color = when (purchaseOrder.orderStatus) {
                                OrderStatus.PENDING -> Color(0xFFFFEB3B)
                                OrderStatus.PROCESSING -> Color(0xFF4CAF50)
                                OrderStatus.SHIPPED -> Color(0xFF2196F3)
                                OrderStatus.DELIVERED -> Color(0xFF4CAF50)
                                OrderStatus.CANCELLED -> Color(0xFFF44336)
                            },
                            style = MaterialTheme.typography.bodyLarge
                        )
                    }

                }
            }
            IconButton(
                onClick = { onDetailsClick(purchaseOrder) }
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                    contentDescription = "Details",
                    modifier = Modifier.size(24.dp)
                )
            }
        }
    }
}




