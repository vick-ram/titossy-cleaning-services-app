package com.example.titossycleaningservicesapp.domain.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.titossycleaningservicesapp.core.Resource
import com.example.titossycleaningservicesapp.data.local.datastore.DataStoreKeys
import com.example.titossycleaningservicesapp.data.remote.util.AuthEvent
import com.example.titossycleaningservicesapp.domain.models.ui_models.SupplierUiState
import com.example.titossycleaningservicesapp.domain.repository.SupplierRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class SupplierAuthViewModel @Inject constructor(
    private val supplierRepository: SupplierRepository,
    private val dataStoreKeys: DataStoreKeys
) : ViewModel() {

    var username by mutableStateOf("")
    var firstName by mutableStateOf("")
    var lastName by mutableStateOf("")
    var phone by mutableStateOf("")
    var email by mutableStateOf("")
    var password by mutableStateOf("")
    var address by mutableStateOf("")

    private val _resultChannel = Channel<AuthEvent>(Channel.BUFFERED)
    val resultChannel = _resultChannel.receiveAsFlow()
    private val _supplierUiState = MutableStateFlow(SupplierUiState(isLoading = true))
    val supplierUiState: StateFlow<SupplierUiState> = _supplierUiState

    var isLoading by mutableStateOf(false)


    init {
        fetchSuppliers()
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
            username,
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

    fun update(id: UUID) = viewModelScope.launch {
        isLoading = true
        val result = supplierRepository.updateSupplier(
            id,
            username,
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

    fun signOut() = viewModelScope.launch {
        isLoading = true
        val result = supplierRepository.signOutSupplier()
        sendEvent(result)
        isLoading = false
    }

    fun onUsernameChange(newUsername: String) {
        username = newUsername
    }

    fun onFirstNameChange(newFirstName: String) {
        firstName = newFirstName
    }

    fun onLastNameChange(newLastName: String) {
        lastName = newLastName
    }

    fun onPhoneChange(newPhone: String) {
        phone = newPhone
    }
    fun onAddressChange(newAddress: String) {
        address = newAddress
    }

    fun onEmailChange(newEmail: String) {
        email = newEmail
    }

    fun onPasswordChange(newPassword: String) {
        password = newPassword
    }

    fun fetchSuppliers() = viewModelScope.launch {
        supplierRepository.getAllSuppliers()
            .collectLatest { resource ->
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
    fun clearToken() = viewModelScope.launch {
        dataStoreKeys.clearToken()
    }

    override fun onCleared() {
        super.onCleared()
        _resultChannel.close()
    }

}