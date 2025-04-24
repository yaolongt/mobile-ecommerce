package com.app.frontend.repositories

import android.util.Log
import com.app.frontend.api.RetrofitClient
import com.app.frontend.models.GetAllProductResponse

class ProductRepository {
    private val apiService = RetrofitClient.instance

    suspend fun getAllProducts(): Result<GetAllProductResponse> = try {
        val response = apiService.getAllProducts()
        Result.success(response)
    } catch (e: Exception) {
        Log.e("ProductRepository", e.toString())
        Result.failure(e)
    }
}