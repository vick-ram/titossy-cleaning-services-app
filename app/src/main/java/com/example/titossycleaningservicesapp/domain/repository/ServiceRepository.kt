package com.example.titossycleaningservicesapp.domain.repository

import com.example.titossycleaningservicesapp.core.Resource
import com.example.titossycleaningservicesapp.domain.models.ui_models.Service
import kotlinx.coroutines.flow.Flow

interface ServiceRepository {
    fun getAllServices() : Flow<Resource<List<Service>>>
    fun searchServices(query: String) : Flow<Resource<List<Service>>>
}