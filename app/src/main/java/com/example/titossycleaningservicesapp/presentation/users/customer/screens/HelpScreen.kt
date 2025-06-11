package com.example.titossycleaningservicesapp.presentation.users.customer.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.Chat
import androidx.compose.material.icons.filled.Email
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController

@Composable
fun HelpScreen(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    paddingValues: PaddingValues
) {

    val contactClick: (ContactMethod) -> Unit = { method ->
        // Handle contact method click
        when (method) {
            ContactMethod.CHAT -> {
                // Navigate to chat screen or open chat dialog
                navController.navigate("chat")
            }
            ContactMethod.EMAIL -> {
                // Open email client or navigate to email screen
            }
            ContactMethod.CALL -> {
                // Initiate phone call or navigate to call screen
            }
        }
    }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        contentPadding = paddingValues,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = "Help Center",
                    style = MaterialTheme.typography.headlineMedium,
                    color = MaterialTheme.colorScheme.primary
                )
                Text(
                    text = "We're here to help you with any questions",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                )
            }
        }

        item {
            ContactOptionsSection(onContactClick = contactClick)
        }
    }
}

@Composable
private fun ContactOptionsSection(
    onContactClick: (ContactMethod) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(
            text = "Still need help?",
            style = MaterialTheme.typography.titleMedium
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            ContactMethod.entries.forEach { method ->
                ContactMethodCard(
                    method = method,
                    onClick = { onContactClick(method) }
                )
            }
        }
    }
}

@Composable
private fun ContactMethodCard(
    method: ContactMethod,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        onClick = onClick,
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Column(
            modifier = modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Box(
                modifier = modifier
                    .size(40.dp)
                    .background(
                        color = method.color,
                        shape = RoundedCornerShape(8.dp)
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = method.icon,
                    contentDescription = null,
                    tint = Color.White
                )
            }

            Text(
                text = method.title,
                style = MaterialTheme.typography.titleSmall
            )

            Text(
                text = method.description,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
            )

            Spacer(modifier = modifier.height(4.dp))

            Text(
                text = method.actionText,
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.primary
            )
        }
    }
}

// Data Models
data class FaqCategory(
    val id: String,
    val title: String,
    val icon: ImageVector,
    val color: Color,
    val items: List<FaqItem>
)

data class FaqItem(
    val id: String,
    val question: String,
    val answer: String,
    val keywords: List<String>
)

enum class ContactMethod(
    val title: String,
    val icon: ImageVector,
    val color: Color,
    val description: String,
    val actionText: String
) {
    CHAT(
        title = "Live Chat",
        icon = Icons.Default.Chat,
        color = Color(0xFF4A6BFF),
        description = "Chat instantly with our team",
        actionText = "Start Chat"
    ),
    EMAIL(
        title = "Email",
        icon = Icons.Default.Email,
        color = Color(0xFFFF7043),
        description = "Response within 2 hours",
        actionText = "Send Email"
    ),
    CALL(
        title = "Phone",
        icon = Icons.Default.Call,
        color = Color(0xFF66BB6A),
        description = "9am-5pm, Mon-Fri",
        actionText = "Call Now"
    )
}

// Sample data
private fun getFilteredCategories(query: String): List<FaqCategory> {
    val allCategories = listOf(
        FaqCategory(
            id = "bookings",
            title = "Bookings",
            icon = Icons.Default.CalendarToday,
            color = Color(0xFF9C27B0),
            items = listOf(
                FaqItem(
                    id = "booking-1",
                    question = "How do I reschedule?",
                    answer = "Answer here...",
                    keywords = listOf("reschedule", "change time")
                ),
                // More items...
            )
        ),
        // More categories...
    )

    return if (query.isBlank()) {
        allCategories
    } else {
        allCategories.map { category ->
            category.copy(
                items = category.items.filter { item ->
                    item.question.contains(query, ignoreCase = true) ||
                            item.keywords.any { it.contains(query, ignoreCase = true) }

                }.filter { it.question.isNotBlank()  }
            )
        }
    }
}