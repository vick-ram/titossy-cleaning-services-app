package com.example.titossycleaningservicesapp.data.remote.api

import com.example.titossycleaningservicesapp.data.remote.api.ApiConstants.CART
import com.example.titossycleaningservicesapp.data.remote.api.ApiConstants.CLEAR_CART
import com.example.titossycleaningservicesapp.data.remote.api.ApiConstants.CUSTOMER
import com.example.titossycleaningservicesapp.data.remote.api.ApiConstants.CUSTOMER_ID
import com.example.titossycleaningservicesapp.data.remote.api.ApiConstants.CUSTOMER_SIGN_IN
import com.example.titossycleaningservicesapp.data.remote.api.ApiConstants.CUSTOMER_SIGN_OUT
import com.example.titossycleaningservicesapp.data.remote.api.ApiConstants.CUSTOMER_SIGN_UP
import com.example.titossycleaningservicesapp.data.remote.api.ApiConstants.DELETE_ADDON
import com.example.titossycleaningservicesapp.data.remote.api.ApiConstants.EMPLOYEE
import com.example.titossycleaningservicesapp.data.remote.api.ApiConstants.EMPLOYEE_ID
import com.example.titossycleaningservicesapp.data.remote.api.ApiConstants.EMPLOYEE_SIGN_IN
import com.example.titossycleaningservicesapp.data.remote.api.ApiConstants.EMPLOYEE_SIGN_OUT
import com.example.titossycleaningservicesapp.data.remote.api.ApiConstants.SERVICE
import com.example.titossycleaningservicesapp.data.remote.api.ApiConstants.SERVICE_ADDON_CART
import com.example.titossycleaningservicesapp.data.remote.api.ApiConstants.SERVICE_CART
import com.example.titossycleaningservicesapp.data.remote.api.ApiConstants.SUPPLIER
import com.example.titossycleaningservicesapp.data.remote.api.ApiConstants.SUPPLIER_ID
import com.example.titossycleaningservicesapp.data.remote.api.ApiConstants.SUPPLIER_SIGN_IN
import com.example.titossycleaningservicesapp.data.remote.api.ApiConstants.SUPPLIER_SIGN_OUT
import com.example.titossycleaningservicesapp.data.remote.api.ApiConstants.SUPPLIER_SIGN_UP
import com.example.titossycleaningservicesapp.data.remote.dto.ApiResponse
import com.example.titossycleaningservicesapp.data.remote.dto.CustomerDto
import com.example.titossycleaningservicesapp.data.remote.dto.EmployeeDto
import com.example.titossycleaningservicesapp.data.remote.dto.ServiceCartDto
import com.example.titossycleaningservicesapp.data.remote.dto.ServiceDto
import com.example.titossycleaningservicesapp.data.remote.dto.SupplierDto
import com.example.titossycleaningservicesapp.domain.models.requests.cart.AddServiceAddonToCart
import com.example.titossycleaningservicesapp.domain.models.requests.cart.AddServiceToCart
import com.example.titossycleaningservicesapp.domain.models.requests.customer.CustomerSignInRequest
import com.example.titossycleaningservicesapp.domain.models.requests.customer.CustomerSignUpRequest
import com.example.titossycleaningservicesapp.domain.models.requests.employee.EmployeeAvailability
import com.example.titossycleaningservicesapp.domain.models.requests.employee.EmployeeSignInRequest
import com.example.titossycleaningservicesapp.domain.models.requests.supplier.SupplierSignInRequest
import com.example.titossycleaningservicesapp.domain.models.requests.supplier.SupplierSignUpRequest
import okhttp3.MultipartBody
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Multipart
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Part
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {
    /**
     * Customer endpoints
     */
    @POST(CUSTOMER_SIGN_IN)
    suspend fun customerSignIn(@Body customerSignInRequest: CustomerSignInRequest): ApiResponse<String>
    @POST(CUSTOMER_SIGN_UP)
    suspend fun customerSignUp(@Body customerSignUpRequest: CustomerSignUpRequest): ApiResponse<CustomerDto>
    @POST(CUSTOMER_SIGN_OUT)
    suspend fun customerSignOut(
        @Header("Authorization") token: String
    ): ApiResponse<Unit>
    @PUT(CUSTOMER_ID)
    suspend fun updateCustomer(
        customerId: String,
        @Body customerUpdateRequest: CustomerSignUpRequest
    ): ApiResponse<CustomerDto>
    @Multipart
    @PATCH(CUSTOMER_ID)
    suspend fun updateCustomerProfilePicture(
        customerId: String,
        @Part profilePicture: MultipartBody.Part
    ): ApiResponse<CustomerDto>

    @GET(CUSTOMER)
    suspend fun getCustomers(): ApiResponse<List<CustomerDto>>
    @GET(CUSTOMER_ID)
    suspend fun getCustomerById(
        @Path("id") customerId: String
    ): ApiResponse<CustomerDto>

    @GET(CUSTOMER)
    suspend fun getCustomerByEmail(
        @Query("email") email: String
    ): ApiResponse<CustomerDto>

    @GET(CUSTOMER)
    suspend fun getCustomerByUsername(
        @Query("username") username: String
    ): ApiResponse<CustomerDto>

    /**
     * Supplier endpoints
     */
    @POST(SUPPLIER_SIGN_IN)
    suspend fun supplierSignIn(@Body supplierSignInRequest: SupplierSignInRequest): ApiResponse<String>
    @POST(SUPPLIER_SIGN_UP)
    suspend fun supplierSignUp(@Body supplierSignUpRequest: SupplierSignUpRequest) : ApiResponse<SupplierDto>
    @POST(SUPPLIER_SIGN_OUT)
    suspend fun supplierSignOut(
        @Header("Authorization") token: String
    ): ApiResponse<Unit>
    @PUT(SUPPLIER_ID)
    suspend fun supplierUpdate(
        supplierId: String,
        @Body supplierUpdateRequest: SupplierSignUpRequest
    ): ApiResponse<Unit>
    @Multipart
    @PATCH(SUPPLIER_ID)
    suspend fun updateSupplierProfile(
        @Part profile: MultipartBody.Part
    ): ApiResponse<Unit>
    @GET(SUPPLIER)
    suspend fun getSuppliers() : ApiResponse<List<SupplierDto>>

    /**
     * Employee endpoints
     */
    @POST(EMPLOYEE_SIGN_IN)
    suspend fun employeeSignIn(@Body employeeSignInRequest: EmployeeSignInRequest): ApiResponse<String>
    @POST(EMPLOYEE_SIGN_OUT)
    suspend fun employeeSignOut(
        @Header("Authorization") token: String
    ): ApiResponse<EmployeeDto>
    @GET(EMPLOYEE)
    suspend fun getEmployees() : ApiResponse<List<EmployeeDto>>
    @PATCH(EMPLOYEE_ID)
    suspend fun updateEmployeeAvailability(
        @Body availability: EmployeeAvailability
    ) : ApiResponse<EmployeeDto>

    @GET(EMPLOYEE)
    suspend fun getAvailableEmployees(
        @Query("status") status: String
    ) : ApiResponse<List<EmployeeDto>>
    @GET(EMPLOYEE_ID)
    suspend fun getEmployeeById(
        @Path("id") id: String
    ): ApiResponse<EmployeeDto>

    /**
     * Services
     */
    @GET(SERVICE)
    suspend fun getServices(): ApiResponse<List<ServiceDto>>

    /**
     * Cart endpoints
     */
    @POST(SERVICE_CART)
    suspend fun addServiceToCart(@Body service: AddServiceToCart) : ApiResponse<Unit>
    @POST(SERVICE_ADDON_CART)
    suspend fun addServiceAddonToCart(@Body serviceAddon: AddServiceAddonToCart) : ApiResponse<Unit>
    @GET(CART)
    suspend fun getCartItems() : ApiResponse<List<ServiceCartDto>>
    @DELETE(DELETE_ADDON)
    suspend fun removeAddon(@Path("id") id: String) : ApiResponse<Unit>
    @DELETE(CLEAR_CART)
    suspend fun clearCart() : ApiResponse<Unit>

}