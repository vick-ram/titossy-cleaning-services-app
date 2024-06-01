package com.example.titossycleaningservicesapp.data.repository

import com.example.titossycleaningservicesapp.core.FileUtils
import com.example.titossycleaningservicesapp.core.Resource
import com.example.titossycleaningservicesapp.data.local.database.dao.SupplierDao
import com.example.titossycleaningservicesapp.data.local.datastore.DataStoreKeys
import com.example.titossycleaningservicesapp.data.remote.api.ApiService
import com.example.titossycleaningservicesapp.data.remote.dto.SupplierAddressDto
import com.example.titossycleaningservicesapp.data.remote.util.AuthEvent
import com.example.titossycleaningservicesapp.domain.models.requests.supplier.SupplierSignInRequest
import com.example.titossycleaningservicesapp.domain.models.requests.supplier.SupplierSignUpRequest
import com.example.titossycleaningservicesapp.domain.models.ui_models.Supplier
import com.example.titossycleaningservicesapp.domain.repository.SupplierRepository
import retrofit2.HttpException
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
        company: String,
        county: String,
        region: String,
        postalCode: String,
        email: String,
        password: String
    ): AuthEvent {
        return try {
            val supplier = SupplierSignUpRequest(
                username = username,
                firstName = firstName,
                lastName = lastName,
                phone = phone,
                company = company,
                address = SupplierAddressDto(
                    county = county,
                    region = region,
                    postalCode = postalCode
                ),
                email = email,
                password = password
            )

            val apiSupplier = apiService.supplierSignUp(supplier)
            when (apiSupplier.status) {
                "success" -> {
                    apiSupplier.data?.toSupplierEntity()?.let { supplierDao.insertSupplier(it) }
                    AuthEvent.Success()
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
            AuthEvent.Error(e.message.toString())
        }
    }

    override suspend fun signInSupplier(email: String, password: String): AuthEvent {
        return try {
            AuthEvent.Loading
            val response = apiService.supplierSignIn(
                SupplierSignInRequest(
                    email, password
                )
            )
            when (response.status) {
                "success" -> {
                    response.data?.let { dataStoreKeys.saveTokenToDataStore(it) }
                    AuthEvent.Success()
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

    override suspend fun signOutSupplier(): AuthEvent {
        return try {
            val token = dataStoreKeys.getTokenFromDataStore()
            val response = token?.let { apiService.supplierSignOut("Bearer $it") }
            when (response?.status) {
                "success" -> {
                    response.data?.let { dataStoreKeys.clearToken() }
                    AuthEvent.Success()
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
        company: String,
        county: String,
        region: String,
        postalCode: String,
        email: String,
        password: String
    ): AuthEvent {
        return try {
            val supplier = SupplierSignUpRequest(
                username = username,
                firstName = firstName,
                lastName = lastName,
                phone = phone,
                company = company,
                address = SupplierAddressDto(
                    county = county,
                    region = region,
                    postalCode = postalCode
                ),
                email = email,
                password = password
            )
            apiService.supplierUpdate(id.toString(), supplier)
            AuthEvent.Success()
        } catch (e: Exception) {
            when (e) {
                is HttpException -> {
                    when (e.code()) {
                        400 -> AuthEvent.Error(e.message())
                        else -> AuthEvent.Error(e.message())
                    }
                }

                else -> {
                    AuthEvent.Error(e.message ?: "An unknown error occurred")
                }
            }
        }
    }

    override suspend fun getSupplierById(id: UUID): Resource<Supplier> {
        TODO("Not yet implemented")
    }

}