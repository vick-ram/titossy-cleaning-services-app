package com.example.titossycleaningservicesapp.domain.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.titossycleaningservicesapp.core.Resource
import com.example.titossycleaningservicesapp.domain.models.requests.po.ProductCartUiState
import com.example.titossycleaningservicesapp.domain.models.requests.po.ProductDataUiState
import com.example.titossycleaningservicesapp.domain.models.requests.po.ProductUiState
import com.example.titossycleaningservicesapp.domain.repository.ProductRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProductViewModel @Inject constructor(
    private val productRepository: ProductRepository
) : ViewModel() {
    private val _productUiState = MutableStateFlow(ProductUiState(isLoading = true))
    val productUiState = _productUiState.asStateFlow()

    private val _productDataUiState = MutableStateFlow(ProductDataUiState(isLoading = true))
    val productDataUiState = _productDataUiState.asStateFlow()

    private val _productCartUiState = MutableStateFlow(ProductCartUiState(isLoading = true))
    val productCartUiState = _productCartUiState.asStateFlow()


    fun fetchProducts() = viewModelScope.launch {
        productRepository.getAllProducts()
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5000L),
                initialValue = Resource.Loading
            )
            .collect {resource ->
                when(resource) {
                    is Resource.Error -> {
                        _productDataUiState.update {
                            it.copy(
                                isLoading = false,
                                errorMessage = resource.message.toString()
                            )
                        }
                    }
                    Resource.Loading -> {
                        _productDataUiState.update {
                            it.copy(
                                isLoading = true
                            )
                        }
                    }
                    is Resource.Success -> {
                        _productDataUiState.update {
                            it.copy(
                                isLoading = false,
                                products = resource.data
                            )
                        }
                    }
                }
            }
    }

    fun fetchProductCart() = viewModelScope.launch {
        productRepository.getProductCart()
            .collect {resource ->
                when(resource) {
                    is Resource.Error -> {
                        _productCartUiState.update {
                            it.copy(
                                isLoading = false,
                                errorMessage = resource.message.toString()
                            )
                        }
                    }
                    Resource.Loading -> {
                        _productCartUiState.update {
                            it.copy(
                                isLoading = true
                            )
                        }
                    }
                    is Resource.Success -> {
                        _productCartUiState.update {
                            it.copy(
                                isLoading = false,
                                products = resource.data
                            )
                        }
                    }
                }
            }
    }



    fun addProductToPurchaseOrder(
        productId: String,
        quantity: Int = 1
    ) = viewModelScope.launch {
        productRepository.addProductToPurchase(productId, quantity)
            .collectLatest { resource ->
                when(resource) {
                    is Resource.Error -> {
                        _productUiState.value = ProductUiState(
                            isLoading = false,
                            errorMessage = resource.message.toString()
                        )
                    }
                    is Resource.Loading -> {
                        _productUiState.value = ProductUiState(
                            isLoading = true
                        )
                    }
                    is Resource.Success -> {
                        _productUiState.value = ProductUiState(
                            isLoading = false,
                            successMessage = resource.data.toString()
                        )
                    }
                }
            }
    }

    fun deleteProduct(productId: String) = viewModelScope.launch {
        productRepository.deleteProductFromCart(productId)
            .collectLatest { resource ->
                when(resource) {
                    is Resource.Error -> {
                        _productUiState.update {
                            it.copy(
                                isLoading = false,
                                errorMessage = resource.message.toString()
                            )
                        }
                    }
                    Resource.Loading -> {
                        _productUiState.update {
                            it.copy(
                                isLoading = true
                            )
                        }
                    }
                    is Resource.Success -> {
                        _productUiState.update {
                            it.copy(
                                isLoading = false,
                                successMessage = resource.data.toString()
                            )
                        }
                    }
                }
            }
    }
}