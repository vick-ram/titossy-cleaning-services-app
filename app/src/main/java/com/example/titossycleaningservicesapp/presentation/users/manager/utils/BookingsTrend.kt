package com.example.titossycleaningservicesapp.presentation.users.manager.utils

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

import androidx.compose.ui.tooling.preview.Preview
import com.example.titossycleaningservicesapp.domain.models.ui_models.BookingMetrics
import com.patrykandpatrick.vico.compose.cartesian.CartesianChartHost
import com.patrykandpatrick.vico.compose.cartesian.axis.rememberBottom
import com.patrykandpatrick.vico.compose.cartesian.axis.rememberStart
import com.patrykandpatrick.vico.compose.cartesian.layer.rememberLine
import com.patrykandpatrick.vico.compose.cartesian.layer.rememberLineCartesianLayer
import com.patrykandpatrick.vico.compose.cartesian.rememberCartesianChart
import com.patrykandpatrick.vico.compose.cartesian.rememberVicoScrollState
import com.patrykandpatrick.vico.core.cartesian.axis.HorizontalAxis
import com.patrykandpatrick.vico.core.cartesian.axis.VerticalAxis
import com.patrykandpatrick.vico.core.cartesian.data.CartesianChartModelProducer
import com.patrykandpatrick.vico.core.cartesian.data.CartesianLayerRangeProvider
import com.patrykandpatrick.vico.core.cartesian.data.CartesianValueFormatter
import com.patrykandpatrick.vico.core.cartesian.data.lineSeries
import com.patrykandpatrick.vico.core.cartesian.layer.LineCartesianLayer
import com.patrykandpatrick.vico.core.cartesian.marker.DefaultCartesianMarker
import com.patrykandpatrick.vico.core.common.shader.ShaderProvider
import java.text.DecimalFormat
import com.patrykandpatrick.vico.compose.common.fill
import com.patrykandpatrick.vico.compose.common.shader.verticalGradient
import kotlinx.coroutines.runBlocking

@Composable
fun BookingsTrendCard(modifier: Modifier = Modifier, bookings: BookingMetrics) {
    val lineColor = Color(0xffa485e0)
    val modelProducer = remember { CartesianChartModelProducer() }
    val entries = remember {
        listOf(
            bookings.daily.toFloat(),
            bookings.weekly.toFloat(),
            bookings.monthly.toFloat(),
            bookings.yearly.toFloat(),
        )
    }

    val xLabels = listOf("Daily", "Weekly", "Monthly", "Yearly")
    val xValues = entries.indices.map { it }
    val bottomAxisValueFormatter = CartesianValueFormatter { context, value, _ ->
        val index = value.toInt()
        if (index >= 0 && index < xLabels.size) {
            xLabels[index]
        } else {
            ""
        }
    }


    LaunchedEffect(Unit) {
        modelProducer.runTransaction {
            lineSeries { series(xValues, entries) }
        }
    }

    Card(
        modifier = Modifier
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text("Bookings Trend", style = MaterialTheme.typography.titleLarge)
            Spacer(Modifier.height(16.dp))
            CartesianChartHost(
                chart =
                    rememberCartesianChart(
                        rememberLineCartesianLayer(
                            lineProvider =
                                LineCartesianLayer.LineProvider.series(
                                    LineCartesianLayer.rememberLine(
                                        fill = LineCartesianLayer.LineFill.single(fill(lineColor)),
                                        areaFill =
                                            LineCartesianLayer.AreaFill.single(
                                                fill(
                                                    ShaderProvider.verticalGradient(
                                                        arrayOf(lineColor.copy(alpha = 0.4f), Color.Transparent)
                                                    )
                                                )
                                            ),
                                    )
                                ),
                        ),
                        startAxis = VerticalAxis.rememberStart(title = "Bookings"),
                        bottomAxis = HorizontalAxis.rememberBottom(
                            title = "Period",
                            valueFormatter = bottomAxisValueFormatter
                        ),
                    ),
                modelProducer = modelProducer,
                modifier = modifier,
                rememberVicoScrollState(scrollEnabled = false),
            )
        }
    }
}
