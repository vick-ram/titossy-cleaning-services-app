package com.example.titossycleaningservicesapp.data.remote.api

object ApiConstants {
    const val CHAT_ENDPOINT = "wss://vickram.tech/api/chat/3bb79280-4"
    const val BASE_URL = "https://vickram.tech/"
    const val CUSTOMER = "api/customer"
    private const val CUSTOMER_AUTH = "$CUSTOMER/auth"
    const val CUSTOMER_SIGN_IN = "$CUSTOMER_AUTH/sign_in"
    const val CUSTOMER_SIGN_UP = "$CUSTOMER_AUTH/sign_up"
    const val CUSTOMER_SIGN_OUT = "$CUSTOMER_AUTH/sign_out"
    const val CUSTOMER_ID = "$CUSTOMER/{id}"


    const val SUPPLIER = "api/supplier"
    private const val SUPPLIER_AUTH = "$SUPPLIER/auth"
    const val SUPPLIER_SIGN_IN = "$SUPPLIER_AUTH/sign_in"
    const val SUPPLIER_SIGN_UP = "$SUPPLIER_AUTH/sign_up"
    const val SUPPLIER_SIGN_OUT = "$SUPPLIER_AUTH/sign_out"
    const val SUPPLIER_ID = "$SUPPLIER/{id}"

    const val EMPLOYEE = "api/employee"
    private const val EMPLOYEE_AUTH = "$EMPLOYEE/auth"
    const val EMPLOYEE_SIGN_IN = "$EMPLOYEE_AUTH/sign_in"
    const val EMPLOYEE_SIGN_OUT = "$EMPLOYEE_AUTH/sign_out"
    const val EMPLOYEE_ID = "$EMPLOYEE/{id}"

    const val SERVICE = "api/service"
    const val SERVICE_ADDON = "$SERVICE/{id}/addon"

    const val CART = "api/cart"
    const val SERVICE_CART = "$CART/add-service"
    const val SERVICE_ADDON_CART = "$CART/add-service-addon"
    const val DELETE_SERVICE = "$CART/{id}"
    const val DELETE_ADDON = "$CART/addon/{id}"
    const val CLEAR_CART = "$CART/clear"

    const val BOOKING = "api/booking"
    const val BOOKING_CUSTOMER = "api/booking/customer"
    const val BOOKING_ID = "$BOOKING/{id}"
    const val ASSIGN = "$BOOKING/assign"
    const val ASSIGN_ID = "$ASSIGN/{bookingId}"
    const val ASSIGN_CLEANER = "$ASSIGN/cleaner"
    const val FEEDBACK = "api/feedback"

    const val PAYMENT = "api/payment"
    const val CUSTOMER_PAYMENT = "$PAYMENT/customer"
    const val CUSTOMER_PAYMENT_ID = "$PAYMENT/customer/{id}"
    const val SUPPLIER_PAYMENT = "$PAYMENT/supplier"
    const val SUPPLIER_PAYMENT_ID = "$PAYMENT/supplier/{id}"

    const val PRODUCT = "api/product"
    const val PRODUCT_ID = "$PRODUCT/{id}"

    const val PRODUCT_CART = "$PRODUCT/cart"
    const val PRODUCT_CART_ID = "$PRODUCT/cart/productId"

    const val ORDER = "api/order"
    const val ORDER_ID = "$ORDER/{id}"

    const val MESSAGES = "api/messages/{receiver}"
}
