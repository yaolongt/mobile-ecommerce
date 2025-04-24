package com.app.frontend.models

data class GetAllProductResponse (
    val products: List<Product>,
    val nextOffset: Int,
    val total: Int
)