package com.example.titossycleaningservicesapp.presentation.users.supplier.screens

import android.content.Context
import android.net.Uri
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AttachFile
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogProperties
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.titossycleaningservicesapp.core.FileUtils
import com.example.titossycleaningservicesapp.domain.models.ui_models.Product
import com.example.titossycleaningservicesapp.domain.viewmodel.MainViewModel
import com.example.titossycleaningservicesapp.domain.viewmodel.ProductViewModel

@Composable
fun ProductsScreen(modifier: Modifier = Modifier) {
    val mainViewModel: MainViewModel = hiltViewModel()
    var supplierId by rememberSaveable { mutableStateOf<String?>(null) }
    val productViewModel: ProductViewModel = hiltViewModel()
    val productsState by productViewModel.productDataUiState.collectAsStateWithLifecycle()
    val context = LocalContext.current
    var requestFile by remember { mutableStateOf<Uri?>(null) }
    var fileName by remember { mutableStateOf("") }
    val launcher =
        rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
            requestFile = uri
            uri?.let {
                fileName = FileUtils.getFileName(context, uri) ?: ""
            }
        }
    var isModalOpen by remember { mutableStateOf(false) }
    var showEditModal by remember { mutableStateOf(false) }
    val products = productsState.products
    var productToEdit by remember { mutableStateOf<Product?>(null) }

    LaunchedEffect(supplierId, mainViewModel) {
        supplierId = mainViewModel.readUserId()
    }

    LaunchedEffect(productViewModel, supplierId) {
        supplierId?.let {
            productViewModel.fetchProducts(supplierId = it)
        }
    }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        floatingActionButton = {
            FloatingActionButton(onClick = {
                isModalOpen = true
            }) { Text("+") }
        }
    ) { paddingValues ->
        if ((products ?: emptyList()).isEmpty()) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = 80.dp),
            ) {
                Text(
                    text = "You have no products to display.",
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier.padding(16.dp)
                )
            }
        } else {
        LazyColumn(
            contentPadding = paddingValues,
            modifier = modifier
                .fillMaxSize()
                .padding(top = 40.dp)
        ) {
            items(products ?: emptyList()) { product ->
                ProductCard(
                    product = product,
                    onEdit = {
                        productToEdit = product
                        showEditModal = true
                    },
                    modifier = Modifier
                )
            }
        }
    }
    }
//    Show dialog for creating a new product
    if (isModalOpen) {
        ProductCreate(
            context = context,
            onDismiss = { isModalOpen = false },
            onValueChange = { newFileName ->
                fileName = newFileName
            },
            requestedFile = requestFile,
            fileName = fileName,
            launcher = launcher,
            productViewModel = productViewModel,
            supplierId = supplierId ?: ""
        )
    }

//    Edit modal
    if (showEditModal) {
       if (productToEdit != null) {
            ProductEdit(
                product = productToEdit!!,
                onDismiss = { showEditModal = false },
                onSave = { updatedProduct ->
                    productViewModel.editProduct(updatedProduct)
                    showEditModal = false
                }
            )
        }
    }
}

@Composable
private fun ProductCard(product: Product, onEdit: () -> Unit, modifier: Modifier = Modifier) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable { onEdit() },
    ) {
        Column(modifier = modifier.padding(16.dp)) {
            Text(text = product.name)
            Text(text = product.description)
            Text(text = "Price: ${product.unitPrice}")
//            Text(text = "Stock: ${product.stock}")
        }
    }
}


@Composable
fun ProductEdit(
    product: Product,
    onDismiss: () -> Unit,
    onSave: (Product) -> Unit
) {
    var name by remember { mutableStateOf(product.name) }
    var description by remember { mutableStateOf(product.description) }
    var price by remember { mutableStateOf(product.unitPrice.toString()) }
    var stock by remember { mutableStateOf(product.stock.toString()) }
    var reorderLevel by remember { mutableStateOf(product.reorderLevel.toString()) }

    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(onClick = {
                onSave(
                    product.copy(
                        name = name,
                        description = description,
                        unitPrice = (price.toDoubleOrNull() ?: 0.0).toString(),
                        stock = stock.toIntOrNull() ?: 0,
                        reorderLevel = reorderLevel.toIntOrNull() ?: 0
                    )
                )
                onDismiss()
            }) { Text("Save") }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Cancel") }
        },
        title = { Text("Edit Product") },
        text = {
            Column {
                OutlinedTextField(value = name, onValueChange = { name = it }, label = { Text("Name") })
                OutlinedTextField(value = description, onValueChange = { description = it }, label = { Text("Description") })
                OutlinedTextField(value = price, onValueChange = { price = it }, label = { Text("Price") })
                OutlinedTextField(value = stock, onValueChange = { stock = it }, label = { Text("Stock") })
                OutlinedTextField(value = reorderLevel, onValueChange = { reorderLevel = it }, label = { Text("Reorder Level") })
            }
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductCreate(
    modifier: Modifier = Modifier,
    context: Context,
    onDismiss: (Boolean) -> Unit = {},
    onValueChange: (String) -> Unit,
    requestedFile: Uri? = null,
    fileName: String,
    launcher: ManagedActivityResultLauncher<String, Uri?>,
    productViewModel: ProductViewModel,
    supplierId: String
) {
    AlertDialog(
        onDismissRequest = { onDismiss(false) },
        confirmButton = {
            TextButton(
                onClick = {
                    requestedFile?.let {
                        productViewModel.createProduct(
                            context = context,
                            uri = it,
                            supplierId = supplierId
                        )
                    }
                    onDismiss(true)
                },
                enabled = productViewModel.name.isNotBlank()
                        && productViewModel.description.isNotBlank()
                        && productViewModel.price.isNotBlank()
            ) {
                Text(text = "Create")
            }
        },
        dismissButton = {
            TextButton(onClick = { onDismiss(false) }) {
                Text(text = "Cancel")
            }
        },
        title = {
            Text(text = "Create Product")
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
//                OutlinedTextField(
//                    modifier = modifier
//                        .fillMaxWidth()
//                        .padding(16.dp),
//                    value = productViewModel.stock,
//                    onValueChange = { productViewModel.stock = it },
//                    shape = MaterialTheme.shapes.medium,
//                    colors = OutlinedTextFieldDefaults.colors(
//                        focusedContainerColor = MaterialTheme.colorScheme.surfaceContainer,
//                        unfocusedContainerColor = MaterialTheme.colorScheme.surfaceContainer,
//                        focusedBorderColor = Color.Transparent,
//                        unfocusedBorderColor = Color.Transparent
//                    ),
//                    placeholder = {
//                        Text(
//                            text = "Stock",
//                            style = MaterialTheme.typography.bodyLarge.copy(
//                                color = MaterialTheme.colorScheme.onSurface.copy(.5f)
//                            )
//                        )
//                    },
//                    textStyle = MaterialTheme.typography.bodyLarge.copy(
//                        color = MaterialTheme.colorScheme.onSurface
//                    )
//                )
//                OutlinedTextField(
//                    modifier = modifier
//                        .fillMaxWidth()
//                        .padding(16.dp),
//                    value = productViewModel.reorderLevel,
//                    onValueChange = { productViewModel.reorderLevel = it },
//                    shape = MaterialTheme.shapes.medium,
//                    colors = OutlinedTextFieldDefaults.colors(
//                        focusedContainerColor = MaterialTheme.colorScheme.surfaceContainer,
//                        unfocusedContainerColor = MaterialTheme.colorScheme.surfaceContainer,
//                        focusedBorderColor = Color.Transparent,
//                        unfocusedBorderColor = Color.Transparent
//                    ),
//                    placeholder = {
//                        Text(
//                            text = "Reorder Level",
//                            style = MaterialTheme.typography.bodyLarge.copy(
//                                color = MaterialTheme.colorScheme.onSurface.copy(.5f)
//                            )
//                        )
//                    },
//                    textStyle = MaterialTheme.typography.bodyLarge.copy(
//                        color = MaterialTheme.colorScheme.onSurface
//                    )
//                )
                OutlinedTextField(
                    modifier = modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    value = fileName,
                    onValueChange = { onValueChange },
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