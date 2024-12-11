package com.example.titossycleaningservicesapp.domain.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.titossycleaningservicesapp.core.Resource
import com.example.titossycleaningservicesapp.data.local.datastore.DataStoreKeys
import com.example.titossycleaningservicesapp.data.remote.util.AuthEvent
import com.example.titossycleaningservicesapp.domain.models.Roles
import com.example.titossycleaningservicesapp.domain.models.ui_models.EmployeeUiState
import com.example.titossycleaningservicesapp.domain.repository.EmployeeRepository
import com.example.titossycleaningservicesapp.presentation.auth.utils.ValidationState
import com.example.titossycleaningservicesapp.presentation.auth.utils.Validations
import com.example.titossycleaningservicesapp.presentation.auth.utils.decodeToken
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class EmployeeViewModel @Inject constructor(
    private val employeeRepository: EmployeeRepository,
    private val dataStore: DataStoreKeys
) : ViewModel() {

    private val _resultChannel = Channel<AuthEvent>(Channel.BUFFERED)
    val resultChannel = _resultChannel.receiveAsFlow()

    private val _employeeUiState = MutableStateFlow(EmployeeUiState(isLoading = true))
    val employeeUiState = _employeeUiState.asStateFlow()

    var isLoading by mutableStateOf(false)
    var email by mutableStateOf("")
    var password by mutableStateOf("")
    var availability by mutableStateOf("")

    private val _emailState = MutableStateFlow<ValidationState>(ValidationState.Idle)
    val emailState = _emailState.asStateFlow()

    private val _emailError = MutableStateFlow("")
    val emailError = _emailError.asStateFlow()

    private val _passwordState = MutableStateFlow<ValidationState>(ValidationState.Idle)
    val passwordState = _passwordState.asStateFlow()

    private val _passwordError = MutableStateFlow("")
    val passwordError = _passwordError.asStateFlow()

    init {
        fetchEmployees()
    }

    fun onFieldChange(type: FieldType, newValue: String) {
        viewModelScope.launch {
            when(type) {
                FieldType.EMAIL -> {
                    email = newValue
                    _emailState.value = Validations.isEmailValid(newValue)
                    if (_emailState.value is ValidationState.Valid) {
                        _emailError.emit("")
                    } else if (_emailState.value is ValidationState.Invalid) {
                        _emailError.emit((_emailState.value as ValidationState.Invalid).message)
                    }
                }
                FieldType.PASSWORD -> {
                    password = newValue
                    _passwordState.value = Validations.isPasswordValid(newValue)
                    if (_passwordState.value is ValidationState.Valid) {
                        _passwordError.emit("")
                    } else if (_emailState.value is ValidationState.Invalid) {
                        _passwordError.emit((_passwordState.value as ValidationState.Invalid).message)
                    }
                }
            }
        }
    }

    fun signIn() = viewModelScope.launch {
        isLoading = true
        val result = employeeRepository.signInEmployee(email, password)
        if (result is AuthEvent.Success) {
            val role = employeeRole()
            role?.let { dataStore.saveUserTypeToDataStore(it) }
        }
        _resultChannel.send(result)
        isLoading = false
    }

    fun update(id: String) = viewModelScope.launch {
        isLoading = true
        val result = employeeRepository.updateAvailability(id, availability)
        _resultChannel.send(result)
        isLoading = false
    }

    fun signOut() = viewModelScope.launch {
        isLoading = true
        val result = employeeRepository.signOutEmployee()
        _resultChannel.send(result)
        isLoading = false
    }

    suspend fun readToken(): String? {
        return dataStore.getTokenFromDataStore()
    }


    suspend fun employeeRole(): String? {
        val token = readToken()
        val decodedToken = token?.let { decodeToken(it) }
        val role = decodedToken?.get("role")
        return role as? String
    }

    fun fetchEmployees() = viewModelScope.launch {
        employeeRepository.getAllEmployees()
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5000L),
                initialValue = Resource.Loading
            )
            .map { resource ->
                when (resource) {
                    is Resource.Error -> EmployeeUiState(
                        isLoading = false,
                        errorMessage = resource.message.toString()
                    )

                    is Resource.Loading -> EmployeeUiState(
                        isLoading = true
                    )

                    is Resource.Success -> EmployeeUiState(
                        isLoading = false,
                        employees = resource.data
                    )
                }
            }
            .collect { state ->
                _employeeUiState.update { state }
            }
    }

    fun fetchEmployeesByRole(role: Roles) = viewModelScope.launch {
        employeeRepository.getEmployeesByRole(role.name)
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5000L),
                initialValue = Resource.Loading
            )
            .map { resource ->
                when (resource) {
                    is Resource.Error -> EmployeeUiState(
                        isLoading = false,
                        errorMessage = resource.message.toString()
                    )

                    is Resource.Loading -> EmployeeUiState(
                        isLoading = true
                    )

                    is Resource.Success -> EmployeeUiState(
                        isLoading = false,
                        employees = resource.data
                    )
                }
            }
            .collect { state ->
                _employeeUiState.update { state }
            }
    }

    fun fetchEmployeeById(id: String) = viewModelScope.launch {
        employeeRepository.getEmployeeById(id)
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5000L),
                initialValue = Resource.Loading
            )
            .map { resource ->
                when(resource) {
                    is Resource.Error -> EmployeeUiState(
                        isLoading = false,
                        errorMessage = resource.message.toString()
                    )
                    is Resource.Loading -> EmployeeUiState(
                        isLoading = true
                    )
                    is Resource.Success -> EmployeeUiState(
                        isLoading = false,
                        employee = resource.data
                    )
                }
            }
            .collect {state ->
                _employeeUiState.update { state }
            }
    }

    enum class FieldType {
        EMAIL,
        PASSWORD
    }


    override fun onCleared() {
        super.onCleared()
        _resultChannel.close()
    }

}
