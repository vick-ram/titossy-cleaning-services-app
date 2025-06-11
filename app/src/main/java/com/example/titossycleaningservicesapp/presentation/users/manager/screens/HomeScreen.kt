package com.example.titossycleaningservicesapp.presentation.users.manager.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.LocalMall
import androidx.compose.material.icons.filled.LocalShipping
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.example.titossycleaningservicesapp.domain.models.OrderStatus
import com.example.titossycleaningservicesapp.domain.viewmodel.MetricViewModel
import com.example.titossycleaningservicesapp.domain.viewmodel.PurchaseOrderViewModel
import com.example.titossycleaningservicesapp.domain.viewmodel.SupplierViewModel
import com.example.titossycleaningservicesapp.presentation.ui.theme.AppTheme
import com.example.titossycleaningservicesapp.presentation.users.manager.utils.BookingsTrendCard
import com.example.titossycleaningservicesapp.presentation.users.manager.utils.PaymentsCard
import com.example.titossycleaningservicesapp.presentation.users.manager.utils.SummaryCardsRow
import com.example.titossycleaningservicesapp.presentation.users.manager.utils.TeamStatsCard

@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    paddingValues: PaddingValues,
    supplierViewModel: SupplierViewModel
) {
    val supplierUiState by supplierViewModel.supplierUiState.collectAsStateWithLifecycle()
//    val suppliers = supplierUiState.suppliers?.size ?: 0
    val purchaseOrderViewModel: PurchaseOrderViewModel = hiltViewModel()
    val purchaseOrderUiState by purchaseOrderViewModel.purchaseOrderDataUiState.collectAsStateWithLifecycle()
//    val purchaseOrders =
//        purchaseOrderUiState.purchaseOrders?.filter { it.orderStatus == OrderStatus.PENDING }?.size
//            ?: 0
    val metricViewModel: MetricViewModel = hiltViewModel()
    val metricUiState by metricViewModel.metricUiState.collectAsStateWithLifecycle()
    val error by metricViewModel.errorFlow.collectAsStateWithLifecycle(initialValue = "")

    val metrics = metricUiState.metrics

    val scrollState = rememberScrollState()

    LaunchedEffect(metricUiState, metricViewModel) {
        metricViewModel.fetchMetrics()
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(paddingValues)
    ) {

        if (metrics != null) {
            SummaryCardsRow(data = metrics)
            BookingsTrendCard(bookings = metrics.bookingMetrics)
            PaymentsCard(payments = metrics.paymentMetrics)
            TeamStatsCard(users = metrics.userMetrics)
        }


        if (metricUiState.loading == true) {
            Box(
                modifier = modifier.weight(1f)
                    .align(Alignment.CenterHorizontally),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        }

        if (error.isNotEmpty()) {
            Box(modifier = modifier.weight(1f)
                .align(Alignment.CenterHorizontally),
                contentAlignment = Alignment.Center
            ) {
                Text(text = error)
            }
        }
    }
}

