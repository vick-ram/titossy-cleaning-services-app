package com.example.titossycleaningservicesapp.data.database.user.data

data class Customer(
    val userId: String,
    val firstName: String,
    val lastName: String,
    val gender: String = "",
    val email: String,
    val password: String,
    val address: String = "",
    val photoUrl: String = "",
    val status: String = "pending"
)