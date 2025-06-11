package com.example.titossycleaningservicesapp.domain.repository

import com.example.titossycleaningservicesapp.core.Resource
import com.example.titossycleaningservicesapp.domain.models.ui_models.MetricModel
import kotlinx.coroutines.flow.Flow

interface MetricsRepository {
    fun getDashboardMetrics(): Flow<Resource<MetricModel>>
}