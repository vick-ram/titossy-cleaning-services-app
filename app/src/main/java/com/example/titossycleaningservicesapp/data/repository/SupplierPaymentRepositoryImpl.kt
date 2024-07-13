package com.example.titossycleaningservicesapp.data.repository

import com.example.titossycleaningservicesapp.core.FileUtils
import com.example.titossycleaningservicesapp.core.Resource
import com.example.titossycleaningservicesapp.data.remote.api.ApiService
import com.example.titossycleaningservicesapp.domain.models.requests.payment.SupplierPaymentRequest
import com.example.titossycleaningservicesapp.domain.models.ui_models.SupplierPayment
import com.example.titossycleaningservicesapp.domain.repository.SupplierPaymentRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class SupplierPaymentRepositoryImpl @Inject constructor(
    private val apiService: ApiService
) : SupplierPaymentRepository{
    override fun createSupplierPayment(
        orderId: String,
        paymentMethod: String
    ): Flow<Resource<String>> {
        return flow {
            emit(Resource.Loading)
            val response = apiService.createSupplierPayment(
                SupplierPaymentRequest(
                    orderId = orderId,
                    method = paymentMethod
                )
            )
            when(response.status) {
                "success" -> {
                    val message = response.message
                    message?.let { emit(Resource.Success(it)) }
                }
                "error" -> {
                    if (response.error != null) {
                        val errors = FileUtils.createErrorMessage(response.error)
                        throw Exception(errors)
                    }
                }
            }
        }.catch { e ->
            e.printStackTrace()
            emit(Resource.Error(e.message.toString()))
        }
    }

    override fun getSupplierPayments(): Flow<Resource<List<SupplierPayment>>> {
        return flow {
            emit(Resource.Loading)
            val response = apiService.getSupplierPayments()
            when(response.status) {
                "success" -> {
                    val suppliers = response.data?.map { it.toSupplierPayment() }
                    suppliers?.let { emit(Resource.Success(it)) }
                }
                "error" -> {
                    if (response.error != null) {
                        val errors = FileUtils.createErrorMessage(response.error)
                        throw Exception(errors)
                    }
                }
            }
        }.catch { e ->
            e.printStackTrace()
            emit(Resource.Error(e.message.toString()))
        }
    }
}