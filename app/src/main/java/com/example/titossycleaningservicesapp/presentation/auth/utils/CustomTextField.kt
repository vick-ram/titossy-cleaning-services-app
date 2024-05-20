package com.example.titossycleaningservicesapp.presentation.auth.utils

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp

@Composable
fun CustomTextField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier,
    readOnly: Boolean = false,
    enabled: Boolean = true,
    label: String,
    leadingIcon: ImageVector,
    onTrailingIconClicked: (() -> Unit)? = null,
    visualTransformation: VisualTransformation? = null,
    trailingIcon: ImageVector? = null,
    keyBoardOptions: KeyboardOptions,
    isError: Boolean = false,
    errorMessage: String? = null
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = modifier,
        readOnly = readOnly,
        label = { Text(text = label, overflow = TextOverflow.Ellipsis) },
        leadingIcon = {
            Icon(
                imageVector = leadingIcon,
                contentDescription = null,
            )
        },
        trailingIcon = {
            trailingIcon?.let {
                Icon(
                    imageVector = trailingIcon,
                    contentDescription = null,
                    modifier = Modifier.clickable {
                        onTrailingIconClicked?.invoke()
                    }
                )
            }
        },
        visualTransformation = visualTransformation ?: VisualTransformation.None,
        keyboardOptions = keyBoardOptions,
        singleLine = true,
        shape = MaterialTheme.shapes.medium,
        colors = OutlinedTextFieldDefaults.colors(),
        isError = isError,
        supportingText = {
            errorMessage?.let {
                Text(
                    text = it,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.padding(start = 16.dp)
                )
            } ?: "required*"
        },
        enabled = enabled
    )

}
