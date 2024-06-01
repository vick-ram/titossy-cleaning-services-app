package com.example.titossycleaningservicesapp.domain.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.titossycleaningservicesapp.data.local.datastore.DataStoreKeys
import com.example.titossycleaningservicesapp.data.remote.util.AuthEvent
import com.example.titossycleaningservicesapp.domain.repository.EmployeeRepository
import com.example.titossycleaningservicesapp.presentation.auth.utils.decodeToken
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
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

    var isLoading by mutableStateOf(false)
    var email by mutableStateOf("")
    var password by mutableStateOf("")
    var availability by mutableStateOf("")

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

    fun update(id: UUID) = viewModelScope.launch {
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


    fun onEmailChange(newEmail: String) {
        email = newEmail
    }

    fun onPasswordChange(newPassword: String) {
        password = newPassword
    }

    suspend fun readToken(): String? {
        return dataStore.getTokenFromDataStore()
    }


    suspend fun employeeRole() : String? {
        val token = readToken()
        val decodedToken = token?.let { decodeToken(it) }
        val role = decodedToken?.get("role")
        return role as? String
    }


    override fun onCleared() {
        super.onCleared()
        _resultChannel.close()
    }

}