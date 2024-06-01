package com.example.titossycleaningservicesapp.data.repository

import com.example.titossycleaningservicesapp.core.FileUtils
import com.example.titossycleaningservicesapp.core.Resource
import com.example.titossycleaningservicesapp.data.remote.api.ApiService
import com.example.titossycleaningservicesapp.domain.models.requests.cart.AddServiceAddonToCart
import com.example.titossycleaningservicesapp.domain.models.requests.cart.AddServiceToCart
import com.example.titossycleaningservicesapp.domain.models.ui_models.CartItem
import com.example.titossycleaningservicesapp.domain.repository.CartRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import java.util.UUID
import javax.inject.Inject

class CartRepositoryImpl @Inject constructor(
    private val apiService: ApiService
): CartRepository
{
    override suspend fun addServiceToCart(serviceId: UUID, quantity: Int): Flow<Resource<String?>> {
        return flow {
            emit(Resource.Loading)
            val response = apiService.addServiceToCart(AddServiceToCart(
                serviceId = serviceId,
                quantity = quantity
            ))
            when(response.status) {
                "success" -> {
                    emit(Resource.Success(response.message))
                }
                "error" -> {
                    throw Exception(response.error.toString())
                }
            }
        }.catch { e ->
            emit(Resource.Error(e.message.toString()))
        }
    }

    override suspend fun addServiceAddonToCart(serviceAddonId: UUID, quantity: Int): Flow<Resource<String?>> {
        return flow {
            emit(Resource.Loading)
            val response = apiService.addServiceAddonToCart(
                AddServiceAddonToCart(
                    serviceAddon = serviceAddonId,
                    quantity = quantity
                )
            )
            when(response.status) {
                "success" -> {
                    emit(Resource.Success(response.message))
                }
                "error" -> {
                    val error = FileUtils.createErrorMessage(response.error)
                    throw Exception(error)
                }
            }
        }.catch { e ->
            emit(Resource.Error(e.message.toString()))
        }
    }

    override fun removeServiceFromCart(addonId: UUID): Flow<Resource<String?>> {
        return flow {
            emit(Resource.Loading)
            val response = apiService.removeAddon(addonId.toString())
            when(response.status) {
                "success" -> {
                    emit(Resource.Success(response.message))
                }
                "error" -> {
                    val errorMessage = FileUtils.createErrorMessage(response.error)
                    throw Exception(errorMessage)
                }
            }
        }.catch { e ->
            e.printStackTrace()
            emit(Resource.Error(e.message.toString()))
        }
    }

    override fun getServicesInCart(): Flow<Resource<List<CartItem>>> {
        return flow {
            emit(Resource.Loading)
            val response = apiService.getCartItems()
            when(response.status) {
                "success" -> {
                    response.data?.let {serviceCartDto ->
                        val cartItem = serviceCartDto.map { it.toCartItem() }
                        emit(Resource.Success(cartItem))
                    }
                }
                "error" -> {
                    val error = FileUtils.createErrorMessage(response.error)
                    throw Exception(error)
                }
            }
        }.catch { e ->
            emit(Resource.Error(e.message.toString()))
        }
    }

    override fun clearCart(): Flow<Resource<String?>> {
        return flow {
            emit(Resource.Loading)
            val response = apiService.clearCart()
            when(response.status) {
                "success" -> {
                    emit(Resource.Success(response.message))
                }
                "error" -> {
                    val errorMessage = FileUtils.createErrorMessage(response.error)
                    throw Exception(errorMessage)
                }
            }
        }.catch { e ->
            e.printStackTrace()
            emit(Resource.Error(e.message.toString()))
        }
    }
}