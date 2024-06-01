@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.titossycleaningservicesapp.presentation.users.customer.screens

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import com.example.titossycleaningservicesapp.R
import com.example.titossycleaningservicesapp.data.remote.api.ApiConstants.BASE_URL
import com.example.titossycleaningservicesapp.domain.viewmodel.ServiceViewModel
import com.example.titossycleaningservicesapp.presentation.users.customer.utils.DetailsRoutes
import com.example.titossycleaningservicesapp.presentation.users.customer.utils.ServiceAddOnCard
import com.example.titossycleaningservicesapp.presentation.users.customer.utils.ServiceCardInCart
import com.example.titossycleaningservicesapp.presentation.utils.CustomIndeterminateProgressIndicator
import kotlinx.coroutines.launch
import java.util.Calendar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ServiceDetailsScreen(
    serviceId: String,
    navController: NavHostController,
) {
    val viewModel: ServiceViewModel = hiltViewModel()
    val services by viewModel.serviceState.collectAsState()
    val service = services.services.find { it.id.toString() == serviceId }
    val cartState by viewModel.cartUiState.collectAsState()
    val cartDataState by viewModel.cartDataUiState.collectAsState()
    val context = LocalContext.current
    var isLoading by remember { mutableStateOf(false) }
    val sheetState = rememberModalBottomSheetState()
    val scope = rememberCoroutineScope()
    var showBottomSheet by remember { mutableStateOf(false) }

    LaunchedEffect(key1 = cartState) {
        when {
            cartState.loading -> isLoading = true
            cartState.message.isNotEmpty() -> {
                isLoading = false
                Toast.makeText(context, cartState.message, Toast.LENGTH_LONG).show()
            }

            cartState.error.isNotEmpty() -> {
                isLoading = false
                Toast.makeText(context, cartState.error, Toast.LENGTH_LONG).show()
            }
        }
    }


    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = service?.name ?: "") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            painter = painterResource(id = R.drawable.baseline_chevron_left_24),
                            contentDescription = null,
                            modifier = Modifier.size(32.dp)
                        )
                    }
                }
            )
        },
        bottomBar = {
            Column {
                HorizontalDivider(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 5.dp)
                )
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                        .clickable { showBottomSheet = true },
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column {
                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                modifier = Modifier.padding(bottom = 4.dp),
                                text = "Total",
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Icon(
                                imageVector = Icons.Filled.KeyboardArrowUp,
                                contentDescription = null,
                                modifier = Modifier.size(32.dp)
                            )
                        }
                        Text(
                            text = "Kshs: ${cartDataState.cartItems.sumOf { it.total }}",
                            color = MaterialTheme.colorScheme.onSurface,
                            fontWeight = FontWeight.Bold
                        )
                    }
                    Button(
                        onClick = { navController.navigate(DetailsRoutes.BookingDetails.route) }
                    ) {
                        Text(text = "Proceed")
                    }
                }
            }
        }
    ) { innerPadding ->

        if (showBottomSheet) {
            ModalBottomSheet(
                onDismissRequest = {
                    showBottomSheet = false
                },
                sheetState = sheetState,
                sheetMaxWidth = BottomSheetDefaults.SheetMaxWidth
            ) {
                when {
                    cartDataState.loading -> CustomIndeterminateProgressIndicator(isLoading = cartDataState.loading)
                    cartDataState.cartItems.isNotEmpty() -> {
                        LazyColumn(
                            state = rememberLazyListState(),
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            contentPadding = innerPadding
                        ) {
                            items(cartDataState.cartItems) { cartItem ->
                                ServiceCardInCart(cartItem = cartItem) {
                                    // TODO: i'm going to work on it
                                }
                            }
                        }
                    }

                    cartDataState.error.isNotEmpty() -> {
                        Toast.makeText(context, cartDataState.error, Toast.LENGTH_LONG).show()
                    }
                }
                Button(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    contentPadding = PaddingValues(18.dp),
                    onClick = {
                        scope.launch { sheetState.hide() }.invokeOnCompletion {
                            if (!sheetState.isVisible) {
                                showBottomSheet = false
                            }
                        }
                    }) {
                    Text(
                        text = "Hide bottom sheet",
                        fontSize = MaterialTheme.typography.labelLarge.fontSize
                    )
                }
            }
        }

        LazyColumn(
            modifier = Modifier
                .padding(innerPadding),
            contentPadding = innerPadding
        ) {
            item {
                Image(
                    painter = rememberAsyncImagePainter(
                        model = ImageRequest.Builder(LocalContext.current)
                            .data("$BASE_URL${service?.image}")
                            .crossfade(true)
                            .error(R.drawable.cleaning1)
                            .build()
                    ),
                    contentScale = ContentScale.Crop,
                    contentDescription = service?.name,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(150.dp),
                )
            }

            item {
                Text(
                    text = service?.name ?: "",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier
                        .padding(16.dp)
                        .fillMaxWidth(),
                    textAlign = TextAlign.Center
                )
            }

            item {
                Text(
                    text = service?.description ?: "",
                    modifier = Modifier
                        .padding(16.dp),
                    color = Color.Gray,
                    fontSize = 16.sp
                )
            }

            item {
                Row(
                    modifier = Modifier
                        .padding(16.dp)
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.End,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = service?.formattedPrice ?: "",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            item {
                Button(
                    onClick = {
                        service?.let {
                            viewModel.addServiceToCart(
                                serviceId = it.id,
                                quantity = 1
                            )
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    contentPadding = PaddingValues(16.dp),
                    elevation = ButtonDefaults.elevatedButtonElevation(
                        pressedElevation = 4.dp,
                        hoveredElevation = 2.dp
                    ),
                    shape = RectangleShape
                ) {
                    Text(
                        text = "Add service to cart",
                        fontSize = 18.sp,
                        textAlign = TextAlign.Center
                    )
                }
            }

            item {
                if (service?.addOns?.isNotEmpty() == true) {
                    Text(
                        text = "Addons",
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp,
                        modifier = Modifier.padding(16.dp)
                    )

                    LazyRow(
                        modifier = Modifier
                            .padding(horizontal = 16.dp)
                            .fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(service.addOns) { addon ->
                            ServiceAddOnCard(
                                serviceAddOn = addon,
                                addToCart = { serviceAddon ->
                                    viewModel.addServiceAddonToCart(
                                        serviceAddonId = serviceAddon.id,
                                        quantity = 1
                                    )
                                }
                            )
                        }
                    }
                }
                CustomIndeterminateProgressIndicator(isLoading = isLoading)
            }
        }
    }
}


@Composable
fun DateTimePickerDemo(paddingValues: PaddingValues, onNavigate: () -> Unit) {
    val context = LocalContext.current
    var selectedDate by remember { mutableStateOf("") }
    var selectedTime by remember { mutableStateOf("") }
    val frequency = Frequency.entries.toTypedArray().map { it.toString() }
    var selectedFrequency by remember { mutableStateOf(Frequency.ONE_TIME.name) }
    var instructions by remember { mutableStateOf("") }
    val hours = listOf("2Hrs", "3Hrs", "4Hrs", "5Hrs", "6Hrs", "7Hrs", "8Hrs")
    val employees = listOf(1, 2, 3, 4, 5, 6, 7, 8)
    var selectedEmployee by rememberSaveable { mutableIntStateOf(1) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues)
            .verticalScroll(state = rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        Text(
            modifier = Modifier
                .padding(8.dp),
            text = "Select date you wish",
            style = MaterialTheme.typography.titleMedium
        )
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            TextField(
                value = selectedDate,
                onValueChange = { selectedDate = it },
                label = {
                    if (selectedDate.isEmpty()) {
                        Text(
                            "Select date",
                            fontSize = 16.sp,
                            color = MaterialTheme.colorScheme.onSurface.copy(0.6f)
                        )
                    }
                },
                readOnly = true,
                modifier = Modifier.weight(1f)
            )
            Spacer(modifier = Modifier.width(8.dp))
            IconButton(
                onClick = {
                    showDatePicker(
                        onDateSelected = { selectedDate = it },
                        context = context
                    )
                }
            ) {
                Icon(
                    modifier = Modifier.size(32.dp),
                    imageVector = Icons.Default.CalendarMonth,
                    contentDescription = null,
                    tint = colorResource(id = R.color.teal_700)
                )
            }
        }
        Text(
            modifier = Modifier.padding(start = 8.dp),
            text = "Select time you wish",
            style = MaterialTheme.typography.titleMedium
        )

        Row(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            TextField(
                value = selectedTime,
                onValueChange = { selectedTime = it },
                label = {
                    if (selectedTime.isEmpty()) {
                        Text(
                            "Select Time..",
                            fontSize = 16.sp,
                            color = MaterialTheme.colorScheme.onSurface.copy(0.6f)
                        )
                    }
                },
                readOnly = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.weight(1f)
            )
            Spacer(modifier = Modifier.width(8.dp))
            IconButton(onClick = {
                showTimePicker(
                    onTimeSelected = { selectedTime = it },
                    context = context
                )
            }) {
                Icon(
                    modifier = Modifier.size(32.dp),
                    imageVector = Icons.Default.Schedule,
                    contentDescription = null,
                    tint = colorResource(id = R.color.teal_700)
                )
            }

        }
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            modifier = Modifier.padding(start = 8.dp),
            text = "Select number of cleaners you wish:",
            style = MaterialTheme.typography.titleMedium
        )
        SelectableBox(
            boxes = employees,
            value = selectedEmployee,
            onSelected = { selectedEmployee = it }
        )
        Text(
            modifier = Modifier.padding(start = 8.dp),
            text = "Select hours you wish",
            style = MaterialTheme.typography.titleMedium
        )
        RangeLazyRow(onRangeSelected = {}, ranges = hours)
        CustomRadioButton(
            values = frequency,
            selected = selectedFrequency,
            onSelected = { newValue ->
                selectedFrequency = newValue
            }
        )
        AdditionalInstructions(
            modifier = Modifier.align(Alignment.CenterHorizontally),
            value = instructions,
            onValueChange = { instructions = it }
        )

        Spacer(modifier = Modifier.height(8.dp))
        Button(
            modifier = Modifier
                .fillMaxWidth(0.8f)
                .align(Alignment.CenterHorizontally)
                .shadow(elevation = 1.dp),
            shape = RectangleShape,
            onClick = { onNavigate() },
            contentPadding = PaddingValues(16.dp)
        ) {
            Text(text = "Proceed to checkout")
        }
    }
}

private fun showDatePicker(onDateSelected: (String) -> Unit, context: Context) {
    val calendar = Calendar.getInstance()
    val datePickerDialog = DatePickerDialog(
        context,
        { _, year, monthOfYear, dayOfMonth ->
            val selectedDate = "$dayOfMonth/${monthOfYear + 1}/$year"
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
            val selectedTime = "$hourOfDay:$minuteOfHour"
            onTimeSelected(selectedTime)
        },
        calendar.get(Calendar.HOUR_OF_DAY),
        calendar.get(Calendar.MINUTE),
        true
    )
    timePickerDialog.show()
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun SelectableBox(
    modifier: Modifier = Modifier,
    boxes: List<Int>,
    value: Int,
    onSelected: (Int) -> Unit,
) {

    FlowRow(
        modifier = Modifier
            .padding(8.dp),
    ) {
        boxes.forEach { box ->
            Box(
                modifier = modifier
                    .size(70.dp)
                    .padding(8.dp)
                    .clip(CircleShape)
                    .background(if (value == box) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.primaryContainer)
                    .border(BorderStroke(0.7.dp, MaterialTheme.colorScheme.onSurface), CircleShape)
                    .shadow(elevation = 0.dp)
                    .clickable { onSelected(box) },
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = box.toString(),
                    color = if (value == box) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onBackground
                )
            }
        }
    }
}

enum class Frequency {
    ONE_TIME, WEEKLY, BIWEEKLY, MONTHLY
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
            .fillMaxWidth()
            .padding(16.dp),
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
            .fillMaxWidth(.8f)
            .height(100.dp),
        placeholder = {
            Text(
                text = "Additional instructions..",
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
            )
        }
    )
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun RangeLazyRow(onRangeSelected: (String) -> Unit, ranges: List<String>) {
    var selectedRange by remember { mutableStateOf<String?>(null) }

    FlowRow(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalArrangement = Arrangement.SpaceEvenly,
        maxItemsInEachRow = 3
    ) {
        ranges.forEach { range ->
            Box(
                modifier = Modifier
                    .size(width = 80.dp, height = 50.dp)
                    .padding(8.dp)
                    .clip(RoundedCornerShape(50))
                    .border(
                        BorderStroke(width = 1.dp, color = MaterialTheme.colorScheme.onSurface),
                        RoundedCornerShape(50)
                    )
                    .background(if (range == selectedRange) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.primaryContainer)
                    .clickable {
                        selectedRange = range
                        onRangeSelected(range)
                    },
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = range,
                    modifier = Modifier
                        .padding(8.dp),
                    color = if (range == selectedRange) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onBackground
                )
            }
        }
    }
}




