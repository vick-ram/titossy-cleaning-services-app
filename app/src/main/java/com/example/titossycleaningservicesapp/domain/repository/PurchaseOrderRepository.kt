package com.example.titossycleaningservicesapp.domain.repository

import com.example.titossycleaningservicesapp.core.Resource
import com.example.titossycleaningservicesapp.domain.models.ui_models.PurchaseOrder
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate

interface PurchaseOrderRepository {
    fun createPurchaseOrder(
        supplierId: String,
        expectedDate: LocalDate
    ) : Flow<Resource<String>>

    fun getPurchaseOrders() : Flow<Resource<List<PurchaseOrder>>>
    fun getCompletedPurchaseOrders(status: String) : Flow<Resource<List<PurchaseOrder>>>
    fun getPurchaseOrderById(id: String) : Flow<Resource<PurchaseOrder>>
    fun updateOrderStatus(
        id: String,
        status: String
    ) : Flow<Resource<String>>
    
}