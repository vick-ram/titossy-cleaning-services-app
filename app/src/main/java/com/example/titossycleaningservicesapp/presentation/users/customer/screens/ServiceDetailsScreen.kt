package com.example.titossycleaningservicesapp.presentation.users.customer.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material3.Button
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import com.example.titossycleaningservicesapp.R
import com.example.titossycleaningservicesapp.data.remote.api.ApiConstants.BASE_URL
import com.example.titossycleaningservicesapp.domain.models.ui_models.CartItem
import com.example.titossycleaningservicesapp.domain.viewmodel.ServiceViewModel
import com.example.titossycleaningservicesapp.presentation.users.customer.utils.BottomRow
import com.example.titossycleaningservicesapp.presentation.users.customer.utils.DetailsRoutes
import com.example.titossycleaningservicesapp.presentation.users.customer.utils.ServiceAddOnCard

@Composable
fun ServiceDetailsScreen(
    serviceId: String,
    onServiceDetailsBack: () -> Unit,
    navController: NavHostController
) {
    val columnState = rememberImeState()
    val scrollState = rememberScrollState()
    val showBottomRow = remember { mutableStateOf(true) }
    var showDescription by remember { mutableStateOf(false) }
    val viewModel: ServiceViewModel = hiltViewModel()
    val services by viewModel.serviceState.collectAsState()
    val service = services.services.find { it.id.toString() == serviceId }

    /*LaunchedEffect(key1 = columnState) {
        if (columnState.value) {
            scrollState.animateScrollTo(
                scrollState.maxValue,
                animationSpec = tween(600)
            )
        }
    }*/

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
        //.verticalScroll(scrollState)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            IconButton(
                onClick = { onServiceDetailsBack() }
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Default.ArrowBack,
                    contentDescription = null
                )
            }
        }

        ElevatedCard(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(18.dp))
                .padding(8.dp),
            colors = CardDefaults.elevatedCardColors(
                containerColor = MaterialTheme.colorScheme.surface,
                contentColor = MaterialTheme.colorScheme.onSurface
            )
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Image(
                    painter = painterResource(
                        id = R.drawable.cleaning1
                    )/*rememberAsyncImagePainter(
                       model =  ImageRequest.Builder(LocalContext.current)
                            .data("$BASE_URL${service?.image}")
                            .crossfade(true)
                            .placeholder(R.drawable.cleaning1)
                            .error(R.drawable.errorimg)
                            .build())*/,
                        //model = "$BASE_URL${service?.image}",
                        contentScale = ContentScale.Crop
                    ,
                    contentDescription = service?.name,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(150.dp),
                )

                Text(
                    text = service?.name ?: "",
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp
                )

                HorizontalDivider(
                    modifier = Modifier.fillMaxWidth()
                )

                AnimatedVisibility(
                    visible = !showDescription,
                ) {
                    Text(
                        text = service?.description ?: "",
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { showDescription = !showDescription },
                        color = Color.Gray,
                        fontSize = 16.sp
                    )
                }
            }
        }
        Spacer(modifier = Modifier.height(16.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Add ons",
                style = TextStyle(
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp,
                )
            )
        }

        LazyRow(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            //contentPadding = PaddingValues(8.dp)
        ) {
            items(service?.addOns.orEmpty()) { addon ->
                ServiceAddOnCard(
                    serviceAddOn = addon,
                    addToCart = {}
                )
            }
        }

        HorizontalDivider(
            modifier = Modifier
                .fillMaxWidth(),
            thickness = 2.dp
        )
        BottomRow(
            navController = navController,
            showBottomRow = showBottomRow,
            modifier = Modifier.navigationBarsPadding()
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CartBottomSheet(
    cart: MutableList<CartItem>,
    onItemRemoved: (CartItem) -> Unit,
    onPayment: () -> Unit
) {
    var showBottomSheet by remember { mutableStateOf(false) }
    var expanded by remember { mutableStateOf(false) }
    val sheetState = rememberModalBottomSheetState()

    fun addToCart(item: CartItem) {
        cart.add(item)
        showBottomSheet = true
    }

    ModalBottomSheet(
        sheetState = sheetState,
        onDismissRequest = { showBottomSheet = false },
        tonalElevation = 4.dp,
        shape = when (showBottomSheet) {
            true -> MaterialTheme.shapes.large
            false -> MaterialTheme.shapes.medium
        },
    ) {
        CarSheetContent(
            cart = cart,
            onItemRemoved = onItemRemoved,
            onPayment = { onPayment() },
            expanded = expanded,
            onExpandChange = { expanded = it }
        )
    }

}

@Composable
fun CarSheetContent(
    cart: MutableList<CartItem>,
    onItemRemoved: (CartItem) -> Unit,
    onPayment: () -> Unit,
    expanded: Boolean,
    onExpandChange: (Boolean) -> Unit
) {

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {

        Text(
            text = "Your cart",
            fontWeight = FontWeight.Bold,
            fontSize = 20.sp
        )

        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {

            items(cart) { cartItem ->
                CartItemRow(
                    item = cartItem,
                    onItemRemoved = onItemRemoved
                )
            }
        }

        val totalAmount = cart.sumOf { it.price }

        Text(
            text = "Total: $${String.format("%.2f", totalAmount)}",
            fontWeight = FontWeight.Bold,
            fontSize = 18.sp
        )

        Button(
            onClick = onPayment,
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text(text = "Proceed to Payment")
        }

        if (expanded) {
            Spacer(modifier = Modifier.height(16.dp))
            Text("Additional Details (Click to Hide)", color = Color.Gray)
            Spacer(modifier = Modifier.height(16.dp))
            Text("Placeholder for more details...")

            Spacer(modifier = Modifier.height(16.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = { onExpandChange(!expanded) }) {
                    Icon(
                        imageVector = if (expanded) Icons.Filled.ExpandLess else Icons.Filled.ExpandMore,
                        contentDescription = if (expanded) "Collapse Details" else "Expand Details"
                    )
                }
            }
        }
    }
}

@Composable
fun CartItemRow(
    item: CartItem,
    onItemRemoved: (CartItem) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column {
            Text(text = item.service.toString(), fontWeight = FontWeight.Bold)
            Text(text = item.service.toString())
        }
        Text(text = "$${String.format("%.2f", item.price)}")
        IconButton(onClick = { onItemRemoved(item) }) {
            Icon(imageVector = Icons.Filled.Delete, contentDescription = "Delete Item")
        }
    }
}


@Preview(showBackground = true, showSystemUi = true)
@Composable
fun CartBottomSheetPreview() {
    val cart = remember { mutableStateListOf<CartItem>() }
    CartBottomSheet(cart = cart, onItemRemoved = {}, onPayment = {})
}


@Composable
fun ApiMessageSnackbar(message: String?, onDismiss: () -> Unit) {
    var showSnackbar by remember { mutableStateOf(false) }
    val snackBarHostState = remember { SnackbarHostState() }

    LaunchedEffect(key1 = message) {
        if (message != null) {
            showSnackbar = true
        }
    }

    Scaffold(
        snackbarHost = {
            SnackbarHost(
                hostState = snackBarHostState,
                snackbar = { data ->
                    Snackbar(
                        snackbarData = data,
                    )
                }
            )
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            contentAlignment = Alignment.Center
        ) {
            if (showSnackbar) {
                LaunchedEffect(key1 = Unit) {
                    onDismiss()
                    showSnackbar = false
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ApiMessageSnackbarPreview() {
    ApiMessageSnackbar(message = "API response message", onDismiss = {})
}