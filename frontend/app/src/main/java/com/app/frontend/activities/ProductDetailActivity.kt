package com.app.frontend.activities

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.app.frontend.components.AppTopBar
import com.app.frontend.components.ProductDetail
import com.app.frontend.models.Product

class ProductDetailActivity : BaseActivity() {
    override val showNavigationBar: Boolean = false

    @Composable
    override fun ActivityContent(modifier: Modifier) {
        val product = intent.getSerializableExtra("product") as Product

        Scaffold(
            topBar = {
                AppTopBar("Product Detail", onBackPressed = { finish() }, onEditPressed = {})
            },
        ) { innerPadding ->
            Column(
                modifier = modifier.padding(innerPadding),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                ProductDetail(product = product)
            }
        }
    }

    @Preview
    @Composable
    override fun PreviewActivityContent() {
        ActivityContent()
    }
}
