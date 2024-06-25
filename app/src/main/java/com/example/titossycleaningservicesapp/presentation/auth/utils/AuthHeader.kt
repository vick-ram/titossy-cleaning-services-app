package com.example.titossycleaningservicesapp.presentation.auth.utils

import android.graphics.Paint
import android.graphics.PathMeasure
import android.graphics.Typeface
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.asAndroidPath
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.unit.dp


@Composable
fun AuthCurve(title: String) {

    BoxWithConstraints(
        modifier = Modifier
            .fillMaxWidth()
            .height(100.dp)
    ) {
        val size = this.constraints
        val primaryColor = MaterialTheme.colorScheme.primary
        val onSurfaceColor = MaterialTheme.colorScheme.onSurface

        val startPoint = Offset(x = 0f, y = size.maxHeight.toFloat())
        val controlPoint = Offset(x = size.maxWidth / 2f, y = 0f)
        val endPoint = Offset(x = size.maxWidth.toFloat(), y = size.maxHeight.toFloat())

        Canvas(
            modifier = Modifier
                .fillMaxWidth()
                .height(100.dp)
        ) {
            val path = Path().apply {
                moveTo(startPoint.x, startPoint.y)
                quadraticBezierTo(
                    controlPoint.x,
                    controlPoint.y,
                    endPoint.x,
                    endPoint.y
                )
                lineTo(size.maxWidth.toFloat(), 0f)
                lineTo(0f, 0f)
                close()
            }

            drawPath(
                path = path,
                color = primaryColor,
                style = Fill
            )

            val textPath = PathMeasure().apply {
                setPath(path.asAndroidPath(), false)
            }
            val textPos = floatArrayOf(0f, 0f)
            textPath.getPosTan(textPath.length * 0.3f, textPos, null)

            drawContext.canvas.nativeCanvas.drawText(
                title,
                size.maxWidth / 2f,
                size.maxHeight / 1f,
                Paint().apply {
                    color = onSurfaceColor.toArgb()
                    textAlign = Paint.Align.CENTER
                    textSize = 28.dp.toPx()
                    typeface = Typeface.DEFAULT_BOLD
                }
            )

        }
    }
}

