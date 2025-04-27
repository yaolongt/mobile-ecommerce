package com.app.frontend.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Sort
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import com.app.frontend.viewmodels.ProductViewModel
import androidx.compose.runtime.setValue
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.max
import com.app.frontend.models.ProductCategory
import com.app.frontend.models.ProductFilterOption
import com.app.frontend.models.ProductSortOption
import com.app.frontend.models.getCategory

@Composable
fun ProductFilterSortBar(
    viewModel: ProductViewModel,
    modifier: Modifier = Modifier
) {
    var showFilterDialog by remember { mutableStateOf(false) }
    var showSortDialog by remember { mutableStateOf(false) }

    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Filter Button
        FilterChip(
            selected = viewModel.currentFilterOption != ProductFilterOption.ALL,
            onClick = { showFilterDialog = true },
            label = {
                Text(
                    text = when (viewModel.currentFilterOption) {
                        is ProductFilterOption.IN_STOCK -> "In Stock"
                        is ProductFilterOption.BY_CATEGORY -> viewModel.currentFilterOption.getCategory()?.value ?: "Filter"
                        else -> "Filter"
                    },
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.weight(1f, fill = false)
                )
            },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.List,
                    contentDescription = "Filter"
                )
            },
            modifier = Modifier.weight(1f)
        )

        // Sort Button
        FilterChip(
            selected = viewModel.currentSortOption != ProductSortOption.DEFAULT,
            onClick = { showSortDialog = true },
            label = {
                Text(
                    text = when (viewModel.currentSortOption) {
                        ProductSortOption.PRICE_LOW_TO_HIGH -> "Price: Low to High"
                        ProductSortOption.PRICE_HIGH_TO_LOW -> "Price: High to Low"
                        ProductSortOption.NAME_A_TO_Z -> "Name: A-Z"
                        ProductSortOption.NAME_Z_TO_A -> "Name: Z-A"
                        else -> "Sort"
                    },
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.weight(1f, fill = false)
                )
            },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Sort,
                    contentDescription = "Filter"
                )
            },
            modifier = Modifier.weight(1f)
        )

        // Clear Button (only visible when filters/sort are applied)
        if (viewModel.isFilterOrSortApplied) {
            TextButton(
                onClick = {
                    viewModel.clearFiltersAndSort()
                    viewModel.fetchProducts(resetForFilterSort = true)
                },
                modifier = Modifier.padding(start = 8.dp)
            ) {
                Text(
                    text = "Clear",
                    style = MaterialTheme.typography.bodySmall,
                    maxLines = 1,
                    overflow = TextOverflow.Visible
                )
            }
        }
    }

    // Filter Dialog
    if (showFilterDialog) {
        AlertDialog(
            onDismissRequest = { showFilterDialog = false },
            title = { Text("Filter Products") },
            text = {
                LazyColumn {
                    item {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    viewModel.applyFilterOption(ProductFilterOption.ALL)
                                    viewModel.fetchProducts(resetForFilterSort = true)
                                    showFilterDialog = false
                                }
                                .padding(vertical = 8.dp)
                        ) {
                            RadioButton(
                                selected = viewModel.currentFilterOption == ProductFilterOption.ALL,
                                onClick = {
                                    viewModel.applyFilterOption(ProductFilterOption.ALL)
                                    viewModel.fetchProducts(resetForFilterSort = true)
                                    showFilterDialog = false
                                }
                            )
                            Text("All Products", modifier = Modifier.padding(start = 8.dp))
                        }
                    }

                    item {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    viewModel.applyFilterOption(ProductFilterOption.IN_STOCK)
                                    viewModel.fetchProducts(resetForFilterSort = true)
                                    showFilterDialog = false
                                }
                                .padding(vertical = 8.dp)
                        ) {
                            RadioButton(
                                selected = viewModel.currentFilterOption == ProductFilterOption.IN_STOCK,
                                onClick = {
                                    viewModel.applyFilterOption(ProductFilterOption.IN_STOCK)
                                    viewModel.fetchProducts(resetForFilterSort = true)
                                    showFilterDialog = false
                                }
                            )
                            Text("In Stock", modifier = Modifier.padding(start = 8.dp))
                        }
                    }

                    item {
                        Text("Categories", style = MaterialTheme.typography.titleSmall)
                    }

                    val productCategory = ProductCategory.entries.toTypedArray()

                    items(productCategory.size) { index ->
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    viewModel.applyCategoryFilter(productCategory[index])
                                    viewModel.fetchProducts(resetForFilterSort = true)
                                    showFilterDialog = false
                                }
                                .padding(vertical = 8.dp)
                        ) {
                            RadioButton(
                                selected = viewModel.currentFilterOption is ProductFilterOption.BY_CATEGORY &&
                                        (viewModel.currentFilterOption as? ProductFilterOption.BY_CATEGORY)?.category == productCategory[index],
                                onClick = {
                                    viewModel.applyCategoryFilter(productCategory[index])
                                    viewModel.fetchProducts(resetForFilterSort = true)
                                    showFilterDialog = false
                                }
                            )
                            Text(text = productCategory[index].toString(), modifier = Modifier.padding(start = 8.dp))
                        }
                    }
                }
            },
            confirmButton = {
                TextButton(onClick = { showFilterDialog = false }) {
                    Text("Close")
                }
            }
        )
    }

    // Sort Dialog
    if (showSortDialog) {
        AlertDialog(
            onDismissRequest = { showSortDialog = false },
            title = { Text("Sort Products") },
            text = {
                Column {
                    ProductSortOption.entries.forEach { option ->
                        if (option != ProductSortOption.DEFAULT) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable {
                                        viewModel.applySort(option)
                                        viewModel.fetchProducts(resetForFilterSort = true)
                                        showSortDialog = false
                                    }
                                    .padding(vertical = 8.dp)
                            ) {
                                RadioButton(
                                    selected = viewModel.currentSortOption == option,
                                    onClick = {
                                        viewModel.applySort(option)
                                        viewModel.fetchProducts(resetForFilterSort = true)
                                        showSortDialog = false
                                    }
                                )
                                Text(
                                    text = when(option) {
                                        ProductSortOption.PRICE_LOW_TO_HIGH -> "Price: Low to High"
                                        ProductSortOption.PRICE_HIGH_TO_LOW -> "Price: High to Low"
                                        ProductSortOption.NAME_A_TO_Z -> "Name: A to Z"
                                        ProductSortOption.NAME_Z_TO_A -> "Name: Z to A"
                                        else -> ""
                                    },
                                    modifier = Modifier.padding(start = 8.dp)
                                )
                            }
                        }
                    }
                }
            },
            confirmButton = {
                TextButton(onClick = { showSortDialog = false }) {
                    Text("Close")
                }
            }
        )
    }
}