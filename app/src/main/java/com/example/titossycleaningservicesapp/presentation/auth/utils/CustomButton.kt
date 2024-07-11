package com.example.titossycleaningservicesapp.presentation.auth.utils

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Shapes
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

@Composable
fun CustomButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier,
    enabled: Boolean = false,
    shape: Shape = RoundedCornerShape(20.dp)
) {
    Button(
        onClick = onClick,
        modifier = modifier
            .fillMaxWidth(.75f)
            .padding(8.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.primary,
            contentColor = MaterialTheme.colorScheme.onPrimary,

        ),
        shape = shape,
        enabled = enabled
    ) {
        Text(
            text = text,
            textAlign = TextAlign.Center
        )
    }
}
