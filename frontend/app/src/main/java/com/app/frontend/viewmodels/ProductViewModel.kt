package com.app.frontend.viewmodels

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

class ProductViewModel(
    private val productRepository: ProductRepository = ProductRepository()
) : ViewModel() {
    private val _products = MutableStateFlow<List<Product>>(emptyList())
    val products: StateFlow<List<Product>> get() = _products.asStateFlow()

    private val _isLoading = mutableStateOf(false)
    val isLoading: Boolean get() = _isLoading.value

    private val _error = mutableStateOf<String?>(null)
    val error: String get() = _error.toString()

    fun fetchProducts() = viewModelScope.launch {
        _isLoading.value = true
        _error.value = null

        try {
            val result = productRepository.getAllProducts()
            if (result.isSuccess) {
                _products.value = result.getOrNull()?.products ?: emptyList()
                Log.d("ProductViewModel", products.value.toString())
            } else {
                _error.value = "Failed to fetch products: ${result.exceptionOrNull()?.message}"
            }
        } catch (e: Exception) {
            _error.value = "Unexpected error: ${e.localizedMessage}"
        } finally {
            _isLoading.value = false
        }
    }

    fun refresh() {
        fetchProducts()
    }}