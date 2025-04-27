package com.app.frontend.api

import com.app.frontend.models.GetProductByIdResponse
import com.app.frontend.models.GetProductResponse
import com.app.frontend.models.GetSearchProductResponse
import com.app.frontend.models.ProductDTO
import com.app.frontend.models.UpdateProductResponse
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {
    @GET("product")
    suspend fun getAllProducts(
        @Query("offset") offset: Int,
        @Query("sort") sort: String? = null,
        @Query("direction")direction: String? = null,
        @Query("filter") filter: String? = null
    ): GetProductResponse

    @GET("product/search")
    suspend fun getSearchedProducts(
        @Query("query") query: String
    ): GetSearchProductResponse

    @GET("product/{id}")
    suspend fun getProductById(
        @Path("id") id: Int
    ): GetProductByIdResponse

    @PUT("product")
    suspend fun updateProduct(
        @Body product: ProductDTO
    ): UpdateProductResponse
}