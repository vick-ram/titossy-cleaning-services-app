package com.example.titossycleaningservicesapp.presentation.users.customer.screens

import android.annotation.SuppressLint
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronLeft
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.titossycleaningservicesapp.core.FAQItem

@OptIn(ExperimentalFoundationApi::class)
@SuppressLint("SuspiciousIndentation")
@Composable
fun FAQsScreen(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    paddingValues: PaddingValues
) {
    val dataItem = listOf(
        FAQItem(
            question = "What services does Tidiness Cleaning Services offer?",
            answer = "Tidiness Cleaning Services provides a comprehensive range of " +
                    "cleaning solutions, including residential cleaning, commercial cleaning, deep " +
                    "cleaning, and specialized services such as carpet and upholstery cleaning.",
        ),
        FAQItem(
            question = "How can I request a cleaning service?",
            answer = "To request a cleaning service, simply visit our" +
                    " website and fill out the online booking form. You can also give us a call, and our friendly" +
                    " customer service team will assist you in scheduling a cleaning appointment."
        ),
        FAQItem(
            question = "Are your cleaning products environmentally friendly?",
            answer = "Yes, we prioritize the use of eco-friendly and non-toxic cleaning products." +
                    " Our commitment to sustainability ensures a healthy and safe environment for our " +
                    "clients and their families."
        ),
        FAQItem(
            question = "How much does your cleaning service cost?",
            answer = "The cost of our cleaning services depends on various factors," +
                    " including the size of the space, the type of cleaning required," +
                    " and any additional services requested. Contact us for a personalized quote based on your specific needs."
        ),
        FAQItem(
            question = "Are your cleaners background-checked and insured?",
            answer = "Absolutely. All our professional cleaners undergo thorough background" +
                    " checks, and we ensure they are fully insured for your peace of mind. Your" +
                    " security and satisfaction are our top priorities."
        ),
        FAQItem(
            question = "Can I schedule a one-time cleaning service, or do you offer recurring plans?",
            answer = "We offer both one-time cleaning services and customizable recurring plans." +
                    " Whether you need a single deep clean or regular maintenance, we can tailor our" +
                    " services to fit your schedule and preferences."
        ),
        FAQItem(
            question = "Do I need to provide cleaning supplies?",
            answer = "No need to worry about cleaning supplies. We bring our own high-quality" +
                    " and professional-grade cleaning equipment and products to ensure the best results."
        ),
        FAQItem(
            question = "How can I provide feedback about my cleaning service?",
            answer = "We appreciate feedback from our valued clients. You can leave a review" +
                    " on our website or contact our customer service team directly to share your" +
                    " thoughts and suggestions."
        )
    )
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(paddingValues)
    ) {
        Row(
            modifier = modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
         IconButton(onClick = { navController.navigateUp() }) {
             Icon(
                 modifier = modifier.size(32.dp),
                 imageVector = Icons.Default.ChevronLeft,
                 contentDescription = null
             )
         }
            Spacer(modifier = modifier.width(8.dp))
            Text(
                text = "Back",
                style = MaterialTheme.typography.bodyMedium.copy(
                    color = MaterialTheme.colorScheme.primary
                )
            )
            Spacer(modifier = modifier.width(32.dp))
            Text(
                text = "FAQs",
                style = MaterialTheme.typography.titleMedium.copy(
                    color = MaterialTheme.colorScheme.onSurface
                )
            )
        }
        LazyColumn(
            modifier = modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            items(
                count = dataItem.size,
                key = { it }
            ) { index ->
                val faqItem = dataItem[index]
                FAQItemCard(
                    modifier = modifier.animateItemPlacement(),
                    faqItem = faqItem,
                    onClick = {
                        dataItem.mapIndexed { idx, item ->
                            if (idx == index) item.copy(isExpanded = !item.isExpanded) else item
                        }
                    }
                )
            }
        }
    }

}

@Composable
fun FAQItemCard(
    modifier: Modifier = Modifier,
    faqItem: FAQItem,
    onClick: () -> Unit
) {
    var isExpanded by remember { mutableStateOf(faqItem.isExpanded) }

    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable {
                isExpanded = !isExpanded
                onClick()
            },
        shape = RoundedCornerShape(8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(modifier = modifier.padding(16.dp)) {
            Row {
                Text(
                    text = faqItem.question,
                    style = MaterialTheme.typography.bodyLarge.copy(
                        fontWeight = FontWeight.SemiBold
                    )
                )
            }
            if (isExpanded) {
                Spacer(modifier = modifier.height(8.dp))
                Text(
                    text = faqItem.answer,
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
    }
}