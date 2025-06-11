package com.example.titossycleaningservicesapp.presentation.users.manager.utils

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.titossycleaningservicesapp.domain.models.ui_models.UserMetrics
import com.patrykandpatrick.vico.compose.cartesian.CartesianChartHost
import com.patrykandpatrick.vico.compose.cartesian.axis.rememberBottom
import com.patrykandpatrick.vico.compose.cartesian.axis.rememberStart
import com.patrykandpatrick.vico.compose.cartesian.layer.rememberColumnCartesianLayer
import com.patrykandpatrick.vico.compose.cartesian.rememberCartesianChart
import com.patrykandpatrick.vico.core.cartesian.axis.HorizontalAxis
import com.patrykandpatrick.vico.core.cartesian.axis.VerticalAxis
import com.patrykandpatrick.vico.core.cartesian.data.CartesianChartModelProducer
import com.patrykandpatrick.vico.core.cartesian.data.CartesianValueFormatter
import com.patrykandpatrick.vico.core.cartesian.data.columnSeries
import com.patrykandpatrick.vico.core.cartesian.marker.DefaultCartesianMarker
import com.patrykandpatrick.vico.core.cartesian.marker.DefaultCartesianMarker.ValueFormatter
import kotlin.text.toInt

@Composable
fun TeamStatsCard(modifier: Modifier = Modifier, users: UserMetrics) {
//    val roles = users.roles
    val roleLabels = listOf("Customers", "Suppliers", "Employees")
    val modelProducer = remember { CartesianChartModelProducer() }
    val entries = remember {
        listOf(
            users.customers.toFloat(),
            users.suppliers.toFloat(),
            users.employees.toFloat()
//            roles.manager.toFloat(),
//            roles.supervisor.toFloat(),
//            roles.cleaner.toFloat(),
//            roles.inventoryManager.toFloat(),
        )
    }

    val xLabels = entries.indices.map { it }
    val bottomAxisValueFormatter = CartesianValueFormatter { context, value, _ ->
        val index = value.toInt()
        if (index >= 0 && index < roleLabels.size) {
            roleLabels[index]
        } else {
            ""
        }
    }

    LaunchedEffect(Unit) {
        modelProducer.runTransaction {
            columnSeries { series(xLabels, entries) }
        }
    }

    Card(modifier = Modifier.padding(16.dp)) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text("Team Composition", style = MaterialTheme.typography.titleLarge)
            CartesianChartHost(
                chart =
                    rememberCartesianChart(
                        rememberColumnCartesianLayer(),
                        startAxis = VerticalAxis.rememberStart(),
                        bottomAxis = HorizontalAxis.rememberBottom(
                            valueFormatter = bottomAxisValueFormatter
                        ),
                    ),
                modelProducer = modelProducer,
                modifier = modifier,
            )
        }
    }
}