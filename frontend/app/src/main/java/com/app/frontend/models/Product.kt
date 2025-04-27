package com.app.frontend.models

import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.google.gson.annotations.SerializedName
import java.io.Serializable
import java.lang.reflect.Type

data class Product(
    val id: Int,
    val name: String,
    val price: Float,
    val inventory: Int,
    val category: ProductCategory,
    val description: String? = null,
    val images: List<String>? = null,
    val createdAt: String? = null,
    val updatedAt: String? = null
): Serializable {
    class Deserializer : JsonDeserializer<Product> {
        override fun deserialize(
            json: JsonElement?,
            typeOfT: Type?,
            context: JsonDeserializationContext?
        ): Product {
            val jsonObject = json!!.asJsonObject
            var images: List<String>? = null
            if (jsonObject.has("images") && !jsonObject.get("images").isJsonNull) {
                images = jsonObject.getAsJsonArray("images").map { it.asString }
            }
            return Product(
                id = jsonObject.get("id").asInt,
                name = jsonObject.get("name").asString,
                price = jsonObject.get("price").asFloat,
                inventory = jsonObject.get("inventory").asInt,
                category = ProductCategory.fromString(jsonObject.get("category").asString),
                description = jsonObject.get("description").asString,
                images = images,
                createdAt = jsonObject.get("created_at").asString,
                updatedAt = jsonObject.get("updated_at").asString
            )
        }

        companion object {
            fun fromDTO(dto: ProductDTO): Product {
                return dto.toProduct()
            }
        }
    }
}

data class ProductDTO(
    @SerializedName("id") val id: Int,
    @SerializedName("name") val name: String,
    @SerializedName("price") val price: Float,
    @SerializedName("inventory") val inventory: Int,
    @SerializedName("category") val category: String,
    @SerializedName("description") val description: String? = null,
    @SerializedName("images") val images: List<String>? = null,
    @SerializedName("created_at") val createdAt: String? = null,
    @SerializedName("updated_at") val updatedAt: String? = null
) {
    // Conversion to domain model
    fun toProduct(): Product {
        return Product(
            id = id,
            name = name,
            price = price,
            inventory = inventory,
            category = ProductCategory.fromString(category),
            description = description,
            images = images,
            createdAt = createdAt,
            updatedAt = updatedAt
        )
    }

    companion object {
        // Conversion from domain model
        fun fromProduct(product: Product): ProductDTO {
            return ProductDTO(
                id = product.id,
                name = product.name,
                price = product.price,
                inventory = product.inventory,
                category = product.category.value,
                description = product.description,
                images = product.images,
                createdAt = product.createdAt,
                updatedAt = product.updatedAt
            )
        }
    }
}