package com.app.kotlin_oauth.activities

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.app.kotlin_oauth.pages.ProductDetailPage

class ProductDetailActivity : BaseActivity() {
    override val showNavigationBar: Boolean = false
    override val showTopBar: Boolean = true

    @Composable
    override fun ActivityContent(modifier: Modifier) {
        val productId = intent.getIntExtra("productId", -1)

        Column(
            modifier = modifier
        ) {
            ProductDetailPage(productId)
        }
    }

    @Preview
    @Composable
    private fun PreviewProductDetailActivity() {
        ProductDetailPage()
    }
}
