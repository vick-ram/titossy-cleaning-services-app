package com.example.titossycleaningservicesapp.data.repository

import com.example.titossycleaningservicesapp.core.FileUtils
import com.example.titossycleaningservicesapp.core.Resource
import com.example.titossycleaningservicesapp.data.remote.api.ApiService
import com.example.titossycleaningservicesapp.domain.models.ui_models.ChatMessage
import com.example.titossycleaningservicesapp.domain.repository.MessageRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class MessageRepositoryImpl @Inject constructor(
    private val apiService: ApiService
): MessageRepository {
    override fun getMessages(sender: String, receiver: String): Flow<Resource<List<ChatMessage>>> {
        return flow {
            emit(Resource.Loading)
            val response = apiService.getMessages(sender, receiver)
            when (response.status) {
                "success" -> {
                    val messages = response.data?.map { it.toChatMessage() }
                    messages?.let { emit(Resource.Success(it)) }
                }

                "error" -> {
                    if (response.error != null) {
                        val error = FileUtils.createErrorMessage(response.error)
                        throw Exception(error)
                    }
                }
            }
        }.catch { e ->
            e.printStackTrace()
            emit(Resource.Error(e.message.toString()))
        }
    }
}