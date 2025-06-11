package com.example.titossycleaningservicesapp.data.remote.dto

import com.example.titossycleaningservicesapp.domain.models.ui_models.BookingMetrics
import com.example.titossycleaningservicesapp.domain.models.ui_models.MetricModel
import com.example.titossycleaningservicesapp.domain.models.ui_models.PaymentMetrics
import com.example.titossycleaningservicesapp.domain.models.ui_models.RoleMetrics
import com.example.titossycleaningservicesapp.domain.models.ui_models.UserMetrics

data class MetricsDto(
    val bookings: BookingMetricsDto,
    val payments: PaymentMetricsDto,
    val users: UserMetricsDto,
) {
    fun toMetrics(): MetricModel {
        return MetricModel(
            bookingMetrics = bookings.toBookingsMetrics(),
            paymentMetrics = payments.toPaymentMetrics(),
            userMetrics = users.toUserMetrics()
        )
    }
}

data class BookingMetricsDto(
    val total: Int,
    val daily: Int,
    val weekly: Int,
    val monthly: Int,
    val yearly: Int
) {
    fun toBookingsMetrics(): BookingMetrics {
        return BookingMetrics(
            total = total,
            daily = daily,
            weekly = weekly,
            monthly = monthly,
            yearly = yearly
        )
    }
}

data class PaymentMetricsDto(
    val customerPayments: Double,
    val supplierPayments: Double,
    val net: Double
) {
    fun toPaymentMetrics(): PaymentMetrics {
        return PaymentMetrics(
            customerPayments = customerPayments,
            supplierPayments = supplierPayments,
            net = net
        )
    }
}

data class UserMetricsDto(
    val employees: Int,
    val customers: Int,
    val suppliers: Int,
    val activeCleaners: Int,
    val totalUsers: Int,
    val roles: RoleMetricsDto
) {
    fun toUserMetrics(): UserMetrics {
        return UserMetrics(
            employees = employees,
            customers = customers,
            suppliers = suppliers,
            activeCleaners = activeCleaners,
            totalUsers = totalUsers,
            roles = roles.toRoleMetrics()
        )
    }
}

data class RoleMetricsDto(
    val manager: Int,
    val supervisor: Int,
    val finance: Int,
    val cleaner: Int,
    val inventoryManager: Int
) {
    fun toRoleMetrics(): RoleMetrics {
        return RoleMetrics(
            manager = manager,
            supervisor = supervisor,
            finance = finance,
            cleaner = cleaner,
            inventoryManager = inventoryManager
        )
    }
}
