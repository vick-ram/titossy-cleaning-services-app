package com.example.titossycleaningservicesapp.presentation.users.customer.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.filled.ChevronLeft
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AboutUsScreen(navController: NavHostController, modifier: Modifier = Modifier) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("About Us") },
                navigationIcon = {
                    androidx.compose.material3.IconButton(onClick = {
                        navController.navigateUp()
                    }) {
                        androidx.compose.material3.Icon(
                            imageVector = androidx.compose.material.icons.Icons.Default.ChevronLeft,
                            contentDescription = "Back"
                        )
                    }
                },
            )
        }
    ) {
        LazyColumn(
            contentPadding = it
        ) {
            item {
                HeadingDescription(
                    modifier = modifier,
                    title = "OverView",
                    description = "Titossy provides high-quality residential and commercial property cleaning services on a contractual and one-off basis. " +
                            "We are committed to delivering exceptional cleaning solutions tailored to meet the unique needs of our clients. " +
                            "Our team of experienced professionals uses eco-friendly products and advanced techniques to ensure a spotless and healthy environment."
                )
            }

            item {
                HeadingDescription(
                    modifier = modifier,
                    title = "Our Mission",
                    description = "To be a leading cleaning services provider in Kenya that resolve and meets our clients needs by delivering high-quality, reliable, and eco-friendly cleaning solutions. " +
                            "We aim to enhance the quality of life for our clients by providing a clean and healthy environment."
                )
            }

            item {
                HeadingDescription(
                    modifier = modifier,
                    title = "Our Vision",
                    description = "Providing high quality cleaning services at affordable prices, " +
                            "ensuring customer satisfaction through exceptional service delivery, and maintaining a commitment to sustainability and environmental responsibility."
                )
            }
        }
    }
}

@Composable
fun HeadingDescription(modifier: Modifier = Modifier, title: String, description: String) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalAlignment = androidx.compose.ui.Alignment.CenterHorizontally,
    ) {
        Text(
            modifier = modifier.padding(bottom = 8.dp),
            text = title,
            style = TextStyle(
                fontSize = MaterialTheme.typography.titleMedium.fontSize,
                fontWeight = MaterialTheme.typography.titleMedium.fontWeight,
            )
        )
        Text(
            text = description,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
        )
    }
}