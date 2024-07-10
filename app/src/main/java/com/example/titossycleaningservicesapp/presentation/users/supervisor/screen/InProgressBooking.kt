package com.example.titossycleaningservicesapp.presentation.users.supervisor.screen

import android.widget.Toast
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ChevronLeft
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
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.example.titossycleaningservicesapp.core.CustomProgressIndicator
import com.example.titossycleaningservicesapp.domain.models.BookingStatus
import com.example.titossycleaningservicesapp.domain.viewmodel.BookingViewModel
import com.example.titossycleaningservicesapp.presentation.utils.NavigationIcon

@Composable
fun InProgressBooking(
    modifier: Modifier = Modifier,
    navController: NavHostController
) {
    val context = LocalContext.current
    val bookingViewModel: BookingViewModel = hiltViewModel()
    val bookingUiState by bookingViewModel.bookingUiState.collectAsStateWithLifecycle()

    val inProgressBookings =
        bookingUiState.bookings?.filter { it.bookingStatus == BookingStatus.IN_PROGRESS }

    LaunchedEffect(key1 = bookingViewModel) {
        bookingViewModel.fetchBookings()
    }
    Column(
        modifier = modifier
            .fillMaxSize()
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
                    imageVector = Icons.Rounded.ChevronLeft,
                    contentDescription = null
                )
            }
            Spacer(modifier = modifier.width(16.dp))
            Text(
                text = "In-Progress Bookings",
                style = MaterialTheme.typography.headlineSmall
            )
        }
        when {
            bookingUiState.isLoading -> {
                Box(
                    modifier = modifier
                        .fillMaxSize()
                        .padding(8.dp),
                    contentAlignment = Alignment.Center,
                    content = { CustomProgressIndicator(isLoading = true) }
                )
            }

            bookingUiState.bookings != null -> {
                inProgressBookings?.let { bookings ->
                    Column(
                        modifier = modifier
                            .fillMaxSize()
                            .padding(16.dp)
                    ) {
                        LazyColumn(
                            modifier = modifier
                                .fillMaxWidth()
                        ) {
                            items(bookings) { booking ->
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