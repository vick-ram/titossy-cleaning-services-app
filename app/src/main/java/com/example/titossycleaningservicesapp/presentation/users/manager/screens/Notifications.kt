package com.example.titossycleaningservicesapp.presentation.users.manager.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController

@Composable
fun Notifications(navController: NavHostController, modifier: Modifier){
    Box(modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ){
        Text(text = "Notifications")
    }
}