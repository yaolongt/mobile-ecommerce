package com.app.frontend.viewmodels

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.frontend.models.Product
import com.app.frontend.repositories.ProductRepository
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlin.system.measureTimeMillis

class ProductViewModel(
    private val productRepository: ProductRepository = ProductRepository()
) : ViewModel() {
    private val _products = MutableStateFlow<List<Product>>(emptyList())
    val products: StateFlow<List<Product>> get() = _products.asStateFlow()

    private val _isLoading = mutableStateOf(false)
    val isLoading: Boolean get() = _isLoading.value

    private val _error = mutableStateOf<String?>(null)
    val error: String get() = _error.toString()

    private val _nextOffset = mutableStateOf(0)
    val nextOffset: Int get() = _nextOffset.value

    private var loadingStartTime = 0L
    private val MIN_LOADING_TIME = 2000L // 2 seconds in milliseconds

    fun fetchProducts() = viewModelScope.launch {
        _isLoading.value = true
        _error.value = null

        loadingStartTime = System.currentTimeMillis()

        try {
            val apiCall = async { productRepository.getAllProducts() }
            val elapsedTime = measureTimeMillis { apiCall.await() }

            // Delay for 2 seconds
            val remainingTime = MIN_LOADING_TIME - elapsedTime
            if (remainingTime > 0) {
                delay(remainingTime)
            }

            val result = apiCall.await()
            if (result.isSuccess) {
                _products.value = result.getOrNull()?.products ?: emptyList()
                Log.d("ProductViewModel", "Loaded ${_products.value.size} products")
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