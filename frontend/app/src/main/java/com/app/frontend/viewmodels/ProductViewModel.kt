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

    private val _isRefreshing = mutableStateOf(false)
    val isRefreshing: Boolean get() = _isRefreshing.value

    private val _isPaginationLoading = mutableStateOf(false)
    val isPaginationLoading: Boolean get() = _isPaginationLoading.value

    private val _error = mutableStateOf<String?>(null)

    private val _nextOffset = mutableStateOf(0)

    private val _total = mutableStateOf(0)

    private var loadingStartTime = 0L
    private val MIN_LOADING_TIME = 2000L // 2 seconds in milliseconds

    init {
        fetchProducts(true)
    }

    fun refresh() {
        _isRefreshing.value = true
        fetchProducts(true)
        _isRefreshing.value = false
    }

    private fun toggleLoading(isInitial: Boolean, load: Boolean) {
        if (isInitial) {
            _isLoading.value = load
        } else {
            _isPaginationLoading.value = load
        }
    }

    fun fetchProducts(isInitial: Boolean = false) = viewModelScope.launch {
        toggleLoading(isInitial, true)
        _error.value = null

        loadingStartTime = System.currentTimeMillis()

        try {
            val offset = if (isInitial || _isRefreshing.value) 0 else _nextOffset.value
            val apiCall = async { productRepository.getAllProducts(offset) }
            val elapsedTime = measureTimeMillis { apiCall.await() }

            // Delay for 2 seconds to prevent skeleton flickering
            val remainingTime = MIN_LOADING_TIME - elapsedTime
            if (remainingTime > 0) {
                delay(remainingTime)
            }


            val result = apiCall.await()
            if (result.isSuccess) {
                _nextOffset.value = result.getOrNull()?.nextOffset ?: 0
                _total.value = result.getOrNull()?.total ?: 0

                val productResult = result.getOrNull()?.products ?: emptyList()

                if (isInitial) {
                    _products.value = productResult
                } else {
                    _products.value = _products.value + productResult
                }

                Log.d("ProductViewModel", "Loaded ${_products.value.size} products")
            } else {
                _error.value = "Failed to fetch products: ${result.exceptionOrNull()?.message}"
            }
        } catch (e: Exception) {
            _error.value = "Unexpected error: ${e.localizedMessage}"
        } finally {
            toggleLoading(isInitial, false)
        }
    }
}