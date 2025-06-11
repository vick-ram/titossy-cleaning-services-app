package com.example.titossycleaningservicesapp.data.repository

import com.example.titossycleaningservicesapp.core.FileUtils
import com.example.titossycleaningservicesapp.core.Resource
import com.example.titossycleaningservicesapp.data.local.database.dao.ServiceDao
import com.example.titossycleaningservicesapp.data.remote.api.ApiService
import com.example.titossycleaningservicesapp.domain.models.ui_models.Service
import com.example.titossycleaningservicesapp.domain.models.ui_models.ServiceAddOn
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
            println("Services from DB: $servicesFromDb")
            if (servicesFromDb.isNullOrEmpty()) {
                val response = apiService.getServices()

                println("Services from the API: ${response.data}")
                when (response.status) {
                    "success" -> {
                        val serviceEntity = response.data?.map { it.toServiceEntity() }
                        serviceEntity?.let { serviceDao.insertAllServices(it) }
                    }

                    "error" -> {
                        if (response.error != null) {
                            val errors = FileUtils.createErrorMessage(response.error)
                            throw Exception(errors)
                        }
                    }
                }
            } else {
                val service = servicesFromDb.map { it.toServiceModel() }
                emit(Resource.Success(service))
            }
        }.catch { e ->
            emit(Resource.Error(e.message.toString()))
        }
    }

    override fun getServiceAddons(serviceId: String): Flow<Resource<List<ServiceAddOn>>> {
        return flow {
            emit(Resource.Loading)
            val serviceEntity = serviceDao.getServiceAddons(serviceId).firstOrNull()
            val response = apiService.getServiceAddons(serviceId)
            if (serviceEntity.isNullOrEmpty()) {
                when (response.status) {
                    "success" -> {
                        val apiServiceAddon = response.data
                        val addonEntity =
                            apiServiceAddon?.let { d -> d.map { it.toServiceAddonEntity() } }
                        addonEntity?.let { serviceDao.insertAllAddOns(it) }
                    }

                    "error" -> {
                        if (response.error != null) {
                            val errors = FileUtils.createErrorMessage(response.error)
                            throw Exception(errors)
                        }
                    }
                }
            } else {
                val service = serviceEntity.let { add -> add.map { it.toServiceAddonModel() } }
                emit(Resource.Success(service))
            }
        }.catch { e ->
            e.printStackTrace()
            emit(Resource.Error(e.message.toString()))
        }
    }

    override fun searchServices(query: String): Flow<Resource<List<Service>>> {
        return flow {
            emit(Resource.Loading)
            val servicesFromDb = serviceDao.searchServices("%$query%").firstOrNull()
            val services = servicesFromDb?.map { it.toServiceModel() }
            services?.let { emit(Resource.Success(it)) }
        }.catch { e ->
            emit(Resource.Error(e.message.toString()))
        }
    }
}
