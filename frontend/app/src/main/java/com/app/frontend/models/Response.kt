package com.app.frontend.models

data class GetProductResponse (
    val products: List<Product>,
    val nextOffset: Int,
    val total: Int
)

data class GetSearchProductResponse (
    val products: List<Product>,
)