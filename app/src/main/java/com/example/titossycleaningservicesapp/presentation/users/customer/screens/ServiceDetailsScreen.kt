@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.titossycleaningservicesapp.presentation.users.customer.screens

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ChevronLeft
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import com.example.titossycleaningservicesapp.R
import com.example.titossycleaningservicesapp.domain.models.ui_models.CartItem
import com.example.titossycleaningservicesapp.domain.viewmodel.ServiceViewModel
import com.example.titossycleaningservicesapp.presentation.users.customer.utils.DetailsRoutes
import com.example.titossycleaningservicesapp.presentation.users.customer.utils.ServiceAddOnCard
import com.example.titossycleaningservicesapp.presentation.users.customer.utils.ServiceCardInCart
import com.example.titossycleaningservicesapp.presentation.utils.NavigationIcon
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ServiceDetailsScreen(
    serviceId: String,
    navController: NavHostController,
) {


    val scope = rememberCoroutineScope()

    val viewModel: ServiceViewModel = hiltViewModel()
    val cartState by viewModel.cartUiState.collectAsState()
    val cartDataState by viewModel.cartDataUiState.collectAsState()
    val context = LocalContext.current
    var isLoading by remember { mutableStateOf(false) }
    val sheetState = rememberModalBottomSheetState()
    var showBottomSheet by remember { mutableStateOf(false) }
    val serviceUiState by viewModel.serviceState.collectAsState()
    val service = serviceUiState.services.find { it.id.toString() == serviceId }
    val cartClearState by viewModel.cartClearState.collectAsState()
    val serviceAddonUiState by viewModel.serviceAddonState.collectAsState()


    LaunchedEffect(key1 = cartState) {
        when {
            cartState.loading -> isLoading = true
            cartState.message.isNotEmpty() -> {
                isLoading = false
                //Toast.makeText(context, cartState.message, Toast.LENGTH_LONG).show()
            }

            cartState.error.isNotEmpty() -> {
                isLoading = false
                //Toast.makeText(context, cartState.error, Toast.LENGTH_LONG).show()
            }
        }
    }
    LaunchedEffect(key1 = cartClearState) {
        when {
            cartClearState.loading -> isLoading = true
            cartClearState.message.isNotEmpty() -> {
                isLoading = false
                //Toast.makeText(context, cartClearState.message, Toast.LENGTH_LONG).show()
            }

            cartClearState.error.isNotEmpty() -> {
                isLoading = false
                //Toast.makeText(context, cartClearState.error, Toast.LENGTH_LONG).show()
            }
        }

    }

    LaunchedEffect(serviceId) {
        viewModel.clearServiceAddons()
        viewModel.fetchServiceAddons(serviceId)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = service?.name ?: "") },
                navigationIcon = {
                    NavigationIcon(
                        modifier = Modifier.padding(end = 8.dp),
                        icon = Icons.Outlined.ChevronLeft
                    ) {
                        if (cartDataState.cartItems.isNotEmpty()) {
                            viewModel.clearCart()
                        }
                        navController.navigateUp()
                    }
                }
            )
        },
        bottomBar = {
            Column {
                HorizontalDivider(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 5.dp),
                    thickness = 2.dp
                )
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                        .clickable { showBottomSheet = true },
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        modifier = Modifier.padding(4.dp),
                        text = "Total",
                        style = MaterialTheme.typography.bodyLarge.copy(
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface,
                            fontSize = 18.sp
                        )
                    )
                    Text(
                        text = "Kshs: ${cartDataState.cartItems.sumOf { it.total }}",
                        color = MaterialTheme.colorScheme.onSurface,
                        fontWeight = FontWeight.Bold
                    )
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
                    cartDataState.loading -> CircularProgressIndicator(
                        modifier = Modifier.align(Alignment.CenterHorizontally)
                    )

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
                                    when (cartItem) {
                                        is CartItem.ServiceAddonCartItem -> {
                                            viewModel.removeServiceAddonFromCart(cartItem.id)
                                            viewModel.fetchCartItems()
                                        }

                                        is CartItem.ServiceCartItem -> {
                                            viewModel.removeServiceFromCart(cartItem.id)
                                            viewModel.fetchCartItems()
                                        }
                                    }
                                }
                            }
                        }
                    }

                    cartDataState.error.isNotEmpty() -> {
                        //Toast.makeText(context, cartDataState.error, Toast.LENGTH_LONG).show()
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
                                navController.navigate(DetailsRoutes.BookingDetails.route)
                            }
                        }
                    },
                    enabled = cartDataState.cartItems.isNotEmpty()
                ) {
                    Text(
                        text = "Proceed",
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
                            .data(service?.image)
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
                            viewModel.fetchCartItems()
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
                        text = "Book Now",
                        fontSize = 18.sp,
                        textAlign = TextAlign.Center
                    )
                }
            }

            item {
                Text(
                    text = "Addons",
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    modifier = Modifier.padding(16.dp)
                )
                when {
                    serviceAddonUiState.serviceAddons.isNotEmpty() -> {
                        LazyRow(
                            modifier = Modifier
                                .padding(horizontal = 16.dp)
                                .fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            items(serviceAddonUiState.serviceAddons) { addon ->
                                ServiceAddOnCard(
                                    serviceAddOn = addon,
                                    addToCart = { serviceAddon ->
                                        viewModel.addServiceAddonToCart(
                                            serviceAddonId = serviceAddon.id,
                                            quantity = 1
                                        )
                                        viewModel.fetchCartItems()
                                    }
                                )
                            }
                        }
                    }

                    serviceAddonUiState.isLoading -> {
                        CircularProgressIndicator()
                    }

                    serviceAddonUiState.error != null -> {
                        val errorMessage = serviceAddonUiState.error?.getContentIfNotHandled()
                        if (errorMessage != null) {
                            Toast.makeText(context, errorMessage, Toast.LENGTH_SHORT).show()
                        }

                    }
                }
            }
        }
    }
}

/*private fun showDatePicker(onDateSelected: (String) -> Unit, context: Context) {
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
}*/





