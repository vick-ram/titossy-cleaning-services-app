package com.example.titossycleaningservicesapp.presentation.users.customer.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController

@Composable
fun ContactScreen(navController: NavHostController) {
    Box(
        modifier = Modifier
            .fillMaxSize(1f)
            .padding(8.dp), contentAlignment = Alignment.Center
    ) {
        Text("Support screen", fontSize = MaterialTheme.typography.titleLarge.fontSize)
    }
}