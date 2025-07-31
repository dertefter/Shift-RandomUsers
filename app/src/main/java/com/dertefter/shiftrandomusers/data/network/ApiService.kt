package com.dertefter.shiftrandomusers.data.network

import com.dertefter.shiftrandomusers.data.model.UsersResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {
    @GET("api")
    suspend fun fetchUsers(@Query("results") results: Int): Response<UsersResponse>
}