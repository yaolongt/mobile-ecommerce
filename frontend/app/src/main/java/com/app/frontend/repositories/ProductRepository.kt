package com.app.frontend.repositories

import android.util.Log
import androidx.compose.ui.text.toLowerCase
import com.app.frontend.api.RetrofitClient
import com.app.frontend.models.GetProductResponse
import com.app.frontend.models.GetSearchProductResponse
import com.app.frontend.models.Product
import com.app.frontend.models.ProductFilterOption
import com.app.frontend.models.ProductSortOption

class ProductRepository {
    private val apiService = RetrofitClient.instance

    suspend fun getAllProducts(
        offset: Int,
        sort: String? = null,
        direction: String? = null,
        filter: String? = null): Result<GetProductResponse> = try {
        val response = apiService.getAllProducts(
            offset,
            sort = sort,
            direction = direction,
            filter = filter?.lowercase()
        )
        Result.success(
            GetProductResponse(
                products = response.products,
                nextOffset = response.nextOffset,
                total = response.total
            )
        )
    } catch (e: Exception) {
        Log.e("ProductRepository", e.toString())
        Result.failure(e)
    }

    suspend fun getSearchedProducts(query: String): Result<List<Product>> = try {
        val response = apiService.getSearchedProducts(query)
        Result.success(response.products)
    } catch (e: Exception) {
        Log.e("ProductRepository", e.toString())
        Result.failure(e)
    }
}