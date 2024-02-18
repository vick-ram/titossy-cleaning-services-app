package com.example.titossycleaningservicesapp.presentation.users.customer.screens.payment

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.titossycleaningservicesapp.presentation.auth.utils.CustomButton

@Composable
fun PaymentScreen(navController: NavHostController) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Box(
            modifier = Modifier
                .size(100.dp)
                .padding(16.dp), contentAlignment = Alignment.Center
        )
        {
            Icon(
                modifier = Modifier.size(70.dp),
                imageVector = Icons.Filled.Check,
                contentDescription = "payment success"
            )
        }

        Text(text = "Your payment was successful")
        CustomButton(
            text = "Done",
            onClick = { /*TODO*/ },
            modifier = Modifier
        )
    }
}