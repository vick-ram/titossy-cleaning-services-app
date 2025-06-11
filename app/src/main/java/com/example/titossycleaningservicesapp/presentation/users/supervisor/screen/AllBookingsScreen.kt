package com.example.titossycleaningservicesapp.presentation.users.supervisor.screen

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronLeft
import androidx.compose.material.icons.rounded.ChevronLeft
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.example.titossycleaningservicesapp.R
import com.example.titossycleaningservicesapp.core.CustomProgressIndicator
import com.example.titossycleaningservicesapp.core.EmptyScreen
import com.example.titossycleaningservicesapp.domain.models.BookingStatus
import com.example.titossycleaningservicesapp.domain.models.ui_models.Booking
import com.example.titossycleaningservicesapp.domain.viewmodel.BookingViewModel
import com.example.titossycleaningservicesapp.presentation.utils.NavigationIcon
import java.time.format.DateTimeFormatter
import java.util.Locale

@Composable
fun AllBookingsScreen(
    modifier: Modifier = Modifier,
    navController: NavHostController
) {
    val context = LocalContext.current
    val bookingViewModel: BookingViewModel = hiltViewModel()
    val bookingUiState by bookingViewModel.bookingUiState.collectAsStateWithLifecycle()

    LaunchedEffect(key1 = bookingViewModel) {
        bookingViewModel.fetchBookings()
    }
    Column(
        modifier = modifier
            .fillMaxSize()
            .systemBarsPadding()
    ) {
        Row(
            modifier = modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = { navController.navigateUp() }) {
                Icon(
                    modifier = modifier.size(32.dp),
                    imageVector = Icons.Default.ChevronLeft,
                    contentDescription = null
                )
            }

            Spacer(modifier = modifier.width(16.dp))
            Text(
                text = "ALL",
                style = MaterialTheme.typography.headlineSmall
            )
        }
        when {
            bookingUiState.isLoading -> {
                Box(modifier = modifier
                    .fillMaxSize(),
                    contentAlignment = Alignment.Center,
                    content = { CustomProgressIndicator(isLoading = true) }
                )
            }

            bookingUiState.bookings != null -> {
                if (bookingUiState.bookings.isNullOrEmpty()) {
                    EmptyScreen(modifier, "No Bookings Found")
                } else {
                    LazyColumn(
                        modifier = modifier
                            .fillMaxWidth()
                    ) {
                        items(bookingUiState.bookings ?: emptyList()) { booking ->
                            BookingCard(
                                booking = booking,
                                onClick = {
                                    navController.navigate("supplierBookingDetails" + "/" + it.bookingId)
                                }
                            )
                        }
                    }
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
    }
}

@Composable
fun BookingCard(
    booking: Booking,
    onClick: (Booking) -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable { onClick(booking) },
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.surface)
                .padding(16.dp)
        ) {
            Text(
                text = "Booking #${booking.bookingId}",
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Customer: ${booking.customer}",
                style = MaterialTheme.typography.bodyMedium
            )
            Text(
                text = "Date & Time: ${booking.bookingDateTime.format(DateTimeFormatter.ofPattern("dd MMM yyyy, hh:mm a"))}",
                style = MaterialTheme.typography.bodyMedium
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Total Amount: ${booking.amount}",
                style = MaterialTheme.typography.bodyMedium,
            )

            Row {
                Text(
                    text = "Status: ",
                    style = MaterialTheme.typography.bodyMedium
                )
                Text(
                    text = booking.bookingStatus.name.replaceFirstChar {
                        if (it.isLowerCase()) it.titlecase(
                            Locale.getDefault()
                        ) else it.toString()
                    },
                    style = MaterialTheme.typography.bodyMedium,
                    color = when (booking.bookingStatus) {
                        BookingStatus.PENDING -> colorResource(id = R.color.pending)
                        BookingStatus.APPROVED -> colorResource(id = R.color.approved)
                        BookingStatus.IN_PROGRESS -> colorResource(id = R.color.in_progress)
                        BookingStatus.CANCELLED -> colorResource(id = R.color.cancelled)
                        BookingStatus.COMPLETED -> colorResource(id = R.color.completed)
                    }
                )
            }

        }
    }
}


