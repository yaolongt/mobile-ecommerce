package com.app.kotlin_oauth.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.app.kotlin_oauth.models.Product

@Composable
fun ProductDisplayList(
    products: List<Product>,
    onLoadMore: () -> Unit = {},
    onProductClick: (Int) -> Unit = {},
) {
    val listState = rememberLazyListState()

    // Track loading state
    var isLoading by remember { mutableStateOf(false) }

    // Pagination detection
    LaunchedEffect(listState) {
        snapshotFlow { listState.layoutInfo }
            .collect { layoutInfo ->
                val lastVisibleItem = layoutInfo.visibleItemsInfo.lastOrNull()
                if (lastVisibleItem?.index == layoutInfo.totalItemsCount - 1 && !isLoading) {
                    onLoadMore()
                }
            }
    }

    LazyColumn(
        state = listState,
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(products.size) { index ->
            val product = products[index]
            ProductDisplayCard(
                productName = product.name,
                price = product.price.toString(),
                images = product.images,
                onClick = { onProductClick(product.id) }
            )
        }
    }
}

// Preview
@Preview
@Composable
private fun ProductDisplayListPreview() {
    val sampleProducts = List(10) { index ->
        Product(
            id = index,
            name = "Product $index",
            price = (12.34).toFloat(),
            inventory = 1,
            description = "Some description",
        )
    }

    ProductDisplayList(
        products = sampleProducts,
        onProductClick = { /* Handle click */ },
    )
}