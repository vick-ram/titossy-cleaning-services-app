package com.example.titossycleaningservicesapp.domain.models.ui_models

data class ChatMessage(
    val id: String,
    val senderId: String,
    val receiverId: String,
    val message: String,
    val timestamp: String,
)
