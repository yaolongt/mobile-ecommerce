package com.app.frontend.api

import com.app.frontend.models.GetAllProductResponse
import retrofit2.http.GET

interface ApiService {
    @GET("product")
    suspend fun getAllProducts(): GetAllProductResponse
}