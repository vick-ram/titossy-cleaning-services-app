package com.example.titossycleaningservicesapp.data.repository

import android.content.Context
import android.net.Uri
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
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.util.UUID
import javax.inject.Inject

class ProductRepositoryImpl @Inject constructor(
    private val apiService: ApiService
) : ProductRepository {
    override fun createProduct(
        context: Context,
        uri: Uri,
        name: String,
        description: String,
        price: String,
        stock: String,
        reorderLevel: String,
        supplierId: String
    ): Flow<Resource<String>> {
        return flow {
            emit(Resource.Loading)
            val file = FileUtils.getFileFromUri(context, uri)
            val requestFile = file?.asRequestBody("image/jpeg".toMediaTypeOrNull())
            val body = MultipartBody.Part.createFormData("image", file?.name, requestFile!!)
            val namePart = name.toRequestBody("text/plain".toMediaTypeOrNull())
            val descriptionPart = description.toRequestBody("text/plain".toMediaTypeOrNull())
            val pricePart = price.toRequestBody("text/plain".toMediaTypeOrNull())
            val stockPart = stock.toRequestBody("text/plain".toMediaTypeOrNull())
            val reorderLevelPart = reorderLevel.toRequestBody("text/plain".toMediaTypeOrNull())
            val supplierIdPart = supplierId.toRequestBody("text/plain".toMediaTypeOrNull())
            val response = apiService.createProduct(
                name = namePart,
                description = descriptionPart,
                unitPrice = pricePart,
                stock = stockPart,
                image = body,
                reorderLevel = reorderLevelPart,
                supplierId = supplierIdPart
            )

            when(response.status) {
                "success" -> {
                    val message = response.message
                    message?.let { emit(Resource.Success(it)) }
                }
                "error" -> {
                    if (response.error != null) {
                        val errors = FileUtils.createErrorMessage(response.error)
                        throw Exception(errors)
                    }
                }
            }
        }.catch { e ->
            e.printStackTrace()
            emit(Resource.Error(e.message.toString()))
        }
    }

    override fun deleteProduct(productId: String): Flow<Resource<String>> {
        return flow {
            emit(Resource.Loading)
            val response = apiService.deleteProduct(UUID.fromString(productId))
            when(response.status) {
                "success" -> {
                    val message = response.message
                    message?.let { emit(Resource.Success(it)) }
                }
                "error" -> {
                    if (response.error != null) {
                        val errorMessage = FileUtils.createErrorMessage(response.error)
                        throw Exception(errorMessage)
                    }
                }
            }
        }.catch { ex ->
            ex.printStackTrace()
            emit(Resource.Error(ex.message.toString()))
        }
    }

    override fun getAllProducts(): Flow<Resource<List<Product>>> {
        return flow {
            emit(Resource.Loading)
            val response = apiService.getProducts()
            when (response.status) {
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
            val response = apiService.removeProductFromCart(
                productId = UUID.fromString(productId)
            )
            when (response.status) {
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
            when (response.status) {
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

    override fun addProductToPurchase(
        productId: String,
        quantity: Int
    ): Flow<Resource<String>> {
        return flow {
            emit(Resource.Loading)
            val response = apiService.addProductToCart(
                AddProductToCart(
                    productId = UUID.fromString(productId),
                    quantity = quantity
                )
            )
            when (response.status) {
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