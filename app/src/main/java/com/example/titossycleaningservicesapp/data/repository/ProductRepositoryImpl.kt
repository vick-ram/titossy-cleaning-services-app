package com.example.titossycleaningservicesapp.data.repository

import com.example.titossycleaningservicesapp.core.FileUtils
import com.example.titossycleaningservicesapp.core.Resource
import com.example.titossycleaningservicesapp.data.remote.api.ApiService
import com.example.titossycleaningservicesapp.domain.models.requests.po.AddProductToCart
import com.example.titossycleaningservicesapp.domain.models.ui_models.Product
import com.example.titossycleaningservicesapp.domain.models.ui_models.ProductCart
import com.example.titossycleaningservicesapp.domain.repository.ProductRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import java.util.UUID
import javax.inject.Inject

class ProductRepositoryImpl @Inject constructor(
    private val apiService: ApiService
): ProductRepository {
    override fun getAllProducts(): Flow<Resource<List<Product>>> {
        return flow {
            emit(Resource.Loading)
            val response = apiService.getProducts()
            when(response.status) {
                "success" -> {
                    val products = response.data?.map { it.toProduct() }

                    products?.let { emit(Resource.Success(it)) }
                }
                "error" -> {
                    if (response.error != null) {
                        val error = FileUtils.createErrorMessage(response.error)
                        throw Exception(error)
                    }
                }
            }
        }.catch { e ->
            emit(Resource.Error(e.message.toString()))
        }
    }

    override fun deleteProductFromCart(productId: String): Flow<Resource<String>> {
        return flow {
            emit(Resource.Loading)
            val response = apiService.removeProductFromCart(UUID.fromString(productId))
            when(response.status){
                "success" -> {
                    val message = response.data
                    message?.let { emit(Resource.Success(it)) }
                }
                "error" -> {
                    if (response.error != null) {
                        val error = FileUtils.createErrorMessage(response.error)
                        throw Exception(error)
                    }
                }
            }
        }.catch { e ->
            e.printStackTrace()
            emit(Resource.Error(e.message.toString()))
        }
    }

    override fun getProductCart(): Flow<Resource<List<ProductCart>>> {
        return flow {
            emit(Resource.Loading)
            val response = apiService.getProductCart()
            when(response.status) {
                "success" -> {
                    val products = response.data?.map { it.toProductCart() }
                    products?.let { emit(Resource.Success(it)) }
                }
                "error" -> {
                    if (response.error != null) {
                        val error = FileUtils.createErrorMessage(response.error)
                        throw Exception(error)
                    }
                }
            }
        }.catch { e ->
            emit(Resource.Error(e.message.toString()))
        }
    }

    override fun addProductToPurchase(productId: String, quantity: Int): Flow<Resource<String>> {
       return flow {
           emit(Resource.Loading)
           val response = apiService.addProductToCart(
               AddProductToCart(productId = UUID.fromString(productId), quantity)
           )
           when(response.status) {
               "success" -> {
                   val message = response.data
                   message?.let { emit(Resource.Success(it)) }
               }
               "error" -> {
                   if (response.error != null) {
                       val errorMessage = FileUtils.createErrorMessage(response.error)
                       throw Exception(errorMessage)
                   }
               }
           }
       }.catch { exception ->
           exception.printStackTrace()
           emit(Resource.Error(exception.message.toString()))
       }
    }
}