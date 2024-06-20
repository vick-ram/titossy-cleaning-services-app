package com.example.titossycleaningservicesapp.presentation.users.inventory.utils

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.titossycleaningservicesapp.domain.viewmodel.ProductViewModel
import com.example.titossycleaningservicesapp.domain.viewmodel.SupplierAuthViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PurchaseOrderScreen(
    modifier: Modifier = Modifier,
    paddingValues: PaddingValues,
    navController: NavHostController,
    productId: String,
    quantity: String
) {
    var expanded by rememberSaveable { mutableStateOf(false) }
    var selected by rememberSaveable { mutableStateOf("") }
    var searchQuery by rememberSaveable { mutableStateOf("") }
    val productViewModel: ProductViewModel = hiltViewModel()
    val productUiState by productViewModel.productDataUiState.collectAsState()
    val supplierViewModel: SupplierAuthViewModel = hiltViewModel()
    val supplierState by supplierViewModel.supplierUiState.collectAsState()
    val product = productUiState.products?.find { it.productId == productId }

    val filteredSuppliers = supplierState.suppliers?.filter { supplier ->
        supplier.fullName.contains(searchQuery, ignoreCase = true)
    }

    LaunchedEffect(supplierState) {
        supplierViewModel.fetchSuppliers()
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(paddingValues)
            .padding(8.dp)
            .verticalScroll(state = rememberScrollState())
    ) {
        Text(
            modifier = modifier.align(Alignment.CenterHorizontally),
            text = "Purchase Order",
            style = MaterialTheme.typography.titleMedium.copy(
                fontSize = 18.sp
            )
        )
        Spacer(modifier = Modifier.height(8.dp))
        Row(
            modifier = modifier,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Order Date: ",
                style = MaterialTheme.typography.bodyLarge.copy()
            )
            Box(
                modifier = modifier
                    .wrapContentSize()
                    .padding(8.dp)
                    .border(
                        BorderStroke(
                            color = MaterialTheme.colorScheme.secondaryContainer,
                            width = 1.dp
                        ), MaterialTheme.shapes.small
                    )
                    .background(MaterialTheme.colorScheme.secondaryContainer)
                    .clip(MaterialTheme.shapes.small)
                    .clickable { },
            ) {
                Text(
                    modifier = modifier
                        .padding(4.dp),
                    text = "24/10/2024",
                    style = MaterialTheme.typography.bodyLarge.copy(
                        color = MaterialTheme.colorScheme.onSecondaryContainer
                    )
                )
            }
        }
        Spacer(modifier = Modifier.height(8.dp))

        CartTableHeader()
        Spacer(modifier = Modifier.height(8.dp))
        product?.let {
            PurchaseOrderTableData(
                product = it, quantity = quantity
            )
        }
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedButton(
            modifier = modifier
                .align(Alignment.End)
                .wrapContentSize(),
            onClick = {
                navController.navigate(NavRoutes.Home.route)
            }
        ) {
            Text(text = "Add Item")
        }
        Spacer(modifier = Modifier.height(8.dp))


        Row(
            modifier = modifier
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                modifier = modifier
                    .padding(end = 16.dp),
                text = "Supplier",
                style = MaterialTheme.typography.bodyLarge.copy()
            )
            ExposedDropdownMenuBox(
                modifier = modifier
                    .weight(1f),
                expanded = expanded,
                onExpandedChange = { expanded = !expanded }
            ) {

                TextField(
                    value = searchQuery,
                    onValueChange = { searchQuery = it },
                    label = { Text("Select supplier") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .menuAnchor()
                        .zIndex(1f),
                    trailingIcon = {
                        ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
                    },
                    colors = ExposedDropdownMenuDefaults.textFieldColors()
                )
                ExposedDropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false },
                    modifier = modifier.zIndex(2f)
                ) {
                    filteredSuppliers?.let { suppliers ->
                        suppliers.forEach { supplier ->
                            DropdownMenuItem(
                                text = { Text(text = supplier.fullName) },
                                onClick = {
                                    selected = supplier.fullName
                                    expanded = false

                                }
                            )
                        }
                    }
                }
            }
        }
        Spacer(
            modifier = Modifier
                .weight(1f)
                .height(16.dp)
        )

        Button(
            modifier = modifier
                .fillMaxWidth(.6f)
                .align(Alignment.CenterHorizontally),
            onClick = { /*TODO*/ },
            shape = MaterialTheme.shapes.extraSmall,
            contentPadding = PaddingValues(16.dp)
        ) {
            Text(text = "Create Purchase Order")
        }
    }
}