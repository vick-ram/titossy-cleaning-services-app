package com.example.titossycleaningservicesapp.presentation.users.inventory.utils

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.titossycleaningservicesapp.domain.models.ui_models.Product

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun CartTableHeader(modifier: Modifier = Modifier) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(8.dp)
            .background(MaterialTheme.colorScheme.surfaceVariant),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = "Item",
            modifier = modifier
                .weight(1f)
                .padding(8.dp)
                .align(Alignment.CenterVertically),
            style = MaterialTheme.typography.titleSmall.copy(
                fontWeight = FontWeight.SemiBold,
                fontSize = 18.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center
            )
        )
        Text(
            text = "curr. stock",
            modifier = modifier
                .weight(1f)
                .padding(8.dp),
            style = MaterialTheme.typography.titleSmall.copy(
                fontWeight = FontWeight.SemiBold,
                fontSize = 18.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center
            )
        )
        Text(
            text = "Qty",
            modifier = modifier
                .weight(1f)
                .padding(8.dp),
            style = MaterialTheme.typography.titleSmall.copy(
                fontWeight = FontWeight.SemiBold,
                fontSize = 18.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center
            )
        )

    }
}

@Composable
fun PurchaseOrderTableData(
    modifier: Modifier = Modifier,
    product: Product,
    quantity: String
) {
    Row {
        Text(
            modifier = modifier
                .weight(1f),
            text = product.name
        )
        Text(
            modifier = modifier
                .weight(1f),
            text = product.stock.toString()
        )
        Text(
            modifier = modifier
                .weight(1f),
            text = quantity
        )
    }
}


