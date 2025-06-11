package com.example.titossycleaningservicesapp.domain.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.titossycleaningservicesapp.core.Resource
import com.example.titossycleaningservicesapp.core.WebsocketService
import com.example.titossycleaningservicesapp.data.remote.api.ApiConstants
import com.example.titossycleaningservicesapp.data.remote.dto.MessageUiState
import com.example.titossycleaningservicesapp.domain.repository.MessageRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MessageViewModel @Inject constructor(
    private val websocketService: WebsocketService,
    private val messageRepository: MessageRepository
): ViewModel() {

    private val _messageUiState = MutableStateFlow(MessageUiState(isLoading = true))
    val messageUiState: StateFlow<MessageUiState> = _messageUiState.asStateFlow()

    init {
        websocketService.connect(ApiConstants.CHAT_ENDPOINT)
    }

    fun sendMessage(message: String) = viewModelScope.launch {
        websocketService.sendMessage(message)
    }

    fun fetchMessages(receiver: String) = viewModelScope.launch {
        messageRepository.getMessages(receiver).collect { resource ->
            when (resource) {
                is Resource.Loading -> {
                    _messageUiState.update { state ->
                        state.copy(isLoading = true, messages = emptyList(), error = "")
                    }
                }
                is Resource.Success -> {
                    _messageUiState.update { state ->
                        state.copy(
                            isLoading = false,
                            messages = resource.data ?: emptyList(),
                            error = ""
                        )
                    }
                }
                is Resource.Error -> {
                    _messageUiState.update { state ->
                        state.copy(
                            isLoading = false,
                            messages = emptyList(),
                            error = resource.message ?: "An error occurred"
                        )
                    }
                }
            }
        }
    }
}