package com.example.titossycleaningservicesapp.presentation.users.finance.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Insights
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.example.titossycleaningservicesapp.domain.viewmodel.PaymentViewModel

@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    paymentViewModel: PaymentViewModel,
    paddingValues: PaddingValues
){
    val paymentUIState by paymentViewModel.customerPaymentUIState.collectAsStateWithLifecycle()
    val incoming = paymentUIState.customerPayments?.firstOrNull()?.totalAmount

    LaunchedEffect(paymentViewModel) {
        paymentViewModel.fetchCustomerPayments()
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(paddingValues)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        UiCard(
            metricIcon = Icons.Default.Insights,
            count = "20",
            name = "Net Payments"
        )

        UiCard(
            modifier = modifier
                .clickable { navController.navigate("CustomerPayment") },
            metricIcon = Icons.Default.Insights,
            count = "$incoming",
            name = "Income"
        )
        UiCard(
            metricIcon = Icons.Default.Insights,
            count = "1,500.00",
            name = "Outcome"
        )
    }
}

@Composable
fun UiCard(
    modifier: Modifier = Modifier,
    metricIcon: ImageVector,
    count: String,
    name: String
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .height(IntrinsicSize.Max),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface,
            contentColor = MaterialTheme.colorScheme.onSurface
        ),
        shape = MaterialTheme.shapes.small,
        elevation = CardDefaults.cardElevation(
            defaultElevation = 0.6.dp,
            pressedElevation = 4.dp
        )
    ) {
        Row(
            modifier = modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,

        ) {
            Column(
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(
                    text = count,
                    style = MaterialTheme.typography.headlineMedium
                )
                Text(
                    text = name,
                    style = MaterialTheme.typography.headlineMedium
                )
            }
            Icon(
                modifier = modifier.size(width = 200.dp, height = 100.dp),
                imageVector = metricIcon,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary
            )
        }
    }
}

