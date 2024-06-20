package com.example.titossycleaningservicesapp.domain.models.ui_models

import com.example.titossycleaningservicesapp.data.remote.util.ErrorEvent
import java.math.BigDecimal
import java.text.DecimalFormat
import java.util.UUID

data class Service(
    val id: UUID,
    val name: String,
    val description: String,
    val price: BigDecimal,
    val image: String
) {
    val formattedPrice: String
        get() = "Kshs. ${DecimalFormat("#,###.00").format(price)}"
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
        get() = "Kshs. ${DecimalFormat("#,###.00").format(price)}"
}

data class ServiceState(
    val services: List<Service> = emptyList(),
    val isLoading: Boolean = false,
    val error: ErrorEvent<String>? = null
)

data class ServiceAddonUiState(
    val serviceAddons: List<ServiceAddOn> = emptyList(),
    val isLoading: Boolean = false,
    val error: ErrorEvent<String>? = null
)