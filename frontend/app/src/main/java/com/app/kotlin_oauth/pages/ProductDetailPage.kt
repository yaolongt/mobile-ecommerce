package com.app.kotlin_oauth.pages

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview

@Composable
fun ProductDetailPage(id: Int = -1) {
    Text("product details page$id")
}

@Preview
@Composable
private fun PreviewProductDetailPage() {
    ProductDetailPage()
}