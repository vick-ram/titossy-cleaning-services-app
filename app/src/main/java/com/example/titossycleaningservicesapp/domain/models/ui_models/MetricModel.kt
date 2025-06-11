package com.example.titossycleaningservicesapp.domain.models.ui_models

data class MetricModel(
    val bookingMetrics: BookingMetrics,
    val paymentMetrics: PaymentMetrics,
    val userMetrics: UserMetrics,
)

data class BookingMetrics(
    val total: Int,
    val daily: Int,
    val weekly: Int,
    val monthly: Int,
    val yearly: Int
)

data class PaymentMetrics(
    val customerPayments: Double,
    val supplierPayments: Double,
    val net: Double
)

data class UserMetrics(
    val employees: Int,
    val customers: Int,
    val suppliers: Int,
    val activeCleaners: Int,
    val totalUsers: Int,
    val roles: RoleMetrics
)

data class RoleMetrics(
    val manager: Int,
    val supervisor: Int,
    val finance: Int,
    val cleaner: Int,
    val inventoryManager: Int
)


data class MetricUiState(
    val loading: Boolean = false,
    val metrics: MetricModel? = null
)
