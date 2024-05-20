package com.example.titossycleaningservicesapp.core

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.titossycleaningservicesapp.R

@Composable
fun EmptyScreen() {
    Box(modifier = Modifier
        .fillMaxSize()
        .padding(top = 64.dp)) {
        Image(painter = painterResource(
            id = R.drawable.no_data_found),
            contentDescription = "No data found",
            contentScale = ContentScale.Crop
        )
    }
}