package com.example.titossycleaningservicesapp.data.remote.dto

import com.example.titossycleaningservicesapp.domain.models.ui_models.ChatMessage

data class MessageDto(
    val id: String,
    val message: String,
    val sender: String,
    val receiver: String,
    val timestamp: String
) {
    fun toChatMessage() = ChatMessage(
        id = id,
        senderId = sender,
        receiverId = receiver,
        message = message,
        timestamp = timestamp
    )
}

data class MessageUiState(
    val isLoading: Boolean = false,
    val messages: List<ChatMessage> = emptyList(),
    val error: String = ""
)
