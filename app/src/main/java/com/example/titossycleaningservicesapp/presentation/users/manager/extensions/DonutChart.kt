import kotlin.math.min
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.dp
//
//@Composable
//fun DonutChart(
//    values: List<Float>,
//    colors: List<Color>,
//    labels: List<String>,
//    modifier: Modifier = Modifier
//) {
//    val total = values.sum()
//    var selectedIndex by remember { mutableStateOf(-1) }
//    val animatedAngles = values.map { it / total * 360f }
//
//    BoxWithConstraints(modifier = modifier, contentAlignment = Alignment.Center) {
//        val size = min(this.maxWidth.value, this.maxHeight.value)
//        val strokeWidth = size * 0.15f // Donut thickness
//
//        Canvas(modifier = Modifier.size(size.dp)) {
//            var startAngle = -90f // Start at top
//
//            animatedAngles.forEachIndexed { index, angle ->
//                val sweepAngle = angle * 0.99f // Small gap between segments
//                val path = Path().apply {
//                    addArc(
//                        Rect(Offset.Zero, Size(size.toFloat(), size.toFloat())),
//                        startAngle,
//                        sweepAngle
//                    )
//                }
//
//                drawPath(
//                    path = path,
//                    color = colors[index],
//                    style = Stroke(
//                        width = strokeWidth,
//                        cap = StrokeCap.Round
//                    )
//                )
//
//                // Highlight selected segment
//                if (index == selectedIndex) {
//                    drawPath(
//                        path = path,
//                        color = colors[index].copy(alpha = 0.3f),
//                        style = Stroke(
//                            width = strokeWidth * 1.3f,
//                            cap = StrokeCap.Round
//                        )
//                    )
//                }
//
//                startAngle += angle
//            }
//        }
//
//        // Center text
//        Text(
//            text = "$${total.toInt()}",
//            style = MaterialTheme.typography.headlineSmall,
//            modifier = Modifier.align(Alignment.Center)
//        )
//
//        Spacer(modifier = Modifier.height(16.dp))
//
//        // 🔍 Show labels + color indicators
//        Row(
//            horizontalArrangement = Arrangement.Center,
//            verticalAlignment = Alignment.CenterVertically,
//            modifier = Modifier
//                .fillMaxWidth()
//                .padding(top = 8.dp)
//        ) {
//            labels.forEachIndexed { index, label ->
//                Row(
//                    verticalAlignment = Alignment.CenterVertically,
//                    modifier = Modifier.padding(horizontal = 8.dp)
//                ) {
//                    Box(
//                        modifier = Modifier
//                            .size(16.dp)
//                            .background(colors[index], shape = CircleShape)
//                    )
//                    Spacer(modifier = Modifier.width(4.dp))
//                    Text(text = label)
//                }
//            }
//        }
//    }
//}

@Composable
fun DonutChart(
    values: List<Float>,
    colors: List<Color>,
    labels: List<String>,
    modifier: Modifier = Modifier
) {
    val total = values.sum()
    var selectedIndex by remember { mutableStateOf(-1) }
    val animatedAngles = values.map { it / total * 360f }

    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .size(200.dp)
                .align(Alignment.CenterHorizontally),
            contentAlignment = Alignment.Center
        ) {
            Canvas(modifier = Modifier.matchParentSize()) {
                val size = size.minDimension
                val strokeWidth = size * 0.15f
                var startAngle = -90f
                animatedAngles.forEachIndexed { index, angle ->
                    val sweepAngle = angle * 0.99f
                    val path = Path().apply {
                        addArc(
                            Rect(Offset.Zero, Size(size, size)),
                            startAngle,
                            sweepAngle
                        )
                    }
                    drawPath(
                        path = path,
                        color = colors[index],
                        style = Stroke(
                            width = strokeWidth,
                            cap = StrokeCap.Round
                        )
                    )
                    if (index == selectedIndex) {
                        drawPath(
                            path = path,
                            color = colors[index].copy(alpha = 0.3f),
                            style = Stroke(
                                width = strokeWidth * 1.3f,
                                cap = StrokeCap.Round
                            )
                        )
                    }
                    startAngle += angle
                }
            }
            Text(
                text = "$${total.toInt()}",
                style = MaterialTheme.typography.headlineSmall
            )
        }
    }
}