package com.example.titossycleaningservicesapp.domain.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.titossycleaningservicesapp.core.Resource
import com.example.titossycleaningservicesapp.domain.models.ui_models.Cart
import com.example.titossycleaningservicesapp.domain.models.ui_models.ServiceState
import com.example.titossycleaningservicesapp.domain.repository.ServiceRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ServiceViewModel @Inject constructor(
    private val serviceRepository: ServiceRepository
) : ViewModel() {

    private val _serviceState = MutableStateFlow(ServiceState(isLoading = true))
    val serviceState = _serviceState.asStateFlow()

    init {
        fetchServices()
    }

    fun searchServices(query: String) {
        viewModelScope.launch {
            val services = serviceRepository.searchServices(query)

            services.collect { serviceState ->
                when (serviceState) {
                    is Resource.Error -> {
                        _serviceState.update {
                            it.copy(error = serviceState.message.toString())
                        }
                    }

                    Resource.Loading -> {
                        _serviceState.update {
                            it.copy(isLoading = true)
                        }
                    }

                    is Resource.Success -> {
                        _serviceState.update {
                            it.copy(services = serviceState.data ?: emptyList())
                        }
                    }
                }
            }
        }
    }

    private fun fetchServices() = viewModelScope.launch {
        serviceRepository.getAllServices()
            .map { resource ->
                when (resource) {
                    is Resource.Error -> resource.message?.let {
                        ServiceState(
                            isLoading = false,
                            error = it
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
                state?.let { _serviceState.value = it }
            }
    }

    fun addToCart(cart: Cart) = viewModelScope.launch { }
}