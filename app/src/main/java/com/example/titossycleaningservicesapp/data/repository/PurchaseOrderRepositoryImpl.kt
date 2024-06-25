package com.example.titossycleaningservicesapp.data.repository

import com.example.titossycleaningservicesapp.core.FileUtils
import com.example.titossycleaningservicesapp.core.Resource
import com.example.titossycleaningservicesapp.data.remote.api.ApiService
import com.example.titossycleaningservicesapp.data.remote.dto.UpdateOrderStatus
import com.example.titossycleaningservicesapp.domain.models.OrderStatus
import com.example.titossycleaningservicesapp.domain.models.requests.po.PurchaseOrderRequest
import com.example.titossycleaningservicesapp.domain.models.ui_models.PurchaseOrder
import com.example.titossycleaningservicesapp.domain.repository.PurchaseOrderRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import java.time.LocalDate
import javax.inject.Inject

class PurchaseOrderRepositoryImpl @Inject constructor(
    private val apiService: ApiService
): PurchaseOrderRepository {
    override fun createPurchaseOrder(
        supplierId: String,
        expectedDate: LocalDate
    ): Flow<Resource<PurchaseOrder>> {
        return flow {
            val purchaseOrder = PurchaseOrderRequest(
                supplierId = supplierId,
                expectedDate = expectedDate
            )
            emit(Resource.Loading)
            val response = apiService.createPurchaseOrder(purchaseOrder)

            when(response.status) {
                "success" -> {
                    val pOrder = response.data?.toPurchaseOrder()
                    pOrder?.let { emit(Resource.Success(it)) }
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

    override fun getPurchaseOrders(): Flow<Resource<List<PurchaseOrder>>> {
        return flow {
            emit(Resource.Loading)
            val response = apiService.getPurchaseOrders()
            when(response.status) {
                "success" -> {
                    val orders = response.data?.map { it.toPurchaseOrder() }
                    orders?.let { emit(Resource.Success(it)) }
                }
                "error" -> {
                    if (response.error !=null) {
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

    override fun updateOrderStatus(id: String, status: String): Flow<Resource<String>> {
        return flow {
            emit(Resource.Loading)
            val response = apiService.updateOrderStatus(
                id = id,
                orderStatus = UpdateOrderStatus(OrderStatus.valueOf(status))
            )
            when(response.status) {
                "success" -> {
                    response.data?.let { emit(Resource.Success(it)) }
                }
                "error" -> {
                    if (response.error !=null) {
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

    override fun getCompletedPurchaseOrders(status: String): Flow<Resource<List<PurchaseOrder>>> {
        return flow {
            emit(Resource.Loading)
            val response = apiService.getCompletedPurchaseOrders(status)
            when(response.status) {
                "success" -> {
                    val orders = response.data?.map { it.toPurchaseOrder() }
                    orders?.let { emit(Resource.Success(it)) }
                }
                "error" -> {
                    if (response.error !=null) {
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