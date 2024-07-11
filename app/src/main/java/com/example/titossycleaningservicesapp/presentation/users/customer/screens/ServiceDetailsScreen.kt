@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.titossycleaningservicesapp.presentation.users.customer.screens

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ChevronLeft
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
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
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
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
import androidx.compose.ui.zIndex
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import com.example.titossycleaningservicesapp.R
import com.example.titossycleaningservicesapp.core.CustomProgressIndicator
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
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    val viewModel: ServiceViewModel = hiltViewModel()
    val cartState by viewModel.cartUiState.collectAsState()
    val cartDataState by viewModel.cartDataUiState.collectAsStateWithLifecycle()
    var isLoading by rememberSaveable { mutableStateOf(false) }
    val sheetState = rememberModalBottomSheetState()
    var showBottomSheet by rememberSaveable { mutableStateOf(false) }
    val serviceUiState by viewModel.serviceState.collectAsStateWithLifecycle()
    val service = serviceUiState.services.find { it.id.toString() == serviceId }
    val cartClearState by viewModel.cartClearState.collectAsStateWithLifecycle()
    val serviceAddonUiState by viewModel.serviceAddonState.collectAsStateWithLifecycle()


    LaunchedEffect(key1 = viewModel) {
        when {
            cartState.loading -> isLoading = true
            cartState.message.isNotEmpty() -> {
                isLoading = false
                Toast.makeText(
                    context,
                    cartState.message,
                    Toast.LENGTH_LONG
                ).show()
            }

            cartState.error.isNotEmpty() -> {
                isLoading = false
                Toast.makeText(
                    context,
                    cartState.error,
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }
    LaunchedEffect(cartClearState, viewModel) {
        when {
            cartClearState.loading -> isLoading = true
            cartClearState.message.isNotEmpty() -> {
                isLoading = false
                Toast.makeText(
                    context,
                    cartClearState.message,
                    Toast.LENGTH_LONG
                ).show()
            }

            cartClearState.error.isNotEmpty() -> {
                isLoading = false
                Toast.makeText(
                    context,
                    cartClearState.error,
                    Toast.LENGTH_LONG
                ).show()
            }
        }

    }

    LaunchedEffect(serviceId, viewModel) {
       // viewModel.clearServiceAddons()
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
                        .systemBarsPadding()
                        .padding(16.dp)
                        .clickable { showBottomSheet = true },
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        modifier = Modifier.padding(4.dp),
                        text = "Total",
                        style = MaterialTheme.typography.bodyLarge.copy(
                            fontWeight = FontWeight.SemiBold,
                            color = MaterialTheme.colorScheme.onSurface
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
                    cartDataState.loading -> CustomProgressIndicator(
                        modifier = Modifier.align(Alignment.CenterHorizontally),
                        isLoading = true
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
                                            viewModel.removeServiceAddonFromCart(it.id)
                                                .also { viewModel.fetchCartItems() }
                                        }

                                        is CartItem.ServiceCartItem -> {
                                            viewModel.removeServiceFromCart(it.id)
                                                .also { viewModel.fetchCartItems() }
                                        }
                                    }
                                }
                            }
                        }
                    }

                    cartDataState.error.isNotEmpty() -> {
                        Toast.makeText(
                            context,
                            cartDataState.error,
                            Toast.LENGTH_LONG
                        ).show()
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
                    text = service?.description ?: "",
                    modifier = Modifier
                        .padding(16.dp),
                    color = Color.Gray,
                    fontSize = 14.sp
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
                        fontSize = 16.sp,
                        fontWeight = FontWeight.SemiBold
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
                        textAlign = TextAlign.Center
                    )
                }
            }

            item {
                Text(
                    text = "Addons",
                    fontWeight = FontWeight.SemiBold,
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
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .zIndex(1f)
                                .padding(16.dp)
                                .background(MaterialTheme.colorScheme.surfaceVariant),
                            contentAlignment = Alignment.Center,
                            content = { CustomProgressIndicator(isLoading = true)}
                        )
                    }

                    serviceAddonUiState.error != null -> {
                        val errorMessage = serviceAddonUiState.error?.getContentIfNotHandled()
                        if (errorMessage != null) {
                            Toast.makeText(
                                context,
                                errorMessage,
                                Toast.LENGTH_SHORT
                            ).show()
                        }

                    }
                }
            }
        }
    }
}
