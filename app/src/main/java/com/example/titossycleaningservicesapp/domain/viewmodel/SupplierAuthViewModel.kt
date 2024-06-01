package com.example.titossycleaningservicesapp.domain.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.titossycleaningservicesapp.data.local.datastore.DataStoreKeys
import com.example.titossycleaningservicesapp.data.remote.util.AuthEvent
import com.example.titossycleaningservicesapp.domain.repository.SupplierRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
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
    var company by mutableStateOf("")
    var email by mutableStateOf("")
    var password by mutableStateOf("")
    var county by mutableStateOf("")
    var region by mutableStateOf("")
    var postalCode by mutableStateOf("")

    private val _resultChannel = Channel<AuthEvent>(Channel.BUFFERED)
    val resultChannel = _resultChannel.receiveAsFlow()

    var isLoading by mutableStateOf(false)

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
            company,
            county,
            region,
            postalCode,
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
            company,
            county,
            region,
            postalCode,
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

    fun onCompanyChange(newCompany: String) {
        company = newCompany
    }

    fun onCountyChange(newCounty: String) {
        county = newCounty
    }

    fun onRegionChange(newRegion: String) {
        region = newRegion
    }

    fun onPostalCodeChange(newPostalCode: String) {
        postalCode = newPostalCode
    }

    fun onEmailChange(newEmail: String) {
        email = newEmail
    }

    fun onPasswordChange(newPassword: String) {
        password = newPassword
    }

    override fun onCleared() {
        super.onCleared()
        _resultChannel.close()
    }

}