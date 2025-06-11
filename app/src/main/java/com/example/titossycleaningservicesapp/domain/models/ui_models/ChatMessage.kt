package com.example.titossycleaningservicesapp.domain.models.ui_models

import com.example.titossycleaningservicesapp.presentation.users.customer.screens.ChatMessageUiModel
import java.time.format.DateTimeFormatter

data class ChatMessage(
    val id: String,
    val senderId: String,
    val receiverId: String,
    val message: String,
    val timestamp: String,
) {
    fun toChatMessage(currentUser: String): ChatMessageUiModel {
        val formattedTime = timestamp.format(DateTimeFormatter.ofPattern("HH:mm"))
        return ChatMessageUiModel(
            id = id,
            text = message,
            timestamp = formattedTime,
            isFromMe = senderId == currentUser
        )
    }
}
