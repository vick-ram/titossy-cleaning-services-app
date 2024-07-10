package com.example.titossycleaningservicesapp.presentation.users.finance.util

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.SentimentVeryDissatisfied
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.titossycleaningservicesapp.R
import com.example.titossycleaningservicesapp.core.CustomProgressIndicator
import com.example.titossycleaningservicesapp.domain.models.PaymentStatus
import com.example.titossycleaningservicesapp.domain.models.ui_models.CustomerPayment
import com.example.titossycleaningservicesapp.domain.viewmodel.PaymentViewModel

@Composable
fun CustomerIncomeScreen(
    modifier: Modifier = Modifier,
    paymentViewModel: PaymentViewModel
) {
    val context = LocalContext.current
    val paymentUiState by paymentViewModel.customerPaymentUIState.collectAsStateWithLifecycle()

    LaunchedEffect(paymentViewModel) {
        paymentViewModel.fetchCustomerPayments()
    }

    when {
        paymentUiState.isLoading -> Box(
            modifier = modifier.fillMaxSize(),
            contentAlignment = Alignment.Center,
            content = {
                CustomProgressIndicator(isLoading = true)
            }
        )

        paymentUiState.customerPayments != null -> {
            Column(
                modifier = modifier
                    .fillMaxSize()
                    .padding(16.dp)
            ) {
                paymentUiState.customerPayments?.let { payments ->
                    LazyColumn(
                        modifier = modifier
                            .fillMaxWidth(),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(
                            items = payments,
                            key = { payment -> payment.paymentId },
                            contentType = { payment -> payment }
                        ) { payment ->
                            CustomerPaymentCard(
                                customerPayment = payment,
                                paymentStatus = PaymentStatus.CONFIRMED,
                                onApprove = {status ->
                                    paymentViewModel.updateCustomerPaymentStatus(
                                        paymentId = payment.paymentId,
                                        status = status.name
                                    )
                                }
                            )
                        }
                    }
                }
            }
        }
        paymentUiState.errorMessage.isNotEmpty() -> {
            Toast.makeText(
                context,
                paymentUiState.errorMessage,
                Toast.LENGTH_SHORT
            ).show()
            Box(
                modifier = modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ){
                Column(
                    modifier = modifier
                ) {
                    Icon(
                        modifier = modifier.size(100.dp),
                        imageVector = Icons.Default.SentimentVeryDissatisfied,
                        contentDescription = null
                    )
                    Spacer(modifier = modifier.height(16.dp))
                    Text(
                        text = "Ooh no..",
                        style = MaterialTheme.typography.bodyLarge
                        )
                }
            }
        }
    }
}

@Composable
fun CustomerPaymentCard(
    customerPayment: CustomerPayment,
    paymentStatus: PaymentStatus,
    onApprove: (PaymentStatus) -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            horizontalAlignment = Alignment.Start
        ) {
            Text(
                text = "Payment ID: ${customerPayment.paymentId}",
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "Booking ID: ${customerPayment.bookingId}",
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp
            )
            Spacer(modifier = Modifier.height(8.dp))

            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Amount: ",
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = customerPayment.totalAmount
                )
            }
            Spacer(modifier = Modifier.height(4.dp))
            if (customerPayment.paymentMethod != null) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Payment Method: ",
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "${customerPayment.paymentMethod}"
                    )
                }
                Spacer(modifier = Modifier.height(4.dp))
            }
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Phone Number: ",
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = customerPayment.phoneNumber
                )
            }
            Spacer(modifier = Modifier.height(4.dp))
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Transaction Code: ",
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = customerPayment.transactionCode
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Status: ",
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.width(4.dp))
                TextButton(onClick = {
                    onApprove(paymentStatus)
                }) {
                    Text(
                        text = customerPayment.paymentStatus.name,
                        color = when (customerPayment.paymentStatus) {
                            PaymentStatus.PENDING -> colorResource(id = R.color.pending)
                            PaymentStatus.CONFIRMED -> colorResource(id = R.color.completed)
                            PaymentStatus.CANCELLED -> colorResource(id = R.color.cancelled)
                            else -> Color.Gray
                        }
                    )
                }
            }
        }
    }
}
