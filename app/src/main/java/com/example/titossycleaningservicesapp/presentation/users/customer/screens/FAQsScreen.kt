package com.example.titossycleaningservicesapp.presentation.users.customer.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController

@OptIn(ExperimentalMaterial3Api::class)
@Preview(showBackground = true)
@Composable
fun FAQsScreen(navController: NavHostController = rememberNavController()) {
    val dataItem = listOf(
        RowItemData(
            title = "What services does Tidiness Cleaning Services offer?",
            content = "Tidiness Cleaning Services provides a comprehensive range of cleaning solutions, including residential cleaning, commercial cleaning, deep cleaning, and specialized services such as carpet and upholstery cleaning.",
        ),
        RowItemData(
            title = "How can I request a cleaning service?",
            content = "To request a cleaning service, simply visit our website and fill out the online booking form. You can also give us a call, and our friendly customer service team will assist you in scheduling a cleaning appointment."
        ),
        RowItemData(
            title = "Are your cleaning products environmentally friendly?",
            content = "Yes, we prioritize the use of eco-friendly and non-toxic cleaning products. Our commitment to sustainability ensures a healthy and safe environment for our clients and their families."
        ),
        RowItemData(
            title = "How much does your cleaning service cost?",
            content = "The cost of our cleaning services depends on various factors, including the size of the space, the type of cleaning required, and any additional services requested. Contact us for a personalized quote based on your specific needs."
        ),
        RowItemData(
            title = "Are your cleaners background-checked and insured?",
            content = "Absolutely. All our professional cleaners undergo thorough background checks, and we ensure they are fully insured for your peace of mind. Your security and satisfaction are our top priorities."
        ),
        RowItemData(
            title = "Can I schedule a one-time cleaning service, or do you offer recurring plans?",
            content = "We offer both one-time cleaning services and customizable recurring plans. Whether you need a single deep clean or regular maintenance, we can tailor our services to fit your schedule and preferences."
        ),
        RowItemData(
            title = "Do I need to provide cleaning supplies?",
            content = "No need to worry about cleaning supplies. We bring our own high-quality and professional-grade cleaning equipment and products to ensure the best results."
        ),
        RowItemData(
            title = "How can I provide feedback about my cleaning service?",
            content = "We appreciate feedback from our valued clients. You can leave a review on our website or contact our customer service team directly to share your thoughts and suggestions."
        )
    )
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = "Frequently Asked Questions", textAlign = TextAlign.Center) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "back")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = Color.White,
                    navigationIconContentColor = Color.White
                )
            )
        }
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize(1f)
                .padding(it),
            contentPadding = PaddingValues(8.dp)
        ) {
            items(dataItem){item ->
                RowItemCard(
                    title = item.title,
                    content = item.content,
                    modifier = Modifier.padding(top = 8.dp, bottom = 8.dp)
                )
            }
        }
    }

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RowItemCard(
    title: String,
    content: String,
    modifier: Modifier
) {
    var expanded by remember { mutableStateOf(false) }
    ElevatedCard(
        onClick = { expanded = !expanded },
        modifier = modifier
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = title,
                    style = TextStyle(
                        fontWeight = FontWeight.SemiBold,
                        fontSize = MaterialTheme.typography.bodyLarge.fontSize,
                        letterSpacing = MaterialTheme.typography.bodyLarge.letterSpacing,
                        textAlign = TextAlign.Start
                    )
                )
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    IconButton(
                        onClick = { expanded != expanded }
                    ) {
                        if (expanded) Icon(
                            imageVector = Icons.Default.KeyboardArrowDown,
                            contentDescription = null
                        ) else Icon(
                            imageVector = Icons.Default.KeyboardArrowUp,
                            contentDescription = null
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.height(8.dp))

            AnimatedVisibility(visible = expanded) {
                Text(
                    text = content,
                    style = TextStyle(
                        fontWeight = MaterialTheme.typography.bodyMedium.fontWeight,
                        fontSize = MaterialTheme.typography.bodyMedium.fontSize,
                        letterSpacing = MaterialTheme.typography.bodyMedium.letterSpacing,
                        textAlign = TextAlign.Start
                    )
                )
            }
        }
    }
}

data class RowItemData(
    val title: String,
    val content: String
)