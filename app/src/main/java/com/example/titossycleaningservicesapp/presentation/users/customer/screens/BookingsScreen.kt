package com.example.titossycleaningservicesapp.presentation.users.customer.screens

import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults.SecondaryIndicator
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.titossycleaningservicesapp.domain.models.Frequency
import kotlinx.coroutines.launch

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun BookingsScreen(navController: NavHostController, paddingValues: PaddingValues) {

    val tabList = listOf("ALL", "PENDING", "COMPLETED")
    val scope = rememberCoroutineScope()
    val pagerState = rememberPagerState(
        initialPage = 0,
        pageCount = { tabList.size }
    )
    Column(
        modifier = Modifier
            .padding(paddingValues)
            .padding(8.dp)
            .fillMaxSize()
    ) {
        TabRow(
            modifier = Modifier.fillMaxWidth(),
            selectedTabIndex = pagerState.currentPage,
            indicator = { tabPositions ->
                SecondaryIndicator(
                    modifier = Modifier.tabIndicatorOffset(tabPositions[pagerState.currentPage]),
                    color = MaterialTheme.colorScheme.primary
                )
            }
        ) {
            tabList.forEachIndexed { index, title ->
                val selected = pagerState.currentPage == index
                Tab(
                    text = {
                        Text(
                            title,
                            maxLines = 1,
                            overflow = TextOverflow.Clip,
                            fontWeight = FontWeight.Bold
                        )
                    },
                    selected = selected,
                    onClick = {
                        scope.launch {
                            pagerState.animateScrollToPage(index, animationSpec = tween(200))
                        }
                    }
                )
            }
        }
        HorizontalPager(
            state = pagerState,
            modifier = Modifier.weight(1f)
        ) { page ->
            when (page) {
                0 -> {
                    AllBookingsScreen(modifier = Modifier.fillMaxSize())
                }

                1 -> {
                    PendingBookingsScreen(modifier = Modifier.fillMaxSize())
                }

                2 -> {
                    CompletedBookingsScreen(modifier = Modifier.fillMaxSize())
                }
            }
        }
    }
}

@Composable
fun AllBookingsScreen(modifier: Modifier) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        AddItemScreen()
        //Text(text = "All Bookings Screen", fontSize = MaterialTheme.typography.titleLarge.fontSize)
    }
}

@Composable
fun PendingBookingsScreen(modifier: Modifier) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Pending Bookings Screen",
            fontSize = MaterialTheme.typography.titleLarge.fontSize
        )
    }
}

@Composable
fun CompletedBookingsScreen(modifier: Modifier) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Completed Bookings Screen",
            fontSize = MaterialTheme.typography.titleLarge.fontSize
        )
    }
}

@Composable
fun BookingCard(modifier: Modifier = Modifier, booking: BookingCardInfo) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(8.dp)
            .wrapContentHeight(),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 4.dp
        ),
        shape = RectangleShape
    ) {
        Row(
            modifier = modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.surface),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Column(
                modifier = modifier
                    .padding(8.dp),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(text = booking.bookingId)
                Text(text = booking.serviceName)
                Text(text = booking.address)
            }
            BookingRight(booking = booking)
        }
    }
}

data class BookingCardInfo(
    val bookingId: String,
    val frequency: Frequency,
    val address: String,
    val serviceName: String,
    val serviceAddonName: List<String>?,
    val bookingStatus: String
)

@Composable
fun BookingRight(modifier: Modifier = Modifier, booking: BookingCardInfo) {
    Column(
        modifier = modifier.padding(8.dp),
        verticalArrangement = Arrangement.spacedBy(4.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = booking.bookingStatus)
        OutlinedButton(onClick = { /*TODO*/ }) {
            Text(text = "View more")
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun BookingCardPreview(modifier: Modifier = Modifier) {
    val booking = BookingCardInfo(
        bookingId = "TUOPFCDL",
        frequency = Frequency.ONE_TIME,
        address = "Summervile",
        serviceName = "Home cleaning",
        serviceAddonName = null,
        bookingStatus = "PENDING"
    )
    Column(
        verticalArrangement = Arrangement.spacedBy(2.dp)
    ) {

        BookingCard(booking = booking)
        BookingCard(booking = booking)
        BookingCard(booking = booking)
        BookingCard(booking = booking)
        BookingCard(booking = booking)
        BookingCard(booking = booking)
    }
}


data class Product(
    val name: String = "",
    val price: String = "",
    val quantity: String = "",
    val sku: String = ""
)

@Composable
fun AddItemScreen() {
    var showForm by remember { mutableStateOf(false) }
    var products = remember{ mutableStateListOf<Product>() }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        if (!showForm) {
            Button(onClick = { showForm = true }) {
                Text("Add Item")
            }
        } else {
            products.forEachIndexed { index, product ->
                ProductForm(
                    product = product,
                    onProductChange = { updatedProduct ->
                        products[index] = updatedProduct
                    }
                )
            }
            Button(onClick = { products.add(Product()) }) {
                Text("Add Another Item")
            }
        }
    }
}

@Composable
fun ProductForm(product: Product, onProductChange: (Product) -> Unit) {
    var name by remember { mutableStateOf(product.name) }
    var price by remember { mutableStateOf(product.price) }
    var quantity by remember { mutableStateOf(product.quantity) }
    var sku by remember { mutableStateOf(product.sku) }

    Column(modifier = Modifier.padding(16.dp)) {
        OutlinedTextField(
            value = name,
            onValueChange = { name = it; onProductChange(product.copy(name = it)) },
            label = { Text("Name") }
        )
        OutlinedTextField(
            value = price,
            onValueChange = { price = it; onProductChange(product.copy(price = it)) },
            label = { Text("Price") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
        )
        OutlinedTextField(
            value = quantity,
            onValueChange = { quantity = it; onProductChange(product.copy(quantity = it)) },
            label = { Text("Quantity") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
        )
        OutlinedTextField(
            value = sku,
            onValueChange = { sku = it; onProductChange(product.copy(sku = it)) },
            label = { Text("SKU") }
        )
    }
}
