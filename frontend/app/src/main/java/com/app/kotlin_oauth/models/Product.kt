package com.app.kotlin_oauth.models

data class Product(
    val id: Int,
    val name: String,
    val price: Float,
    val inventory: Int,
    val description: String,
    val images: Array<String>? = null
)