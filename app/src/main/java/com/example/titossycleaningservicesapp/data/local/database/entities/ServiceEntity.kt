package com.example.titossycleaningservicesapp.data.local.database.entities

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.Relation
import com.example.titossycleaningservicesapp.domain.models.ui_models.Service
import com.example.titossycleaningservicesapp.domain.models.ui_models.ServiceAddOn
import java.util.UUID

@Entity("services")
data class ServiceEntity(
    @ColumnInfo("service_id") @PrimaryKey val id: String,
    @ColumnInfo("name") val name: String,
    @ColumnInfo("description") val description: String,
    @ColumnInfo("price") val price: String,
    @ColumnInfo("image_url") val image: String,
    @ColumnInfo("created_at") val createdAt: String,
    @ColumnInfo("updated_at") val updatedAt: String,
)

@Entity("services_addons")
data class ServiceAddonEntity(
    @ColumnInfo("service_addon_id") @PrimaryKey val id: String,
    @ColumnInfo("service_id") val serviceId: String,
    @ColumnInfo("name")  val name: String,
    @ColumnInfo("description") val description: String,
    @ColumnInfo("price") val price: String,
    @ColumnInfo("image_url") val image: String,
    @ColumnInfo("created_at") val createdAt: String,
    @ColumnInfo("updated_at") val updatedAt: String,
)

data class ServiceWithAddOns(
    @Embedded val service: ServiceEntity?,
    @Relation(
        parentColumn = "service_id",
        entityColumn = "service_id"
    )
    val addOns: List<ServiceAddonEntity>? = null
) {
    fun toService() = service?.let { service ->
        Service(
            id = UUID.fromString(service.id),
            name = service.name,
            description = service.description,
            price = service.price.toBigDecimal(),
            image = service.image,
            addOns = addOns?.map { addOn ->
                ServiceAddOn(
                    id = UUID.fromString(addOn.id),
                    serviceId = UUID.fromString(addOn.serviceId),
                    name = addOn.name,
                    description = addOn.description,
                    price = addOn.price.toBigDecimal(),
                    image = addOn.image
                )
            }
        )
    }
}
