package com.example.titossycleaningservicesapp.domain.repository

import com.example.titossycleaningservicesapp.core.Resource
import com.example.titossycleaningservicesapp.domain.models.ui_models.SupplierPayment
import kotlinx.coroutines.flow.Flow

interface SupplierPaymentRepository {
    fun createSupplierPayment(
        orderId: String,
        paymentMethod: String
    ): Flow<Resource<String>>
    fun getSupplierPayments() : Flow<Resource<List<SupplierPayment>>>
}