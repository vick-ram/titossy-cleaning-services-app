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
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class SupplierAuthViewModel @Inject constructor(
    private val supplierRepository: SupplierRepository,
    private val dataStoreKeys: DataStoreKeys
) : ViewModel() {

    private val _resultChannel = Channel<AuthEvent>(Channel.BUFFERED)
    val resultChannel = _resultChannel.receiveAsFlow()

    private val _supplierUiState = MutableStateFlow(SupplierUiState(isLoading = true))
    val supplierUiState: StateFlow<SupplierUiState> = _supplierUiState.asStateFlow()

    var isLoading by mutableStateOf(false)


    init {
        fetchSuppliers()
    }

    private fun sendEvent(event: AuthEvent) = viewModelScope.launch {
        _resultChannel.send(event)
    }

    fun signIn(email: String, password: String) = viewModelScope.launch {
        isLoading = true
        val result = supplierRepository.signInSupplier(email, password)
        if (result is AuthEvent.Success) {
            dataStoreKeys.saveUserTypeToDataStore("SUPPLIER")
        }
        sendEvent(result)
        isLoading = false
    }

    fun signUp(
        firstName: String,
        lastName: String,
        phone: String,
        address: String,
        email: String,
        password: String,
    ) = viewModelScope.launch {
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

    fun clearToken() = viewModelScope.launch {
        dataStoreKeys.clearToken()
    }

    override fun onCleared() {
        super.onCleared()
        _resultChannel.close()
    }

}