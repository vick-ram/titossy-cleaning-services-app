package com.example.titossycleaningservicesapp.data.remote.dto

data class ApiResponse<T>(
    val status: String?,
    val statusCode: Int?,
    val data: T?,
    val message: String?,
    val error: Any?
)

