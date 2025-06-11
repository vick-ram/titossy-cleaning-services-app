package com.example.titossycleaningservicesapp.domain.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.titossycleaningservicesapp.core.Resource
import com.example.titossycleaningservicesapp.domain.models.ui_models.CustomerPaymentUIState
import com.example.titossycleaningservicesapp.domain.models.ui_models.SupplierPaymentStatusUiState
import com.example.titossycleaningservicesapp.domain.models.ui_models.SupplierPaymentUiState
import com.example.titossycleaningservicesapp.domain.repository.CustomerPaymentRepository
import com.example.titossycleaningservicesapp.domain.repository.SupplierPaymentRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PaymentViewModel @Inject constructor(
    private val customerPaymentRepository: CustomerPaymentRepository,
    private val supplierPaymentRepository: SupplierPaymentRepository
) : ViewModel() {
    private val _customerPaymentUiState = MutableStateFlow(CustomerPaymentUIState(isLoading = true))
    val customerPaymentUIState = _customerPaymentUiState.asStateFlow()

    private val _supplierPaymentUiState = MutableStateFlow(SupplierPaymentUiState(isLoading = true))
    val supplierPaymentUiState = _supplierPaymentUiState.asStateFlow()

    private val _supplierPaymentStatusUiState = MutableStateFlow(SupplierPaymentStatusUiState(isLoading = true))
    val supplierPaymentStatusUiState = _supplierPaymentStatusUiState.asStateFlow()

    init {
        fetchCustomerPayments()
        fetchSupplierPayments()
    }

    fun makePayment(
        bookingId: String,
        phoneNumber: String,
        transactionCode: String
    ) =
        viewModelScope.launch {
            customerPaymentRepository.makePayment(bookingId, phoneNumber, transactionCode)
                .map { resource ->
                    when (resource) {
                        is Resource.Error -> CustomerPaymentUIState(
                            isLoading = false,
                            errorMessage = resource.message.toString()
                        )

                        is Resource.Loading -> CustomerPaymentUIState(
                            isLoading = true
                        )

                        is Resource.Success -> CustomerPaymentUIState(
                            isLoading = false,
                            successMessage = resource.data.toString()
                        )
                    }
                }
                .collect { state ->
                    _customerPaymentUiState.update { state }
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
            .collectLatest { resource ->
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

    fun paySupplier(
        orderId: String,
        method: String
    ) = viewModelScope.launch {
        supplierPaymentRepository.createSupplierPayment(
            orderId, method
        )
            .collectLatest {resource ->
                when(resource) {
                    is Resource.Error -> {
                        _supplierPaymentStatusUiState.update {
                            it.copy(
                                isLoading = false,
                                errorMessage = resource.message.toString()
                            )
                        }
                    }
                    is Resource.Loading -> {
                        _supplierPaymentStatusUiState.update {
                            it.copy(
                                isLoading = true
                            )
                        }
                    }
                    is Resource.Success -> {
                        _supplierPaymentStatusUiState.update {
                            it.copy(
                                isLoading = false,
                                successMessage = resource.data.toString()
                            )
                        }
                    }
                }
            }
    }

    fun fetchSupplierPayments() = viewModelScope.launch {
        supplierPaymentRepository.getSupplierPayments()
            .collect { resource ->
                when(resource) {
                    is Resource.Error -> {
                        _supplierPaymentUiState.update {
                            it.copy(
                                isLoading = false,
                                errorMessage = resource.message.toString()
                            )
                        }
                    }
                    is Resource.Loading -> {
                        _supplierPaymentUiState.update {
                            it.copy(
                                isLoading = true
                            )
                        }
                    }
                    is Resource.Success -> {
                        resource.data?.let {
                            _supplierPaymentUiState.update { state ->
                                state.copy(
                                    isLoading = false,
                                    supplierPayments = it
                                )
                            }
                        }
                    }
                }
            }
    }
}