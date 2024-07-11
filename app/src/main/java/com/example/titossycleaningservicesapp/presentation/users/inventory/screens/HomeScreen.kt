package com.example.titossycleaningservicesapp.presentation.users.inventory.screens

import android.content.ContentValues.TAG
import android.icu.text.DecimalFormat
import android.net.Uri
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
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
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AttachFile
import androidx.compose.material.icons.rounded.MoreVert
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
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
import androidx.compose.ui.window.DialogProperties
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import com.example.titossycleaningservicesapp.core.CustomProgressIndicator
import com.example.titossycleaningservicesapp.core.FileUtils
import com.example.titossycleaningservicesapp.core.showToast
import com.example.titossycleaningservicesapp.domain.models.ui_models.Product
import com.example.titossycleaningservicesapp.domain.viewmodel.ProductViewModel
import com.example.titossycleaningservicesapp.presentation.utils.CustomSearch
import com.example.titossycleaningservicesapp.presentation.utils.SearchTextField
import kotlinx.coroutines.delay
import java.math.BigDecimal

@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    paddingValues: PaddingValues
) {
    val context = LocalContext.current
    var requestFile by remember { mutableStateOf<Uri?>(null) }
    var fileName by remember { mutableStateOf("") }
    var openDialog by rememberSaveable { mutableStateOf(false) }
    val launcher =
        rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
            requestFile = uri
            uri?.let {
                fileName = FileUtils.getFileName(context, uri) ?: ""
            }
        }
    val productViewModel: ProductViewModel = hiltViewModel()
    val productsState by productViewModel.productDataUiState.collectAsState()
    val pUiState by productViewModel.productUiState.collectAsState()
    val search = remember { mutableStateOf(TextFieldValue("")) }

    LaunchedEffect(productsState, productViewModel) {
        productViewModel.fetchProducts()
    }

    LaunchedEffect(pUiState) {
        when {
            pUiState.isLoading -> {
                delay(100L)
            }

            pUiState.successMessage.isNotEmpty() -> {
                showToast(
                    context = context,
                    message = pUiState.successMessage
                )
                productViewModel.fetchProducts()
                openDialog = false
            }

            pUiState.errorMessage.isNotEmpty() -> {
                showToast(
                    context = context,
                    message = pUiState.errorMessage
                )
            }
        }
    }
    Box(
        modifier = modifier
            .fillMaxSize()
            .padding(paddingValues)
    ) {
        Column(
            modifier = modifier
                .fillMaxWidth()
        ) {
            /*productsState.products?.let { products ->
                OverviewSection(
                    modifier = modifier.padding(horizontal = 8.dp),
                    totalItems = products.size,
                    totalValue = products.sumOf { it.unitPrice.toBigDecimal() * it.stock.toBigDecimal() }
                )
            }
            Spacer(modifier = modifier.height(8.dp))*/

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
            if (openDialog) {
                AlertDialog(
                    onDismissRequest = { openDialog = false },
                    confirmButton = {
                        TextButton(
                            onClick = {
                                requestFile?.let {
                                    productViewModel.createProduct(
                                        context = context,
                                        uri = it
                                    )
                                }
                            },
                            enabled = productViewModel.name.isNotBlank()
                                    && productViewModel.description.isNotBlank()
                                    && productViewModel.price.isNotBlank()
                        ) {
                            Text(text = "Create")
                        }
                    },
                    dismissButton = {
                        TextButton(onClick = { openDialog = false }) {
                            Text(text = "Cancel")
                        }
                    },
                    title = {
                        Text(text = "Add new inventory")
                    },
                    text = {
                        Column(
                            modifier = modifier
                                .fillMaxWidth()
                                .verticalScroll(state = rememberScrollState())
                        ) {
                            OutlinedTextField(
                                modifier = modifier
                                    .fillMaxWidth()
                                    .padding(16.dp),
                                value = productViewModel.name,
                                onValueChange = { productViewModel.name = it },
                                shape = MaterialTheme.shapes.medium,
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedContainerColor = MaterialTheme.colorScheme.surfaceContainer,
                                    unfocusedContainerColor = MaterialTheme.colorScheme.surfaceContainer,
                                    focusedBorderColor = Color.Transparent,
                                    unfocusedBorderColor = Color.Transparent
                                ),
                                placeholder = {
                                    Text(
                                        text = "Name",
                                        style = MaterialTheme.typography.bodyLarge.copy(
                                            color = MaterialTheme.colorScheme.onSurface.copy(.5f)
                                        )
                                    )
                                },
                                textStyle = MaterialTheme.typography.bodyLarge.copy(
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                            )
                            OutlinedTextField(
                                modifier = modifier
                                    .fillMaxWidth()
                                    .padding(16.dp),
                                value = productViewModel.description,
                                onValueChange = { productViewModel.description = it },
                                shape = MaterialTheme.shapes.medium,
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedContainerColor = MaterialTheme.colorScheme.surfaceContainer,
                                    unfocusedContainerColor = MaterialTheme.colorScheme.surfaceContainer,
                                    focusedBorderColor = Color.Transparent,
                                    unfocusedBorderColor = Color.Transparent
                                ),
                                placeholder = {
                                    Text(
                                        text = "Description",
                                        style = MaterialTheme.typography.bodyLarge.copy(
                                            color = MaterialTheme.colorScheme.onSurface.copy(.5f)
                                        )
                                    )
                                },
                                textStyle = MaterialTheme.typography.bodyLarge.copy(
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                            )
                            OutlinedTextField(
                                modifier = modifier
                                    .fillMaxWidth()
                                    .padding(16.dp),
                                value = productViewModel.price,
                                onValueChange = { productViewModel.price = it },
                                shape = MaterialTheme.shapes.medium,
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedContainerColor = MaterialTheme.colorScheme.surfaceContainer,
                                    unfocusedContainerColor = MaterialTheme.colorScheme.surfaceContainer,
                                    focusedBorderColor = Color.Transparent,
                                    unfocusedBorderColor = Color.Transparent
                                ),
                                placeholder = {
                                    Text(
                                        text = "Price",
                                        style = MaterialTheme.typography.bodyLarge.copy(
                                            color = MaterialTheme.colorScheme.onSurface.copy(.5f)
                                        )
                                    )
                                },
                                textStyle = MaterialTheme.typography.bodyLarge.copy(
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                            )
                            OutlinedTextField(
                                modifier = modifier
                                    .fillMaxWidth()
                                    .padding(16.dp),
                                value = productViewModel.stock,
                                onValueChange = { productViewModel.stock = it },
                                shape = MaterialTheme.shapes.medium,
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedContainerColor = MaterialTheme.colorScheme.surfaceContainer,
                                    unfocusedContainerColor = MaterialTheme.colorScheme.surfaceContainer,
                                    focusedBorderColor = Color.Transparent,
                                    unfocusedBorderColor = Color.Transparent
                                ),
                                placeholder = {
                                    Text(
                                        text = "Stock",
                                        style = MaterialTheme.typography.bodyLarge.copy(
                                            color = MaterialTheme.colorScheme.onSurface.copy(.5f)
                                        )
                                    )
                                },
                                textStyle = MaterialTheme.typography.bodyLarge.copy(
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                            )
                            OutlinedTextField(
                                modifier = modifier
                                    .fillMaxWidth()
                                    .padding(16.dp),
                                value = productViewModel.reorderLevel,
                                onValueChange = { productViewModel.reorderLevel = it },
                                shape = MaterialTheme.shapes.medium,
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedContainerColor = MaterialTheme.colorScheme.surfaceContainer,
                                    unfocusedContainerColor = MaterialTheme.colorScheme.surfaceContainer,
                                    focusedBorderColor = Color.Transparent,
                                    unfocusedBorderColor = Color.Transparent
                                ),
                                placeholder = {
                                    Text(
                                        text = "Reorder Level",
                                        style = MaterialTheme.typography.bodyLarge.copy(
                                            color = MaterialTheme.colorScheme.onSurface.copy(.5f)
                                        )
                                    )
                                },
                                textStyle = MaterialTheme.typography.bodyLarge.copy(
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                            )
                            OutlinedTextField(
                                modifier = modifier
                                    .fillMaxWidth()
                                    .padding(16.dp),
                                value = fileName,
                                onValueChange = { fileName = it },
                                readOnly = true,
                                shape = MaterialTheme.shapes.medium,
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedContainerColor = MaterialTheme.colorScheme.surfaceContainer,
                                    unfocusedContainerColor = MaterialTheme.colorScheme.surfaceContainer,
                                    focusedBorderColor = Color.Transparent,
                                    unfocusedBorderColor = Color.Transparent
                                ),
                                textStyle = MaterialTheme.typography.bodyLarge.copy(
                                    color = MaterialTheme.colorScheme.onSurface
                                ),
                                trailingIcon = {
                                    IconButton(onClick = {
                                        launcher.launch("image/*")
                                    }) {
                                        Icon(
                                            imageVector = Icons.Default.AttachFile,
                                            contentDescription = null
                                        )
                                    }
                                }
                            )
                        }
                    },
                    properties = DialogProperties(
                        dismissOnBackPress = false,
                        dismissOnClickOutside = false
                    )
                )
            }
        }
        FloatingActionButton(
            modifier = modifier
                .align(Alignment.BottomEnd)
                .systemBarsPadding()
                .padding(bottom = 16.dp, end = 16.dp),
            onClick = { openDialog = true }
        ) {
            Icon(
                imageVector = Icons.Default.Add,
                contentDescription = null
            )
        }
    }
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
    var quantity by remember { mutableStateOf("") }
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
        AlertDialog(
            onDismissRequest = { showDialog = false },
            confirmButton = {
                TextButton(
                    onClick = {
                        onRestock(quantity)
                        showDialog = false
                    },
                    enabled = quantity.isNotEmpty()
                ) {
                    Text(text = "Ok")
                }
            },
            dismissButton = {
                TextButton(onClick = { showDialog = false }) {
                    Text(text = "Close")
                }
            },
            title = {
                Text(
                    text = "Enter Restock Quantity",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSurface
                )
            },
            text = {
                TextField(
                    modifier = modifier
                        .focusRequester(focusRequester),
                    value = quantity,
                    onValueChange = {
                        quantity = if ((it.toIntOrNull() ?: 0) <= 30) {
                            it
                        } else {
                            "30"
                        }
                    },
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = MaterialTheme.colorScheme.surfaceContainer,
                        unfocusedContainerColor = MaterialTheme.colorScheme.surfaceContainer,
                    ),
                    keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Done),
                    keyboardActions = KeyboardActions(onDone = {
                        onRestock(quantity)
                        showDialog = false
                    }),
                )
            }
        )
    }
}

@Composable
fun OverviewSection(
    modifier: Modifier = Modifier,
    totalItems: Int,
    totalValue: BigDecimal
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFFFFCC80),
            contentColor = Color(0xFF424242)
        )
    ) {
        Column(
            modifier = modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text(
                text = "Inventory Overview",
                style = MaterialTheme.typography.headlineSmall
            )
            Spacer(modifier = modifier.height(8.dp))
            Row(
                modifier = modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text(
                        text = "Total Items",
                        style = MaterialTheme.typography.bodyLarge
                    )
                    Text(
                        text = totalItems.toString(),
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold
                    )
                }
                Column {
                    Text(
                        text = "Total Value",
                        style = MaterialTheme.typography.bodyLarge
                    )
                    Text(
                        text = "Kshs. ${DecimalFormat("#,###.00").format(totalValue)}",
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}



