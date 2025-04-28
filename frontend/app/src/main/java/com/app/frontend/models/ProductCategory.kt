package com.app.frontend.models

enum class ProductCategory(private val displayValue: String) {
    ELECTRONICS("Electronics"),
    CLOTHING("Clothing"),
    HOME_APPLIANCES("Home Appliances"),
    BOOKS("Books"),
    TOYS("Toys"),
    MISC("Misc"); // Fallback value

    val value: String
        get() = displayValue.replaceFirstChar {
            if (it.isLowerCase()) it.titlecase()
            else it.toString()
        }

    companion object {
        fun fromString(value: String): ProductCategory {
            return ProductCategory.entries.firstOrNull() {
                it.name.equals(value, ignoreCase = true) ||
                        it.displayValue.equals(value, ignoreCase = true)
            } ?: MISC
        }
    }
}
