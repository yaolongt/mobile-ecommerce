package com.app.frontend.activities

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.app.frontend.components.ProductDetail
import com.app.frontend.models.Product

class ProductDetailActivity : BaseActivity() {
    override val showNavigationBar: Boolean = false
    override val showTopBar: Boolean = true

    @Composable
    override fun ActivityContent(modifier: Modifier) {
        val product = intent.getSerializableExtra("product") as Product

        Column(
            modifier = modifier,
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            ProductDetail(product = product)
        }
    }

    @Preview
    @Composable
    override fun PreviewActivityContent() {
        ActivityContent()
    }
}
