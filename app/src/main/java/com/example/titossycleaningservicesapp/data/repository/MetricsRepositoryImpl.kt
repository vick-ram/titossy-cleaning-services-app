package com.example.titossycleaningservicesapp.data.repository

import com.example.titossycleaningservicesapp.core.Resource
import com.example.titossycleaningservicesapp.data.remote.api.ApiService
import com.example.titossycleaningservicesapp.domain.models.ui_models.MetricModel
import com.example.titossycleaningservicesapp.domain.repository.MetricsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class MetricsRepositoryImpl @Inject constructor(
    private val apiService: ApiService
): MetricsRepository {
    override fun getDashboardMetrics(): Flow<Resource<MetricModel>> {
        return flow {
            emit(Resource.Loading)
            try {
                val apiResponse = apiService.getMetrics()
                if (apiResponse.status == "success") {
                    val apiMetrics = apiResponse.data
                    val metrics = apiMetrics?.toMetrics()
                    metrics?.let { emit(Resource.Success(it)) }
                } else if (apiResponse.status == "error") {
                    throw Exception(apiResponse.error.toString())
                } else {
                    throw Exception("Something went wrong")
                }
            } catch (e: Exception) {
                emit(Resource.Error(e.message.toString()))
            }
        }
    }
}