package com.app.frontend.viewmodels

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.frontend.models.Product
import com.app.frontend.repositories.ProductRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

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

    fun clearError() {
        _error.value = null
    }
}
