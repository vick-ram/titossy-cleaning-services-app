package com.example.titossycleaningservicesapp.presentation.utils

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.titossycleaningservicesapp.R


@Composable
fun DrawerUserInfo(
    name: String,
    email: String
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 8.dp)
            .background(MaterialTheme.colorScheme.primary)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Image(
                modifier = Modifier.size(100.dp),
                painter = painterResource(id = R.drawable.user),
                contentDescription = "user profile"
            )
            Text(
                modifier = Modifier.padding(top = 8.dp, bottom = 8.dp),
                text = name,
                fontSize = MaterialTheme.typography.bodyMedium.fontSize,
                color = Color.White
            )
            Text(
                text = email,
                color = Color.White,
                fontWeight = FontWeight.SemiBold
            )
        }
    }
}