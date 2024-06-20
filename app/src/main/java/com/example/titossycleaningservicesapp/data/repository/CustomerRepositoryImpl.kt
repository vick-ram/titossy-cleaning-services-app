package com.example.titossycleaningservicesapp.data.repository

import com.example.titossycleaningservicesapp.core.FileUtils
import com.example.titossycleaningservicesapp.core.Resource
import com.example.titossycleaningservicesapp.data.local.database.dao.CustomerDao
import com.example.titossycleaningservicesapp.data.local.datastore.DataStoreKeys
import com.example.titossycleaningservicesapp.data.remote.api.ApiService
import com.example.titossycleaningservicesapp.data.remote.util.AuthEvent
import com.example.titossycleaningservicesapp.domain.models.requests.customer.CustomerSignInRequest
import com.example.titossycleaningservicesapp.domain.models.requests.customer.CustomerSignUpRequest
import com.example.titossycleaningservicesapp.domain.models.ui_models.Customer
import com.example.titossycleaningservicesapp.domain.repository.CustomerRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flow
import java.util.UUID
import javax.inject.Inject

class CustomerRepositoryImpl @Inject constructor(
    private val apiService: ApiService,
    private val customerDao: CustomerDao,
    private val dataStore: DataStoreKeys,
) : CustomerRepository {
    override suspend fun createCustomer(
        username: String,
        firstName: String,
        lastName: String,
        phone: String,
        email: String,
        password: String
    ): AuthEvent {
        return try {
            AuthEvent.Loading
            val customer = CustomerSignUpRequest(
                username = username,
                firstName = firstName,
                lastName = lastName,
                phone = phone,
                email = email,
                password = password
            )

            val response = apiService.customerSignUp(customer)
            when (response.status) {
                "success" -> {
                    val entityCustomer = response.data?.toCustomerEntity()
                    entityCustomer?.let { customerDao.insertCustomer(it) }
                    AuthEvent.Success(response.message)
                }

                "error" -> {
                    if (response.error != null) {
                        val errorMessage = FileUtils.createErrorMessage(response.error)
                        throw Exception(errorMessage)
                    } else {
                        throw Exception("Something went wrong")
                    }
                }
                else -> {
                    throw Exception("Unknown error")
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
            AuthEvent.Error(e.message.toString())
        }
    }


    override suspend fun signInCustomer(
        username: String?,
        email: String?,
        password: String
    ): AuthEvent {
        return try {
            val customer = CustomerSignInRequest(
                username, email, password
            )
            val response = apiService.customerSignIn(customer)
            when (response.status) {
                "success" -> {
                    response.data?.let { dataStore.saveTokenToDataStore(it) }
                    val customerEntity = when {
                        email != null -> apiService.getCustomerByEmail(email).data?.toCustomerEntity()
                        username != null -> apiService.getCustomerByUsername(username).data?.toCustomerEntity()
                        else -> null
                    }

                    customerEntity?.let {
                        customerDao.insertCustomer(it)
                        dataStore.saveApprovalStatusToDataStore(it.status.name)
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

    override suspend fun signOutCustomer(): AuthEvent {
        return try {
            val token = dataStore.getTokenFromDataStore()
            val response = token?.let { apiService.customerSignOut("Bearer $it") }
            when (response?.status) {
                "success" -> {
                    response.data?.let { dataStore.clearToken() }
                }

                "error" -> {
                    if (response.error != null) {
                        dataStore.clearToken()
                        val errorMessage = FileUtils.createErrorMessage(response.error)
                        throw Exception(errorMessage)
                    }
                }
            }
            dataStore.clearToken()
            AuthEvent.Success(response?.message)
        } catch (e: Exception) {
            AuthEvent.Error(e.message.toString())
        }
    }

    override suspend fun updateCustomer(
        customerId: UUID,
        username: String,
        firstName: String,
        lastName: String,
        phone: String,
        email: String,
        password: String
    ): AuthEvent {
        val customer = CustomerSignUpRequest(
            username = username,
            firstName = firstName,
            lastName = lastName,
            phone = phone,
            email = email,
            password = password
        )
        return try {
            val response = apiService.updateCustomer(customerId.toString(), customer)
            when (response.status) {
                "success" -> {
                    response.data?.toCustomerEntity()?.let {
                        customerDao.updateCustomer(
                            customerId,
                            username,
                            "$firstName $lastName",
                            phone,
                            email,
                            password
                        )
                    }
                }

                "error" -> {
                    if (response.error != null) {
                        val errorMessage = FileUtils.createErrorMessage(response.error)
                        throw Exception(errorMessage)
                    }
                }
            }
            AuthEvent.Success(response.message)
        } catch (e: Exception) {
            AuthEvent.Error(e.message.toString())
        }
    }
    override fun getCustomers(): Flow<Resource<List<Customer>>> {
        return flow {
            emit(Resource.Loading)
            val customersFromDb = customerDao.getCustomers().firstOrNull()
            if (customersFromDb.isNullOrEmpty()) {

                val response = apiService.getCustomers()
                when (response.status) {
                    "success" -> {
                        val customers = response.data?.map { it.toCustomerEntity() }
                        customers?.let { customerDao.insertCustomers(it) }
                    }

                    "error" -> {
                        if (response.error != null) {
                            val errorMessage = FileUtils.createErrorMessage(response.error)
                            throw Exception(errorMessage)
                        }
                    }
                }
            }
            val customers = customersFromDb.let { entityCustomers ->
                entityCustomers?.map { it.toCustomer() }
            }
            customers?.let { emit(Resource.Success(it)) }
        }.catch { e ->
            emit(Resource.Error(e.message.toString()))
        }
    }

    override fun getCustomerById(id: UUID): Flow<Resource<Customer>> {
        return flow {
            emit(Resource.Loading)
            val customerFromDb = customerDao.getCustomerById(id)

            if (customerFromDb == null) {
                val response = apiService.getCustomerById(id.toString())
                when (response.status) {
                    "success" -> {
                        response.data?.toCustomerEntity()?.let {
                            customerDao.insertCustomer(it)
                        }
                    }

                    "error" -> {
                        if (response.error != null) {
                            val errorMessage = FileUtils.createErrorMessage(response.error)
                            throw Exception(errorMessage)
                        }
                    }
                }
            }
            customerFromDb?.toCustomer()?.let { emit(Resource.Success(it)) }
        }.catch { e ->
            emit(Resource.Error(e.message.toString()))
        }
    }

    override fun getCustomerByUsername(username: String): Flow<Resource<Customer>> {
        return flow {
            emit(Resource.Loading)
            val customerFromDb = customerDao.getCustomerByUsername(username)

            if (customerFromDb == null) {
                val response = apiService.getCustomerByUsername(username)
                when (response.status) {
                    "success" -> {
                        response.data?.toCustomerEntity()?.let {
                            customerDao.insertCustomer(it)
                        }
                    }

                    "error" -> {
                        if (response.error != null) {
                            val errorMessage = FileUtils.createErrorMessage(response.error)
                            throw Exception(errorMessage)
                        }
                    }
                }
            }
            customerFromDb?.toCustomer()?.let { emit(Resource.Success(it)) }
        }.catch { e ->
            emit(Resource.Error(e.message.toString()))
        }
    }

    override fun getCustomersByEmail(email: String): Flow<Resource<Customer>> {
        return flow<Resource<Customer>> {
            emit(Resource.Loading)
            val customerFromDb = customerDao.getCustomerByEmail(email)
            val customer = customerFromDb?.toCustomer()
            customer?.let { Resource.Success(it) }

            if (customerFromDb == null) {

                val response = apiService.getCustomerByEmail(email)
                when (response.status) {
                    "success" -> {
                        response.data?.toCustomerEntity()?.let {
                            customerDao.insertCustomer(it)
                        }
                    }

                    "error" -> {
                        if (response.error != null) {
                            val errorMessage = FileUtils.createErrorMessage(response.error)
                            throw Exception(errorMessage)
                        }
                    }
                }
            }
        }.catch { e ->
            emit(Resource.Error(e.message.toString()))
        }
    }
}