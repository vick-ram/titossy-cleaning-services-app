package com.example.titossycleaningservicesapp.presentation.users.customer.utils

import android.util.Log
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ChevronLeft
import androidx.compose.material.icons.outlined.MoreVert
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.titossycleaningservicesapp.R
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
    var showDialog by remember { mutableStateOf(false) }
    val bookingViewModel: BookingViewModel = hiltViewModel()
    val bookingUiState by bookingViewModel.bookingUiState.collectAsState()
    val booking = bookingUiState.bookings?.find { it.bookingId == bookingId }
    var phone by remember { mutableStateOf("") }
    var transactionCode by remember { mutableStateOf("") }

    val paymentViewModel: CustomerPaymentViewModel = hiltViewModel()

    Log.d("BookingID", "BookingId: ${booking?.bookingId} = $bookingId")

    LaunchedEffect(bookingUiState) {
        bookingViewModel.fetchBookings()
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { },
                navigationIcon = {
                    NavigationIcon(icon = Icons.Outlined.ChevronLeft) {
                        navController.navigateUp()
                    }
                },
                actions = {
                    NavigationIcon(icon = Icons.Outlined.MoreVert, onClick = {})
                }
            )
        }
    ) { innerPadding ->

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = 16.dp, start = 8.dp, end = 8.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                BookingHeader(booking = booking)
                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    item {
                        BookingSummeryHeader()
                    }
                    booking?.bookingServiceAddons?.let { addons ->
                        items(addons) { addon ->
                            TableRowData(bookingServiceAddOn = addon)
                        }
                    }
                    item {
                        HorizontalDivider()
                    }
                    item {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(8.dp),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = "Total",
                                style = MaterialTheme.typography.bodyLarge.copy(
                                    fontWeight = FontWeight.Bold
                                ),
                                modifier = Modifier.padding(8.dp)
                            )
                            Text(
                                text = "Kshs. ${booking?.totalAmount}",
                                style = MaterialTheme.typography.bodyLarge.copy(
                                    fontWeight = FontWeight.Bold
                                ),
                                modifier = Modifier.padding(8.dp)
                            )
                        }
                    }
                }

            }
            Button(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(16.dp),
                contentPadding = PaddingValues(16.dp),
                onClick = {
                    showDialog = true
                },
                shape = RectangleShape
            ) {
                Text(
                    text = "Proceed to Payment"
                )
            }
        }
        if (showDialog) {
            TransactionDialog(
                onConfirm = { phone, code, amount ->
                    paymentViewModel.makePayment(
                        bookingId = bookingId ,
                        phoneNumber = phone,
                        transactionCode = code
                    )
                    showDialog = false
                    navController.navigate(DetailsRoutes.Payment.route)
                },
                onCancel = { showDialog = false },
                phone = phone,
                transactionCode = transactionCode,
                amount = booking?.totalAmount.toString(),
                onPhoneChange = { phone = it },
                onTransactionChange = { transactionCode = it }
            )
        }
    }
}

@Composable
fun BookingSummeryHeader() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .background(androidx.compose.ui.graphics.Color.LightGray),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Name",
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier
                    .padding(vertical = 8.dp)
            )
        }
        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Qty",
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier
                    .padding(vertical = 8.dp)
            )
        }
        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Price",
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier
                    .padding(vertical = 8.dp)
            )
        }
    }
    HorizontalDivider(
        modifier = Modifier.padding(horizontal = 8.dp),
        thickness = 1.5.dp,
        color = MaterialTheme.colorScheme.primary
    )
}


@Composable
fun TableRowData(
    modifier: Modifier = Modifier,
    bookingServiceAddOn: BookingServiceAddOns?
) {
    val serviceName = bookingServiceAddOn?.service ?: bookingServiceAddOn?.serviceAddOn
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 16.dp, horizontal = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Column(
            modifier = modifier.weight(1f),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.Start
        ) {
            Text(text = serviceName ?: "")
        }
        Column(
            modifier = modifier.weight(1f),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = bookingServiceAddOn?.quantity.toString())
        }
        Column(
            modifier = modifier.weight(1f),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = bookingServiceAddOn?.subtotal.toString())
        }
    }
}


@Composable
fun TransactionDialog(
    onConfirm: (String, String, String) -> Unit,
    onCancel: () -> Unit,
    phone: String,
    transactionCode: String,
    amount: String,
    onPhoneChange: (String) -> Unit,
    onTransactionChange: (String) -> Unit
) {

    AlertDialog(
        onDismissRequest = onCancel,
        title = { Text("Confirm Transaction") },
        text = {
            PaymentCard(
                phone = phone,
                transactionCode = transactionCode,
                amount = amount,
                onPhoneChange = onPhoneChange,
                onTransactionChange = onTransactionChange
            )
        },
        confirmButton = {
            Button(
                onClick = {
                    onConfirm(phone, transactionCode, amount)
                }
            ) {
                Text("Confirm")
            }
        },
        dismissButton = {
            Button(onClick = onCancel) {
                Text("Cancel")
            }
        }
    )
}

@Composable
fun PaymentCard(
    modifier: Modifier = Modifier,
    phone: String,
    transactionCode: String,
    amount: String,
    onPhoneChange: (String) -> Unit,
    onTransactionChange: (String) -> Unit
) {
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(
            containerColor = colorResource(id = R.color.dark_green),
            contentColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 4.dp
        )
    ) {
        Box(
            modifier = modifier
                .fillMaxWidth()
                .padding(16.dp)
                .height(100.dp)
                .clip(MaterialTheme.shapes.small)
        ) {
            Column(
                modifier = modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Confirm Payment",
                    style = MaterialTheme.typography.titleLarge.copy(
                        color = MaterialTheme.colorScheme.surface
                    )
                )
                Spacer(modifier = Modifier.height(10.dp))
                Image(
                    painter = painterResource(id = R.drawable.mpesa_logo),
                    contentDescription = null
                )
            }
        }
        Spacer(modifier = Modifier.height(8.dp))
        TextField(
            value = phone,
            onValueChange = onPhoneChange,
            modifier = modifier
                .fillMaxWidth(.85f)
                .align(Alignment.CenterHorizontally)
                .padding(8.dp)
                .background(MaterialTheme.colorScheme.surface),
            placeholder = { Text(text = "Phone") }
        )
        Spacer(modifier = Modifier.height(8.dp))
        TextField(
            value = transactionCode,
            onValueChange = onTransactionChange,
            modifier = modifier
                .fillMaxWidth(.85f)
                .align(Alignment.CenterHorizontally)
                .padding(8.dp)
                .background(MaterialTheme.colorScheme.surface),
            placeholder = {
                Text(
                    text = "AUORTQPDVS",
                    style = MaterialTheme.typography.bodySmall.copy(
                        color = MaterialTheme.colorScheme.onSurface.copy(.7f)
                    )
                )
            },
        )
        Spacer(modifier = Modifier.height(8.dp))
        TextField(
            value = amount,
            onValueChange = { },
            modifier = modifier
                .fillMaxWidth(.85f)
                .align(Alignment.CenterHorizontally)
                .padding(8.dp)
                .background(MaterialTheme.colorScheme.surface),
            placeholder = { Text(text = "Amount") },
        )
        Spacer(modifier = Modifier.height(16.dp))
    }
}


@Composable
fun BookingHeader(
    modifier: Modifier = Modifier,
    booking: Booking?
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        Image(
            modifier = modifier
                .size(80.dp)
                .align(Alignment.CenterHorizontally),
            painter = painterResource(id = R.drawable.titossy_img),
            contentDescription = null
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            modifier = modifier
                .align(Alignment.CenterHorizontally),
            text = "Titossy Cleaning Services",
            style = MaterialTheme.typography.titleSmall.copy(
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onSurface
            )
        )
        Spacer(modifier = Modifier.height(8.dp))
        Row(
            modifier = modifier
                .fillMaxWidth()
                .padding(2.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Row {
                    Text(text = "BookingID: ", fontWeight = FontWeight.W500)
                    Text(text = booking?.bookingId ?: "")
                }
                Row {
                    Text(text = "Booking Date: ", fontWeight = FontWeight.W500)
                    Text(text = booking?.bookingDateTime.toString())
                }
                Row {
                    Text(text = "Customer: ", fontWeight = FontWeight.W500)
                    Text(text = booking?.customer.toString())
                }
            }
            Column(
                modifier = modifier,
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Row {
                    Text(text = "Address: ", fontWeight = FontWeight.W500)
                    Text(text = booking?.address ?: "")
                }
                Row {
                    Text(text = "Status: ", fontWeight = FontWeight.W500)
                    Text(text = booking?.bookingStatus?.name ?: "")
                }
            }
        }

    }
}