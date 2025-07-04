@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.titossycleaningservicesapp.presentation.users.customer.utils

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronLeft
import androidx.compose.material.icons.outlined.CalendarMonth
import androidx.compose.material.icons.outlined.Schedule
import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.titossycleaningservicesapp.R
import com.example.titossycleaningservicesapp.core.LoadingButton
import com.example.titossycleaningservicesapp.core.generateItextPdf
import com.example.titossycleaningservicesapp.core.generatePdf
import com.example.titossycleaningservicesapp.core.showToast
import com.example.titossycleaningservicesapp.domain.models.Frequency
import com.example.titossycleaningservicesapp.domain.models.requests.booking.BookingRequest
import com.example.titossycleaningservicesapp.domain.models.ui_models.Booking
import com.example.titossycleaningservicesapp.domain.viewmodel.BookingViewModel
import com.example.titossycleaningservicesapp.domain.viewmodel.ServiceViewModel
import kotlinx.coroutines.delay
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.util.Calendar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BookingDataScreen(
    navController: NavHostController,
) {
    val bookingViewModel: BookingViewModel = hiltViewModel()
    val bookingUiState by bookingViewModel.bookingUiState.collectAsState()
    val serviceViewModel: ServiceViewModel = hiltViewModel()

    val context = LocalContext.current
    var selectedDate by remember { mutableStateOf("") }
    var selectedTime by remember { mutableStateOf("") }
    val frequency = Frequency.entries.toTypedArray().map { it.toString() }
    var selectedFrequency by rememberSaveable { mutableStateOf(Frequency.ONE_TIME.name) }
    var instructions by rememberSaveable { mutableStateOf("") }
    var address by rememberSaveable { mutableStateOf("") }

    LaunchedEffect(bookingViewModel) {
        bookingViewModel.fetchBookings()
    }

    LaunchedEffect(bookingUiState) {
        when {
            bookingUiState.isLoading -> delay(100L)
            bookingUiState.isSuccess.isNotEmpty() -> {
                showToast(
                    context = context,
                    length = Toast.LENGTH_LONG,
                    message = bookingUiState.isSuccess
                )
                bookingUiState.booking?.let { booking ->
                    navController.navigate(
                        DetailsRoutes.CheckOut.route + "/${booking.bookingId}"
                    )
                }
            }

            bookingUiState.errorMessage.isNotEmpty() -> {
                showToast(
                    context = context,
                    message = bookingUiState.errorMessage
                )
                serviceViewModel.clearCart()
            }
        }
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = "Booking Details"
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { navController.navigateUp(); serviceViewModel.clearCart() }) {
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

        val bookingRequest = BookingRequest(
            bookingDate = selectedDate,
            bookingTime = selectedTime,
            frequency = Frequency.valueOf(selectedFrequency),
            additionalInstructions = instructions,
            address = address
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp)
                .verticalScroll(state = rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            BookingItemSection(title = "Choose Date:") {
                OutlinedTextField(
                    modifier = Modifier.fillMaxWidth(),
                    value = selectedDate,
                    onValueChange = {},
                    readOnly = true,
                    trailingIcon = {
                        IconButton(onClick = {
                            showDatePicker(
                                onDateSelected = { selectedDate = it },
                                context = context
                            )
                        }) {
                            Icon(
                                imageVector = Icons.Outlined.CalendarMonth,
                                contentDescription = "date"
                            )
                        }
                    },
                    placeholder = {
                        Text(
                            text = "Select date",
                            style = MaterialTheme.typography.bodyLarge.copy(
                                color = MaterialTheme.colorScheme.onSurface.copy(.5f)
                            )
                        )
                    },
                    shape = MaterialTheme.shapes.small
                )
            }
            BookingItemSection(title = "Choose Time:") {
                OutlinedTextField(
                    modifier = Modifier.fillMaxWidth(),
                    value = selectedTime,
                    onValueChange = {},
                    readOnly = true,
                    trailingIcon = {
                        IconButton(onClick = {
                            showTimePicker(
                                onTimeSelected = { selectedTime = it },
                                context = context
                            )
                        }) {
                            Icon(
                                imageVector = Icons.Outlined.Schedule,
                                contentDescription = "time"
                            )
                        }
                    },
                    placeholder = {
                        Text(
                            text = "Select time",
                            style = MaterialTheme.typography.bodyLarge.copy(
                                color = MaterialTheme.colorScheme.onSurface.copy(.5f)
                            )
                        )
                    },
                    shape = MaterialTheme.shapes.small
                )
            }
            BookingItemSection(title = "Choose Frequency:") {
                CustomRadioButton(
                    values = frequency,
                    selected = selectedFrequency,
                    onSelected = { selectedFrequency = it }
                )
            }
            BookingItemSection(title = "Additional Instructions:") {
                AdditionalInstructions(
                    value = instructions,
                    onValueChange = { instructions = it }
                )
            }
            BookingItemSection(title = "Booking Location:") {
                BookingInput(
                    value = address,
                    onValueChange = { address = it },
                    label = "Enter booking address"
                )
            }
            Button(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp)
                    .align(Alignment.CenterHorizontally),
                shape = RectangleShape,
                onClick = {
                    bookingViewModel.createBooking(bookingRequest)
                },
                contentPadding = PaddingValues(16.dp),
                enabled = selectedDate.isNotEmpty()
                        && selectedTime.isNotEmpty()
                        && address.isNotEmpty()
            ) { Text(text = "Proceed to checkout") }

//            Button(
//                onClick = {
//                    bookingUiState.bookings?.let {
//                        generateItextPdf(
//                            context = context,
//                            fileName = "Booking",
//                            data = it,
//                            headerTitles = listOf(
//                                "Booking ID",
//                                "Customer",
//                                "Date",
//                                "Frequency",
//                                "Address",
//                                "Instructions",
//                                "Amount",
//                                "Paid",
//                                "Status"
//                            ),
//                            title = "Titossy Cleaning Service",
//                            logoResId = R.drawable.titossy_logo,
//                            contentBuilder = { booking, table ->
//                                table.addCell(booking.bookingId)
//                                table.addCell(booking.customer)
//                                table.addCell(booking.formattedDate)
//                                table.addCell(booking.frequency.name)
//                                table.addCell(booking.address)
//                                booking.additionalInstructions.let {
//                                    table.addCell(booking.additionalInstructions)
//                                }
//                                table.addCell(booking.amount)
//                                table.addCell(booking.paidString)
//                                table.addCell(booking.bookingStatus.name)
//                            },
//                            onComplete = { filePath ->
//                                filePath?.let { path ->
//                                    Toast.makeText(context, "PDF saved to $path", Toast.LENGTH_LONG)
//                                        .show()
//                                }
//                            }
//                        )
//                    }
//                }
//            ) {
//                Text("Download")
//            }
        }
    }
}


@Composable
fun BookingItemSection(
    modifier: Modifier = Modifier,
    title: String,
    content: @Composable () -> Unit
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.titleSmall.copy(
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )
        )
        Spacer(modifier = Modifier.height(8.dp))
        content()
    }
}


private fun showDatePicker(onDateSelected: (String) -> Unit, context: Context) {
    val calendar = Calendar.getInstance()
    val datePickerDialog = DatePickerDialog(
        context,
        { _, year, monthOfYear, dayOfMonth ->
            val selectedDate = LocalDate.of(year, monthOfYear + 1, dayOfMonth)
                .format(DateTimeFormatter.ISO_LOCAL_DATE)
            onDateSelected(selectedDate)
        },
        calendar.get(Calendar.YEAR),
        calendar.get(Calendar.MONTH),
        calendar.get(Calendar.DAY_OF_MONTH)
    )
    datePickerDialog.datePicker.minDate = calendar.timeInMillis
    datePickerDialog.show()
}


private fun showTimePicker(onTimeSelected: (String) -> Unit, context: Context) {
    val calendar = Calendar.getInstance()
    val timePickerDialog = TimePickerDialog(
        context,
        { _, hourOfDay, minuteOfHour ->
            val selectedTime = LocalTime.of(hourOfDay, minuteOfHour)
            val currentTime = LocalTime.now()
            if (selectedTime.isBefore(currentTime)) {
                Toast.makeText(
                    context,
                    "Please select a future time",
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                onTimeSelected(selectedTime.format(DateTimeFormatter.ISO_LOCAL_TIME))
            }
        },
        calendar.get(Calendar.HOUR_OF_DAY),
        calendar.get(Calendar.MINUTE),
        true
    )
    timePickerDialog.show()
}

@Composable
fun CustomRadioButton(
    modifier: Modifier = Modifier,
    values: List<String>,
    selected: String,
    onSelected: (String) -> Unit
) {
    Column(
        modifier = modifier
            .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        values.chunked(2)
            .forEach { row ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    row.forEach { value ->
                        Row(
                            modifier = Modifier
                                .weight(1f)
                                .padding(8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            RadioButton(
                                selected = value == selected,
                                onClick = { onSelected(value) }
                            )
                            Text(text = value)
                        }
                    }
                }
            }
    }
}

@Composable
fun AdditionalInstructions(
    modifier: Modifier = Modifier,
    value: String,
    onValueChange: (String) -> Unit
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = modifier
            .fillMaxWidth()
            .height(100.dp),
        placeholder = {
            Text(
                text = "Enter instructions(Optional)",
                style = MaterialTheme.typography.bodyMedium.copy(
                    color = MaterialTheme.colorScheme.onSurface.copy(.5f)
                )
            )
        },
        textStyle = MaterialTheme.typography.bodyMedium,
        shape = MaterialTheme.shapes.medium,
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = MaterialTheme.colorScheme.onSurface.copy(.2f),
            focusedContainerColor = MaterialTheme.colorScheme.surface,
            unfocusedContainerColor = MaterialTheme.colorScheme.surfaceContainer,
            unfocusedBorderColor = MaterialTheme.colorScheme.onSurface.copy(.3f)
        )
    )
}

@Composable
fun BookingInput(
    modifier: Modifier = Modifier,
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    maxLines: Int = 1,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default
) {

    OutlinedTextField(
        modifier = modifier
            .fillMaxWidth(),
        value = value,
        onValueChange = onValueChange,
        textStyle = MaterialTheme.typography.bodyLarge,
        maxLines = maxLines,
        keyboardOptions = keyboardOptions,
        placeholder = {
            Text(
                text = label,
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurface.copy(.5f)
            )
        },
        shape = MaterialTheme.shapes.medium,
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = MaterialTheme.colorScheme.onSurface.copy(.2f),
            focusedContainerColor = MaterialTheme.colorScheme.surface,
            unfocusedContainerColor = MaterialTheme.colorScheme.surfaceContainer,
            unfocusedBorderColor = MaterialTheme.colorScheme.onSurface.copy(.3f)
        )
    )
}
