package com.example.titossycleaningservicesapp.presentation.utils

import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp

@Composable
fun CustomSearchBar(
    query: String,
    onQueryChange: (String) -> Unit,
    active: Boolean,
    onActiveChange: (Boolean) -> Unit,
    placeholder: String
) {
    OutlinedTextField(
        modifier = Modifier.height(IntrinsicSize.Max),
        value = query,
        onValueChange = onQueryChange,
        trailingIcon = {
            if (active) {
                IconButton(onClick = { onActiveChange(false) }) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "clear",
                    )
                }
            }
        },
        colors = OutlinedTextFieldDefaults.colors(
            focusedTrailingIconColor = Color.Blue,
            unfocusedTrailingIconColor = Color.Black,
            focusedTextColor = Color.DarkGray,
            focusedContainerColor = Color.White,
            unfocusedContainerColor = MaterialTheme.colorScheme.onPrimary,
            unfocusedPlaceholderColor = Color.DarkGray
        ),
        placeholder = {
            Text(
                modifier = Modifier.alpha(0.7f),
                text = placeholder,
                color = MaterialTheme.colorScheme.onPrimary
            )
        },
        maxLines = 1,
        singleLine = true,
        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
        keyboardActions = KeyboardActions(onSearch = {onActiveChange(false)})
    )
}