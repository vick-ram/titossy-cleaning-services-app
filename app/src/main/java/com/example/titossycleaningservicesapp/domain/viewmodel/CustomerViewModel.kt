package com.example.titossycleaningservicesapp.domain.viewmodel

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
import kotlinx.coroutines.delay
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
class CustomerViewModel @Inject constructor(
    private val customerRepository: CustomerRepository,
    private val dataStoreKeys: DataStoreKeys
) : ViewModel() {

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

    private val _firstnameState = MutableStateFlow<ValidationState>(ValidationState.Idle)
    val firstnameState: StateFlow<ValidationState> = _firstnameState.asStateFlow()

    private val _firstnameErrorMessage = MutableStateFlow("")
    val firstnameErrorMessage: StateFlow<String> = _firstnameErrorMessage.asStateFlow()

    private val _lastnameState = MutableStateFlow<ValidationState>(ValidationState.Idle)
    val lastnameState: StateFlow<ValidationState> = _lastnameState.asStateFlow()

    private val _lastnameErrorMessage = MutableStateFlow("")
    val lastnameErrorMessage: StateFlow<String> = _lastnameErrorMessage.asStateFlow()

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

    fun onFieldChange(type: FieldType, newValue: String) = viewModelScope.launch {
        when(type) {
            FieldType.EMAIL -> {
                email = newValue
                _emailState.value = Validations.isEmailValid(newValue)
                if (_emailState.value is ValidationState.Valid) {
                    _emailErrorMessage.emit("")
                } else if (_emailState.value is ValidationState.Invalid) {
                    _emailErrorMessage.emit((_emailState.value as ValidationState.Invalid).message)
                }
            }
            FieldType.PASSWORD -> {
                password = newValue
                _passwordState.value = Validations.isPasswordValid(newValue)
                if (_passwordState.value is ValidationState.Valid) {
                    _passwordErrorMessage.emit("")
                } else if (_passwordState.value is ValidationState.Invalid) {
                    _passwordErrorMessage.emit((_passwordState.value as ValidationState.Invalid).message)
                }
            }
            FieldType.PHONE -> {
                phone = newValue
                _phoneState.value = Validations.isPhoneValid(newValue)
                if (_phoneState.value is ValidationState.Valid) {
                    _phoneErrorMessage.emit("")
                } else if (_phoneState.value is ValidationState.Invalid) {
                    _phoneErrorMessage.emit((_phoneState.value as ValidationState.Invalid).message)
                }
            }
            FieldType.FIRST_NAME -> {
                firstName = newValue
                _firstnameState.value = Validations.isValidName(newValue)
                if (_firstnameState.value is ValidationState.Valid) {
                    _firstnameErrorMessage.emit("")
                } else if (_firstnameState.value is ValidationState.Invalid) {
                    _firstnameErrorMessage.emit((_firstnameState.value as ValidationState.Invalid).message)
                }
            }
            FieldType.LAST_NAME -> {
                lastName = newValue
                _lastnameState.value = Validations.isValidName(newValue)
                if (_lastnameState.value is ValidationState.Valid) {
                    _lastnameErrorMessage.emit("")
                } else if (_lastnameState.value is ValidationState.Invalid) {
                    _lastnameErrorMessage.emit((_lastnameState.value as ValidationState.Invalid).message)
                }
            }
        }
    }

    private fun resetSignFields() {
        firstName = ""
        lastName = ""
        phone = ""
        email = ""
        password = ""
    }


    fun signIn() = viewModelScope.launch {
        isLoading = true
        val result = customerRepository.signInCustomer(
            email = email,
            password = password
        )
        if (result is AuthEvent.Success) {
            dataStoreKeys.saveUserTypeToDataStore("CUSTOMER")
            resetSignFields()
        }
        sendEvent(result)
        isLoading = false
    }

    fun signUp() = viewModelScope.launch {
        isLoading = true
        val result =
            customerRepository.createCustomer(firstName, lastName, phone, email, password)
        if (result is AuthEvent.Success) {
            resetSignFields()
        }
        sendEvent(result)
        isLoading = false
    }

    fun update(customerId: String) = viewModelScope.launch {
        isLoading = true
        val result = customerRepository.updateCustomer(
            customerId, firstName, lastName, phone, email, password
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

    fun getCustomerById(id: UUID) = viewModelScope.launch {
        val result = customerRepository.getCustomerById(id)
        result.collect { res ->
            when (res) {
                is Resource.Error -> {
                    _customerUiState.value = CustomerState(
                        isLoading = false,
                        error = res.message
                    )
                }

                Resource.Loading -> {
                    _customerUiState.value = CustomerState(isLoading = true)
                }

                is Resource.Success -> {
                    _customerUiState.value = CustomerState(
                        isLoading = false,
                        customer = res.data
                    )
                }
            }
        }
    }

    fun getCustomersByEmail(email: String) = viewModelScope.launch {
        val result = customerRepository.getCustomersByEmail(email)
        result.collect { res ->
            when (res) {
                is Resource.Error -> {
                    _customerUiState.value = CustomerState(
                        isLoading = false,
                        error = res.message
                    )
                }

                Resource.Loading -> {
                    _customerUiState.value = CustomerState(isLoading = true)
                }

                is Resource.Success -> {
                    _customerUiState.value = CustomerState(
                        isLoading = false,
                        customer = res.data
                    )
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

    enum class FieldType{
        EMAIL,
        PASSWORD,
        PHONE,
        FIRST_NAME,
        LAST_NAME
    }

    override fun onCleared() {
        super.onCleared()
        _authEvent.close()
    }

}