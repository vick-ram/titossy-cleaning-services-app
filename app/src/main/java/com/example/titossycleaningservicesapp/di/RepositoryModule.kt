package com.example.titossycleaningservicesapp.di

import com.example.titossycleaningservicesapp.data.repository.BookingRepositoryImpl
import com.example.titossycleaningservicesapp.data.repository.CartRepositoryImpl
import com.example.titossycleaningservicesapp.data.repository.CustomerPaymentRepositoryImpl
import com.example.titossycleaningservicesapp.data.repository.CustomerRepositoryImpl
import com.example.titossycleaningservicesapp.data.repository.EmployeeRepositoryImpl
import com.example.titossycleaningservicesapp.data.repository.FeedbackRepositoryImpl
import com.example.titossycleaningservicesapp.data.repository.ProductRepositoryImpl
import com.example.titossycleaningservicesapp.data.repository.PurchaseOrderRepositoryImpl
import com.example.titossycleaningservicesapp.data.repository.ServiceRepositoryImpl
import com.example.titossycleaningservicesapp.data.repository.SupplierPaymentRepositoryImpl
import com.example.titossycleaningservicesapp.data.repository.SupplierRepositoryImpl
import com.example.titossycleaningservicesapp.domain.repository.BookingRepository
import com.example.titossycleaningservicesapp.domain.repository.CartRepository
import com.example.titossycleaningservicesapp.domain.repository.CustomerPaymentRepository
import com.example.titossycleaningservicesapp.domain.repository.CustomerRepository
import com.example.titossycleaningservicesapp.domain.repository.EmployeeRepository
import com.example.titossycleaningservicesapp.domain.repository.FeedbackRepository
import com.example.titossycleaningservicesapp.domain.repository.ProductRepository
import com.example.titossycleaningservicesapp.domain.repository.PurchaseOrderRepository
import com.example.titossycleaningservicesapp.domain.repository.ServiceRepository
import com.example.titossycleaningservicesapp.domain.repository.SupplierPaymentRepository
import com.example.titossycleaningservicesapp.domain.repository.SupplierRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    abstract fun bindSupplierRepository(
        repo: SupplierRepositoryImpl
    ): SupplierRepository

    @Binds
    abstract fun bindCustomerRepository(
        customerRepositoryImpl: CustomerRepositoryImpl
    ): CustomerRepository

    @Binds
    abstract fun bindEmployeeRepository(
        repo: EmployeeRepositoryImpl
    ): EmployeeRepository

    @Binds
    abstract fun bindServiceRepository(
        serviceRepositoryImpl: ServiceRepositoryImpl
    ): ServiceRepository

    @Binds
    abstract fun bindCartRepository(
        cartRepositoryImpl: CartRepositoryImpl
    ): CartRepository

    @Binds
    abstract fun bindBookingRepository(
        bookingRepositoryImpl: BookingRepositoryImpl
    ): BookingRepository

    @Binds
    abstract fun bindCustomerPayment(
        customerPaymentRepositoryImpl: CustomerPaymentRepositoryImpl
    ): CustomerPaymentRepository

    @Binds
    abstract fun bindProductCartRepository(
        productRepositoryImpl: ProductRepositoryImpl
    ): ProductRepository

    @Binds
    abstract fun bindPurchaseOrder(
        purchaseOrderRepositoryImpl: PurchaseOrderRepositoryImpl
    ): PurchaseOrderRepository

    @Binds
    abstract fun bindFeedbackRepository(
        feedbackRepositoryImpl: FeedbackRepositoryImpl
    ) : FeedbackRepository

    @Binds
    abstract fun bindSupplierPayment(
        supplierPaymentRepositoryImpl: SupplierPaymentRepositoryImpl
    ) : SupplierPaymentRepository
}