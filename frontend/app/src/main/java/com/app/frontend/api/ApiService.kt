package com.app.frontend.api

import com.app.frontend.models.GetProductResponse
import com.app.frontend.models.GetSearchProductResponse
import com.app.frontend.models.Product
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {
    @GET("product")
    suspend fun getAllProducts(
        @Query("offset") offset: Int
    ): GetProductResponse

    @GET("product/search")
    suspend fun getSearchedProducts(
        @Query("query") query: String
    ): GetSearchProductResponse
}