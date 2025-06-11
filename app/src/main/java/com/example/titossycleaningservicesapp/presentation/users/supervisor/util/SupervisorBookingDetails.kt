package com.example.titossycleaningservicesapp.presentation.users.supervisor.util

import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material.icons.rounded.ChevronLeft
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.SheetState
import androidx.compose.material3.SuggestionChip
import androidx.compose.material3.SuggestionChipDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
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
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.example.titossycleaningservicesapp.R
import com.example.titossycleaningservicesapp.core.CustomProgressIndicator
import com.example.titossycleaningservicesapp.core.dateTimeUiFormat
import com.example.titossycleaningservicesapp.core.showToast
import com.example.titossycleaningservicesapp.domain.models.BookingStatus
import com.example.titossycleaningservicesapp.domain.models.Roles
import com.example.titossycleaningservicesapp.domain.models.requests.booking.UpdateBookingStatus
import com.example.titossycleaningservicesapp.domain.models.ui_models.Booking
import com.example.titossycleaningservicesapp.domain.models.ui_models.BookingCleanerAssignment
import com.example.titossycleaningservicesapp.domain.models.ui_models.BookingServiceAddOns
import com.example.titossycleaningservicesapp.domain.models.ui_models.Employee
import com.example.titossycleaningservicesapp.domain.viewmodel.BookingViewModel
import com.example.titossycleaningservicesapp.domain.viewmodel.EmployeeViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SupervisorBookingDetails(
    bookingId: String,
    navController: NavHostController,
    modifier: Modifier = Modifier,
    paddingValues: PaddingValues
) {
    val context = LocalContext.current
    val sheetState = rememberModalBottomSheetState()
    val bookingViewModel: BookingViewModel = hiltViewModel()
    val bookingUiState by bookingViewModel.bookingUiState.collectAsStateWithLifecycle()
    val bookingUpdateUiState by bookingViewModel.bookingUpdate.collectAsStateWithLifecycle()
    val bookingAssignmentUiState by bookingViewModel.bookingAssignmentUiState.collectAsStateWithLifecycle()
    val bookingAssignmentUpdateUiState by bookingViewModel.bookingAssignmentUpdateUiState.collectAsStateWithLifecycle()
    val booking = bookingUiState.bookings?.find { it.bookingId == bookingId }
    val employeeViewModel: EmployeeViewModel = hiltViewModel()
    val employeeState by employeeViewModel.employeeUiState.collectAsStateWithLifecycle()
    var showBottomSheet by rememberSaveable { mutableStateOf(false) }
    var searchQuery by rememberSaveable { mutableStateOf("") }
    val scrollState = rememberScrollState()

    LaunchedEffect(bookingViewModel) {
        bookingViewModel.fetchBookings()
    }

    LaunchedEffect(key1 = bookingId) {
        bookingViewModel.fetchBookingAssignments(bookingId)
    }

    LaunchedEffect(key1 = employeeViewModel) {
        employeeViewModel.fetchEmployees()
    }
    val employees = when {
        employeeState.employees != null -> {
            employeeState.employees
                ?.filter { employee ->
                    employee.role == Roles.CLEANER && employee.fullName.contains(
                        searchQuery,
                        ignoreCase = true
                    )
                }
                ?.filterNot { cleaner ->
                    bookingAssignmentUiState.assignedBookings?.any {
                        it.cleaner == cleaner.fullName
                    } ?: false
                }
        }

        employeeState.isLoading -> {
            Box(
                modifier = modifier.fillMaxSize(),
                contentAlignment = Alignment.Center,
                content = {
                    CustomProgressIndicator(isLoading = true)
                }
            )
            null
        }

        employeeState.errorMessage.isNotEmpty() -> {
            Toast.makeText(
                context,
                employeeState.errorMessage,
                Toast.LENGTH_SHORT
            ).show()
            null
        }

        else -> null
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
                navController.navigate(BookingRoutes.ApprovedBookings.route) {
                    popUpTo(BookingRoutes.PendingBookings.route) { inclusive = true }
                }
            }

            bookingUpdateUiState.errorMessage.isNotEmpty() -> {
                showToast(
                    context = context,
                    message = bookingUpdateUiState.errorMessage
                )
            }
        }
    }

    LaunchedEffect(key1 = bookingAssignmentUpdateUiState) {
        when {
            bookingAssignmentUpdateUiState.isLoading -> return@LaunchedEffect
            bookingAssignmentUpdateUiState.successMessage.isNotEmpty() -> {
               showToast(
                   context = context,
                   length = Toast.LENGTH_LONG,
                   message = bookingUpdateUiState.successMessage
               )
                showBottomSheet = false
            }

            bookingAssignmentUpdateUiState.errorMessage.isNotEmpty() -> {
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
                text = "Booking Details",
                style = MaterialTheme.typography.headlineSmall
            )
        }
        booking?.let { book ->
            BookingData(
                booking = book,
                onApprove = { bookingStatus ->
                    bookingViewModel.updateBookingStatus(
                        bookingId = book.bookingId,
                        updateBookingStatus = UpdateBookingStatus(bookingStatus)
                    )
                },
                sheetState = sheetState,
                showBottomSheet = showBottomSheet,
                onDismiss = { showBottomSheet = it },
                query = searchQuery,
                onSearch = { searchQuery = it },
                employees = employees,
                assign = { employee ->
                    bookingViewModel.assignBooking(
                        bookingId = book.bookingId,
                        cleanerId = employee.id
                    )
                },
                bookingCleanerAssignment = bookingAssignmentUiState.assignedBookings,
                scrollState = scrollState
            )
        }
    }
}

@Composable
fun BookingDataRow(
    modifier: Modifier = Modifier,
    label: String,
    value: String,
    style: TextStyle = MaterialTheme.typography.bodyMedium,
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
            text = value,
            style = style,
            color = color
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BookingData(
    modifier: Modifier = Modifier,
    booking: Booking,
    onApprove: (BookingStatus) -> Unit,
    sheetState: SheetState,
    showBottomSheet: Boolean = false,
    onDismiss: (Boolean) -> Unit,
    query: String,
    onSearch: (String) -> Unit,
    employees: List<Employee>?,
    assign: (Employee) -> Unit,
    bookingCleanerAssignment: List<BookingCleanerAssignment>?,
    scrollState: ScrollState
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(state = scrollState),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {

        BookingDataRow(
            label = "BookingID",
            value = "#${booking.bookingId}"
        )
        Spacer(modifier = modifier.width(4.dp))
        BookingDataRow(
            label = "Customer",
            value = booking.customer
        )
        Spacer(modifier = modifier.width(4.dp))
        BookingDataRow(
            label = "Booking Date & Time",
            value = booking.bookingDateTime.format(dateTimeUiFormat)
        )
        Spacer(modifier = modifier.width(4.dp))
        BookingDataRow(
            label = "Frequency",
            value = "${booking.frequency}"
        )
        Spacer(modifier = modifier.width(4.dp))
        BookingDataRow(
            label = "Instructions",
            value = booking.additionalInstructions
        )
        Spacer(modifier = modifier.width(8.dp))
        BookingDataRow(
            label = "Address",
            value = booking.address
        )
        Spacer(modifier = modifier.width(4.dp))
        BookingDataRow(
            label = "Paid",
            value = booking.paidString
        )
        Spacer(modifier = modifier.width(4.dp))
        BookingDataRow(
            label = "Booking Status",
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

        if (booking.bookingStatus == BookingStatus.PENDING) {
            Button(
                modifier = modifier
                    .fillMaxWidth(),
                onClick = { onApprove(BookingStatus.APPROVED) },
                colors = ButtonDefaults.buttonColors()
            ) {
                Text(
                    text = "Approve",
                    style = MaterialTheme.typography.labelLarge
                )
            }
        }
        Row(
            modifier = modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Assigned To: ",
                style = MaterialTheme.typography.bodyLarge.copy(
                    fontWeight = FontWeight.Bold
                )
            )
            if (booking.bookingStatus == BookingStatus.APPROVED || booking.bookingStatus == BookingStatus.PENDING) {
                OutlinedButton(
                    onClick = {
                        onDismiss(true)
                    },
                    enabled = booking.bookingStatus == BookingStatus.APPROVED,
                    colors = ButtonDefaults.outlinedButtonColors(
                        contentColor = MaterialTheme.colorScheme.onSurface,
                        containerColor = MaterialTheme.colorScheme.surface
                    ),
                    border = BorderStroke(1.dp, Color.Transparent),
                    modifier = modifier.padding(8.dp)
                ) {
                    Icon(
                        modifier = modifier.size(32.dp),
                        imageVector = Icons.Outlined.Add,
                        contentDescription = "assign",
                        tint = MaterialTheme.colorScheme.onSurface
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Add",
                        style = MaterialTheme.typography.bodyLarge.copy(
                            fontWeight = FontWeight.Bold
                        )
                    )
                }
            }
        }
        FlowRow(
            modifier = modifier
                .fillMaxWidth()
                .padding(8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            bookingCleanerAssignment?.forEach { cleanerAssign ->
                SuggestionChip(
                    shape = RoundedCornerShape(16.dp),
                    onClick = {},
                    label = {
                        Text(
                            modifier = modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                            text = cleanerAssign.cleaner,
                            style = MaterialTheme.typography.bodyLarge.copy(
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onPrimary
                            )
                        )
                    },
                    colors = SuggestionChipDefaults.suggestionChipColors(
                        containerColor = MaterialTheme.colorScheme.primary,
                        labelColor = MaterialTheme.colorScheme.onPrimary
                    )
                )
            }
        }
    }
    if (showBottomSheet) {
        ModalBottomSheet(
            modifier = modifier
                .fillMaxHeight(.7f)
                .imePadding(),
            onDismissRequest = {
                onDismiss(false)
            },
            sheetState = sheetState,
        ) {
            OutlinedTextField(
                modifier = modifier
                    .fillMaxWidth(.8f)
                    .align(Alignment.CenterHorizontally),
                value = query,
                onValueChange = onSearch,
                shape = RoundedCornerShape(50),
                textStyle = MaterialTheme.typography.bodyLarge,
                placeholder = {
                    Text(
                        text = "Search cleaner",
                        style = MaterialTheme.typography.bodyLarge.copy(
                            color = MaterialTheme.colorScheme.onSurface.copy(.5f)
                        )
                    )
                },
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color.Transparent,
                    unfocusedBorderColor = Color.Transparent,
                    focusedContainerColor = MaterialTheme.colorScheme.surfaceContainer,
                    unfocusedContainerColor = MaterialTheme.colorScheme.surfaceContainer
                ),
                keyboardOptions = KeyboardOptions(
                    imeAction = ImeAction.Search
                ),
                keyboardActions = KeyboardActions(
                    onSearch = { onSearch(query) }
                ),
            )
            Spacer(modifier = modifier.height(8.dp))

            LazyColumn(
                modifier = modifier
                    .fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                if (employees != null) {
                    items(employees) { employee ->
                        EmployeesCard(
                            employee = employee,
                            assign = assign
                        )
                    }
                }
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
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.onSurface.copy(.5f))
    ) {
        Column(
            modifier = modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.surface)
        ) {
            Text(
                modifier = modifier.padding(
                    horizontal = 8.dp,
                    vertical = 2.dp
                ),
                text = "Service:  ${addOn.service}",
                style = MaterialTheme.typography.bodyLarge.copy(
                    fontWeight = FontWeight.Bold
                )
            )
            Text(
                modifier = modifier.padding(
                    horizontal = 8.dp,
                    vertical = 2.dp
                ),
                text = "ServiceAddon:  ${addOn.addon}"
            )
            Text(
                modifier = modifier.padding(
                    horizontal = 8.dp,
                    vertical = 2.dp
                ),
                text = "Qty:  ${addOn.quantity}"
            )
            Text(
                modifier = modifier.padding(
                    horizontal = 8.dp,
                    vertical = 2.dp
                ),
                text = "Subtotal:  ${addOn.total}"
            )
        }
    }
}


@Composable
fun NameCircle(
    modifier: Modifier = Modifier,
    name: String
) {
    val initials = getInitials(name)
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .size(40.dp)
            .background(MaterialTheme.colorScheme.primary, CircleShape),
        content = {
            Text(
                text = initials,
                style = MaterialTheme.typography.bodyLarge.copy(
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp,
                    color = MaterialTheme.colorScheme.onPrimary
                )
            )
        }
    )
}

@Composable
fun EmployeesCard(
    modifier: Modifier = Modifier,
    employee: Employee,
    assign: (Employee) -> Unit
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable { assign(employee) },
        verticalAlignment = Alignment.CenterVertically
    ) {
        NameCircle(
            name = employee.fullName
        )
        Spacer(modifier = modifier.width(16.dp))
        Column {
            Text(
                text = employee.fullName,
                style = MaterialTheme.typography.bodyLarge.copy(
                    fontWeight = FontWeight.W600
                )
            )
            Text(
                text = employee.email,
                style = MaterialTheme.typography.bodyLarge.copy(
                    fontWeight = FontWeight.W400
                )
            )
        }
    }
}

fun getInitials(name: String): String {
    return name.split(" ")
        .filter { it.isNotEmpty() }
        .map { it[0].uppercaseChar() }
        .take(2)
        .joinToString("")
}