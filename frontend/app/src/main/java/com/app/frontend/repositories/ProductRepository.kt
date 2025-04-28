package com.app.frontend.repositories

import android.util.Log
import androidx.compose.ui.text.toLowerCase
import com.app.frontend.api.RetrofitClient
import com.app.frontend.models.GetProductResponse
import com.app.frontend.models.GetSearchProductResponse
import com.app.frontend.models.Product
import com.app.frontend.models.ProductDTO
import com.app.frontend.models.ProductFilterOption
import com.app.frontend.models.ProductSortOption
import okhttp3.MultipartBody

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

    suspend fun getProductById(id: Int): Result<Product> = try {
        val response = apiService.getProductById(id)
        Result.success(response.product)
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

    suspend fun updateProduct(product: Product): Result<Boolean> {
        return try {
            val request = ProductDTO(
                id = product.id,
                name = product.name,
                price = product.price,
                inventory = product.inventory,
                category = product.category.value.lowercase(),
                description = product.description
            )

            val response = apiService.updateProduct(request)

            if (response.error?.isEmpty() != false) {
                Result.success(true)
            } else {
                val err = response.error.toString()
                Result.failure(Exception("API error: $err"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun uploadImages(productId: Int, images: List<MultipartBody.Part>): Result<List<String>> {
        return try {
            val response = apiService.uploadProduct(productId, images)
            if (response.error?.isEmpty() != false) {
                Result.success(response.urls!!)
            } else {
                val err = response.error.toString()
                Result.failure(Exception("API error: $err"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}