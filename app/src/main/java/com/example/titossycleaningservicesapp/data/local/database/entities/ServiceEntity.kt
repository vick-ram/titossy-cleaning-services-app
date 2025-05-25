package com.example.titossycleaningservicesapp.data.local.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import androidx.room.Index
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
) {
    fun toServiceModel() = Service(
        id = UUID.fromString(id),
        name = name, description = description,
        price =  price.toBigDecimal(),
        image = image
    )
}

@Entity(
    tableName = "services_addons",
    foreignKeys = [ForeignKey(
        entity = ServiceEntity::class,
        parentColumns = ["service_id"],
        childColumns = ["service_id"],
        onDelete = ForeignKey.CASCADE
    )],
    indices = [Index(value = ["service_id"])]
)
data class ServiceAddonEntity(
    @ColumnInfo("service_addon_id") @PrimaryKey val id: String,
    @ColumnInfo("service_id") val serviceId: String,
    @ColumnInfo("name")  val name: String,
    @ColumnInfo("description") val description: String,
    @ColumnInfo("price") val price: String,
    @ColumnInfo("image_url") val image: String,
    @ColumnInfo("created_at") val createdAt: String,
    @ColumnInfo("updated_at") val updatedAt: String,
) {
    fun toServiceAddonModel() = ServiceAddOn(
        id = UUID.fromString(id),
        serviceId = UUID.fromString(serviceId),
        name = name,
        description = description,
        price = price.toBigDecimal(),
        image = image
    )
}
