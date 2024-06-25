package com.example.titossycleaningservicesapp.presentation.users.customer.utils

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.titossycleaningservicesapp.R
import com.example.titossycleaningservicesapp.presentation.utils.UserRoutes

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun BookingSuccessScreen(
    modifier: Modifier = Modifier,
    onHomeClick: (() -> Unit)? = null
) {

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Image(
            modifier = modifier.size(90.dp),
            painter = painterResource(id = R.drawable.check),
            contentDescription = "success"
        )
        Spacer(modifier = modifier.height(16.dp))
        Text(
            text = "Booking Successful",
            style = MaterialTheme.typography.headlineMedium.copy(
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                color = Color.Green
            )
        )
        Spacer(modifier = modifier.height(16.dp))
        Text(
            text = "Your booking has been successfully placed. Thank you for choosing our services.",
            style = MaterialTheme.typography.bodyMedium.copy(
                fontSize = 16.sp,
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.onSurface
            )
        )

        Spacer(modifier = modifier.height(32.dp))
        Button(
            modifier = modifier,
            onClick = { onHomeClick?.invoke() },
            contentPadding = PaddingValues(16.dp)
        ) {
            Text(text = "Continue Booking")
        }
    }
}