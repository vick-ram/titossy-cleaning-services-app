package com.example.titossycleaningservicesapp.presentation.users.supervisor.util

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ChevronLeft
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.example.titossycleaningservicesapp.R
import com.example.titossycleaningservicesapp.core.dateTimeUiFormat
import com.example.titossycleaningservicesapp.domain.models.requests.booking.BookingStatus
import com.example.titossycleaningservicesapp.domain.models.ui_models.Booking
import com.example.titossycleaningservicesapp.domain.models.ui_models.BookingServiceAddOns
import com.example.titossycleaningservicesapp.domain.viewmodel.BookingViewModel
import com.example.titossycleaningservicesapp.presentation.utils.NavigationIcon

@Composable
fun SupervisorBookingDetails(
    bookingId: String,
    navController: NavHostController
) {
    val bookingViewModel: BookingViewModel = hiltViewModel()
    val bookingUiState by bookingViewModel.bookingUiState.collectAsStateWithLifecycle()
    val booking = bookingUiState.bookings?.find { it.bookingId == bookingId }

    LaunchedEffect(key1 = bookingId) {
        bookingViewModel.fetchBookings()
    }

    booking?.let { book ->
        BookingData(
            booking = book,
            onClick = { navController.navigateUp() }
        )
    }

}

@Composable
fun BookingDataRow(
    modifier: Modifier = Modifier,
    label: String,
    value: String,
    style: TextStyle = MaterialTheme.typography.bodyLarge,
    color: Color = MaterialTheme.colorScheme.onSurface,
    labelColor: Color = MaterialTheme.colorScheme.onSurface
) {
    Row(
        modifier = modifier
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "$label: ",
            style = style.copy(fontWeight = FontWeight.W600),
            color = labelColor
        )
        Text(
            text = "$value: ",
            style = style,
            color = color
        )
    }
}

@Composable
fun BookingData(
    modifier: Modifier = Modifier,
    booking: Booking,
    onClick: () -> Unit
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Row(
            modifier = modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            NavigationIcon(
                icon = Icons.Rounded.ChevronLeft,
                onClick = onClick
            )
            Spacer(modifier = modifier.width(16.dp))
            Text(
                text = "Booking Details",
                style = MaterialTheme.typography.headlineSmall
            )
        }
        BookingDataRow(
            label = "BookingID: ",
            value = "#${booking.bookingId}"
        )
        Spacer(modifier = modifier.width(8.dp))
        BookingDataRow(
            label = "Customer: ",
            value = booking.customer
        )
        Spacer(modifier = modifier.width(8.dp))
        BookingDataRow(
            label = "Booking Date & Time: ",
            value = booking.bookingDateTime.format(dateTimeUiFormat)
        )
        Spacer(modifier = modifier.width(8.dp))
        BookingDataRow(
            label = "Frequency: ",
            value = "${booking.frequency}"
        )
        Spacer(modifier = modifier.width(8.dp))
        BookingDataRow(
            label = "Instructions: ",
            value = booking.additionalInstructions
        )
        Spacer(modifier = modifier.width(8.dp))
        BookingDataRow(
            label = "Address: ",
            value = booking.address
        )
        Spacer(modifier = modifier.width(8.dp))
        BookingDataRow(
            label = "Paid: ",
            value = booking.paidString
        )
        Spacer(modifier = modifier.width(8.dp))
        BookingDataRow(
            label = "Booking Status: ",
            value = "${booking.bookingStatus}",
            color = when (booking.bookingStatus) {
                BookingStatus.PENDING -> colorResource(id = R.color.pending)
                BookingStatus.APPROVED -> colorResource(id = R.color.approved)
                BookingStatus.IN_PROGRESS -> colorResource(id = R.color.in_progress)
                BookingStatus.CANCELLED -> colorResource(id = R.color.cancelled)
                BookingStatus.COMPLETED -> colorResource(id = R.color.completed)
            },
            labelColor = MaterialTheme.colorScheme.onSurface
        )
        Spacer(modifier = Modifier.height(16.dp))
        LazyRow(
            modifier = modifier,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(booking.bookingServiceAddons) { serviceAddon ->
                BookingDetailsAddon(addOn = serviceAddon)
            }
        }
    }
}

@Composable
fun BookingDetailsAddon(
    modifier: Modifier = Modifier,
    addOn: BookingServiceAddOns
) {
    Card(
        modifier = modifier
            .fillMaxWidth(.8f),
        shape = MaterialTheme.shapes.medium,
        elevation = CardDefaults.cardElevation(
            defaultElevation = 8.dp
        ),
    ) {
        Column(
            modifier = modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.surface)
        ) {
            Text(
                modifier = modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                text = "Service:  ${addOn.service}",
            )
            Text(
                modifier = modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                text = "ServiceAddon:  ${addOn.addon}"
            )
            Text(
                modifier = modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                text = "Qty:  ${addOn.quantity}"
            )
            Text(
                modifier = modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                text = "Subtotal:  ${addOn.total}"
            )
        }
    }
}