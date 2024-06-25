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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ChevronLeft
import androidx.compose.material.icons.outlined.MoreVert
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
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
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.titossycleaningservicesapp.R
import com.example.titossycleaningservicesapp.core.CustomProgressIndicator
import com.example.titossycleaningservicesapp.core.dateTimeUiFormat
import com.example.titossycleaningservicesapp.domain.models.ui_models.Booking
import com.example.titossycleaningservicesapp.domain.models.ui_models.BookingServiceAddOns
import com.example.titossycleaningservicesapp.domain.viewmodel.BookingViewModel
import com.example.titossycleaningservicesapp.domain.viewmodel.CustomerPaymentViewModel
import com.example.titossycleaningservicesapp.presentation.utils.NavigationIcon

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
    val paymentViewModel: CustomerPaymentViewModel = hiltViewModel()

    LaunchedEffect(bookingId) {
        bookingViewModel.fetchBookings()
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { },
                navigationIcon = {
                    NavigationIcon(
                        icon = Icons.Outlined.ChevronLeft,
                        onClick = { navController.popBackStack() }
                    )
                },
                actions = {
                    NavigationIcon(
                        icon = Icons.Outlined.MoreVert,
                        onClick = {}
                    )
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
                        showDialog = false
                        navController.navigate(DetailsRoutes.BookingSuccess.route)
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
    var transactionCodeError by rememberSaveable { mutableStateOf(false) }
    var phoneError by rememberSaveable { mutableStateOf(false) }

    AlertDialog(
        onDismissRequest = onCancel,
        title = { Text("Confirm Transaction") },
        text = {
            PaymentCard(
                phone = phone,
                transactionCode = transactionCode,
                amount = booking?.amount ?: "",
                onPhoneChange = {newPhone ->
                    phoneError = !(newPhone.startsWith("01") || newPhone.startsWith("07")) || newPhone.length != 10
                    if (!phoneError) {
                        onPhoneChange(newPhone)
                    }
                },
                onTransactionChange = { newTransactionCode ->
                    transactionCodeError = !(newTransactionCode.any { it.isDigit() } && newTransactionCode.length == 10)
                    if (!transactionCodeError) {
                        onTransactionChange(newTransactionCode.uppercase())
                    }

                },
                phoneError = phoneError,
                transactionError = transactionCodeError
            )
        },
        confirmButton = {
            TextButton(
                onClick = {
                    if (!phoneError && !transactionCodeError) {
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
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PaymentCard(
    modifier: Modifier = Modifier,
    phone: String,
    transactionCode: String,
    amount: String,
    onPhoneChange: (String) -> Unit,
    onTransactionChange: (String) -> Unit,
    phoneError: Boolean,
    transactionError: Boolean
) {
    Card(
        modifier = modifier
            .padding(vertical = 16.dp)
            .fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface,
            contentColor = MaterialTheme.colorScheme.onSurface
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 8.dp
        ),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(12.dp))
            Image(
                painter = painterResource(id = R.drawable.mpesa_logo),
                contentDescription = null,
                contentScale = ContentScale.Fit
            )
            Spacer(modifier = Modifier.height(8.dp))
            TextField(
                value = phone,
                onValueChange = onPhoneChange,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
                    .background(MaterialTheme.colorScheme.background, RoundedCornerShape(8.dp)),
                placeholder = {
                    Text(
                        text = "Phone",
                        style = MaterialTheme.typography.bodyLarge.copy(
                            color = MaterialTheme.colorScheme.onSurface.copy(
                                0.5f
                            )
                        )
                    )
                },
                singleLine = true,
                colors = TextFieldDefaults.textFieldColors(
                    containerColor = MaterialTheme.colorScheme.surface,
                    focusedIndicatorColor = MaterialTheme.colorScheme.primary,
                    unfocusedIndicatorColor = MaterialTheme.colorScheme.onSurface.copy(0.2f)
                ),
                isError = phoneError,
                supportingText = {
                    Text(
                        text = "Phone must begin with 01 or 07",
                        color = MaterialTheme.colorScheme.error,
                        fontSize = 12.sp
                    )
                }
            )
            Spacer(modifier = Modifier.height(8.dp))
            TextField(
                value = transactionCode,
                onValueChange = {
                    if (it.length <= 10) {
                        onTransactionChange(it.uppercase())
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
                    .background(MaterialTheme.colorScheme.background, RoundedCornerShape(8.dp)),
                placeholder = {
                    Text(
                        text = "Transaction Code",
                        style = MaterialTheme.typography.bodyLarge.copy(
                            color = MaterialTheme.colorScheme.onSurface.copy(
                                0.5f
                            )
                        )
                    )
                },
                singleLine = true,
                colors = TextFieldDefaults.textFieldColors(
                    containerColor = MaterialTheme.colorScheme.surface,
                    focusedIndicatorColor = MaterialTheme.colorScheme.primary,
                    unfocusedIndicatorColor = MaterialTheme.colorScheme.onSurface.copy(0.2f)
                ),
                isError = transactionError,
                supportingText = {
                    Text(
                        text = "Transaction code must contain at least one digit",
                        color = MaterialTheme.colorScheme.error,
                        fontSize = 12.sp
                    )
                }

                )
            Spacer(modifier = Modifier.height(8.dp))
            TextField(
                value = amount,
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
                colors = TextFieldDefaults.textFieldColors(
                    containerColor = MaterialTheme.colorScheme.surface,
                    focusedIndicatorColor = MaterialTheme.colorScheme.primary,
                    unfocusedIndicatorColor = MaterialTheme.colorScheme.onSurface.copy(0.2f)
                )
            )
        }
    }
}



