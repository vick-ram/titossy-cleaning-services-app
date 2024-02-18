package com.example.titossycleaningservicesapp.domain.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.titossycleaningservicesapp.data.repo.EmployeeRepository
import com.example.titossycleaningservicesapp.data.utils.Resource
import com.example.titossycleaningservicesapp.domain.viewmodel.util.EmployeeUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FirebaseViewModel @Inject constructor(
    private val employeeRepository: EmployeeRepository
) : ViewModel() {
    private val _employeeData = MutableStateFlow(EmployeeUiState())
    val employeeData = _employeeData.asStateFlow()

    fun getEmployee() = viewModelScope.launch {
        val result = employeeRepository.fetchEmployee()

        result.collect { res ->
            when (res) {
                is Resource.Loading -> {
                    _employeeData.update { it.copy(isLoading = true) }
                }

                is Resource.Failure -> {
                    _employeeData.update { it.copy(errorMessage = res.message) }
                }

                is Resource.Success -> {
                    _employeeData.update { it.copy(employee = res.data, isLoading = false) }
                }
            }
        }
    }

    fun getAllEmployees() = viewModelScope.launch {
        val result = employeeRepository.getAllEmployeeData()
        result.collect { res ->
            when (res) {
                is Resource.Loading -> {
                    _employeeData.update { it.copy(isLoading = true) }
                }

                is Resource.Failure -> {
                    _employeeData.update { it.copy(errorMessage = res.message) }
                }

                is Resource.Success -> {
                    _employeeData.update { it.copy(employees = res.data, isLoading = false) }
                }
            }
        }
    }
}