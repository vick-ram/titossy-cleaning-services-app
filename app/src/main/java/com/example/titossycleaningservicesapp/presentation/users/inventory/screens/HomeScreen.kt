package com.example.titossycleaningservicesapp.presentation.users.inventory.screens

import android.content.ContentValues.TAG
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.MoreVert
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import com.example.titossycleaningservicesapp.core.CustomProgressIndicator
import com.example.titossycleaningservicesapp.domain.models.ui_models.Product
import com.example.titossycleaningservicesapp.domain.viewmodel.ProductViewModel
import com.example.titossycleaningservicesapp.presentation.utils.CustomSearch
import com.example.titossycleaningservicesapp.presentation.utils.SearchTextField

@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    paddingValues: PaddingValues
) {
    val context = LocalContext.current
    val productViewModel: ProductViewModel = hiltViewModel()
    val productsState by productViewModel.productDataUiState.collectAsState()
    val pUiState by productViewModel.productUiState.collectAsState()
    val search = rememberSaveable { mutableStateOf(TextFieldValue("")) }

    LaunchedEffect(productsState, productViewModel) {
        productViewModel.fetchProducts()
    }

    LaunchedEffect(pUiState) {
        when {
            pUiState.isLoading -> {}

            pUiState.successMessage.isNotEmpty() -> {
                Toast.makeText(
                    context,
                    pUiState.successMessage,
                    Toast.LENGTH_SHORT
                ).show()
            }

            pUiState.errorMessage.isNotEmpty() -> {}
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(paddingValues)
    ) {
        SearchTextField(
            value = search.value,
            onValueChange = {
                search.value = it
            }
        )

        when {

            productsState.products != null -> {
                productsState.products?.let { products ->
                    CustomSearch(
                        items = products,
                        searchQuery = search,
                        searchPredicate = { product: Product, query ->
                            product.name.contains(query, ignoreCase = true)
                        },
                        itemContent = { product, modifier ->
                            ProductCard(
                                modifier = modifier,
                                product = product,
                                onRestock = { quantity ->
                                    productViewModel.addProductToPurchaseOrder(
                                        productId = product.productId,
                                        quantity = quantity.toInt()
                                    )
                                    navController.navigate("purchaseOrder")
                                },
                                onDelete = { productViewModel.deleteProduct(it.productId) }
                            )
                        },
                        key = { product -> product.productId }
                    )
                }
            }

            productsState.isLoading -> {
                Box(
                    modifier = modifier
                        .fillMaxSize()
                        .padding(),
                    contentAlignment = Alignment.Center
                ) {
                    CustomProgressIndicator(isLoading = true)
                }
            }

            productsState.errorMessage.isNotEmpty() -> {
                Log.d(TAG, "HomeScreen: ${productsState.errorMessage}")
            }
        }
    }
    CustomProgressIndicator(isLoading = productsState.isLoading)
}

@Composable
fun ProductCard(
    modifier: Modifier = Modifier,
    product: Product,
    onRestock: (String) -> Unit,
    onDelete: (Product) -> Unit
) {
    var showDialog by rememberSaveable { mutableStateOf(false) }
    var expanded by rememberSaveable { mutableStateOf(false) }
    var quantity by rememberSaveable { mutableStateOf("") }
    val focusRequester = remember { FocusRequester() }

    LaunchedEffect(showDialog) {
        if (showDialog) {
            focusRequester.requestFocus()
        }
    }


    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Row(
            modifier = modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.surface)
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = rememberAsyncImagePainter(
                    model = product.image,
                    contentScale = ContentScale.Crop
                ),
                contentDescription = product.name,
                modifier = modifier
                    .size(100.dp),
                contentScale = ContentScale.Crop
            )
            Column {
                Text(
                    text = product.name,
                    style = MaterialTheme.typography.headlineSmall.copy(
                        fontWeight = FontWeight.W500,
                        fontSize = 16.sp,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                )
                Text(
                    text = "In Stock: ${product.stock}",
                    style = MaterialTheme.typography.headlineSmall.copy(
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp,
                        color = Color.Green
                    )
                )
            }
            Box {
                FilledIconButton(
                    colors = IconButtonDefaults.filledIconButtonColors(
                        containerColor = MaterialTheme.colorScheme.surfaceContainer,
                        contentColor = MaterialTheme.colorScheme.onSurfaceVariant
                    ),
                    onClick = { expanded = !expanded },
                ) {
                    Icon(
                        modifier = Modifier.size(32.dp),
                        imageVector = Icons.Rounded.MoreVert,
                        contentDescription = null
                    )
                }
                DropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false },
                    modifier = Modifier.width(IntrinsicSize.Min)
                ) {
                    DropdownMenuItem(
                        text = { Text("Restock") },
                        onClick = {
                            showDialog = true
                            expanded = false
                        }
                    )
                    DropdownMenuItem(
                        text = { Text("Delete") },
                        onClick = {
                            onDelete(product)
                            expanded = false
                        }
                    )
                }
            }
        }

    }
    if (showDialog) {
        Dialog(
            onDismissRequest = { showDialog = false }
        ) {

            Column(
                modifier = Modifier
                    .padding(16.dp)
                    .background(MaterialTheme.colorScheme.surface, RoundedCornerShape(8.dp))
                    .padding(16.dp)
            ) {

                Text(
                    text = "Enter Restock Quantity",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.padding(bottom = 8.dp)
                )

                BasicTextField(
                    value = quantity,
                    onValueChange = { quantity = it },
                    keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Done),
                    keyboardActions = KeyboardActions(onDone = {
                        onRestock(quantity)
                        showDialog = false
                    }),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp)
                        .background(
                            color = MaterialTheme.colorScheme.background,
                            shape = RoundedCornerShape(4.dp)
                        )
                        .padding(8.dp)
                        .focusRequester(focusRequester),
                )

                Spacer(modifier = Modifier.height(8.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    TextButton(
                        onClick = {
                            onRestock(quantity)
                            showDialog = false
                        },
                        enabled = quantity.isNotEmpty()
                    ) {
                        Text(text = "OK")
                    }
                }
            }
        }
    }
}
