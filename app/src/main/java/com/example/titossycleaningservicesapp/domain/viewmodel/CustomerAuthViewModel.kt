package com.example.titossycleaningservicesapp.domain.viewmodel

import android.content.Context
import android.net.Uri
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.titossycleaningservicesapp.core.Resource
import com.example.titossycleaningservicesapp.data.local.datastore.DataStoreKeys
import com.example.titossycleaningservicesapp.data.remote.util.AuthEvent
import com.example.titossycleaningservicesapp.domain.models.ui_models.CustomerState
import com.example.titossycleaningservicesapp.domain.repository.CustomerRepository
import com.example.titossycleaningservicesapp.presentation.auth.utils.ValidationState
import com.example.titossycleaningservicesapp.presentation.auth.utils.Validations
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class CustomerAuthViewModel @Inject constructor(
    private val customerRepository: CustomerRepository,
    private val dataStoreKeys: DataStoreKeys
) : ViewModel() {

    var username by mutableStateOf("")
    var firstName by mutableStateOf("")
    var lastName by mutableStateOf("")
    var phone by mutableStateOf("")
    var email by mutableStateOf("")
    var password by mutableStateOf("")

    var isLoading by mutableStateOf(false)

    private val _passwordState = MutableStateFlow<ValidationState>(ValidationState.Idle)
    val passwordState: StateFlow<ValidationState> = _passwordState.asStateFlow()

    private val _passwordErrorMessage = MutableStateFlow("")
    val passwordErrorMessage: StateFlow<String> = _passwordErrorMessage.asStateFlow()

    private val _phoneState = MutableStateFlow<ValidationState>(ValidationState.Idle)
    val phoneState: StateFlow<ValidationState> = _phoneState.asStateFlow()

    private val _phoneErrorMessage = MutableStateFlow("")
    val phoneErrorMessage: StateFlow<String> = _phoneErrorMessage.asStateFlow()

    private val _emailState = MutableStateFlow<ValidationState>(ValidationState.Idle)
    val emailState: StateFlow<ValidationState> = _emailState.asStateFlow()

    private val _emailErrorMessage = MutableStateFlow("")
    val emailErrorMessage: StateFlow<String> = _emailErrorMessage.asStateFlow()

    private val _authEvent = Channel<AuthEvent>(Channel.BUFFERED)
    val authEvent = _authEvent.receiveAsFlow()

    private suspend fun sendEvent(event: AuthEvent) {
        _authEvent.send(event)
    }

    private val _customerUiState = MutableStateFlow(CustomerState(isLoading = true))
    val customerUiState: StateFlow<CustomerState> = _customerUiState

    init {
        getCustomers()
    }


    fun signIn() = viewModelScope.launch {
        isLoading = true
        val usernameOrEmail = email.ifEmpty { username }
        val result = customerRepository.signInCustomer(
            username = if (usernameOrEmail.contains("@")) null else usernameOrEmail,
            email = if (usernameOrEmail.contains("@")) usernameOrEmail else null,
            password = password
        )
        if (result is AuthEvent.Success) {
            dataStoreKeys.saveUserTypeToDataStore("CUSTOMER")
        }
        sendEvent(result)
        isLoading = false
    }

    fun signUp() = viewModelScope.launch {
        isLoading = true
        val result =
            customerRepository.createCustomer(username, firstName, lastName, phone, email, password)
        sendEvent(result)
        isLoading = false
    }

    fun update(customerId: UUID) = viewModelScope.launch {
        isLoading = true
        val result = customerRepository.updateCustomer(
            customerId, username, firstName, lastName, phone, email, password
        )
        sendEvent(result)
        isLoading = false
    }

    fun signOut() = viewModelScope.launch {
        isLoading = true
        val result = customerRepository.signOutCustomer()
        sendEvent(result)
        isLoading = false
    }

    fun updateProfilePicture(
        customerId: UUID,
        profilePicture: Uri,
        context: Context
    ) = viewModelScope.launch {
        isLoading = true
        val result = customerRepository
            .updateCustomerProfilePicture(
                customerId, profilePicture, context
            )
        sendEvent(result)
        isLoading = false
    }

    fun getCustomerById(id: UUID) = viewModelScope.launch {
        val result = customerRepository.getCustomerById(id)
        result.collect { res ->
            when (res) {
                is Resource.Error -> {
                    _customerUiState.value = CustomerState(error = res.message)
                }

                Resource.Loading -> {
                    _customerUiState.value = CustomerState(isLoading = true)
                }

                is Resource.Success -> {
                    _customerUiState.value = CustomerState(customer = res.data)
                }
            }
        }
    }

    fun getCustomersByEmail(email: String) = viewModelScope.launch {
        val result = customerRepository.getCustomersByEmail(email)
        result.collect { res ->
            when (res) {
                is Resource.Error -> {
                    _customerUiState.value = CustomerState(error = res.message)
                }

                Resource.Loading -> {
                    _customerUiState.value = CustomerState(isLoading = true)
                }

                is Resource.Success -> {
                    _customerUiState.value = CustomerState(customer = res.data)
                }
            }
        }
    }

    fun getCustomerByUsername() = viewModelScope.launch {
        customerRepository.getCustomerByUsername(username)
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.Eagerly,
                initialValue = Resource.Loading
            )
            .collect { resource ->
                when (resource) {
                    is Resource.Error -> {
                        _customerUiState.update {
                            it.copy(
                                error = resource.message,
                                isLoading = false
                            )
                        }
                    }

                    is Resource.Loading -> {
                        _customerUiState.update { it.copy(isLoading = true) }
                    }

                    is Resource.Success -> {
                        _customerUiState.update {
                            it.copy(
                                customer = resource.data,
                                isLoading = false
                            )
                        }
                    }
                }
            }
    }

    fun getCustomers() = viewModelScope.launch {
        customerRepository.getCustomers()
            .map { resource ->
                when (resource) {
                    is Resource.Error -> CustomerState(error = resource.message, isLoading = false)
                    is Resource.Loading -> CustomerState(isLoading = true)
                    is Resource.Success -> CustomerState(
                        customers = resource.data,
                        isLoading = false
                    )
                }
            }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.Eagerly,
                initialValue = CustomerState(isLoading = true)
            )
            .collect { state ->
                _customerUiState.value = state
            }
    }

    fun onPasswordChange(newPassword: String) {
        viewModelScope.launch {
            _passwordState.value = Validations.isPasswordValid(newPassword)
            if (_passwordState.value is ValidationState.Valid) {
                _passwordErrorMessage.emit("")
            } else if (_passwordState.value is ValidationState.Invalid) {
                _passwordErrorMessage.emit((_passwordState.value as ValidationState.Invalid).message)
            }
        }
    }

    fun onPhoneChange(newPhone: String) {
        viewModelScope.launch {
            _phoneState.value = Validations.isPhoneValid(newPhone)
            if (_phoneState.value is ValidationState.Valid) {
                _phoneErrorMessage.emit("")
            } else if (_phoneState.value is ValidationState.Invalid) {
                _phoneErrorMessage.emit((_phoneState.value as ValidationState.Invalid).message)
            }
        }
    }

    fun onEmailChange(newEmail: String) {
        viewModelScope.launch {
            _emailState.value = Validations.isEmailValid(newEmail)
            if (_emailState.value is ValidationState.Valid) {
                _emailErrorMessage.emit("")
            } else if (_emailState.value is ValidationState.Invalid) {
                _emailErrorMessage.emit((_emailState.value as ValidationState.Invalid).message)
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        _authEvent.close()
    }

}