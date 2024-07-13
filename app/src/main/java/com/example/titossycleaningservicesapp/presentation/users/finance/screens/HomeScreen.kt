@file:OptIn(ExperimentalFoundationApi::class)

package com.example.titossycleaningservicesapp.presentation.users.finance.screens

import android.icu.text.DecimalFormat
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDownward
import androidx.compose.material.icons.filled.ArrowUpward
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.example.titossycleaningservicesapp.core.CustomProgressIndicator
import com.example.titossycleaningservicesapp.core.showToast
import com.example.titossycleaningservicesapp.domain.models.PaymentMethod
import com.example.titossycleaningservicesapp.domain.viewmodel.PaymentViewModel
import kotlinx.coroutines.launch
import java.math.BigDecimal

@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    paymentViewModel: PaymentViewModel,
    paddingValues: PaddingValues
){
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val pages = listOf("Income", "OutCome")
    val customerPaymentUIState by paymentViewModel.customerPaymentUIState.collectAsStateWithLifecycle()
    val supplierPaymentUIState by paymentViewModel.supplierPaymentUiState.collectAsStateWithLifecycle()
    val pagerState = rememberPagerState(initialPage = 0, pageCount = { pages.size })

    val earnings = customerPaymentUIState.customerPayments?.sumOf { it.amount } ?: BigDecimal.ZERO
    val expenditure = supplierPaymentUIState.supplierPayments?.sumOf { it.amount } ?: BigDecimal.ZERO

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(paddingValues)
            .padding(horizontal = 16.dp)
    ) {
        OverviewSection(earnings = earnings, expenditure = expenditure)
        Spacer(modifier = modifier.height(8.dp))
        TabRow(selectedTabIndex = pagerState.currentPage, modifier = modifier.fillMaxWidth()) {
            pages.forEachIndexed { index, title ->
                Tab(
                    selected = pagerState.currentPage == index,
                    onClick = {
                        scope.launch {
                            pagerState.animateScrollToPage(index)
                        }
                    },
                    text = { Text(title) }
                )
            }
        }
        HorizontalPager(
            state = pagerState,
            modifier = modifier.weight(1f)
        ) {page ->
            when(page) {
                0 -> {
                    when {
                        customerPaymentUIState.isLoading -> Box(
                            modifier = modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center,
                            content = { CustomProgressIndicator(isLoading = true)}
                        )
                        customerPaymentUIState.customerPayments != null -> {
                            customerPaymentUIState.customerPayments?.let { customerPayments ->
                                LazyColumn(
                                    modifier = modifier
                                        .fillMaxWidth()
                                        .padding(top = 4.dp),
                                    verticalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    items(customerPayments) {customerPayment ->
                                        PaymentCard(
                                            icon = Icons.Default.ArrowUpward,
                                            iconColor = MaterialTheme.colorScheme.primary,
                                            itemName = "Booking",
                                            itemId = customerPayment.bookingId,
                                            amount = customerPayment.amount
                                        )
                                    }
                                }
                            }
                        }
                        customerPaymentUIState.errorMessage.isNotEmpty() -> showToast(
                            context = context,
                            message = customerPaymentUIState.errorMessage
                        )
                    }
                }
                1 -> {
                    when {
                        supplierPaymentUIState.isLoading -> Box(
                            modifier = modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center,
                            content = { CustomProgressIndicator(isLoading = true)}
                        )
                        supplierPaymentUIState.supplierPayments != null -> {
                            supplierPaymentUIState.supplierPayments?.let { supplierPayments ->
                                LazyColumn(
                                    modifier = modifier
                                        .fillMaxWidth()
                                        .padding(top = 4.dp)
                                ) {
                                    items(supplierPayments) {supplierPayment ->
                                        PaymentCard(
                                            icon = Icons.Default.ArrowDownward,
                                            iconColor = MaterialTheme.colorScheme.error,
                                            itemName = "Purchase Order",
                                            itemId = supplierPayment.orderId,
                                            amount = supplierPayment.amount
                                        )
                                    }
                                }
                            }
                        }
                        supplierPaymentUIState.errorMessage.isNotEmpty() -> showToast(
                            context = context,
                            message = customerPaymentUIState.errorMessage
                        )
                    }
                }
            }
        }
    }
}


@Composable
fun OverviewSection(
    modifier: Modifier = Modifier,
    earnings: BigDecimal,
    expenditure: BigDecimal
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFFFFCC80),
            contentColor = Color(0xFF424242)
        )
    ) {
        Column(
            modifier = modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
        ) {
            Text(
                text = "Revenue Overview",
                style = MaterialTheme.typography.headlineSmall
            )
            Spacer(modifier = modifier.height(8.dp))
            Row(
                modifier = modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column(
                    modifier = modifier
                        .weight(1f)
                ) {
                    Text(
                        text = "Earnings",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                        )
                    )
                    Text(
                        text = "Kshs. ${DecimalFormat("#,###.00").format(earnings)}",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                }
                Column(
                    modifier = modifier
                        .weight(1f),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Expenditure",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                        )
                    )
                    Text(
                        text = "Kshs. ${DecimalFormat("#,###.00").format(expenditure)}",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}

@Composable
private fun PaymentCard(
    modifier: Modifier = Modifier,
    labelStyle: TextStyle = MaterialTheme.typography.titleMedium.copy(
        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
    ),
    valueStyle: TextStyle = MaterialTheme.typography.titleMedium,
    icon: ImageVector,
    iconColor: Color,
    itemName: String,
    itemId: String,
    amount: BigDecimal
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .height(IntrinsicSize.Max),
        shape = MaterialTheme.shapes.medium
    ) {
        Row(
            modifier = modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(
                modifier = modifier
                    .width(IntrinsicSize.Max)
            ) {
                Text(
                    text = itemName,
                    style = labelStyle
                )
                Text(
                    text = itemId,
                    style = valueStyle
                )
            }

            Row(
                modifier = modifier
                    .width(IntrinsicSize.Max)
            ) {
                Column(
                    modifier = modifier
                        .width(IntrinsicSize.Max)
                ) {
                    Text(
                        text = "Amount",
                        style = labelStyle
                    )
                    Text(
                        text = amount.toPlainString(),
                        style = valueStyle
                    )
                }
                Spacer(modifier = modifier.width(8.dp))
                Icon(
                    modifier = modifier
                        .size(width = 24.dp, height = 48.dp),
                    imageVector = icon,
                    contentDescription = null,
                    tint = iconColor
                )
            }
        }
    }
}

@Composable
private fun cardColor(paymentType: PaymentMethod) : Color {
    return when(paymentType) {
        PaymentMethod.CASH -> Color(0xFF4CAF50)
        PaymentMethod.CARD -> Color(0xFFBDBDBD)
        else -> Color(0xFFE57373)
    }
}

