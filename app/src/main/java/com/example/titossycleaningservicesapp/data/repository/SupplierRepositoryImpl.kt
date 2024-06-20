package com.example.titossycleaningservicesapp.data.repository

import android.util.Log
import com.example.titossycleaningservicesapp.core.FileUtils
import com.example.titossycleaningservicesapp.core.Resource
import com.example.titossycleaningservicesapp.data.local.database.dao.SupplierDao
import com.example.titossycleaningservicesapp.data.local.datastore.DataStoreKeys
import com.example.titossycleaningservicesapp.data.remote.api.ApiService
import com.example.titossycleaningservicesapp.data.remote.util.AuthEvent
import com.example.titossycleaningservicesapp.domain.models.ApprovalStatus
import com.example.titossycleaningservicesapp.domain.models.requests.customer.CustomerSignInRequest
import com.example.titossycleaningservicesapp.domain.models.requests.supplier.SupplierSignInRequest
import com.example.titossycleaningservicesapp.domain.models.requests.supplier.SupplierSignUpRequest
import com.example.titossycleaningservicesapp.domain.models.ui_models.Supplier
import com.example.titossycleaningservicesapp.domain.repository.SupplierRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.singleOrNull
import java.util.UUID
import javax.inject.Inject

class SupplierRepositoryImpl @Inject constructor(
    private val dataStoreKeys: DataStoreKeys,
    private val supplierDao: SupplierDao,
    private val apiService: ApiService
) : SupplierRepository {
    override suspend fun createSupplier(
        username: String,
        firstName: String,
        lastName: String,
        phone: String,
        address: String,
        email: String,
        password: String
    ): AuthEvent {
        return try {
            val supplier = SupplierSignUpRequest(
                username = username,
                firstName = firstName,
                lastName = lastName,
                phone = phone,
                address = address,
                email = email,
                password = password
            )

            val apiSupplier = apiService.supplierSignUp(supplier)
            when (apiSupplier.status) {
                "success" -> {
                    val entitySupplier = apiSupplier.data?.toSupplierEntity()
                    entitySupplier?.let { supplierDao.insertSupplier(it) }
                    AuthEvent.Success(message = apiSupplier.message)
                }

                "error" -> {
                    if (apiSupplier.error != null) {
                        val errorMessage = FileUtils.createErrorMessage(apiSupplier.error)
                        throw Exception(errorMessage)
                    } else {
                        throw Exception("Could not process your request")
                    }
                }

                else -> throw Exception("Something went wrong")
            }

        } catch (e: Exception) {
            e.printStackTrace()
            AuthEvent.Error(e.message.toString())
        }
    }

    override fun getSupplierByEmail(email: String): Flow<Resource<Supplier>> {
        return flow {
            emit(Resource.Loading)
            val supplierEntity = supplierDao.getSupplierByEmail(email).firstOrNull()

            if (supplierEntity != null) {
                val response = apiService.getSupplierByEmail(email)
                when(response.status) {
                    "success" -> {
                        val entitySupplier = response.data?.toSupplierEntity()
                        entitySupplier?.let { supplierDao.insertSupplier(it) }
                    }
                    "error" -> {
                        if (response.error != null) {
                            val errors = FileUtils.createErrorMessage(response.error)
                            throw Exception(errors)
                        } else {
                            throw Exception("something occurred")
                        }
                    }
                }
            }
            val supplier = supplierEntity?.toSupplier()
            supplier?.let { emit(Resource.Success(it)) }
        }.catch { e ->
            e.printStackTrace()
            emit(Resource.Error(e.message.toString()))
        }
    }

    override suspend fun signInSupplier(email: String, password: String): AuthEvent {
        return try {
            val supplier = SupplierSignInRequest(
                email, password
            )
            val response = apiService.supplierSignIn(supplier)
            when (response.status) {
                "success" -> {
                    response.data?.let { dataStoreKeys.saveTokenToDataStore(it) }
                    val supplierEntity = apiService.getSupplierByEmail(email).data?.toSupplierEntity()

                    supplierEntity?.let {
                        supplierDao.insertSupplier(it)
                        dataStoreKeys.saveApprovalStatusToDataStore(it.status.name)
                        AuthEvent.Success(response.message, it.status)
                    } ?: throw Exception("cannot complete process")
                }

                "error" -> {
                    if (response.error != null) {
                        val errorMessage = FileUtils.createErrorMessage(response.error)
                        throw Exception(errorMessage)
                    } else {
                        throw Exception("something went wrong")
                    }
                }
                else -> throw Exception("Internal error")
            }
        } catch (e: Exception) {
            AuthEvent.Error(e.message.toString())
        }
    }

    override suspend fun signOutSupplier(): AuthEvent {
        return try {
            val token = dataStoreKeys.getTokenFromDataStore()
            val response = token?.let { apiService.supplierSignOut("Bearer $it") }
            when (response?.status) {
                "success" -> {
                    response.data?.let { dataStoreKeys.clearToken() }
                    AuthEvent.Success(
                        message = response.message
                    )
                }

                "error" -> {
                    if (response.error != null) {
                        val error = FileUtils.createErrorMessage(response.error)
                        throw Exception(error)
                    } else {
                        throw Exception("Could not process your request")
                    }
                }

                else -> {
                    throw Exception("Something went wrong")
                }
            }
        } catch (e: Exception) {
            AuthEvent.Error(e.message.toString())
        }
    }

    override suspend fun updateSupplier(
        id: UUID,
        username: String,
        firstName: String,
        lastName: String,
        phone: String,
        address: String,
        email: String,
        password: String
    ): AuthEvent {
        return try {
            val supplier = SupplierSignUpRequest(
                username = username,
                firstName = firstName,
                lastName = lastName,
                phone = phone,
                address = address,
                email = email,
                password = password
            )
            val response = apiService.supplierUpdate(id.toString(), supplier)
            when(response.status) {
                "success" -> {
                    AuthEvent.Success(
                        message = response.message
                    )
                }
                "error" -> {
                    if (response.error != null) {
                        val errors = FileUtils.createErrorMessage(response.error)
                        throw Exception(errors)
                    } else {
                        throw Exception("Something went wrong")
                    }
                }
                else -> {
                    throw Exception("Error")
                }
            }
        } catch (e: Exception) {
            AuthEvent.Error(e.message.toString())
        }
    }

    override fun getAllSuppliers(): Flow<Resource<List<Supplier>>> {
        return flow {
            emit(Resource.Loading)
            val dbSuppliers = supplierDao.getAllSuppliers().firstOrNull()

            if (dbSuppliers.isNullOrEmpty()) {
                val response = apiService.getSuppliers()
                when(response.status) {
                    "success" -> {
                        val entitySuppliers = response.data?.map { it.toSupplierEntity() }
                        entitySuppliers?.let { supplierDao.insertAllSuppliers(it) }
                    }
                    "error" -> {
                        if (response.error != null) {
                            val error = FileUtils.createErrorMessage(response.error)
                            throw Exception(error)
                        }
                    }
                }
            } else {
                val suppliers = dbSuppliers.map { it.toSupplier() }
                emit(Resource.Success(suppliers))
            }
        }.catch { exception ->
            exception.printStackTrace()
            emit(Resource.Error(exception.message.toString()))
        }
    }

    override suspend fun getSupplierById(id: UUID): Flow<Resource<Supplier>> {
        return flow {
            emit(Resource.Loading)
            val dbSupplier = supplierDao.getSupplierById(id.toString()).singleOrNull()

            if (dbSupplier == null) {
                val response = apiService.getSupplierById(id.toString())
                when(response.status) {
                    "success" -> {
                        val entitySupplier = response.data?.toSupplierEntity()
                        entitySupplier?.let { supplierDao.insertSupplier(it) }
                    }
                    "error" -> {
                        if (response.error != null) {
                            val error = FileUtils.createErrorMessage(response.error)
                            throw Exception(error)
                        }
                    }
                }
            } else {
                val supplier = dbSupplier.toSupplier()
                emit(Resource.Success(supplier))
            }
        }.catch { e ->
            e.printStackTrace()
            emit(Resource.Error(e.message.toString()))
        }
    }

}