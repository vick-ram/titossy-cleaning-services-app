package com.example.titossycleaningservicesapp.presentation.users.supplier.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Print
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.titossycleaningservicesapp.R
import com.example.titossycleaningservicesapp.core.generatePdf
import com.example.titossycleaningservicesapp.domain.models.ui_models.PurchaseOrderItem
import com.example.titossycleaningservicesapp.domain.viewmodel.PaymentViewModel
import com.example.titossycleaningservicesapp.domain.viewmodel.PurchaseOrderViewModel

@Composable
fun SupplierReceiptScreen(
    modifier: Modifier = Modifier,
    paddingValues: PaddingValues,
    purchaseOrderId: String
) {
    val scrollState = rememberScrollState()
    val paymentViewModel: PaymentViewModel = hiltViewModel()
    val purchaseOrderViewModel: PurchaseOrderViewModel = hiltViewModel()
    val supplierPaymentUiState by paymentViewModel.supplierPaymentUiState.collectAsStateWithLifecycle()
    val purchaseOrderUiState by purchaseOrderViewModel.purchaseOrderDataUiState.collectAsStateWithLifecycle()
    val supplierPayment =
        supplierPaymentUiState.supplierPayments?.find { it.orderId == purchaseOrderId }

    LaunchedEffect(purchaseOrderId) {
        paymentViewModel.fetchSupplierPayments()
        purchaseOrderViewModel.getPurchaseOrderById(purchaseOrderId)
    }
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(paddingValues)
            .padding(horizontal = 8.dp)
            .verticalScroll(scrollState)
    ) {
        /*IconButton(
            modifier = modifier
                .align(Alignment.End),
            onClick = {
                generatePdf(context) {
                    SupplierReceiptScreen(
                        modifier = modifier,
                        paddingValues = paddingValues,
                        purchaseOrderId = purchaseOrderId
                    )
                }
            }
        ) {
            Icon(
                imageVector = Icons.Filled.Print,
                contentDescription = null
            )
        }
        Spacer(modifier = modifier.height(16.dp))*/
        Image(
            modifier = modifier
                .align(Alignment.CenterHorizontally)
                .size(64.dp),
            painter = painterResource(id = R.drawable.titossy_img),
            contentDescription = null
        )
        Spacer(modifier = modifier.height(8.dp))
        Text(
            modifier = modifier.align(Alignment.CenterHorizontally),
            text = "Titossy Cleaning Services",
            style = MaterialTheme.typography.titleSmall.copy(
                textAlign = TextAlign.Center
            )
        )
        Spacer(modifier = modifier.height(16.dp))
        ReceiptMainRow(
            label = "Payment ID",
            value = "${supplierPayment?.paymentId?.take(8)}.."
        )
        ReceiptMainRow(
            label = "Order ID",
            value = supplierPayment?.orderId ?: ""
        )
        ReceiptMainRow(
            label = "Method",
            value = supplierPayment?.method?.name ?: ""
        )
        ReceiptMainRow(
            label = "Ref",
            value = supplierPayment?.paymentReference ?: ""
        )
        Spacer(modifier = modifier.height(8.dp))
        Row(
            modifier = modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "Product",
                style = MaterialTheme.typography.bodyMedium.copy(
                    fontWeight = FontWeight.Bold
                )
            )
            Text(
                text = "Qty",
                style = MaterialTheme.typography.bodyMedium.copy(
                    fontWeight = FontWeight.Bold
                )
            )
            Text(
                text = "Unit Price",
                style = MaterialTheme.typography.bodyMedium.copy(
                    fontWeight = FontWeight.Bold
                )
            )
            Text(
                text = "Subtotal",
                style = MaterialTheme.typography.bodyMedium.copy(
                    fontWeight = FontWeight.Bold
                )
            )
        }
        HorizontalDivider()
        purchaseOrderUiState.purchaseOrder?.purchaseOrderItems?.let { orders ->
            orders.forEach { order ->
                RowData(purchaseOrderItem = order)
            }
        }
        HorizontalDivider()
        Row(
            modifier = modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "Total",
                style = MaterialTheme.typography.bodyMedium.copy(
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                )
            )
            Text(
                text = supplierPayment?.formattedAmount ?: "",
                style = MaterialTheme.typography.bodyMedium.copy(
                    color = MaterialTheme.colorScheme.onSurface,
                    textAlign = TextAlign.End
                )
            )

        }
        Row(
            modifier = modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.End
        ) {
            Text(
                text = "Processed by: ${supplierPayment?.employee}",
                style = MaterialTheme.typography.labelSmall.copy(
                    color = MaterialTheme.colorScheme.onSurface.copy(.5f),
                    fontStyle = FontStyle.Italic
                )
            )
        }
    }
}

@Composable
private fun ReceiptMainRow(
    modifier: Modifier = Modifier,
    label: String,
    value: String,
    labelStyle: TextStyle = MaterialTheme.typography.bodyMedium.copy(
        fontWeight = FontWeight.Bold,
        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
    ),
    valueStyle: TextStyle = MaterialTheme.typography.bodyMedium.copy(
        color = MaterialTheme.colorScheme.onSurface,
        textAlign = TextAlign.End
    )
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = label, style = labelStyle)
        Text(text = value, style = valueStyle)
    }
}

@Composable
private fun RowData(
    modifier: Modifier = Modifier,
    purchaseOrderItem: PurchaseOrderItem
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = purchaseOrderItem.product,
            style = MaterialTheme.typography.bodyMedium
        )
        Text(
            text = "X ${purchaseOrderItem.quantity}",
            style = MaterialTheme.typography.bodyMedium
        )
        Text(
            text = purchaseOrderItem.unitPrice.toPlainString(),
            style = MaterialTheme.typography.bodyMedium
        )
        Text(
            text = purchaseOrderItem.subtotal.toPlainString(),
            style = MaterialTheme.typography.bodyMedium
        )
    }
}

@Composable
fun SupplierReceiptScreenWithPrint(
    modifier: Modifier = Modifier,
    paddingValues: PaddingValues,
    purchaseOrderId: String
) {
    val context = LocalContext.current
    val density = LocalDensity.current

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(paddingValues)
    ) {
        IconButton(
            modifier = modifier.align(Alignment.End),
            onClick = {
                generatePdf(context, density) {
                    SupplierReceiptScreen(
                        modifier = modifier,
                        paddingValues = paddingValues,
                        purchaseOrderId = purchaseOrderId
                    )
                }
            }
        ) {
            Icon(
                imageVector = Icons.Filled.Print,
                contentDescription = null
            )
        }

        SupplierReceiptScreen(
            modifier = modifier,
            paddingValues = paddingValues,
            purchaseOrderId = purchaseOrderId
        )
    }
}
