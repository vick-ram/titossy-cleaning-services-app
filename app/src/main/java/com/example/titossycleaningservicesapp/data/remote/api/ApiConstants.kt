package com.example.titossycleaningservicesapp.data.remote.api

object ApiConstants {
    const val BASE_URL = "http://10.0.2.2:8080/"
    const val CUSTOMER = "api/customer"
    private const val CUSTOMER_AUTH = "$CUSTOMER/auth"
    const val CUSTOMER_SIGN_IN = "$CUSTOMER_AUTH/sign_in"
    const val CUSTOMER_SIGN_UP = "$CUSTOMER_AUTH/sign_up"
    const val CUSTOMER_SIGN_OUT = "$CUSTOMER_AUTH/sign_out"
    const val CUSTOMER_ID = "$CUSTOMER/{id}"

    val id = "79cb8d0f-a520-4d15-9a06-babcefee3cef"

    const val SUPPLIER = "api/supplier"
    private const val SUPPLIER_AUTH = "$CUSTOMER/auth"
    const val SUPPLIER_SIGN_IN = "$SUPPLIER_AUTH/sign_in"
    const val SUPPLIER_SIGN_UP = "$SUPPLIER_AUTH/sign_up"
    const val SUPPLIER_SIGN_OUT = "$SUPPLIER_AUTH/sign_out"
    const val SUPPLIER_ID = "$SUPPLIER/{id}"

    const val EMPLOYEE = "api/employee"
    private const val EMPLOYEE_AUTH = "$CUSTOMER/auth"
    const val EMPLOYEE_SIGN_IN = "$EMPLOYEE_AUTH/sign_in"
    const val EMPLOYEE_SIGN_OUT = "$EMPLOYEE_AUTH/sign_out"

    const val SERVICE = "api/service"
    const val SERVICE_ADDON = "api/addon"

    const val CART = "api/cart"
}
