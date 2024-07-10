package com.example.titossycleaningservicesapp.presentation.users.manager.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocalShipping
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
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
    val suppliers = supplierUiState.suppliers?.size
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(paddingValues)
    ) {
        ManagerDashboardCard(
            onClick = { navController.navigate("managerSuppliers") },
            suppliers = suppliers
        )
    }
}

@Composable
fun ManagerDashboardCard(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    suppliers: Int?,
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
        Icon(
            modifier = modifier
                .size(50.dp)
                .align(Alignment.CenterHorizontally),
            imageVector = Icons.Filled.LocalShipping,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.primary
        )
        Spacer(modifier = modifier.height(16.dp))
        Text(
            modifier = modifier.align(Alignment.CenterHorizontally),
            text = suppliers.toString(),
            style = MaterialTheme.typography.headlineMedium.copy(
                color = MaterialTheme.colorScheme.onSurface.copy(.8f),
                fontWeight = FontWeight.Bold
            )
        )
        Spacer(modifier = modifier.height(8.dp))
        Text(
            modifier = modifier
                .padding(bottom = 8.dp)
                .align(Alignment.CenterHorizontally),
            text = "Suppliers",
            style = MaterialTheme.typography.bodyLarge.copy(
                color = MaterialTheme.colorScheme.onSurface,
                fontWeight = FontWeight.Bold
            )
        )
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun MCardPreview(modifier: Modifier = Modifier) {
    AppTheme {
        ManagerDashboardCard(onClick = {}, suppliers = 2)
    }
}
