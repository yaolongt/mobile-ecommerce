package com.app.frontend.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.app.frontend.models.Product
import com.app.frontend.viewmodels.ProductViewModel
import com.app.frontend.models.ProductCategory

@Composable
fun ProductDisplayList(
    viewModel: ProductViewModel = viewModel(),
    onProductClick: (Int) -> Unit = {},
) {
    val sampleProducts = List(10) { index ->
        Product(
            id = index,
            name = "Product $index",
            price = (12.34).toFloat(),
            inventory = 1,
            category = ProductCategory.Electronics,
            description = "Some description"
        )
    }

    val state by viewModel.products.collectAsState()
    val listState = rememberLazyListState()

    // Pagination detection
    LaunchedEffect(listState) {
        snapshotFlow { listState.layoutInfo.visibleItemsInfo.lastOrNull()?.index }
            .collect { lastVisibleIndex ->
                if (lastVisibleIndex!! >= state.size - 1 && !viewModel.isLoading) {
                    viewModel.fetchProducts()
                }
            }
    }

    val products = sampleProducts

    LazyColumn(
        state = listState,
        modifier = Modifier.fillMaxSize(),
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

        item {
            if (viewModel.isLoading) {
                CircularProgressIndicator()
            }
        }
    }
}

// Preview
@Preview
@Composable
private fun ProductDisplayListPreview() {
    ProductDisplayList(
        onProductClick = { /* Handle click */ },
    )
}