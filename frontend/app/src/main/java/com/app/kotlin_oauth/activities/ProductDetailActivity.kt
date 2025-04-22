package com.app.kotlin_oauth.activities

import androidx.compose.runtime.Composable
import com.app.kotlin_oauth.pages.ProductDetailPage

class ProductDetailActivity : BaseActivity() {
    override val showNavigationBar: Boolean = false

    @Composable
    override fun ActivityContent() {
        val productId = intent.getIntExtra("productId", -1)
        ProductDetailPage(productId)
    }
}
