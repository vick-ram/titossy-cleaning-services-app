package com.example.titossycleaningservicesapp.domain.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.titossycleaningservicesapp.core.Resource
import com.example.titossycleaningservicesapp.data.local.datastore.DataStoreKeys
import com.example.titossycleaningservicesapp.data.remote.util.AuthEvent
import com.example.titossycleaningservicesapp.domain.models.ApprovalStatus
import com.example.titossycleaningservicesapp.domain.models.ui_models.SupplierUiState
import com.example.titossycleaningservicesapp.domain.repository.SupplierRepository
import com.example.titossycleaningservicesapp.presentation.auth.utils.ValidationState
import com.example.titossycleaningservicesapp.presentation.auth.utils.Validations
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.Locale
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class SupplierViewModel @Inject constructor(
    private val supplierRepository: SupplierRepository,
    private val dataStoreKeys: DataStoreKeys
) : ViewModel() {

    var firstName by mutableStateOf("")
    var lastName by mutableStateOf("")
    var phone by mutableStateOf("")
    var email by mutableStateOf("")
    var password by mutableStateOf("")
    var address by mutableStateOf("")

    private val _resultChannel = Channel<AuthEvent>(Channel.BUFFERED)
    val resultChannel = _resultChannel.receiveAsFlow()

    private val _supplierUiState = MutableStateFlow(SupplierUiState(isLoading = true))
    val supplierUiState: StateFlow<SupplierUiState> = _supplierUiState.asStateFlow()

    var isLoading by mutableStateOf(false)

    private val _emailErrorMessage = MutableStateFlow("")
    val emailErrorMessage: StateFlow<String> = _emailErrorMessage.asStateFlow()

    private val _emailState = MutableStateFlow<ValidationState>(ValidationState.Idle)
    val emailState = _emailState.asStateFlow()

    private val _phoneState = MutableStateFlow<ValidationState>(ValidationState.Idle)
    val phoneState = _phoneState.asStateFlow()

    private val _phoneErrorMessage = MutableStateFlow("")
    val phoneErrorMessage: StateFlow<String> = _phoneErrorMessage.asStateFlow()

    private val _passwordState = MutableStateFlow<ValidationState>(ValidationState.Idle)
    val passwordState = _passwordState.asStateFlow()

    private val _passwordErrorMessage = MutableStateFlow("")
    val passwordErrorMessage: StateFlow<String> = _passwordErrorMessage.asStateFlow()

    private val _firstnameState = MutableStateFlow<ValidationState>(ValidationState.Idle)
    val firstnameState = _firstnameState.asStateFlow()

    private val _firstnameErrorMessage = MutableStateFlow("")
    val firstnameErrorMessage: StateFlow<String> = _firstnameErrorMessage.asStateFlow()

    private val _lastnameState = MutableStateFlow<ValidationState>(ValidationState.Idle)
    val lastnameState = _lastnameState.asStateFlow()

    private val _lastnameErrorMessage = MutableStateFlow("")
    val lastnameErrorMessage: StateFlow<String> = _lastnameErrorMessage.asStateFlow()

    private val _addressState = MutableStateFlow<ValidationState>(ValidationState.Idle)
    val addressState = _addressState.asStateFlow()

    private val _addressErrorMessage = MutableStateFlow("")
    val addressErrorMessage: StateFlow<String> = _addressErrorMessage.asStateFlow()


    init {
        fetchSuppliers()
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

            FieldType.ADDRESS -> {
                address = newValue
                _addressState.value = Validations.isValidName(newValue)
                if (_addressState.value is ValidationState.Valid) {
                    _addressErrorMessage.emit("")
                } else if (_addressState.value is ValidationState.Invalid) {
                    _addressErrorMessage.emit((_addressState.value as ValidationState.Invalid).message)
                }
            }
        }
    }

    private fun sendEvent(event: AuthEvent) = viewModelScope.launch {
        _resultChannel.send(event)
    }

    fun signIn() = viewModelScope.launch {
        isLoading = true
        val result = supplierRepository.signInSupplier(email, password)
        if (result is AuthEvent.Success) {
            dataStoreKeys.saveUserTypeToDataStore("SUPPLIER")
        }
        sendEvent(result)
        isLoading = false
    }

    fun signUp() = viewModelScope.launch {
        isLoading = true
        val result = supplierRepository.createSupplier(
            firstName,
            lastName,
            phone,
            address,
            email,
            password
        )
        sendEvent(result)
        isLoading = false
    }

    fun updateSupplierStatus(
        id: UUID,
        approvalStatus: ApprovalStatus
    ) = viewModelScope.launch {
        isLoading = true
        val result = supplierRepository.updateSupplierStatus(
            id = id,
            approvalStatus = approvalStatus
        )
        sendEvent(result)
        if (result is AuthEvent.Success) {
            val updatedSuppliers = supplierUiState.value.suppliers?.map {
                if (it.id == id) {
                    it.copy(status = approvalStatus)
                } else {
                    it
                }
            }
            _supplierUiState.update {
                it.copy(
                    suppliers = updatedSuppliers
                )
            }
            fetchSuppliers()
        }
        isLoading = false
    }

    fun update(
        id: UUID,
        firstName: String,
        lastName: String,
        phone: String,
        address: String,
        email: String,
        password: String,
    ) = viewModelScope.launch {
        isLoading = true
        val result = supplierRepository.updateSupplier(
            id,
            firstName,
            lastName,
            phone,
            address,
            email,
            password
        )
        sendEvent(result)
        isLoading = false
    }

    fun deleteSupplier(id: UUID) = viewModelScope.launch {
        isLoading = true
        val result = supplierRepository.deleteSupplier(id)
        sendEvent(result)
        if (result is AuthEvent.Success) {
            val updatedSuppliers = supplierUiState.value.suppliers?.filter { it.id != id }
            _supplierUiState.update {
                it.copy(
                    suppliers = updatedSuppliers
                )
            }
            fetchSuppliers()
        }
        isLoading = false
    }

    fun signOut() = viewModelScope.launch {
        isLoading = true
        val result = supplierRepository.signOutSupplier()
        sendEvent(result)
        isLoading = false
    }

    fun fetchSuppliers() = viewModelScope.launch {
        supplierRepository.getAllSuppliers()
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5000L),
                initialValue = Resource.Loading
            )
            .collect { resource ->
                when(resource){
                    is Resource.Error -> {
                        _supplierUiState.update {
                            it.copy(
                                isLoading = false,
                                errorMessage = resource.message.toString()
                            )
                        }
                    }
                    is Resource.Loading -> {
                        _supplierUiState.update {
                            it.copy(
                                isLoading = true
                            )
                        }
                    }
                    is Resource.Success -> {
                        _supplierUiState.update {
                            it.copy(
                                isLoading = false,
                                suppliers = resource.data
                            )
                        }
                    }
                }
            }
    }

    fun fetchSupplierById(id: String) = viewModelScope.launch {
        supplierRepository.getSupplierById(UUID.fromString(id))
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5000L),
                initialValue = Resource.Loading
            )
            .map { resource ->
                when(resource) {
                    is Resource.Error -> SupplierUiState(
                        isLoading = false,
                        errorMessage = resource.message.toString()
                    )
                    is Resource.Loading -> SupplierUiState(
                        isLoading = true
                    )
                    is Resource.Success -> SupplierUiState(
                        isLoading = false,
                        supplier = resource.data
                    )
                }
            }
            .collectLatest {state ->
                _supplierUiState.update { state }
            }
    }

    fun onNameSwap() {
        viewModelScope.launch {
            _supplierUiState.value = supplierUiState.value.copy(
                suppliers = supplierUiState.value.suppliers?.sortedBy { it.fullName.lowercase(Locale.ROOT) }
            )
        }
    }

    fun onStatusSwap() {
        viewModelScope.launch {
            _supplierUiState.value = supplierUiState.value.copy(
                suppliers = supplierUiState.value.suppliers?.sortedBy { it.status }
            )
        }
    }

    fun clearToken() = viewModelScope.launch {
        dataStoreKeys.clearToken()
    }

    enum class FieldType {
        EMAIL,
        PASSWORD,
        PHONE,
        FIRST_NAME,
        LAST_NAME,
        ADDRESS
    }

    override fun onCleared() {
        super.onCleared()
        _resultChannel.close()
    }

}