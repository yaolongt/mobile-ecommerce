package com.app.frontend.components

import android.content.Intent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.app.frontend.activities.ProductDetailActivity
import com.app.frontend.models.Product
import com.app.frontend.viewmodels.ProductViewModel
import com.app.frontend.models.ProductCategory

@Composable
fun ProductDisplayList(
    viewModel: ProductViewModel = viewModel(),
) {
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

    LazyColumn(
        state = listState,
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // Show skeletons when loading initial data
        if (viewModel.isLoading || state.isEmpty()) {
            items(10) { // Show 10 skeleton items
                ProductDisplayCardSkeleton(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                )
            }
        }
        // Show actual products when loaded
        else {
            items(state.size) { index ->
                val product = state[index]
                val ctx = LocalContext.current
                ProductDisplayCard(
                    product = product,
                    onClick = {
                        val intent = Intent(ctx, ProductDetailActivity::class.java)
                        intent.putExtra("product", product)
                        ctx.startActivity(intent)
                    }
                )
            }

            // Show loading indicator at bottom during pagination
            if (viewModel.isLoading && state.isNotEmpty()) {
                item {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        CircularProgressIndicator()
                    }
                }
            }
        }
    }
}

// Preview
@Preview
@Composable
private fun ProductDisplayListPreview() {
    ProductDisplayList()
}