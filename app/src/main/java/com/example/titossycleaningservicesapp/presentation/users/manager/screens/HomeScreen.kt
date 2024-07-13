package com.example.titossycleaningservicesapp.presentation.users.manager.screens

import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.LocalMall
import androidx.compose.material.icons.filled.LocalShipping
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.filled.Store
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.example.titossycleaningservicesapp.domain.models.OrderStatus
import com.example.titossycleaningservicesapp.domain.viewmodel.PurchaseOrderViewModel
import com.example.titossycleaningservicesapp.domain.viewmodel.SupplierViewModel
import com.example.titossycleaningservicesapp.presentation.ui.theme.AppTheme

@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    paddingValues: PaddingValues,
    supplierViewModel: SupplierViewModel
) {
    val supplierUiState by supplierViewModel.supplierUiState.collectAsStateWithLifecycle()
    val suppliers = supplierUiState.suppliers?.size ?: 0
    val purchaseOrderViewModel: PurchaseOrderViewModel = hiltViewModel()
    val purchaseOrderUiState by purchaseOrderViewModel.purchaseOrderDataUiState.collectAsStateWithLifecycle()
    val purchaseOrders =
        purchaseOrderUiState.purchaseOrders?.filter { it.orderStatus == OrderStatus.PENDING }?.size
            ?: 0

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(paddingValues)
    ) {
        ManagerDashboardCard(
            onClick = { navController.navigate("managerSuppliers") },
            count = suppliers,
            icon = Icons.Filled.LocalShipping,
            text = "Suppliers"
        )

        ManagerDashboardCard(
            onClick = { navController.navigate("managerOrders") },
            count = purchaseOrders,
            icon = Icons.Filled.LocalMall,
            text = "Orders"
        )
    }
}

@Composable
fun ManagerDashboardCard(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    count: Int?,
    icon: ImageVector,
    text: String
) {
    Card(
        onClick = onClick,
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp),
        colors = CardDefaults.cardColors().copy(
            containerColor = MaterialTheme.colorScheme.surface,
            contentColor = MaterialTheme.colorScheme.onSurface
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 1.dp,
            pressedElevation = 6.dp
        )
    ) {
        Row(
            modifier = modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Icon(
                modifier = modifier
                    .size(70.dp),
                imageVector = icon,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary
            )
            Column(
                modifier = modifier
                    .width(IntrinsicSize.Max),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    modifier = modifier
                        .padding(bottom = 8.dp),
                    text = text,
                    style = MaterialTheme.typography.bodyLarge.copy(
                        color = MaterialTheme.colorScheme.onSurface.copy(.6f),
                        fontWeight = FontWeight.Bold
                    )
                )
                Text(
                    modifier = modifier,
                    text = count.toString(),
                    style = MaterialTheme.typography.headlineMedium.copy(
                        color = MaterialTheme.colorScheme.onSurface.copy(.8f),
                        fontWeight = FontWeight.Bold
                    )
                )
            }
            FilledIconButton(
                onClick = onClick,
                colors = IconButtonDefaults.filledIconButtonColors(
                    containerColor = MaterialTheme.colorScheme.surfaceContainer,
                    contentColor = MaterialTheme.colorScheme.onSurface
                )
            ) {
                Icon(
                    modifier = modifier.size(width = 32.dp, height = 60.dp),
                    imageVector = Icons.Default.ChevronRight,
                    contentDescription = null
                )
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun MCardPreview(modifier: Modifier = Modifier) {
    AppTheme {
        Column {
            ManagerDashboardCard(
                onClick = {},
                count = 2,
                icon = Icons.Filled.LocalShipping,
                text = "Suppliers"
            )
            ManagerDashboardCard(
                onClick = {},
                count = 2,
                icon = Icons.Filled.LocalMall,
                text = "Orders"
            )
        }
    }
}







