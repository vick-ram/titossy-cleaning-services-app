package com.example.titossycleaningservicesapp.presentation.users.customer.screens

import android.widget.Toast
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.EventNote
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.StarBorder
import androidx.compose.material.icons.outlined.Inbox
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.DialogProperties
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.example.titossycleaningservicesapp.R
import com.example.titossycleaningservicesapp.core.CustomProgressIndicator
import com.example.titossycleaningservicesapp.core.SmallSearchField
import com.example.titossycleaningservicesapp.core.showToast
import com.example.titossycleaningservicesapp.domain.models.BookingStatus
import com.example.titossycleaningservicesapp.domain.models.ui_models.Booking
import com.example.titossycleaningservicesapp.domain.viewmodel.BookingViewModel
import com.example.titossycleaningservicesapp.domain.viewmodel.FeedbackViewModel
import kotlinx.coroutines.delay

@Composable
fun BookingsScreen(
    modifier: Modifier = Modifier,
    paddingValues: PaddingValues
) {
    val context = LocalContext.current
    val bookingViewModel: BookingViewModel = hiltViewModel()
    val bookingState by bookingViewModel.bookingUiState.collectAsStateWithLifecycle()
    var query by rememberSaveable { mutableStateOf("") }
    var dialog by rememberSaveable { mutableStateOf(false) }
    var feedbackMessage by rememberSaveable { mutableStateOf("") }
    var rating by rememberSaveable { mutableIntStateOf(0) }
    val feedbackViewModel: FeedbackViewModel = hiltViewModel()
    val feedbackUiState by feedbackViewModel.feedbackState.collectAsStateWithLifecycle()

    LaunchedEffect(bookingViewModel) {
        bookingViewModel.fetchCustomerBookings()
    }

    LaunchedEffect(key1 = feedbackUiState) {
        when {
            feedbackUiState.isLoading -> {
                delay(100L)
            }

            feedbackUiState.successMessage.isNotEmpty() -> {
                showToast(
                    context = context,
                    length = Toast.LENGTH_LONG,
                    message = feedbackUiState.successMessage
                )
                dialog = false
            }

            feedbackUiState.errorMessage.isNotEmpty() -> showToast(
                context = context,
                message = feedbackUiState.errorMessage
            )
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(paddingValues)
    ) {
        Spacer(modifier = modifier.height(16.dp))
        SmallSearchField(
            modifier = modifier.align(Alignment.CenterHorizontally),
            value = query,
            onValueChange = { query = it }
        )
        Spacer(modifier = modifier.height(8.dp))
        when {
            bookingState.isLoading -> Box(
                modifier = modifier.fillMaxSize(),
                contentAlignment = Alignment.Center,
                content = { CustomProgressIndicator(isLoading = true) }
            )

            bookingState.bookings != null -> {
                bookingState.bookings?.filter {
                    it.bookingStatus.name.contains(query, ignoreCase = true) ||
                            it.frequency.name.contains(query, ignoreCase = true) ||
                            it.formattedDate.contains(query, ignoreCase = true)
                }?.let { bookings ->
                    LazyColumn(
                        modifier = modifier
                            .fillMaxWidth()
                            .padding(8.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        items(
                            items = bookings,
                            key = { it.bookingId }
                        ) { booking ->
                            BookingCustomerCard(
                                booking = booking,
                                color = getCardColor(bookingStatus = booking.bookingStatus),
                                dialog = dialog,
                                onDialog = { dialog = it },
                                message = feedbackMessage,
                                rating = rating,
                                onMessageChange = { feedbackMessage = it },
                                onRatingsChanged = { rating = it },
                                onFeedBack = {
                                    feedbackViewModel.giveFeedback(
                                        bookingId = booking.bookingId,
                                        feedback = feedbackMessage,
                                        rating = rating.toDouble()
                                    )
                                }
                            )
                        }
                    }
                }
            }

            bookingState.errorMessage.isNotEmpty() -> {
                showToast(
                    context = context,
                    message = bookingState.errorMessage
                )
                Box(
                    modifier = modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center,
                    content = {
                        Column(
                            modifier = modifier.fillMaxWidth(),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Icon(
                                modifier = modifier.size(60.dp),
                                imageVector = Icons.Outlined.Inbox,
                                contentDescription = null
                            )
                            Spacer(modifier = modifier.height(8.dp))
                            Text(text = "Ooops..", style = MaterialTheme.typography.bodyLarge)
                        }
                    }
                )
            }
        }
    }
}

@Composable
fun getCardColor(bookingStatus: BookingStatus): Color {
    return when (bookingStatus) {
        BookingStatus.PENDING -> colorResource(id = R.color.pending)
        BookingStatus.APPROVED -> colorResource(id = R.color.approved)
        BookingStatus.IN_PROGRESS -> colorResource(id = R.color.in_progress)
        BookingStatus.COMPLETED -> colorResource(id = R.color.completed)
        else -> Color.Gray
    }
}

@Composable
private fun BookingCustomerCard(
    modifier: Modifier = Modifier,
    booking: Booking,
    color: Color,
    dialog: Boolean = false,
    onDialog: (Boolean) -> Unit,
    message: String,
    rating: Int,
    onMessageChange: (String) -> Unit,
    onRatingsChanged: (Int) -> Unit,
    onFeedBack: () -> Unit
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        ElevatedCard(
            modifier = modifier
                .shadow(
                    elevation = 2.dp,
                    shape = MaterialTheme.shapes.small
                ),
            onClick = { /*TODO*/ },
            colors = CardDefaults.elevatedCardColors(
                containerColor = MaterialTheme.colorScheme.surface,
                contentColor = MaterialTheme.colorScheme.onSurface
            ),
            elevation = CardDefaults.elevatedCardElevation(0.dp),
            shape = MaterialTheme.shapes.small
        ) {
            Row(
                modifier = modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.EventNote,
                        contentDescription = null,
                        tint = color
                    )
                    Spacer(modifier = modifier.width(16.dp))
                    Column {
                        Text(
                            text = booking.bookingStatus.name,
                            style = MaterialTheme.typography.bodyLarge.copy(
                                color = color
                            )
                        )
                        Text(
                            text = booking.formattedDate,
                            style = MaterialTheme.typography.bodyLarge.copy(
                                color = MaterialTheme.colorScheme.onSurface,
                                fontWeight = FontWeight.Bold
                            )
                        )
                    }
                }
                if (booking.bookingStatus == BookingStatus.COMPLETED) {
                    Button(
                        modifier = modifier
                            .wrapContentSize(),
                        onClick = { onDialog(true) },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.primary,
                            contentColor = MaterialTheme.colorScheme.onPrimary
                        ),
                        contentPadding = PaddingValues(horizontal = 8.dp, vertical = 4.dp)
                    ) {
                        Text(
                            text = "Leave feedback",
                            style = MaterialTheme.typography.bodySmall
                        )
                    }
                }
            }
            Row(
                modifier = modifier
                    .fillMaxWidth()
                    .padding(
                        start = 16.dp,
                        end = 16.dp,
                        bottom = 16.dp,
                        top = 8.dp
                    ),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.CalendarMonth,
                        contentDescription = null
                    )
                    Spacer(modifier = modifier.width(16.dp))
                    Column {
                        Text(
                            text = "Booking",
                            style = MaterialTheme.typography.bodyLarge.copy(
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSurface.copy(.3f)
                            )
                        )
                        Text(
                            text = booking.bookingId,
                            style = MaterialTheme.typography.bodyLarge.copy(
                                color = MaterialTheme.colorScheme.onSurface,
                                fontWeight = FontWeight.Bold
                            )
                        )
                    }
                }
                Column {
                    Text(
                        text = "Frequency",
                        style = MaterialTheme.typography.bodyLarge.copy(
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface.copy(.3f)
                        )
                    )
                    Text(
                        text = booking.frequency.name,
                        style = MaterialTheme.typography.bodyLarge.copy(
                            color = MaterialTheme.colorScheme.onSurface,
                            fontWeight = FontWeight.Bold
                        )
                    )
                }
            }
        }
        if (dialog) {
            AlertDialog(
                onDismissRequest = { onDialog(false) },
                confirmButton = {
                    TextButton(
                        onClick = { onFeedBack() },
                        enabled = message.isNotEmpty() && rating > 0
                    ) {
                        Text(text = "Send")
                    }
                },
                dismissButton = {
                    TextButton(
                        onClick = { onDialog(false) }
                    ) {
                        Text(text = "cancel")
                    }
                },
                title = {
                    Text(text = "Leave Feedback")
                },
                text = {
                    Column(
                        modifier = modifier
                            .fillMaxWidth()
                            .padding(8.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text(
                            text = "Your feedback helps us improve our service delivery😊",
                            style = MaterialTheme.typography.bodyMedium.copy(
                                textAlign = TextAlign.Center,
                                color = MaterialTheme.colorScheme.onSurface,
                                fontWeight = FontWeight.Bold
                            )
                        )
                        Spacer(modifier = modifier.height(4.dp))
                        Text(
                            text = "Comment:",
                            style = MaterialTheme.typography.bodyMedium
                        )
                        OutlinedTextField(
                            modifier = modifier.fillMaxWidth(),
                            value = message,
                            onValueChange = onMessageChange,
                        )
                        Spacer(modifier = modifier.height(4.dp))
                        Text(
                            text = "Rating:",
                            style = MaterialTheme.typography.bodyMedium
                        )
                        StarRating(
                            rating = rating,
                            onRatingChanged = onRatingsChanged
                        )
                    }
                },
                properties = DialogProperties(
                    dismissOnClickOutside = false,
                    dismissOnBackPress = false
                )
            )
        }
    }
}


@Composable
fun StarRating(
    modifier: Modifier = Modifier,
    rating: Int,
    onRatingChanged: (Int) -> Unit,
    maxRating: Int = 5
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        for (i in 1..maxRating) {
            Star(
                filled = i <= rating,
                onClick = { onRatingChanged(i) }
            )
        }
    }
}

@Composable
fun Star(
    modifier: Modifier = Modifier,
    filled: Boolean,
    onClick: () -> Unit
) {
    Icon(
        imageVector = if (filled) Icons.Filled.Star else Icons.Filled.StarBorder,
        contentDescription = null,
        tint = if (filled) Color(0xFFD4AF37) else Color.Gray,
        modifier = modifier
            .size(32.dp)
            .clickable(onClick = onClick)
    )
}

