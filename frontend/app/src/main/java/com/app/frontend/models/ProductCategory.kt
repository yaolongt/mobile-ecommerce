package com.app.frontend.models

enum class ProductCategory(val value: String) {
    ELECTRONICS("Electronics"),
    CLOTHING("Clothing"),
    HOME_APPLIANCES("Home Appliances"),
    BOOKS("Books"),
    TOYS("Toys"),
    MISC("Misc"); // Fallback value

    companion object {
        fun fromString(value: String): ProductCategory {
            return try {
                valueOf(value)
            } catch (_: IllegalArgumentException) {
                MISC
            }
        }
    }
}
