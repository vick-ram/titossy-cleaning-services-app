package com.example.titossycleaningservicesapp.presentation.users.cleaner.screens

import android.widget.Toast
import androidx.compose.foundation.border
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.example.titossycleaningservicesapp.R
import com.example.titossycleaningservicesapp.core.CustomProgressIndicator
import com.example.titossycleaningservicesapp.core.showToast
import com.example.titossycleaningservicesapp.domain.models.BookingStatus
import com.example.titossycleaningservicesapp.domain.models.requests.booking.UpdateBookingStatus
import com.example.titossycleaningservicesapp.domain.models.ui_models.BookingAssignment
import com.example.titossycleaningservicesapp.domain.models.ui_models.BookingCleanerAssignment
import com.example.titossycleaningservicesapp.domain.viewmodel.BookingViewModel

@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    paddingValues: PaddingValues
) {
    val bookingViewModel: BookingViewModel = hiltViewModel()
    val context = LocalContext.current
    var search by rememberSaveable { mutableStateOf("") }
    val bookingAssignmentUiState by bookingViewModel.bookingAssignmentUiState.collectAsStateWithLifecycle()
    val bookingUpdateUiState by bookingViewModel.bookingUpdate.collectAsStateWithLifecycle()

    LaunchedEffect(bookingViewModel) {
        bookingViewModel.fetchCleanerAssignments()
    }

    LaunchedEffect(key1 = bookingUpdateUiState) {
        when {
            bookingUpdateUiState.isLoading -> return@LaunchedEffect
            bookingUpdateUiState.successMessage.isNotEmpty() -> {
                showToast(
                    context = context,
                    length = Toast.LENGTH_LONG,
                    message = bookingUpdateUiState.successMessage
                )
            }

            bookingUpdateUiState.errorMessage.isNotEmpty() -> {
                showToast(
                    context = context,
                    message = bookingUpdateUiState.errorMessage
                )
            }
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(paddingValues)
    ) {
        Row(
            modifier = modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .border(
                    width = 1.dp,
                    color = MaterialTheme.colorScheme.surface.copy(.1f),
                    MaterialTheme.shapes.medium
                ),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            OutlinedTextField(
                modifier = modifier.height(48.dp),
                value = search,
                onValueChange = { search = it },
                shape = MaterialTheme.shapes.extraLarge,
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = MaterialTheme.colorScheme.surfaceVariant,
                    unfocusedContainerColor = MaterialTheme.colorScheme.surfaceVariant,
                    focusedBorderColor = Color.Transparent,
                    unfocusedBorderColor = Color.Transparent
                ),
                placeholder = {
                    Text(
                        text = "Search",
                        style = MaterialTheme.typography.bodyMedium.copy(
                            color = MaterialTheme.colorScheme.onSurface.copy(.5f)
                        )
                    )
                },
                textStyle = MaterialTheme.typography.bodyMedium.copy(
                    color = MaterialTheme.colorScheme.onSurface
                )
            )

            FilledIconButton(
                onClick = { bookingViewModel.onStatusSwap() },
                shape = MaterialTheme.shapes.medium,
                colors = IconButtonDefaults.filledIconButtonColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant,
                    contentColor = MaterialTheme.colorScheme.onSurface
                )
            ) {
                Icon(
                    imageVector = Icons.Filled.FilterList,
                    contentDescription = null
                )
            }
        }
        Spacer(modifier = modifier.height(8.dp))
        when {
            bookingAssignmentUiState.isLoading -> {
                Box(
                    modifier = modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center,
                    content = { CustomProgressIndicator(isLoading = true) }
                )
            }

            bookingAssignmentUiState.assignedBookings != null -> {
                if (bookingAssignmentUiState.assignedBookings.isNullOrEmpty()) {
                    Box(
                        modifier = modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "No bookings assigned",
                            style = MaterialTheme.typography.bodyLarge.copy(
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                            )
                        )
                    }
                }
                bookingAssignmentUiState.assignedBookings?.filter {
                    it.bookingAssignment.bookingStatus.name.contains(
                        search,
                        ignoreCase = true
                    ) || it.bookingAssignment.customer.contains(search, ignoreCase = true)
                            || it.formattedDate.contains(search, ignoreCase = true)
                }?.let { assignments ->
                    LazyColumn(
                        modifier = modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(assignments) { assignment ->
                            CleanerBookingAssignmentCard(
                                bookingAssignment = assignment.bookingAssignment,
                                bookingDate = assignment,
                                onInProgress = { assign, status ->
                                    bookingViewModel.updateBookingStatus(
                                        assign.bookingId,
                                        UpdateBookingStatus(status)
                                    )
                                },
                                onCompleted = { assign, status ->
                                    bookingViewModel.updateBookingStatus(
                                        assign.bookingId,
                                        UpdateBookingStatus(status)
                                    )
                                    bookingViewModel.fetchBookingAssignments(assign.bookingId)
                                }
                            )
                        }
                    }
                }
            }

            bookingAssignmentUiState.errorMessage.isNotEmpty() -> {
                showToast(
                    context = context,
                    message = bookingAssignmentUiState.errorMessage
                )
            }
        }
    }
}

@Composable
fun CleanerBookingAssignmentCard(
    modifier: Modifier = Modifier,
    bookingAssignment: BookingAssignment,
    bookingDate: BookingCleanerAssignment,
    onInProgress: (BookingAssignment, BookingStatus) -> Unit,
    onCompleted: (BookingAssignment, BookingStatus) -> Unit,
) {

    var expanded by rememberSaveable { mutableStateOf(false) }
    Card(
        modifier = modifier
            .fillMaxWidth(),
        colors = CardDefaults.cardColors()
    ) {
        Row(
            modifier = modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(
                modifier = modifier
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "BookingID: ",
                        style = MaterialTheme.typography.bodyLarge.copy(
                            fontWeight = FontWeight.W600
                        )
                    )
                    Text(
                        text = bookingAssignment.bookingId.prependIndent("#"),
                        style = MaterialTheme.typography.bodyLarge
                    )
                }
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Customer: ",
                        style = MaterialTheme.typography.bodyLarge.copy(
                            fontWeight = FontWeight.W600
                        )
                    )
                    Text(
                        text = bookingAssignment.customer,
                        style = MaterialTheme.typography.bodyLarge
                    )
                }
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Service: ",
                        style = MaterialTheme.typography.bodyLarge.copy(
                            fontWeight = FontWeight.W600
                        )
                    )
                    Text(
                        text = bookingAssignment.service,
                        style = MaterialTheme.typography.bodyLarge
                    )
                }
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Address: ",
                        style = MaterialTheme.typography.bodyLarge.copy(
                            fontWeight = FontWeight.W600
                        )
                    )
                    Text(
                        text = "${bookingAssignment.address}",
                        style = MaterialTheme.typography.bodyLarge
                    )
                }
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Date: ",
                        style = MaterialTheme.typography.bodyLarge.copy(
                            fontWeight = FontWeight.W600
                        )
                    )
                    Text(text = bookingDate.formattedDate)
                }

                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Status: ",
                        style = MaterialTheme.typography.bodyLarge.copy(
                            fontWeight = FontWeight.W600
                        )
                    )
                    Text(
                        text = bookingAssignment.bookingStatus.name,
                        style = MaterialTheme.typography.bodyLarge.copy(
                            fontWeight = FontWeight.Bold,
                            color = getColorForStatus(bookingAssignment.bookingStatus)
                        )
                    )

                }
            }
            Spacer(modifier = modifier.weight(1f))
            Box(modifier = modifier) {
                IconButton(
                    onClick = { expanded = true }
                ) {
                    Icon(
                        imageVector = Icons.Filled.MoreVert,
                        contentDescription = null
                    )
                }

                DropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false }
                ) {
                    when (bookingAssignment.bookingStatus) {
                        BookingStatus.APPROVED -> {
                            DropdownMenuItem(
                                text = {
                                    Text(
                                        text = "In Progress",
                                        style = MaterialTheme.typography.bodyLarge.copy(
                                            fontWeight = FontWeight.Bold,
                                            color = MaterialTheme.colorScheme.onSurface
                                        )
                                    )
                                },
                                onClick = {
                                    onInProgress(bookingAssignment, BookingStatus.IN_PROGRESS)
                                    expanded = false
                                }
                            )
                        }

                        BookingStatus.IN_PROGRESS -> {
                            DropdownMenuItem(
                                text = {
                                    Text(
                                        text = "Completed",
                                        style = MaterialTheme.typography.bodyLarge.copy(
                                            fontWeight = FontWeight.Bold,
                                            color = MaterialTheme.colorScheme.onSurface
                                        )
                                    )
                                },
                                onClick = {
                                    onCompleted(bookingAssignment, BookingStatus.COMPLETED)
                                    expanded = false
                                }
                            )
                        }

                        else -> {}
                    }
                }
            }
        }
    }
}

@Composable
fun getColorForStatus(status: BookingStatus): Color {
    return when (status) {
        BookingStatus.PENDING -> colorResource(id = R.color.pending)
        BookingStatus.APPROVED -> colorResource(id = R.color.approved)
        BookingStatus.IN_PROGRESS -> colorResource(id = R.color.in_progress)
        BookingStatus.COMPLETED -> colorResource(id = R.color.completed)
        BookingStatus.CANCELLED -> colorResource(id = R.color.cancelled)
    }
}

