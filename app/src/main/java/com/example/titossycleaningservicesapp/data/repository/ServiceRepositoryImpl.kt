package com.example.titossycleaningservicesapp.data.repository

import com.example.titossycleaningservicesapp.core.FileUtils
import com.example.titossycleaningservicesapp.core.Resource
import com.example.titossycleaningservicesapp.data.local.database.dao.ServiceDao
import com.example.titossycleaningservicesapp.data.remote.api.ApiService
import com.example.titossycleaningservicesapp.domain.models.ui_models.Service
import com.example.titossycleaningservicesapp.domain.repository.ServiceRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class ServiceRepositoryImpl @Inject constructor(
    private val serviceDao: ServiceDao,
    private val apiService: ApiService
) : ServiceRepository {
    override fun getAllServices(): Flow<Resource<List<Service>>> {
        return flow {
            emit(Resource.Loading)
            val servicesFromDb = serviceDao.getAllServices().firstOrNull()
            if (servicesFromDb.isNullOrEmpty()) {
                val response = apiService.getServices()
                when (response.status) {
                    "success" -> {
                        val apiServices = response.data
                        val entityServices = apiServices?.mapNotNull { it.toServiceWithAddOns().service }
                        entityServices?.let { serviceDao.insertAllServices(it) }

                        apiServices?.map { it.toServiceWithAddOns() }?.forEach { serviceWithAddOns ->
                            serviceWithAddOns.addOns?.let { serviceDao.insertAllAddOns(it) }
                        }
                    }
                    "error" -> {
                        if (response.message != null) {
                            throw Exception(response.message)
                        } else if (response.error != null) {
                            val errorMessage = FileUtils.createErrorMessage(response.error)
                            throw Exception(errorMessage)
                        }
                    }
                }
            }
            val services = servicesFromDb?.mapNotNull { it.toService() }

            services?.let { emit(Resource.Success(it)) }
        }.catch { e ->
            emit(Resource.Error(e.message.toString()))
        }
    }

    override fun searchServices(query: String): Flow<Resource<List<Service>>> {
        return flow {
            emit(Resource.Loading)
            val servicesFromDb = serviceDao.searchServices("%$query%").firstOrNull()
            val services = servicesFromDb?.mapNotNull { it.toService() }
            services?.let { emit(Resource.Success(it)) }
        }.catch { e ->
            emit(Resource.Error(e.message.toString()))
        }
    }
}
