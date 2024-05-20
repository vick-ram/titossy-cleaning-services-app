package com.example.titossycleaningservicesapp.presentation.users.customer.utils

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import com.example.titossycleaningservicesapp.presentation.utils.UserRoutes

@Composable
fun BookingSuccessScreen(navController: NavHostController) {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Button(onClick = {
            navController.popBackStack()
            navController.navigate(UserRoutes.Customer.route) }
        ) {
            Text(text = "Booking Success")
        }
    }
}