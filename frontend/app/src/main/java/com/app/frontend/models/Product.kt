package com.app.frontend.models

sealed class ProductCategory(val value: String) {
    data object Electronics : ProductCategory("electronics")
    data object Clothing : ProductCategory("clothing")
    data object HomeAppliances : ProductCategory("home_appliances")
    data object Books : ProductCategory("books")
    data object Toys : ProductCategory("toys")
    data object Misc : ProductCategory("misc")
}
data class Product(
    val id: Int,
    val name: String,
    val price: Float,
    val inventory: Int,
    val category: ProductCategory,
    val description: String? = null,
    val images: Array<String>? = null,
    val createdAt: String? = null,
    val updatedAt: String? = null
)