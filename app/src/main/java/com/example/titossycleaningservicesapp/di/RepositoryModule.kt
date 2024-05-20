package com.example.titossycleaningservicesapp.di

import com.example.titossycleaningservicesapp.data.repository.AddressRepositoryImpl
import com.example.titossycleaningservicesapp.data.repository.CustomerRepositoryImpl
import com.example.titossycleaningservicesapp.data.repository.EmployeeRepositoryImpl
import com.example.titossycleaningservicesapp.data.repository.ServiceRepositoryImpl
import com.example.titossycleaningservicesapp.data.repository.SupplierRepositoryImpl
import com.example.titossycleaningservicesapp.domain.repository.AddressRepository
import com.example.titossycleaningservicesapp.domain.repository.CustomerRepository
import com.example.titossycleaningservicesapp.domain.repository.EmployeeRepository
import com.example.titossycleaningservicesapp.domain.repository.ServiceRepository
import com.example.titossycleaningservicesapp.domain.repository.SupplierRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    abstract fun bindSupplierRepository(repo: SupplierRepositoryImpl): SupplierRepository

    @Binds
    abstract fun bindCustomerRepository(customerRepositoryImpl: CustomerRepositoryImpl): CustomerRepository


    @Binds
    abstract fun bindEmployeeRepository(repo: EmployeeRepositoryImpl): EmployeeRepository

    @Binds
    abstract fun bindAddressRepository(addressRepository: AddressRepositoryImpl): AddressRepository

    @Binds
    abstract fun bindServiceRepository(serviceRepositoryImpl: ServiceRepositoryImpl) : ServiceRepository
}