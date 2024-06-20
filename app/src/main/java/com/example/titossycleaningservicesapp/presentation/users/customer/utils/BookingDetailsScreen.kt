@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.titossycleaningservicesapp.presentation.users.customer.utils

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Context
import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ChevronLeft
import androidx.compose.material.icons.outlined.MoreVert
import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
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
import com.example.titossycleaningservicesapp.domain.models.Frequency
import com.example.titossycleaningservicesapp.domain.models.requests.booking.BookingRequest
import com.example.titossycleaningservicesapp.domain.viewmodel.BookingViewModel
import com.example.titossycleaningservicesapp.domain.viewmodel.ServiceViewModel
import com.example.titossycleaningservicesapp.presentation.utils.BookingTimeCard
import com.example.titossycleaningservicesapp.presentation.utils.NavigationIcon
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.util.Calendar

@Composable
fun BookingDetailsScreen(
    navController: NavHostController,
) {
    val bookingViewModel: BookingViewModel = hiltViewModel()
    val bookingUiState by bookingViewModel.bookingUiState.collectAsState()

    val context = LocalContext.current
    var selectedDate by remember { mutableStateOf("") }
    var selectedTime by remember { mutableStateOf("") }
    val frequency = Frequency.entries.toTypedArray().map { it.toString() }
    var selectedFrequency by remember { mutableStateOf(Frequency.ONE_TIME.name) }
    var instructions by remember { mutableStateOf("") }
    var address by remember { mutableStateOf("") }

    LaunchedEffect(bookingUiState.booking) {
        bookingUiState.booking?.let { booking ->
            navController.navigate(
                DetailsRoutes.CheckOut.route + "/${booking.bookingId}"
            )
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
            BookingItemTitle(
                modifier = Modifier.padding(top = 8.dp),
                value = "Please choose a date & Time you would like the service to be done:"
            )
            BookingTimeCard(
                selectedDate = selectedDate,
                selectedTime = selectedTime,
                onDateSelected = {
                    showDatePicker(
                        onDateSelected = { selectedDate = it },
                        context = context
                    )
                },
                onTimeSelected = {
                    showTimePicker(
                        onTimeSelected = { selectedTime = it },
                        context = context
                    )
                }
            )
            BookingItemTitle(value = "Choose the frequency of your booking: ")
            CustomRadioButton(
                values = frequency,
                selected = selectedFrequency,
                onSelected = { selectedFrequency = it })
            BookingItemTitle(value = "Type any additional instructions you would like our team to consider:")
            AdditionalInstructions(value = instructions, onValueChange = { instructions = it })
            BookingItemTitle(value = "Type the location address you would our team to report to:")
            BookingAddressField(value = address, onValueChange = { address = it })
            Button(
                modifier = Modifier
                    .fillMaxWidth(0.8f)
                    .padding(vertical = 16.dp)
                    .align(Alignment.CenterHorizontally),
                shape = RectangleShape,
                onClick = {
                    bookingViewModel.createBooking(bookingRequest)
                    //serviceViewModel.clearCart()
                },
                contentPadding = PaddingValues(16.dp),
                enabled = selectedDate.isNotEmpty() && selectedTime.isNotEmpty() && address.isNotEmpty()
            ) { Text(text = "Proceed to checkout") }
        }
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
    datePickerDialog.show()
}


private fun showTimePicker(onTimeSelected: (String) -> Unit, context: Context) {
    val calendar = Calendar.getInstance()
    val timePickerDialog = TimePickerDialog(
        context,
        { _, hourOfDay, minuteOfHour ->
            val selectedTime = LocalTime.of(hourOfDay, minuteOfHour)
                .format(DateTimeFormatter.ISO_LOCAL_TIME)
            onTimeSelected(selectedTime)
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
                text = "Additional instructions..",
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
            )
        }
    )
}

@Composable
fun BookingAddressField(
    modifier: Modifier = Modifier,
    value: String, onValueChange: (String) -> Unit
) {
    OutlinedTextField(
        modifier = modifier
            .fillMaxWidth(),
        value = value,
        onValueChange = onValueChange,
        placeholder = {
            Text(
                text = "Enter booking address",
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.76f)
            )
        },
        shape = MaterialTheme.shapes.small
    )
}

@Composable
fun BookingItemTitle(modifier: Modifier = Modifier, value: String) {
    Text(
        modifier = modifier,
        text = value,
        style = MaterialTheme.typography.titleSmall.copy(
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold
        )
    )
}