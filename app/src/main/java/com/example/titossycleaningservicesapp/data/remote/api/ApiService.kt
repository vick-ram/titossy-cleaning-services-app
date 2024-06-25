package com.example.titossycleaningservicesapp.data.remote.api

import com.example.titossycleaningservicesapp.data.remote.api.ApiConstants.BOOKING
import com.example.titossycleaningservicesapp.data.remote.api.ApiConstants.BOOKING_ID
import com.example.titossycleaningservicesapp.data.remote.api.ApiConstants.CART
import com.example.titossycleaningservicesapp.data.remote.api.ApiConstants.CLEAR_CART
import com.example.titossycleaningservicesapp.data.remote.api.ApiConstants.CUSTOMER
import com.example.titossycleaningservicesapp.data.remote.api.ApiConstants.CUSTOMER_ID
import com.example.titossycleaningservicesapp.data.remote.api.ApiConstants.CUSTOMER_PAYMENT
import com.example.titossycleaningservicesapp.data.remote.api.ApiConstants.CUSTOMER_PAYMENT_ID
import com.example.titossycleaningservicesapp.data.remote.api.ApiConstants.CUSTOMER_SIGN_IN
import com.example.titossycleaningservicesapp.data.remote.api.ApiConstants.CUSTOMER_SIGN_OUT
import com.example.titossycleaningservicesapp.data.remote.api.ApiConstants.CUSTOMER_SIGN_UP
import com.example.titossycleaningservicesapp.data.remote.api.ApiConstants.DELETE_ADDON
import com.example.titossycleaningservicesapp.data.remote.api.ApiConstants.DELETE_SERVICE
import com.example.titossycleaningservicesapp.data.remote.api.ApiConstants.EMPLOYEE
import com.example.titossycleaningservicesapp.data.remote.api.ApiConstants.EMPLOYEE_ID
import com.example.titossycleaningservicesapp.data.remote.api.ApiConstants.EMPLOYEE_SIGN_IN
import com.example.titossycleaningservicesapp.data.remote.api.ApiConstants.EMPLOYEE_SIGN_OUT
import com.example.titossycleaningservicesapp.data.remote.api.ApiConstants.ORDER
import com.example.titossycleaningservicesapp.data.remote.api.ApiConstants.ORDER_ID
import com.example.titossycleaningservicesapp.data.remote.api.ApiConstants.PAYMENT
import com.example.titossycleaningservicesapp.data.remote.api.ApiConstants.PRODUCT
import com.example.titossycleaningservicesapp.data.remote.api.ApiConstants.PRODUCT_CART
import com.example.titossycleaningservicesapp.data.remote.api.ApiConstants.PRODUCT_CART_ID
import com.example.titossycleaningservicesapp.data.remote.api.ApiConstants.SERVICE
import com.example.titossycleaningservicesapp.data.remote.api.ApiConstants.SERVICE_ADDON
import com.example.titossycleaningservicesapp.data.remote.api.ApiConstants.SERVICE_ADDON_CART
import com.example.titossycleaningservicesapp.data.remote.api.ApiConstants.SERVICE_CART
import com.example.titossycleaningservicesapp.data.remote.api.ApiConstants.SUPPLIER
import com.example.titossycleaningservicesapp.data.remote.api.ApiConstants.SUPPLIER_ID
import com.example.titossycleaningservicesapp.data.remote.api.ApiConstants.SUPPLIER_SIGN_IN
import com.example.titossycleaningservicesapp.data.remote.api.ApiConstants.SUPPLIER_SIGN_OUT
import com.example.titossycleaningservicesapp.data.remote.api.ApiConstants.SUPPLIER_SIGN_UP
import com.example.titossycleaningservicesapp.data.remote.dto.ApiResponse
import com.example.titossycleaningservicesapp.data.remote.dto.BookingDto
import com.example.titossycleaningservicesapp.data.remote.dto.CustomerDto
import com.example.titossycleaningservicesapp.data.remote.dto.CustomerPaymentDto
import com.example.titossycleaningservicesapp.data.remote.dto.EmployeeDto
import com.example.titossycleaningservicesapp.data.remote.dto.ProductCartDto
import com.example.titossycleaningservicesapp.data.remote.dto.ProductDto
import com.example.titossycleaningservicesapp.data.remote.dto.PurchaseOrderDto
import com.example.titossycleaningservicesapp.data.remote.dto.ServiceAddOnDto
import com.example.titossycleaningservicesapp.data.remote.dto.ServiceCartDto
import com.example.titossycleaningservicesapp.data.remote.dto.ServiceDto
import com.example.titossycleaningservicesapp.data.remote.dto.SupplierDto
import com.example.titossycleaningservicesapp.data.remote.dto.UpdateOrderStatus
import com.example.titossycleaningservicesapp.domain.models.requests.booking.BookingRequest
import com.example.titossycleaningservicesapp.domain.models.requests.booking.UpdateBookingStatus
import com.example.titossycleaningservicesapp.domain.models.requests.cart.AddServiceAddonToCart
import com.example.titossycleaningservicesapp.domain.models.requests.cart.AddServiceToCart
import com.example.titossycleaningservicesapp.domain.models.requests.customer.CustomerSignInRequest
import com.example.titossycleaningservicesapp.domain.models.requests.customer.CustomerSignUpRequest
import com.example.titossycleaningservicesapp.domain.models.requests.employee.EmployeeAvailability
import com.example.titossycleaningservicesapp.domain.models.requests.employee.EmployeeSignInRequest
import com.example.titossycleaningservicesapp.domain.models.requests.payment.CustomerPaymentRequest
import com.example.titossycleaningservicesapp.domain.models.requests.payment.CustomerPaymentStatusUpdate
import com.example.titossycleaningservicesapp.domain.models.requests.po.AddProductToCart
import com.example.titossycleaningservicesapp.domain.models.requests.po.PurchaseOrderRequest
import com.example.titossycleaningservicesapp.domain.models.requests.supplier.SupplierSignInRequest
import com.example.titossycleaningservicesapp.domain.models.requests.supplier.SupplierSignUpRequest
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query
import java.util.UUID

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
    suspend fun supplierSignUp(@Body supplierSignUpRequest: SupplierSignUpRequest): ApiResponse<SupplierDto>

    @POST(SUPPLIER_SIGN_OUT)
    suspend fun supplierSignOut(
        @Header("Authorization") token: String
    ): ApiResponse<Unit>

    @PUT(SUPPLIER_ID)
    suspend fun supplierUpdate(
        supplierId: String,
        @Body supplierUpdateRequest: SupplierSignUpRequest
    ): ApiResponse<Unit>

    @GET(SUPPLIER_ID)
    suspend fun getSupplierById(@Path("id") supplierId: String) : ApiResponse<SupplierDto>

    @GET(SUPPLIER)
    suspend fun getSupplierByEmail(@Query("email") email: String): ApiResponse<SupplierDto>

    @GET(SUPPLIER)
    suspend fun getSuppliers(): ApiResponse<List<SupplierDto>>


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
    suspend fun getEmployees(): ApiResponse<List<EmployeeDto>>

    @PATCH(EMPLOYEE_ID)
    suspend fun updateEmployeeAvailability(
        @Body availability: EmployeeAvailability
    ): ApiResponse<EmployeeDto>

    @GET(EMPLOYEE)
    suspend fun getAvailableEmployees(
        @Query("status") status: String
    ): ApiResponse<List<EmployeeDto>>

    @GET(EMPLOYEE_ID)
    suspend fun getEmployeeById(
        @Path("id") id: String
    ): ApiResponse<EmployeeDto>

    @GET(EMPLOYEE)
    suspend fun getEmployeeByEmail(
        @Query("email") email: String
    ): ApiResponse<EmployeeDto>


    /**
     * Services
     */
    @GET(SERVICE)
    suspend fun getServices(): ApiResponse<List<ServiceDto>>

    @GET(SERVICE_ADDON)
    suspend fun getServiceAddons(@Path("id") id: String): ApiResponse<List<ServiceAddOnDto>>

    /**
     * Cart endpoints
     */
    @POST(SERVICE_CART)
    suspend fun addServiceToCart(@Body service: AddServiceToCart): ApiResponse<String>

    @POST(SERVICE_ADDON_CART)
    suspend fun addServiceAddonToCart(@Body serviceAddon: AddServiceAddonToCart): ApiResponse<String>

    @GET(CART)
    suspend fun getCartItems(): ApiResponse<List<ServiceCartDto>>

    @DELETE(DELETE_SERVICE)
    suspend fun removeService(@Path("id") id: String): ApiResponse<String>

    @DELETE(DELETE_ADDON)
    suspend fun removeAddon(@Path("id") id: String): ApiResponse<String>

    @DELETE(CLEAR_CART)
    suspend fun clearCart(): ApiResponse<String>


    /**
     * Booking
     */
    @POST(BOOKING)
    suspend fun createBooking(
        @Body bookingRequest: BookingRequest
    ): ApiResponse<BookingDto>

    @PATCH(BOOKING_ID)
    suspend fun updateBookingStatus(
        @Path("id") bookingId: String,
        @Body bookingStatus: UpdateBookingStatus
    ): ApiResponse<String>

    @PUT(BOOKING_ID)
    suspend fun updateBooking(
        @Path("id") bookingId: String,
        @Body bookingUpdate: BookingRequest
    ): ApiResponse<String>

    @GET(BOOKING)
    suspend fun getBookings(): ApiResponse<List<BookingDto>>

    @GET(BOOKING)
    suspend fun searchBookings(@Query("search") query: String): ApiResponse<List<BookingDto>>

    @GET(BOOKING_ID)
    suspend fun getBookingById(@Path("id") id: String): ApiResponse<BookingDto>

    @DELETE(BOOKING_ID)
    suspend fun deleteBooking(@Path("id") id: String): ApiResponse<String>


    @POST(CUSTOMER_PAYMENT)
    suspend fun createPayment(
        @Body customerPayment: CustomerPaymentRequest
    ): ApiResponse<CustomerPaymentDto>

    @GET(CUSTOMER_PAYMENT)
    suspend fun getCustomerPayment(): ApiResponse<List<CustomerPaymentDto>>

    @PUT(CUSTOMER_PAYMENT_ID)
    suspend fun updateCustomerPayment(
        @Path("id") id: String,
        @Body customerPaymentUpdate: CustomerPaymentRequest
    ): ApiResponse<String>

    @PATCH(CUSTOMER_PAYMENT_ID)
    suspend fun updatePaymentStatus(
        paymentId: String,
        @Body paymentStatusUpdate: CustomerPaymentStatusUpdate
    ): ApiResponse<String>

    @DELETE(CUSTOMER_PAYMENT_ID)
    suspend fun deletePayment(@Path("id") id: String): ApiResponse<String>

    @GET(PRODUCT)
    suspend fun getProducts() : ApiResponse<List<ProductDto>>

    @POST(PRODUCT_CART)
    suspend fun addProductToCart(
        @Body addProductToCart: AddProductToCart
    ) : ApiResponse<String>

    @GET(PRODUCT_CART)
    suspend fun getProductCart(): ApiResponse<List<ProductCartDto>>

    @DELETE(PRODUCT_CART_ID)
    suspend fun removeProductFromCart(
        @Path("productId") productId: UUID
    ) : ApiResponse<String>


    @POST(ORDER)
    suspend fun createPurchaseOrder(
        @Body purchaseOrder: PurchaseOrderRequest
    ) : ApiResponse<PurchaseOrderDto>

    @GET(ORDER)
    suspend fun getPurchaseOrders(): ApiResponse<List<PurchaseOrderDto>>

    @GET(ORDER)
    suspend fun getCompletedPurchaseOrders(
        @Query("status") status: String
    ): ApiResponse<List<PurchaseOrderDto>>

    @PATCH(ORDER_ID)
    suspend fun updateOrderStatus(
        @Path("id") id: String,
        @Body orderStatus: UpdateOrderStatus
    ) : ApiResponse<String>

}