package com.example.titossycleaningservicesapp.data.remote.dto

import org.threeten.bp.format.DateTimeFormatter

data class ApiResponse<T>(
    val status: String?,
    val statusCode: Int?,
    val data: T?,
    val message: String?,
    val error: Any?
)

val dateTimeFormatter: DateTimeFormatter? = DateTimeFormatter.ISO_LOCAL_DATE_TIME
