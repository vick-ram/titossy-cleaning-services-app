package com.example.titossycleaningservicesapp.domain.repository

import com.example.titossycleaningservicesapp.core.Resource
import com.example.titossycleaningservicesapp.domain.models.ui_models.ChatMessage
import kotlinx.coroutines.flow.Flow

interface MessageRepository {
    fun getMessages(sender: String, receiver: String): Flow<Resource<List<ChatMessage>>>
}