package com.app.frontend.components

import android.content.Context
import android.content.Intent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SearchBar
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
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
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.debounce

@OptIn(ExperimentalMaterial3Api::class, FlowPreview::class)
@Composable
fun ProductDisplayList(
    viewModel: ProductViewModel = viewModel(),
) {
    val products by viewModel.products.collectAsState()
    val searchResults by viewModel.searchResults.collectAsState()
    val listState = rememberLazyListState()
    val searchQuery = rememberSaveable { mutableStateOf("") }
    val pullToRefreshState = rememberPullToRefreshState()
    val ctx = LocalContext.current

    fun onCardClicked(product: Product) {
        val intent = Intent(ctx, ProductDetailActivity::class.java)
        intent.putExtra("product", product)
        ctx.startActivity(intent)
    }
    val searchQueryFlow = remember { MutableStateFlow("") }

    // Debounce the search queries to prevent excessive API calling
    LaunchedEffect(searchQueryFlow) {
        searchQueryFlow
            .debounce(500)
            .collect { query ->
                // Only search if the query isn't empty
                if (query.isNotEmpty()) {
                    viewModel.searchProducts(query)
                }
            }
    }

    // Pagination detection
    LaunchedEffect(listState) {
        snapshotFlow { listState.layoutInfo.visibleItemsInfo.lastOrNull()?.index }
            .collect { lastVisibleIndex ->
                if (lastVisibleIndex!! >= products.size - 1
                    && !viewModel.isLoading
                    && !viewModel.isPaginationLoading
                    && !viewModel.isSearchActive) {
                    viewModel.fetchProducts()
                }
            }
    }

    Column {
        // Search Bar
        SearchBar(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            query = searchQuery.value,
            onQueryChange = { query ->
                searchQuery.value = query
                searchQueryFlow.value = query // Update flow instead of calling search directly
                viewModel.isSearchActive = query.isNotEmpty()
                if (query.isEmpty()) viewModel.clearSearch()
            },
            onSearch = { viewModel.searchProducts(searchQuery.value) },
            active = false, // prevent expansion of search dropdown
            onActiveChange = { },
            placeholder = { Text("Search products...") }
        ) {}

        // Product List
        PullToRefreshBox(
            state = pullToRefreshState,
            isRefreshing = viewModel.isRefreshing,
            onRefresh = {
                if (!viewModel.isSearchActive) {  // Only allow refresh when not in search mode
                    viewModel.refresh()
                } },
        ) {
            val itemsToShow = if (viewModel.isSearchActive) searchResults else products

            LazyColumn(
                state = listState,
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // Show loading state
                if (viewModel.isLoading && itemsToShow.isEmpty()) {
                    items(10) {
                        ProductDisplayCardSkeleton(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp)
                        )
                    }
                }
                // Show empty state
                else if (!viewModel.isLoading && !viewModel.isSearching && itemsToShow.isEmpty()) {
                    item {
                        Text(
                            if (searchResults.isEmpty()) "No matching products found"
                            else "No products available",
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp)
                        )
                    }
                }
                // Show products
                else {
                    items(itemsToShow.size) { index ->
                        val product = itemsToShow[index]
                        ProductDisplayCard(
                            product = product,
                            onClick = { onCardClicked(product) },
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp)
                        )
                    }

                    // Show pagination loading only when not searching
                    if (viewModel.isPaginationLoading &&
                        itemsToShow.isNotEmpty() &&
                        !viewModel.isSearchActive) {
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
    }
}

// Preview
@Preview
@Composable
private fun ProductDisplayListPreview() {
    ProductDisplayList()
}