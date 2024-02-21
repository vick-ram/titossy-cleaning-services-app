package com.example.titossycleaningservicesapp.presentation.users.customer.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController

@Composable
fun AboutScreen(navController: NavHostController){
    Box(
        modifier = Modifier.fillMaxSize()
    ){
        Text(text = "Customer About screen")
    }
}