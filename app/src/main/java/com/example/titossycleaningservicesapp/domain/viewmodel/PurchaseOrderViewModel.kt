package com.example.titossycleaningservicesapp.domain.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.titossycleaningservicesapp.core.Resource
import com.example.titossycleaningservicesapp.domain.models.ui_models.PurchaseOrderUiState
import com.example.titossycleaningservicesapp.domain.repository.PurchaseOrderRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.LocalDateTime
import javax.inject.Inject

@HiltViewModel
class PurchaseOrderViewModel @Inject constructor(
    private val purchaseOrderRepository: PurchaseOrderRepository
) : ViewModel() {

    private val _purchaseOrderDataUiState = MutableStateFlow(PurchaseOrderUiState(isLoading = true))
    val purchaseOrderDataUiState = _purchaseOrderDataUiState.asStateFlow()

    fun createPurchaseOrder(
        supplierId: String,
        expectedDate: LocalDate
    ) = viewModelScope.launch {
        purchaseOrderRepository.createPurchaseOrder(supplierId, expectedDate)
            .map { resource ->
                when (resource) {
                    is Resource.Error -> PurchaseOrderUiState(
                        isLoading = false,
                        errorMessage = resource.message.toString()
                    )

                    is Resource.Loading -> PurchaseOrderUiState(isLoading = true)
                    is Resource.Success -> PurchaseOrderUiState(
                        isLoading = false,
                        purchaseOrder = resource.data
                    )
                }
            }
            .collect { pOrderState ->
                _purchaseOrderDataUiState.value = pOrderState
            }
    }

    fun fetchPurchaseOrders() = viewModelScope.launch {
        purchaseOrderRepository.getPurchaseOrders()
            .collect { resource ->
                when (resource) {
                    is Resource.Error -> {
                        _purchaseOrderDataUiState.update {
                            it.copy(
                                isLoading = false,
                                errorMessage = resource.message.toString()
                            )
                        }
                    }

                    is Resource.Loading -> {
                        _purchaseOrderDataUiState.update {
                            it.copy(isLoading = true)
                        }
                    }

                    is Resource.Success -> {
                        _purchaseOrderDataUiState.update {
                            it.copy(
                                isLoading = false,
                                purchaseOrders = resource.data
                            )
                        }
                    }
                }
            }
    }

    fun completedPurchaseOrders(status: String) = viewModelScope.launch {
        purchaseOrderRepository.getCompletedPurchaseOrders(status)
            .collect { resource ->
                when (resource) {
                    is Resource.Error -> {
                        _purchaseOrderDataUiState.update {
                            it.copy(
                                isLoading = false,
                                errorMessage = resource.message.toString()
                            )
                        }
                    }

                    is Resource.Loading -> {
                        _purchaseOrderDataUiState.update {
                            it.copy(isLoading = true)
                        }
                    }

                    is Resource.Success -> {
                        _purchaseOrderDataUiState.update {
                            it.copy(
                                isLoading = false,
                                purchaseOrders = resource.data
                            )
                        }
                    }
                }
            }
    }

    fun updateOrderStatus(
        id: String,
        status: String
    ) = viewModelScope.launch {
        purchaseOrderRepository.updateOrderStatus(
            id = id,
            status = status
        ).collectLatest { res ->
            when (res) {
                is Resource.Error -> {
                    _purchaseOrderDataUiState.update {
                        it.copy(
                            isLoading = false,
                            errorMessage = res.message.toString()
                        )
                    }
                }

                is Resource.Loading -> {
                    _purchaseOrderDataUiState.update {
                        it.copy(
                            isLoading = true
                        )
                    }
                }

                is Resource.Success -> {
                    _purchaseOrderDataUiState.update {
                        it.copy(
                            isLoading = false,
                            successMessage = res.data.toString()
                        )
                    }
                }
            }
        }
    }
}