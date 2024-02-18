package com.example.titossycleaningservicesapp.domain.viewmodel.util

import com.example.titossycleaningservicesapp.data.database.user.data.Employees

data class EmployeeUiState(
    val isLoading: Boolean = false,
    val employees: List<Employees>? = null,
    val employee: Employees? = null,
    val errorMessage: String? = null
)
