package com.app.frontend.models

data class GetProductResponse (
    val products: List<Product>,
    val nextOffset: Int,
    val total: Int
)

data class GetSearchProductResponse (
    val products: List<Product>,
)

data class UpdateProductResponse (
    val message: String?,
    val error: String?
)

data class GetProductByIdResponse (val product: Product)

