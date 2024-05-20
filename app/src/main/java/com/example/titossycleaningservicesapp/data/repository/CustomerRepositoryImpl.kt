package com.example.titossycleaningservicesapp.data.repository

import android.content.Context
import android.net.Uri
import com.example.titossycleaningservicesapp.core.FileUtils
import com.example.titossycleaningservicesapp.core.Resource
import com.example.titossycleaningservicesapp.data.local.database.dao.AddressDao
import com.example.titossycleaningservicesapp.data.local.database.dao.CustomerDao
import com.example.titossycleaningservicesapp.data.local.datastore.DataStoreKeys
import com.example.titossycleaningservicesapp.data.remote.api.ApiService
import com.example.titossycleaningservicesapp.data.remote.util.AuthEvent
import com.example.titossycleaningservicesapp.domain.models.requests.customer.CustomerSignInRequest
import com.example.titossycleaningservicesapp.domain.models.requests.customer.CustomerSignUpRequest
import com.example.titossycleaningservicesapp.domain.models.ui_models.Customer
import com.example.titossycleaningservicesapp.domain.repository.CustomerRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.util.UUID
import javax.inject.Inject

class CustomerRepositoryImpl @Inject constructor(
    private val apiService: ApiService,
    private val customerDao: CustomerDao,
    private val dataStore: DataStoreKeys,
    private val addressDao: AddressDao
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
                    response.data?.toCustomerEntity()?.customer?.let {
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
            AuthEvent.Success(response.message)
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
                }

                "error" -> {
                    if (response.error != null) {
                        val errorMessage = FileUtils.createErrorMessage(response.error)
                        throw Exception(errorMessage)
                    }
                }
            }
            val customerFromApi = apiService.customerSignIn(customer).data
            customerFromApi?.let { dataStore.saveTokenToDataStore(it) }
            AuthEvent.Success(response.message)
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
                    response.data?.toCustomerEntity()?.customer?.let {
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

    override suspend fun updateCustomerProfilePicture(
        customerId: UUID,
        profilePicture: Uri,
        context: Context
    ): AuthEvent = withContext(Dispatchers.IO) {
        return@withContext try {
            val file = FileUtils.getFileFromUri(context, profilePicture)
            val requestFile = file!!.asRequestBody("multipart/form-data".toMediaTypeOrNull())
            val filePart = MultipartBody.Part.createFormData("file", file.name, requestFile)
            val response = apiService.updateCustomerProfilePicture(customerId.toString(), filePart)
            when (response.status) {
                "success" -> {
                    response.data?.toCustomerEntity()?.customer?.let {
                        it.profilePic?.let { it1 ->
                            customerDao.updateCustomerProfilePicture(
                                customerId,
                                it1
                            )
                        }
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
                println("All customers from api: ${response.data}")
                when (response.status) {
                    "success" -> {
                        val customers = response.data?.mapNotNull { it.toCustomerEntity().customer }
                        customers?.let { customerDao.insertCustomers(it) }

                        response.data?.forEach { customer ->
                            val addresses = customer.address.map { it.toAddressEntity() }
                            addressDao.insertAddresses(addresses)
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
            val customers = customersFromDb.let { entityCustomers ->
                entityCustomers?.mapNotNull { it.toCustomerWithAddress() }
            }
            customers?.let { emit(Resource.Success(it)) }
        }.catch { e ->
            emit(Resource.Error(e.message.toString()))
        }
    }

    override suspend fun getCustomerById(id: UUID): Flow<Resource<Customer>> {
        return flow {
            emit(Resource.Loading)
            val customerFromDb = customerDao.getCustomerById(id)

            if (customerFromDb == null) {
                val response = apiService.getCustomerById(id.toString())
                when (response.status) {
                    "success" -> {
                        response.data?.toCustomerEntity()?.customer?.let {
                            customerDao.insertCustomer(it)
                        }
                        response.data?.address?.let {
                            val addresses = it.map { address -> address.toAddressEntity() }
                            addressDao.insertAddresses(addresses)
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
            customerFromDb?.toCustomerWithAddress()?.let { emit(Resource.Success(it)) }
        }.catch { e ->
            emit(Resource.Error(e.message.toString()))
        }
    }

    override suspend fun getCustomersByEmail(email: String): Flow<Resource<Customer>> {
        return flow<Resource<Customer>> {
            emit(Resource.Loading)
            val customerFromDb = customerDao.getCustomerByEmail(email)
            val customer = customerFromDb?.toCustomerWithAddress()
            customer?.let { Resource.Success(it) }

            if (customerFromDb == null) {

                val response = apiService.getCustomerByEmail(email)
                when (response.status) {
                    "success" -> {
                        response.data?.toCustomerEntity()?.customer?.let {
                            customerDao.insertCustomer(it)
                        }

                        response.data?.address?.let {
                            val addresses = it.map { address -> address.toAddressEntity() }
                            addressDao.insertAddresses(addresses)
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