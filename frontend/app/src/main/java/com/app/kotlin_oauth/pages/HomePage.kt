package com.app.kotlin_oauth.pages

import android.content.Intent
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import com.app.kotlin_oauth.activities.ProductDetailActivity
import com.app.kotlin_oauth.components.ProductDisplayList
import com.app.kotlin_oauth.models.Product

@Composable
fun HomePage() {
    val sampleProducts = List(10) { index ->
        Product(
            id = index,
            name = "Product $index",
            price = (12.34).toFloat(),
            inventory = 1,
            description = "Some description",
        )
    }

    val ctx = LocalContext.current

    ProductDisplayList(
        products = sampleProducts,
        onProductClick = {
            val intent = Intent(ctx, ProductDetailActivity::class.java)
            intent.putExtra("productId", it)
            ctx.startActivity(intent)
        },
    )
}

@Preview
@Composable
private fun PreviewHomePage() {
    HomePage()
}