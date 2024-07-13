package com.example.titossycleaningservicesapp.core

import androidx.compose.ui.graphics.Color
import com.example.titossycleaningservicesapp.domain.models.OrderStatus

fun statusToColor(orderStatus: OrderStatus): Color {
    return when (orderStatus) {
        OrderStatus.PENDING -> Color(0xFFFFA500) // Orange
        OrderStatus.REVIEWED -> Color(0xFF42A5F5) // Light Blue
        OrderStatus.APPROVED -> Color(0xFF4CAF50) // Green
        OrderStatus.ACKNOWLEDGED -> Color(0xFF29B6F6) // Lighter Blue
        OrderStatus.RECEIVED -> Color(0xFF388E3C) // Darker Green
        OrderStatus.COMPLETED -> Color(0xFF2E7D32) // Dark Green
        OrderStatus.CANCELLED -> Color(0xFFF44336) // Red
    }
}
