package com.example.titossycleaningservicesapp.domain.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.titossycleaningservicesapp.core.Resource
import com.example.titossycleaningservicesapp.data.remote.util.ErrorEvent
import com.example.titossycleaningservicesapp.domain.models.requests.cart.CartClearState
import com.example.titossycleaningservicesapp.domain.models.requests.cart.CartUiDataState
import com.example.titossycleaningservicesapp.domain.models.requests.cart.CartUiState
import com.example.titossycleaningservicesapp.domain.models.ui_models.ServiceAddonUiState
import com.example.titossycleaningservicesapp.domain.models.ui_models.ServiceState
import com.example.titossycleaningservicesapp.domain.repository.CartRepository
import com.example.titossycleaningservicesapp.domain.repository.ServiceRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class ServiceViewModel @Inject constructor(
    private val serviceRepository: ServiceRepository,
    private val cartRepository: CartRepository
) : ViewModel() {

    private val _serviceState = MutableStateFlow(ServiceState(isLoading = true))
    val serviceState = _serviceState.asStateFlow()

    private val _serviceAddonState = MutableStateFlow(ServiceAddonUiState(isLoading = true))
    val serviceAddonState = _serviceAddonState.asStateFlow()

    private val _cartUiState = MutableStateFlow(CartUiState(loading = true))
    val cartUiState = _cartUiState.asStateFlow()

    private val _cartDataUiState = MutableStateFlow(CartUiDataState(loading = true))
    val cartDataUiState = _cartDataUiState.asStateFlow()

    private val _cartClearState = MutableStateFlow(CartClearState())
    val cartClearState = _cartClearState.asStateFlow()

    private var searchJob: Job? = null

    init {
        fetchServices()
        fetchCartItems()
    }

    fun searchServices(query: String) {
        searchJob?.cancel()
        searchJob = viewModelScope.launch {
            delay(300)
            serviceRepository.searchServices(query)
                .map { resource ->
                    when (resource) {
                        is Resource.Error -> resource.message?.let {
                            ServiceState(
                                isLoading = false,
                                error = ErrorEvent(it)
                            )
                        }

                        is Resource.Loading -> ServiceState(
                            isLoading = true
                        )

                        is Resource.Success -> resource.data?.let {
                            ServiceState(
                                services = it,
                                isLoading = false
                            )
                        }
                    }
                }
                .collectLatest { service ->
                    service?.let {
                        _serviceState.value = it
                    }
                }

        }
    }

    fun fetchServices() = viewModelScope.launch {
        serviceRepository.getAllServices()
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.Eagerly,
                initialValue = Resource.Loading
            )
            .map { resource ->
                when (resource) {
                    is Resource.Error -> resource.message?.let {
                        ServiceState(
                            isLoading = false,
                            error = ErrorEvent(it)
                        )
                    }

                    is Resource.Loading -> ServiceState(
                        isLoading = false
                    )

                    is Resource.Success -> resource.data?.let {
                        ServiceState(services = it)
                    }
                }
            }
            .collectLatest { state ->
                state?.let {
                    _serviceState.value = it
                }
            }
    }

    fun fetchServiceAddons(serviceId: String) = viewModelScope.launch {
        serviceRepository.getServiceAddons(serviceId)
            .map { resource ->
                when (resource) {
                    is Resource.Error -> resource.message?.let {
                        ServiceAddonUiState(
                            isLoading = false,
                            error = ErrorEvent(it)
                        )
                    }

                    is Resource.Loading -> ServiceAddonUiState(
                        isLoading = false
                    )

                    is Resource.Success -> resource.data?.let {
                        ServiceAddonUiState(
                            isLoading = false,
                            serviceAddons = it
                        )
                    }
                }
            }
            .collectLatest { state ->
                state?.let {
                    _serviceAddonState.value = it
                }
            }
    }

    fun addServiceToCart(
        serviceId: UUID,
        quantity: Int
    ) = viewModelScope.launch {
        cartRepository.addServiceToCart(
            serviceId = serviceId,
            quantity = quantity
        ).collect { resource ->
            when (resource) {
                is Resource.Error -> {
                    _cartUiState.update {
                        it.copy(
                            loading = false,
                            error = resource.message.toString()
                        )
                    }
                }

                is Resource.Loading -> {
                    _cartUiState.update {
                        it.copy(
                            loading = true
                        )
                    }
                }

                is Resource.Success -> {
                    _cartUiState.update {
                        it.copy(
                            loading = false,
                            message = resource.data.toString()
                        )
                    }
                    fetchCartItems()
                }
            }
        }
    }

    fun addServiceAddonToCart(
        serviceAddonId: UUID,
        quantity: Int = 1
    ) = viewModelScope.launch {
        cartRepository.addServiceAddonToCart(serviceAddonId, quantity)
            .collectLatest { resource ->
                when (resource) {
                    is Resource.Error -> {
                        _cartUiState.update {
                            it.copy(
                                loading = false,
                                error = resource.message.toString()
                            )
                        }
                    }

                    is Resource.Loading -> {
                        _cartUiState.update { it.copy(loading = true) }
                    }

                    is Resource.Success -> {
                        _cartUiState.update {
                            it.copy(
                                loading = false,
                                message = resource.data.toString()
                            )
                        }
                        fetchCartItems()
                    }
                }
            }
    }

    fun fetchCartItems() = viewModelScope.launch {
        cartRepository.getServicesInCart()
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5000L),
                initialValue = Resource.Loading
            )
            .map { resource ->
                when (resource) {
                    is Resource.Error -> CartUiDataState(
                        loading = false,
                        error = resource.message.toString()
                    )

                    is Resource.Loading -> CartUiDataState(loading = true)
                    is Resource.Success -> resource.data?.let {
                        CartUiDataState(
                            loading = false,
                            cartItems = it
                        )
                    }
                }
            }
            .collect {
                it?.let { cartData ->
                    _cartDataUiState.value = cartData
                }
            }
    }

    fun removeServiceAddonFromCart(serviceAddonId: UUID) = viewModelScope.launch {
        cartRepository.removeAddonFromCart(serviceAddonId)
            .map { resource ->
                when (resource) {
                    is Resource.Error -> CartClearState(
                        loading = false,
                        error = resource.message.toString()
                    )

                    is Resource.Loading -> CartClearState(
                        loading = true
                    )

                    is Resource.Success -> CartClearState(
                        loading = false,
                        message = resource.data.toString()
                    )
                }
            }
            .collectLatest { state ->
                _cartClearState.update { state }
                if (!state.loading && state.message.isNotEmpty()) {
                    fetchCartItems()
                }
            }
    }

    fun removeServiceFromCart(serviceId: UUID) = viewModelScope.launch {
        cartRepository.removeServiceFromCart(serviceId)
            .map { resource ->
                when (resource) {
                    is Resource.Error -> CartClearState(
                        loading = false,
                        error = resource.message.toString()
                    )

                    is Resource.Loading -> CartClearState(
                        loading = true
                    )

                    is Resource.Success -> CartClearState(
                        loading = false,
                        message = resource.data.toString()
                    )
                }
            }
            .collectLatest { state ->
                _cartClearState.update { state }
                if (!state.loading && state.message.isNotEmpty()) {
                    fetchCartItems()
                }
            }
    }

    fun clearCart() = viewModelScope.launch {
        cartRepository.clearCart()
            .map { resource ->
                when (resource) {
                    is Resource.Error -> CartClearState(
                        loading = false,
                        error = resource.message.toString()
                    )

                    is Resource.Loading -> CartClearState(loading = true)
                    is Resource.Success -> CartClearState(
                        loading = false,
                        message = resource.data.toString()
                    )
                }
            }
            .collectLatest { state ->
                _cartClearState.value = state
            }
    }

    fun clearServiceAddons() {
        _serviceAddonState.value = ServiceAddonUiState()
    }
}

