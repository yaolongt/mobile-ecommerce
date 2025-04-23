package com.app.kotlin_oauth.pages

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.app.kotlin_oauth.components.ProductDetailPager

@Composable
fun ProductDetailPage(id: Int = -1) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        ProductDetailPager()
    }
}

@Preview
@Composable
private fun PreviewProductDetailPage() {
    ProductDetailPage()
}