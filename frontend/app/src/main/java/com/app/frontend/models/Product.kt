package com.app.frontend.models

import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
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
) {
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
    }
}