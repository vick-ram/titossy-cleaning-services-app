package com.example.titossycleaningservicesapp.presentation.users.manager.utils

import DonutChart
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.titossycleaningservicesapp.domain.models.ui_models.PaymentMetrics

@Composable
fun PaymentsCard(payments: PaymentMetrics) {
    val entries = remember(payments) {
        listOf(
            payments.customerPayments.toFloat() to Color(0xFF4CAF50),
            payments.supplierPayments.toFloat() to Color(0xFFF44336),
        )
    }

    val labels = remember(payments) {
        listOf("Customer Payments", "Supplier Payments", "Net Profit")
    }

    Card(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxWidth(),
        shape = MaterialTheme.shapes.large,
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(
            modifier = Modifier.padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text(
                text = "Payments Flow",
                style = MaterialTheme.typography.titleLarge,
            )

            Spacer(Modifier.height(32.dp))
                // Donut Chart
                DonutChart(
                    values = entries.map { it.first },
                    colors = entries.map { it.second },
                    labels = listOf("Customer", "Supplier"),
                    modifier = Modifier.size(180.dp)
                )

                Spacer(Modifier.height(24.dp))

                // Legend and values
                Column {
                    entries.forEachIndexed { index, (value, color) ->
                        ChartLegendItem(
                            color = color,
                            label = labels[index],
                            value = "$${"%.2f".format(value)}",
                            modifier = Modifier.padding(vertical = 4.dp)
                        )
                    }

                    HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))

                    ChartLegendItem(
                        color = MaterialTheme.colorScheme.primary,
                        label = "Net Profit",
                        value = "$${"%.2f".format(payments.net)}",
                        modifier = Modifier.padding(vertical = 4.dp)
                    )
                }
        }
    }
}

@Composable
private fun ChartLegendItem(
    color: Color,
    label: String,
    value: String,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(12.dp)
                .clip(CircleShape)
                .background(color)
        )
        Spacer(Modifier.width(8.dp))
        Text(
            text = label,
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.weight(1f)
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodyLarge,
            fontWeight = FontWeight.Medium
        )
    }
}