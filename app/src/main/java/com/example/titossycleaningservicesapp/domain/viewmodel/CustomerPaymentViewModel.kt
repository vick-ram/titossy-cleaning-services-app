package com.example.titossycleaningservicesapp.domain.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.titossycleaningservicesapp.core.Resource
import com.example.titossycleaningservicesapp.domain.models.ui_models.CustomerPaymentUIState
import com.example.titossycleaningservicesapp.domain.repository.CustomerPaymentRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CustomerPaymentViewModel @Inject constructor(
    private val customerPaymentRepository: CustomerPaymentRepository
) : ViewModel() {
    private val _customerPaymentUiState = MutableStateFlow(CustomerPaymentUIState(isLoading = true))
    val customerPaymentUIState = _customerPaymentUiState.asStateFlow()

    fun makePayment(bookingId: String, phoneNumber: String, transactionCode: String) =
        viewModelScope.launch {
            customerPaymentRepository.makePayment(bookingId, phoneNumber, transactionCode)
                .collect { resource ->
                    when (resource) {
                        is Resource.Error -> {
                            _customerPaymentUiState.update {
                                it.copy(
                                    isLoading = false,
                                    errorMessage = resource.message.toString()
                                )
                            }
                        }

                        is Resource.Loading -> {
                            _customerPaymentUiState.update {
                                it.copy(
                                    isLoading = true
                                )
                            }
                        }

                        is Resource.Success -> {
                            _customerPaymentUiState.update {
                                it.copy(
                                    isLoading = false,
                                    customerPayment = resource.data
                                )
                            }
                        }
                    }
                }
        }

    fun updateCustomerPayment(
        paymentId: String,
        bookingId: String,
        phoneNumber: String,
        transactionCode: String
    ) = viewModelScope.launch {
        customerPaymentRepository.updatePayment(
            paymentId, bookingId, phoneNumber, transactionCode
        )
            .collect { resource ->
                when (resource) {
                    is Resource.Error -> {
                        _customerPaymentUiState.update {
                            it.copy(
                                isLoading = false,
                                errorMessage = resource.message.toString()
                            )
                        }
                    }

                    is Resource.Loading -> {
                        _customerPaymentUiState.update {
                            it.copy(
                                isLoading = true
                            )
                        }
                    }

                    is Resource.Success -> {
                        _customerPaymentUiState.update {
                            it.copy(
                                isLoading = false,
                                successMessage = resource.data.toString()
                            )
                        }
                        fetchCustomerPayments()
                    }
                }
            }
    }

    fun updateCustomerPaymentStatus(
        paymentId: String,
        status: String
    ) = viewModelScope.launch {
        customerPaymentRepository.updatePaymentStatus(
            paymentId, status
        )
            .collect { resource ->
                when (resource) {
                    is Resource.Error -> {
                        _customerPaymentUiState.update {
                            it.copy(
                                isLoading = false,
                                errorMessage = resource.message.toString()
                            )
                        }
                    }

                    is Resource.Loading -> {
                        _customerPaymentUiState.update {
                            it.copy(
                                isLoading = true
                            )
                        }
                    }

                    is Resource.Success -> {
                        _customerPaymentUiState.update {
                            it.copy(
                                isLoading = false,
                                successMessage = resource.data.toString()
                            )
                        }
                        fetchCustomerPayments()
                    }
                }
            }
    }

    fun fetchCustomerPayments() = viewModelScope.launch {
        customerPaymentRepository.getCustomerPayments()
            .collect { resource ->
                when (resource) {
                    is Resource.Error -> {
                        _customerPaymentUiState.update {
                            it.copy(
                                isLoading = false,
                                errorMessage = resource.message.toString()
                            )
                        }
                    }

                    is Resource.Loading -> {
                        _customerPaymentUiState.update {
                            it.copy(
                                isLoading = true
                            )
                        }
                    }

                    is Resource.Success -> {
                        resource.data?.let {
                            _customerPaymentUiState.update { state ->
                                state.copy(
                                    isLoading = false,
                                    customerPayments = it
                                )
                            }
                        }
                    }
                }
            }
    }

    fun deleteCustomerPayment(paymentId: String) = viewModelScope.launch {
        customerPaymentRepository.deletePayment(paymentId)
            .collect { resource ->
                when (resource) {
                    is Resource.Error -> {
                        _customerPaymentUiState.update {
                            it.copy(
                                isLoading = false,
                                errorMessage = resource.message.toString()
                            )
                        }
                    }

                    is Resource.Loading -> {
                        _customerPaymentUiState.update {
                            it.copy(
                                isLoading = true
                            )
                        }
                    }

                    is Resource.Success -> {
                        _customerPaymentUiState.update {
                            it.copy(
                                isLoading = false,
                                successMessage = resource.data.toString()
                            )
                        }
                        fetchCustomerPayments()
                    }
                }
            }
    }
}