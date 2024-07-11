package com.example.titossycleaningservicesapp.core

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay

@Composable
fun CustomProgressIndicator(
    modifier: Modifier = Modifier,
    size: Dp = 30.dp,
    strokeWidth: Dp = 8.dp,
    isLoading: Boolean = false
) {
    val color = MaterialTheme.colorScheme.surface

    if (isLoading) {

        val infiniteTransition = rememberInfiniteTransition(label = "")
        val animatedProgress by infiniteTransition.animateFloat(
            initialValue = 0f,
            targetValue = 1f,
            animationSpec = infiniteRepeatable(
                animation = tween(durationMillis = 1200, easing = LinearEasing)
            ), label = ""
        )

        Box(
            modifier = modifier
                .size(size),
            contentAlignment = Alignment.Center
        ) {
            Canvas(modifier = modifier.size(size)) {
                drawAutumnProgressCircle(
                    progress = animatedProgress,
                    size = size.toPx(),
                    strokeWidth = strokeWidth.toPx(),
                    color = color
                )
            }
        }
    }
}

private fun DrawScope.drawAutumnProgressCircle(
    progress: Float,
    size: Float,
    strokeWidth: Float,
    color: Color
) {
    val radius = size / 2f - strokeWidth / 2f
    val startAngle = -90f
    val sweepAngle = progress * 360f

    val autumnColors = listOf(
        Color(0xFFF7B733),
        Color(0xFF00BCD4),
        Color(0xFF238AB4),
        Color(0xFF808080),
        Color(0xFF735A2A)
    )

    val brush = Brush.sweepGradient(
        colors = autumnColors,
        center = center,
    )

    drawArc(
        brush = brush,
        startAngle = startAngle,
        sweepAngle = sweepAngle,
        useCenter = false,
        style = Stroke(
            width = strokeWidth,
            cap = StrokeCap.Round
        )
    )

    drawCircle(
        color = color,
        radius = radius,
        style = Stroke(width = strokeWidth, cap = StrokeCap.Round)
    )
}

@Composable
fun LoadingButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    isLoading: Boolean = false,
    enabled: Boolean = true,
    text: String = "Button",
    loadingText: String = "Loading...",
    contentColor: Color = MaterialTheme.colorScheme.primary,
    shape: Shape = MaterialTheme.shapes.large
) {
    Button(
        onClick = onClick,
        modifier = modifier,enabled = enabled,
        colors = ButtonDefaults.buttonColors(contentColor = contentColor),
        contentPadding = PaddingValues(16.dp),
        shape = shape
    ) {
        if (isLoading) {
            LoadingAnimation(
                text = loadingText,
                color = contentColor
            )
        } else {
            Text(text = text)
        }
    }
}

@Composable
fun LoadingAnimation(
    text: String,
    color: Color
) {
    var dots by rememberSaveable { mutableStateOf("") }
    val dotCount = 3

    LaunchedEffect(key1 = Unit) {while (true) {
        dots = (1..dotCount).joinToString("") { ".".repeat(it) }
        delay(500)
        dots = ""
        delay(500)
    }
    }

    Text(
        text = "$text $dots",
        color = color
    )
}