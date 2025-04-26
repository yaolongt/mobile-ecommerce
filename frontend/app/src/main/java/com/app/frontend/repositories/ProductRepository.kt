package com.app.frontend.repositories

import android.util.Log
import androidx.compose.ui.geometry.Offset
import com.app.frontend.api.RetrofitClient
import com.app.frontend.models.GetAllProductResponse

class ProductRepository {
    private val apiService = RetrofitClient.instance

    suspend fun getAllProducts(offset: Int): Result<GetAllProductResponse> = try {
        val response = apiService.getAllProducts(offset)
        Result.success(
            GetAllProductResponse(
                products = response.products,
                nextOffset = response.nextOffset,
                total = response.total
            )
        )
    } catch (e: Exception) {
        Log.e("ProductRepository", e.toString())
        Result.failure(e)
    }
}