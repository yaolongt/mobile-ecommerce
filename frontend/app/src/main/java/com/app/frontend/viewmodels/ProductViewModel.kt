package com.app.frontend.viewmodels

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.frontend.models.Product
import com.app.frontend.models.ProductCategory
import com.app.frontend.models.ProductFilterOption
import com.app.frontend.models.ProductSortOption
import com.app.frontend.models.getCategory
import com.app.frontend.models.toApiString
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

    private val _currentSortOption = mutableStateOf(ProductSortOption.DEFAULT)
    val currentSortOption: ProductSortOption get() = _currentSortOption.value

    private val _currentFilterOption = mutableStateOf<ProductFilterOption>(ProductFilterOption.ALL)
    val currentFilterOption: ProductFilterOption get() = _currentFilterOption.value

    private val _isFilterOrSortApplied = mutableStateOf(false)
    val isFilterOrSortApplied: Boolean get() = _isFilterOrSortApplied.value

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

    fun clearError() {
        _error.value = null
    }

    private fun toggleLoading(isInitial: Boolean, load: Boolean) {
        if (isInitial) {
            _isLoading.value = load
        } else {
            _isPaginationLoading.value = load
        }
    }

    fun fetchProducts(isInitial: Boolean = false, resetForFilterSort: Boolean = false) = viewModelScope.launch {
        val shouldResetOffset = isInitial || _isRefreshing.value || resetForFilterSort
        val offset = if (shouldResetOffset) 0 else _nextOffset.value

        toggleLoading(isInitial, true)
        _error.value = null

        loadingStartTime = System.currentTimeMillis()

        try {
            val sortParams = _currentSortOption.value.toQueryParam().split(",")
            val apiCall = async {
                productRepository.getAllProducts(
                    offset = offset,
                    sort = sortParams.getOrElse(0) { null },
                    direction = sortParams.getOrElse(1) { null },
                    filter = if (_isFilterOrSortApplied.value) _currentFilterOption.value.toApiString() else null
                )
            }
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

                if (isInitial || resetForFilterSort) {
                    _products.value = productResult
                } else {
                    _products.value = _products.value + productResult
                }
            } else {
                _error.value = "Failed to fetch products: ${result.exceptionOrNull()?.message}"
            }
        } catch (e: Exception) {
            _error.value = "Unexpected error: ${e.localizedMessage}"
        } finally {
            toggleLoading(isInitial, false)
        }
    }

    private val _searchResults = MutableStateFlow<List<Product>>(emptyList())
    val searchResults: StateFlow<List<Product>> = _searchResults.asStateFlow()

    private val _isSearching = mutableStateOf(false)
    val isSearching: Boolean get() = _isSearching.value

    private val _isSearchActive = mutableStateOf(false)
    var isSearchActive: Boolean
        get() = _isSearchActive.value
        set(value) {
            _isSearchActive.value = value
            if (!value) {  // When setting to false, clear search results
                _searchResults.value = emptyList()
            }
        }

    fun searchProducts(query: String) = viewModelScope.launch {
        _isSearchActive.value = query.isNotEmpty() // Automatically set active state
        if (query.isEmpty()) {
            _searchResults.value = emptyList()
            return@launch
        }

        clearFiltersAndSort()

        try {
            val results = productRepository.getSearchedProducts(query)
            _searchResults.value = results.getOrDefault(emptyList())
        } catch (e: Exception) {
            _error.value = "Search failed: ${e.message}"
        }
    }

    fun clearSearch() {
        _searchResults.value = emptyList()
        _isSearchActive.value = false
        _products.value = emptyList()
        refresh()
    }

    fun applySort(sortOption: ProductSortOption) {
        _currentSortOption.value = sortOption
        _isFilterOrSortApplied.value = true
        fetchProducts(isInitial = true, resetForFilterSort = true)
    }

    fun applyFilterOption(filterOption: ProductFilterOption) {
        _currentFilterOption.value = filterOption
        _isFilterOrSortApplied.value = true
        fetchProducts(resetForFilterSort = true)
    }

    fun applyCategoryFilter(category: ProductCategory) {
        _currentFilterOption.value = ProductFilterOption.BY_CATEGORY(category)
        _isFilterOrSortApplied.value = true
        fetchProducts(resetForFilterSort = true)
    }

    fun clearFiltersAndSort() {
        _currentSortOption.value = ProductSortOption.DEFAULT
        _currentFilterOption.value = ProductFilterOption.ALL
        _isFilterOrSortApplied.value = false
    }
}