package com.example.titossycleaningservicesapp.domain.models.ui_models

import java.math.BigDecimal
import java.text.DecimalFormat
import java.util.UUID

data class Service(
    val id: UUID,
    val name: String,
    val description: String,
    val price: BigDecimal,
    val image: String,
    val addOns: List<ServiceAddOn>? = null,
) {
    val formattedPrice: String
        get() = DecimalFormat("#,###.00").format(price)
}

data class ServiceAddOn(
    val id: UUID,
    val serviceId: UUID,
    val name: String,
    val description: String,
    val price: BigDecimal,
    val image: String,
) {
    val formattedPrice: String
        get() = DecimalFormat("#,###.00").format(price)
}

data class ServiceState(
    val services: List<Service> = emptyList(),
    val isLoading: Boolean = false,
    val error: String = ""
)