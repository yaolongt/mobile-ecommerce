package com.app.frontend.models

enum class ProductSortOption(val field: String, val direction: String) {
    DEFAULT("", ""),
    PRICE_LOW_TO_HIGH("price", "asc"),
    PRICE_HIGH_TO_LOW("price", "desc"),
    NAME_A_TO_Z("name", "asc"),
    NAME_Z_TO_A("name", "desc");

    // Convert to query parameter string for API call (e.g. "price,asc")
    fun toQueryParam(): String = if (this == DEFAULT) "" else "$field,$direction"
}

sealed class ProductFilterOption {
    object ALL : ProductFilterOption()
    object IN_STOCK : ProductFilterOption()
    data class BY_CATEGORY(val category: ProductCategory) : ProductFilterOption()
}

fun ProductFilterOption.getCategory(): ProductCategory? {
    return when (this) {
        is ProductFilterOption.BY_CATEGORY -> this.category
        else -> null
    }
}

fun ProductFilterOption.toApiString(): String {
    return when (this) {
        ProductFilterOption.ALL -> ""
        ProductFilterOption.IN_STOCK -> "in_stock"
        is ProductFilterOption.BY_CATEGORY -> this.category.value
    }
}