package com.example.titossycleaningservicesapp.presentation.users.customer.utils

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronLeft
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogProperties
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.titossycleaningservicesapp.R
import com.example.titossycleaningservicesapp.core.CustomProgressIndicator
import com.example.titossycleaningservicesapp.core.dateTimeUiFormat
import com.example.titossycleaningservicesapp.domain.models.ui_models.Booking
import com.example.titossycleaningservicesapp.domain.models.ui_models.BookingServiceAddOns
import com.example.titossycleaningservicesapp.domain.viewmodel.BookingViewModel
import com.example.titossycleaningservicesapp.domain.viewmodel.PaymentViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CheckOutScreen(
    bookingId: String,
    navController: NavHostController
) {
    val context = LocalContext.current
    var showDialog by rememberSaveable { mutableStateOf(false) }
    val bookingViewModel: BookingViewModel = hiltViewModel()
    val bookingUiState by bookingViewModel.bookingUiState.collectAsState()
    val booking = bookingUiState.bookings?.find { it.bookingId == bookingId }
    var phone by rememberSaveable { mutableStateOf("") }
    var transactionCode by rememberSaveable { mutableStateOf("") }
    val paymentViewModel: PaymentViewModel = hiltViewModel()
    val customerPaymentUiState by paymentViewModel.customerPaymentUIState.collectAsState()

    LaunchedEffect(bookingId) {
        bookingViewModel.fetchBookings()
    }

    LaunchedEffect(key1 = customerPaymentUiState) {
        when {
            customerPaymentUiState.isLoading -> {}
            customerPaymentUiState.successMessage.isNotEmpty() -> {
                Toast.makeText(
                    context,
                    customerPaymentUiState.successMessage,
                    Toast.LENGTH_LONG
                ).show()
                showDialog = false
                navController.navigate(DetailsRoutes.BookingSuccess.route)
            }
            customerPaymentUiState.errorMessage.isNotEmpty() -> {
                Toast.makeText(
                    context,
                    customerPaymentUiState.errorMessage,
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            modifier = Modifier.size(32.dp),
                            imageVector = Icons.Default.ChevronLeft,
                            contentDescription = null
                        )
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            when {
                bookingUiState.isLoading -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CustomProgressIndicator(isLoading = true)
                    }
                }

                bookingUiState.bookings != null -> {
                    booking?.let { b ->
                        BookingDetailsScreen(
                            booking = b,
                            showDialog = showDialog,
                            onShowDialog = { showDialog = true }
                        )
                    }
                }

                bookingUiState.errorMessage.isNotEmpty() -> {
                    Toast.makeText(
                        context,
                        bookingUiState.errorMessage,
                        Toast.LENGTH_SHORT
                    ).show()
                }

            }
            if (showDialog) {
                TransactionDialog(
                    onConfirm = { phone, code ->
                        paymentViewModel.makePayment(
                            bookingId = bookingId,
                            phoneNumber = phone,
                            transactionCode = code
                        )
                    },
                    onCancel = { showDialog = false },
                    phone = phone,
                    transactionCode = transactionCode,
                    booking = booking,
                    onPhoneChange = { phone = it },
                    onTransactionChange = { transactionCode = it }
                )
            }
        }
    }
}


@Composable
fun DetailRow(label: String, value: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = "$label:",
            style = MaterialTheme.typography.bodyLarge,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodyLarge
        )
    }
}

@Composable
fun BookingServiceAddOnCard(addOn: BookingServiceAddOns) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        shape = RoundedCornerShape(8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Row(
            modifier = Modifier
                .background(MaterialTheme.colorScheme.surface)
                .padding(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = addOn.service ?: "Unknown Service",
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Bold
                )
                addOn.serviceAddOn?.let {
                    Text(
                        text = "Add-on: $it",
                        style = MaterialTheme.typography.bodyLarge
                    )
                }
                Text(
                    text = "Quantity: ${addOn.quantity}",
                    style = MaterialTheme.typography.bodyLarge
                )
                Text(
                    text = "Subtotal: ${addOn.subtotal}",
                    style = MaterialTheme.typography.bodyLarge
                )
            }
        }
    }
}


@Composable
fun BookingDetailsScreen(
    booking: Booking,
    showDialog: Boolean,
    onShowDialog: (Boolean) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
    ) {
        Text(
            text = "Booking Details",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(16.dp))
        DetailRow(
            label = "Booking ID",
            value = booking.bookingId
        )
        DetailRow(
            label = "Customer",
            value = booking.customer
        )
        DetailRow(
            label = "Date & Time",
            value = booking.bookingDateTime.format(dateTimeUiFormat)
        )
        DetailRow(
            label = "Frequency",
            value = booking.frequency.name
        )
        DetailRow(
            label = "Address",
            value = booking.address
        )
        DetailRow(
            label = "Instructions",
            value = booking.additionalInstructions
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "Service Add-ons",
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(8.dp))
        if (booking.bookingServiceAddons.isNotEmpty()) {
            booking.bookingServiceAddons.forEach { addOn ->
                BookingServiceAddOnCard(addOn)
            }
        } else {
            Text(
                text = "No service add-ons",
                style = MaterialTheme.typography.bodyLarge
            )
        }
        Spacer(modifier = Modifier.height(16.dp))
        DetailRow(
            label = "Total Amount",
            value = booking.amount
        )
        DetailRow(
            label = "Paid",
            value = booking.paidString
        )
        DetailRow(
            label = "Status",
            value = booking.bookingStatus.name
        )
        Button(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.CenterHorizontally),
            onClick = { onShowDialog(showDialog) },
            shape = MaterialTheme.shapes.extraSmall,
            contentPadding = PaddingValues(16.dp)
        ) {
            Text(
                text = "Proceed and Pay",
                style = MaterialTheme.typography.bodyLarge
            )
        }
    }
}


@Composable
fun TransactionDialog(
    onConfirm: (String, String) -> Unit,
    onCancel: () -> Unit,
    phone: String,
    transactionCode: String,
    booking: Booking?,
    onPhoneChange: (String) -> Unit,
    onTransactionChange: (String) -> Unit
) {

    AlertDialog(
        onDismissRequest = onCancel,
        title = { Text("Confirm Transaction") },
        text = {
            Column(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(modifier = Modifier.height(12.dp))
                Image(
                    modifier = Modifier.size(100.dp),
                    painter = painterResource(id = R.drawable.mpesa_logo),
                    contentDescription = null,
                    contentScale = ContentScale.Fit
                )
                Spacer(modifier = Modifier.height(8.dp))
                TextField(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    value = phone,
                    onValueChange = { newPhone ->
                        onPhoneChange(newPhone)
                    },
                    placeholder = {
                        Text(
                            text = "0182630465 or 07..",
                            style = MaterialTheme.typography.bodyLarge.copy(
                                color = MaterialTheme.colorScheme.onSurface.copy(
                                    0.5f
                                )
                            )
                        )
                    },
                    singleLine = true,
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = MaterialTheme.colorScheme.surface,
                        unfocusedContainerColor = MaterialTheme.colorScheme.surface,
                        focusedIndicatorColor = MaterialTheme.colorScheme.primary,
                        unfocusedIndicatorColor = MaterialTheme.colorScheme.onSurface.copy(0.2f)
                    )
                )
                Spacer(modifier = Modifier.height(8.dp))
                TextField(
                    value = transactionCode,
                    onValueChange = { newTransactionCode ->
                        onTransactionChange(newTransactionCode.uppercase())
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp)
                        .background(MaterialTheme.colorScheme.background, RoundedCornerShape(8.dp)),
                    placeholder = {
                        Text(
                            text = "GSQRWO63DMA",
                            style = MaterialTheme.typography.bodyLarge.copy(
                                color = MaterialTheme.colorScheme.onSurface.copy(
                                    0.5f
                                )
                            )
                        )
                    },
                    singleLine = true,
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = MaterialTheme.colorScheme.surface,
                        unfocusedContainerColor = MaterialTheme.colorScheme.surface,
                        focusedIndicatorColor = MaterialTheme.colorScheme.primary,
                        unfocusedIndicatorColor = MaterialTheme.colorScheme.onSurface.copy(0.2f)
                    )

                )
                Spacer(modifier = Modifier.height(8.dp))
                TextField(
                    value = booking?.amount ?: "",
                    onValueChange = { },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp)
                        .background(
                            MaterialTheme.colorScheme.background,
                            RoundedCornerShape(8.dp)
                        ),
                    placeholder = {
                        Text(
                            text = "Amount",
                            style = MaterialTheme.typography.bodyLarge.copy(
                                color = MaterialTheme.colorScheme.onSurface.copy(
                                    0.5f
                                )
                            )
                        )
                    },
                    readOnly = true,
                    singleLine = true,
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = MaterialTheme.colorScheme.surface,
                        focusedIndicatorColor = MaterialTheme.colorScheme.primary,
                        unfocusedIndicatorColor = MaterialTheme.colorScheme.onSurface.copy(0.2f)
                    )
                )
            }
        },
        confirmButton = {
            TextButton(
                onClick = {
                    if (phone.isNotEmpty() && transactionCode.isNotEmpty()) {
                        onConfirm(
                            phone,
                            transactionCode.uppercase()
                        )
                    }
                }
            ) {
                Text("Confirm")
            }
        },
        dismissButton = {
            TextButton(onClick = onCancel) {
                Text("Cancel")
            }
        },
        properties = DialogProperties(
            dismissOnBackPress = false,
            dismissOnClickOutside = false
        )
    )
}




