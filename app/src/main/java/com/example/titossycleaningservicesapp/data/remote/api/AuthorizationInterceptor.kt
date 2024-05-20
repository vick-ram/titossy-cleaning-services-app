package com.example.titossycleaningservicesapp.data.remote.api

import com.example.titossycleaningservicesapp.data.local.datastore.DataStoreKeys
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject

class AuthorizationInterceptor @Inject constructor(
    private val dataStoreKeys: DataStoreKeys
) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val token = runBlocking {
            dataStoreKeys.getTokenFromDataStore()
        } ?: ""

        val request = chain.request().newBuilder()
            .addHeader("Authorization", "Bearer $token")
            .build()
        return chain.proceed(request)
    }
}