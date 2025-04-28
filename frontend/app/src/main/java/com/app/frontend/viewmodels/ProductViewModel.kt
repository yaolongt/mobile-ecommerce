package com.app.frontend.viewmodels

import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.frontend.models.Product
import com.app.frontend.repositories.ProductRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File
import java.io.FileOutputStream

class ProductViewModel(
    private val productRepository: ProductRepository = ProductRepository()
) : ViewModel() {
    // Product state
    private val _product = MutableStateFlow<Product?>(null)
    val product: StateFlow<Product?> get() = _product.asStateFlow()

    // Loading state
    private val _isLoading = mutableStateOf(false)
    val isLoading: Boolean get() = _isLoading.value

    // Error state
    private val _error = mutableStateOf<String?>(null)
    val error: String? get() = _error.value

    fun fetchProduct(id: Int) = viewModelScope.launch {
        _isLoading.value = true
        _error.value = null

        try {
            val result = productRepository.getProductById(id)
            if (result.isSuccess) {
                _product.value = result.getOrNull()
            } else {
                _error.value = result.exceptionOrNull()?.message ?: "Failed to load product"
            }
        } catch (e: Exception) {
            _error.value = e.message ?: "An unexpected error occurred"
        } finally {
            _isLoading.value = false
        }
    }

    fun updateProduct(updatedProduct: Product) = viewModelScope.launch {
        _error.value = null
        _isLoading.value = true

        try {
            val result = productRepository.updateProduct(updatedProduct)
            if (result.isSuccess) {
                _product.value = updatedProduct
            } else {
                val errorMessage = result.exceptionOrNull()?.message ?: "Update failed"
                _error.value = errorMessage
            }
        } catch (e: Exception) {
            val errorMessage = e.message ?: "An unexpected error occurred"
            _error.value = errorMessage
        } finally {
            _isLoading.value = false
        }
    }

    fun uploadImages(productId: Int, imagesUri: List<Uri>, context: Context) = viewModelScope.launch {
        _error.value = null
        _isLoading.value = true

        try {
            // Convert URIs to MultipartBody.Part
            val images = imagesUri.mapNotNull { uri ->
                context.contentResolver.openInputStream(uri)?.use { inputStream ->
                    // Create temporary file
                    val file = File.createTempFile(
                        "upload_${System.currentTimeMillis()}",
                        ".jpg",
                        context.cacheDir
                    ).apply {
                        deleteOnExit()
                    }

                    FileOutputStream(file).use { outputStream ->
                        inputStream.copyTo(outputStream)
                    }

                    // Create multipart part
                    MultipartBody.Part.createFormData(
                        "images",
                        file.name,
                        file.asRequestBody("image/jpeg".toMediaTypeOrNull())
                    )
                }
            }

            if (images.isEmpty()) {
                throw IllegalArgumentException("No valid images to upload")
            }

            val result = productRepository.uploadImages(productId, images)
            if (result.isSuccess) {
                val imagesUrls = result.getOrNull() ?: emptyList()
              _product.value = _product.value?.copy(images = imagesUrls)
            } else {
                Log.d("ProductViewModel", "$productId, ${result}")
                val errorMessage = result.exceptionOrNull()?.message ?: "Update failed"
                _error.value = errorMessage
            }
        } catch (e: Exception) {
            Log.d("ProductViewModel", "Uploading images for product $productId, ${e.message}")
            val errorMessage = e.message ?: "An unexpected error occurred"
            _error.value = errorMessage
        } finally {
            _isLoading.value = false
        }
    }

    fun clearError() {
        _error.value = null
    }
}
