package com.example.titossycleaningservicesapp.presentation.users.supervisor.screen

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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.titossycleaningservicesapp.core.CustomProgressIndicator
import com.example.titossycleaningservicesapp.domain.models.ui_models.Feedback
import com.example.titossycleaningservicesapp.domain.viewmodel.FeedbackViewModel
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@Composable
fun FeedbackScreen(paddingValues: PaddingValues) {
    val feedbackViewmodel: FeedbackViewModel = hiltViewModel()
    val feedbackUiState by feedbackViewmodel.feedbackState.collectAsStateWithLifecycle()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        if (feedbackUiState.isLoading) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CustomProgressIndicator(isLoading = true)
            }
        }

        if (feedbackUiState.errorMessage.isNotEmpty()) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = feedbackUiState.errorMessage,
                    style = MaterialTheme.typography.bodyLarge.copy(color = MaterialTheme.colorScheme.error)
                )
            }
        }

        if (feedbackUiState.feedbacks.isNotEmpty()) {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                contentPadding = paddingValues
            ) {
                items(feedbackUiState.feedbacks.size) { index ->
                    FeedbackItem(
                        modifier = Modifier.padding(bottom = 8.dp),
                        feedback = feedbackUiState.feedbacks[index]
                    )
                }
            }
        }


    }
}

@Composable
fun FeedbackItem(modifier: Modifier = Modifier, feedback: Feedback) {
    Card(
        modifier = modifier
            .fillMaxWidth(),
        shape = MaterialTheme.shapes.medium,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            // Header row with booking id and rating
            Column(
                modifier = modifier.fillMaxWidth(),
            ) {
                Text(
                    text = "Booking:  ${feedback.bookingId}",
                    style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold)
                )

                RatingDisplay(rating = feedback.rating)

                // Feedback text
                Text(
                    text = feedback.feedback,
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier.height(16.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "Customer ID: ${feedback.customerId}",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )

                    Text(
                        text = formatDateTime(feedback.createdAt),
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }
    }
}

@Composable
fun RatingDisplay(modifier: Modifier = Modifier,rating: Double) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Text(
            "Rating: ",
        )
        Text(
            text = rating.toString(),
            style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold)
        )

        Icon(
            imageVector = Icons.Default.Star,
            contentDescription = "Rating",
            tint = Color(0xFFFFD700),
            modifier = modifier.size(20.dp)
        )
    }
}

fun formatDateTime(dateTimeString: String): String {
    return try {
        val formatter = DateTimeFormatter.ofPattern("MMM dd, yyyy hh:mm a")
        val dateTime = LocalDateTime.parse(dateTimeString)
        dateTime.format(formatter)
    } catch (e: Exception) {
        dateTimeString
    }
}