package com.example.titossycleaningservicesapp.presentation.users.manager.utils

import android.widget.Toast
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDownward
import androidx.compose.material.icons.filled.ArrowUpward
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.ChevronLeft
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.SwapVert
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.example.titossycleaningservicesapp.core.CustomProgressIndicator
import com.example.titossycleaningservicesapp.core.showToast
import com.example.titossycleaningservicesapp.data.remote.util.AuthEvent
import com.example.titossycleaningservicesapp.domain.models.ApprovalStatus
import com.example.titossycleaningservicesapp.domain.models.ui_models.Supplier
import com.example.titossycleaningservicesapp.domain.viewmodel.SupplierViewModel
import kotlinx.coroutines.flow.collectLatest

@Composable
fun SuppliersScreen(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    supplierViewModel: SupplierViewModel,
    paddingValues: PaddingValues
) {
    val context = LocalContext.current
    val supplierUiState by supplierViewModel.supplierUiState.collectAsStateWithLifecycle()
    var search by rememberSaveable { mutableStateOf("") }
    val keyboardController = LocalSoftwareKeyboardController.current


    LaunchedEffect(supplierViewModel, context) {
        supplierViewModel.resultChannel.collectLatest { result ->
            when(result) {
                is AuthEvent.Error -> showToast(
                    context = context,
                    message = result.message
                )
                is AuthEvent.Loading -> supplierViewModel.isLoading
                is AuthEvent.Success -> showToast(
                    context = context,
                    message = result.message.toString()
                )
            }
        }
    }

    when {
        supplierUiState.isLoading -> {
            Box(
                modifier = modifier.fillMaxSize(),
                contentAlignment = Alignment.Center,
                content = { CustomProgressIndicator(isLoading = true) }
            )
        }

        supplierUiState.suppliers != null -> {
            supplierUiState.suppliers?.filter { it.fullName.contains(search, ignoreCase = true) }
                ?.let { suppliers ->
                    Column(
                        modifier = modifier
                            .fillMaxSize()
                            .padding(paddingValues)
                    ) {
                        Row(
                            modifier = modifier
                                .fillMaxWidth()
                                .padding(bottom = 4.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            IconButton(onClick = { navController.navigateUp() }) {
                                Icon(
                                    modifier = modifier.size(32.dp),
                                    imageVector = Icons.Default.ChevronLeft,
                                    contentDescription = null
                                )
                            }
                            Text(
                                text = "Suppliers",
                                style = MaterialTheme.typography.titleLarge
                            )
                        }
                        Column(
                            modifier = modifier
                                .fillMaxWidth()
                                .padding(vertical = 16.dp)
                                .border(
                                    width = 2.dp,
                                    color = MaterialTheme.colorScheme.onSurface.copy(.1f),
                                    shape = MaterialTheme.shapes.medium
                                )
                        ) {
                            Row(
                                modifier = modifier
                                    .fillMaxWidth()
                                    .padding(8.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                OutlinedTextField(
                                    modifier = modifier.height(48.dp),
                                    value = search,
                                    onValueChange = { search = it },
                                    shape = MaterialTheme.shapes.medium,
                                    leadingIcon = {
                                        Icon(
                                            imageVector = Icons.Default.Search,
                                            contentDescription = null,
                                            tint = MaterialTheme.colorScheme.onSurface.copy(.5f)
                                        )
                                    },
                                    colors = OutlinedTextFieldDefaults.colors(
                                        focusedBorderColor = Color.Transparent,
                                        unfocusedBorderColor = Color.Transparent,
                                        focusedContainerColor = MaterialTheme.colorScheme.surfaceContainer,
                                        unfocusedContainerColor = MaterialTheme.colorScheme.surfaceContainer
                                    ),
                                    placeholder = {
                                        Text(
                                            text = "Search",
                                            style = MaterialTheme.typography.bodyMedium.copy(
                                                color = MaterialTheme.colorScheme.onSurface.copy(.5f)
                                            )
                                        )
                                    },
                                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
                                    keyboardActions = KeyboardActions(
                                        onSearch = { keyboardController?.hide() }
                                    )
                                )
                                FilledIconButton(
                                    onClick = { },
                                    colors = IconButtonDefaults.filledIconButtonColors(
                                        containerColor = MaterialTheme.colorScheme.surfaceContainer,
                                        contentColor = MaterialTheme.colorScheme.onSurface
                                    ),
                                    shape = MaterialTheme.shapes.small
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.FilterList,
                                        contentDescription = null,
                                        tint = MaterialTheme.colorScheme.onSurface.copy(.5f)
                                    )
                                }
                            }
                            HorizontalDivider(
                                modifier = modifier,
                                color = MaterialTheme.colorScheme.onSurface.copy(.1f)
                            )
                            Column(
                                modifier = modifier
                                    .fillMaxWidth()
                                    .padding(16.dp)
                            ) {
                                TableHeader(
                                    modifier = modifier,
                                    onNameSwap = { supplierViewModel.onNameSwap() },
                                    onStatusSwap = { supplierViewModel.onStatusSwap() }
                                )
                                HorizontalDivider(
                                    modifier = modifier,
                                    color = MaterialTheme.colorScheme.onSurface.copy(.1f)
                                )
                                LazyColumn(
                                    modifier = modifier,
                                    verticalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    itemsIndexed(
                                        items = suppliers,
                                        key = { _, item: Supplier ->
                                            item.id
                                        },
                                        contentType = { _, supplier: Supplier ->
                                            supplier
                                        }
                                    ) { index: Int, supplier: Supplier ->
                                        SupplierCard(
                                            modifier = modifier,
                                            supplier = supplier,
                                            approvalStatus = ApprovalStatus.APPROVED,
                                            onApprove = { supp, status ->
                                                supplierViewModel.updateSupplierStatus(
                                                    id = supp.id,
                                                    approvalStatus = status
                                                )
                                            },
                                            onDelete = { supp ->
                                                supplierViewModel.deleteSupplier(supp.id)
                                            }
                                        )
                                        if (index < suppliers.size - 1) {
                                            FullWidthDivider()
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
        }

        supplierUiState.errorMessage.isNotEmpty() -> {
            Toast.makeText(
                context,
                supplierUiState.errorMessage,
                Toast.LENGTH_SHORT
            ).show()
        }
    }
}


@Composable
fun SupplierCard(
    modifier: Modifier = Modifier,
    supplier: Supplier,
    approvalStatus: ApprovalStatus,
    onApprove: (Supplier, ApprovalStatus) -> Unit,
    onDelete: (Supplier) -> Unit
) {
    var expanded by rememberSaveable { mutableStateOf(false) }
    Card(
        modifier = modifier
            .fillMaxWidth(),
        shape = MaterialTheme.shapes.small,
        elevation = CardDefaults.cardElevation(
            defaultElevation = 0.dp,
            pressedElevation = 1.dp
        ),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface,
            contentColor = MaterialTheme.colorScheme.onSurface
        )
    ) {
        Row(
            modifier = modifier
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = modifier
                    .weight(6f)
            ) {
                Text(
                    text = supplier.fullName,
                    style = MaterialTheme.typography.bodyLarge
                )
                Spacer(modifier = modifier.height(2.dp))
                Text(
                    text = supplier.email,
                    style = MaterialTheme.typography.bodySmall.copy(
                        color = MaterialTheme.colorScheme.onSurface.copy(.5f)
                    )
                )
            }

            Text(
                modifier = modifier
                    .weight(4f),
                text = supplier.status.name,
                style = MaterialTheme.typography.bodyLarge.copy(
                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = getStatusColor(supplier.status)
                )
            )

            Box(
                modifier = modifier
                    .weight(2f)
                    .wrapContentWidth(align = Alignment.CenterHorizontally)
            ) {
                IconButton(
                    onClick = { expanded = !expanded },
                ) {
                    Icon(
                        imageVector = Icons.Filled.MoreVert,
                        contentDescription = null
                    )
                }

                DropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false }
                ) {
                    DropdownMenuItem(
                        text = {
                            Row(
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Check,
                                    contentDescription = null,
                                    tint = MaterialTheme.colorScheme.primary
                                )
                                Spacer(modifier = modifier.width(8.dp))
                                Text(text = "Approve")
                            }
                        },
                        onClick = {
                            onApprove(supplier, approvalStatus)
                            expanded = false
                        }
                    )
                    DropdownMenuItem(
                        text = {
                            Row(
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Delete,
                                    contentDescription = null,
                                    tint = MaterialTheme.colorScheme.error
                                )
                                Spacer(modifier = modifier.width(8.dp))
                                Text(text = "Delete")
                            }
                        },
                        onClick = {
                            onDelete(supplier)
                            expanded = false
                        }
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun TableHeader(
    modifier: Modifier = Modifier,
    onNameSwap: (() -> Unit)? = null,
    onStatusSwap: (() -> Unit)? = null,
) {
    var nameSwapState by rememberSaveable { mutableIntStateOf(0) }
    var statusSwapState by rememberSaveable { mutableIntStateOf(0) }
    Row(
        modifier = modifier
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            modifier = modifier.weight(6f),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Name",
                style = MaterialTheme.typography.bodyMedium.copy(
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.onSurface.copy(.5f)
                )
            )
            Box(modifier = modifier) {
                IconButton(onClick = {
                    nameSwapState = (nameSwapState + 1) % 3
                    onNameSwap?.invoke()
                }) {
                    Icon(
                        imageVector = when (nameSwapState) {
                            0 -> Icons.Default.SwapVert
                            1 -> Icons.Default.ArrowUpward
                            else -> Icons.Default.ArrowDownward
                        },
                        contentDescription = null,
                        tint = when (nameSwapState) {
                            0 -> MaterialTheme.colorScheme.onSurface.copy(.5f)
                            1 -> MaterialTheme.colorScheme.primary
                            else -> MaterialTheme.colorScheme.primary
                        }
                    )
                }
            }
        }
        Row(
            modifier = modifier
                .weight(6f)
                .wrapContentWidth(Alignment.CenterHorizontally),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Status",
                style = MaterialTheme.typography.bodyMedium.copy(
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.onSurface.copy(.5f)
                )
            )

            IconButton(onClick = {
                statusSwapState = (statusSwapState + 1) % 3
                onStatusSwap?.invoke()
            }) {
                Icon(
                    imageVector = when (statusSwapState) {
                        0 -> Icons.Default.SwapVert
                        1 -> Icons.Default.ArrowUpward
                        else -> Icons.Default.ArrowDownward
                    },
                    contentDescription = null,
                    tint = when (statusSwapState) {
                        0 -> MaterialTheme.colorScheme.onSurface.copy(.5f)
                        1 -> MaterialTheme.colorScheme.primary
                        else -> MaterialTheme.colorScheme.primary
                    }
                )
            }
        }

        Text(
            modifier = modifier.weight(2f),
            text = "Action",
            style = MaterialTheme.typography.bodyMedium.copy(
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onSurface.copy(.5f)
            )
        )

    }
}

private fun getStatusColor(status: ApprovalStatus): Color {
    return when (status) {
        ApprovalStatus.APPROVED -> Color.Green
        ApprovalStatus.PENDING -> Color(0xFFE0A800)
        ApprovalStatus.REJECTED -> Color.Red
    }
}

@Composable
fun FullWidthDivider() {
    BoxWithConstraints(modifier = Modifier.fillMaxWidth()) {
        HorizontalDivider(
            modifier = Modifier
                .width(maxWidth)
                .padding(vertical = 8.dp),
            thickness = 1.dp,
            color = MaterialTheme.colorScheme.onSurface.copy(.1f)
        )
    }
}