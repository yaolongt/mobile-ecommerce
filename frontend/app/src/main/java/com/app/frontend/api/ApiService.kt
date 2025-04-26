package com.app.frontend.api

import com.app.frontend.models.GetAllProductResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {
    @GET("product")
    suspend fun getAllProducts(
        @Query("offset") offset: Int
    ): GetAllProductResponse
}