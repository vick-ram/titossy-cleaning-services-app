package com.example.titossycleaningservicesapp.data.local.database.entities

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.Relation
import com.example.titossycleaningservicesapp.domain.models.ui_models.Cart
import com.example.titossycleaningservicesapp.domain.models.ui_models.CartItem
import java.util.UUID

@Entity("cart")
data class CartEntity(
    @ColumnInfo("cart_id") @PrimaryKey val id: String,
    @ColumnInfo("customer_id") val customerId: String,
    @ColumnInfo("total_amount") val totalAmount: String,
    @ColumnInfo("created_at") val createdAt: String,
    @ColumnInfo("updated_at") val updatedAt: String,
)

@Entity("cart_items")
data class CartItemsEntity(
    @ColumnInfo("cart_item") @PrimaryKey val id: String,
    @ColumnInfo("cart_id") val cartId: String,
    @ColumnInfo("service_id") val service: String,
    @ColumnInfo("service_addon_id") val serviceAddOn: String,
    @ColumnInfo("quantity") val quantity: Int,
    @ColumnInfo("price") val price: String,
    @ColumnInfo("subtotal") val subtotal: String,
    @ColumnInfo("created_at") val createdAt: String,
    @ColumnInfo("updated_at") val updatedAt: String,
) {
    fun toCartItem() = CartItem(
        id = UUID.fromString(id),
        cartId = UUID.fromString(cartId),
        service = UUID.fromString(service),
        serviceAddOn = UUID.fromString(serviceAddOn),
        quantity = quantity,
        price = price.toBigDecimal(),
        subtotal = subtotal.toBigDecimal()
    )

}

data class CartWithItems(
    @Embedded val cart: CartEntity,
    @Relation(
        parentColumn = "cart_id",
        entityColumn = "cart_id"
    )
    val items: List<CartItemsEntity> = emptyList()
) {
    fun toCart() = Cart(
        id = UUID.fromString(cart.id),
        customerId = UUID.fromString(cart.customerId),
        totalAmount = cart.totalAmount.toBigDecimal(),
        cartItems = items.map { it.toCartItem() }
    )
}
