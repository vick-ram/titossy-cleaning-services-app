package com.example.titossycleaningservicesapp.domain.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.titossycleaningservicesapp.core.Resource
import com.example.titossycleaningservicesapp.domain.models.ui_models.MetricUiState
import com.example.titossycleaningservicesapp.domain.repository.MetricsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MetricViewModel @Inject constructor(
    private val metricsRepository: MetricsRepository
) : ViewModel() {
    private val _metricUiState = MutableStateFlow(MetricUiState(loading = true))
    val metricUiState = _metricUiState.asStateFlow()

    private val _errorChannel = Channel<String>(Channel.BUFFERED)
    val errorFlow = _errorChannel.receiveAsFlow()

    fun fetchMetrics() = viewModelScope.launch {
        val res = metricsRepository.getDashboardMetrics()
        res.collect { resource ->
            when (resource) {
                is Resource.Loading -> {
                    _metricUiState.update { state ->
                        state.copy(loading = true)
                    }
                }

                is Resource.Success -> {
                    _metricUiState.update { state ->
                        state.copy(
                            loading = false,
                            metrics = resource.data
                        )
                    }
                }

                is Resource.Error -> {
                    _errorChannel.send(resource.message ?: "Unknown error")
                }
            }
        }
    }
}

