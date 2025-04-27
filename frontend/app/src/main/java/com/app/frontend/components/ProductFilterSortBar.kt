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
        FilterChip(
            selected = viewModel.currentFilterOption != ProductFilterOption.ALL,
            onClick = { showFilterDialog = true },
            label = { FilterLabel(viewModel.currentFilterOption) },
            leadingIcon = { FilterIcon() },
            modifier = Modifier.weight(1f)
        )

        FilterChip(
            selected = viewModel.currentSortOption != ProductSortOption.DEFAULT,
            onClick = { showSortDialog = true },
            label = { SortLabel(viewModel.currentSortOption) },
            leadingIcon = { SortIcon() },
            modifier = Modifier.weight(1f)
        )

        ClearButton(
            isVisible = viewModel.isFilterOrSortApplied,
            onClick = {
                viewModel.clearFiltersAndSort()
                viewModel.fetchProducts(resetForFilterSort = true)
            }
        )
    }

    FilterDialog(
        showDialog = showFilterDialog,
        currentFilter = viewModel.currentFilterOption,
        onDismiss = { showFilterDialog = false },
        onFilterSelected = { filter ->
            viewModel.applyFilterOption(filter)
            viewModel.fetchProducts(resetForFilterSort = true)
            showFilterDialog = false
        }
    )

    SortDialog(
        showDialog = showSortDialog,
        currentSort = viewModel.currentSortOption,
        onDismiss = { showSortDialog = false },
        onSortSelected = { sort ->
            viewModel.applySort(sort)
            viewModel.fetchProducts(resetForFilterSort = true)
            showSortDialog = false
        }
    )
}

@Composable
private fun FilterLabel(currentFilter: ProductFilterOption) {
    Text(
        text = when (currentFilter) {
            is ProductFilterOption.IN_STOCK -> "In Stock"
            is ProductFilterOption.BY_CATEGORY -> currentFilter.getCategory()?.value ?: "Filter"
            else -> "Filter"
        },
        maxLines = 1,
        overflow = TextOverflow.Ellipsis,
    )
}

@Composable
private fun FilterIcon() {
    Icon(
        imageVector = Icons.Default.List,
        contentDescription = "Filter"
    )
}

@Composable
private fun SortLabel(currentSort: ProductSortOption) {
    Text(
        text = when (currentSort) {
            ProductSortOption.PRICE_LOW_TO_HIGH -> "Price: Low to High"
            ProductSortOption.PRICE_HIGH_TO_LOW -> "Price: High to Low"
            ProductSortOption.NAME_A_TO_Z -> "Name: A-Z"
            ProductSortOption.NAME_Z_TO_A -> "Name: Z-A"
            else -> "Sort"
        },
        maxLines = 1,
        overflow = TextOverflow.Ellipsis,
    )
}

@Composable
private fun SortIcon() {
    Icon(
        imageVector = Icons.Default.Sort,
        contentDescription = "Sort"
    )
}

@Composable
private fun ClearButton(
    isVisible: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    if (isVisible) {
        TextButton(
            onClick = onClick,
            modifier = modifier.padding(start = 8.dp)
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

@Composable
private fun FilterDialog(
    showDialog: Boolean,
    currentFilter: ProductFilterOption,
    onDismiss: () -> Unit,
    onFilterSelected: (ProductFilterOption) -> Unit
) {
    if (showDialog) {
        AlertDialog(
            onDismissRequest = onDismiss,
            title = { Text("Filter Products") },
            text = {
                FilterDialogContent(
                    currentFilter = currentFilter,
                    onFilterSelected = onFilterSelected
                )
            },
            confirmButton = {
                TextButton(onClick = onDismiss) {
                    Text("Close")
                }
            }
        )
    }
}

@Composable
private fun FilterDialogContent(
    currentFilter: ProductFilterOption,
    onFilterSelected: (ProductFilterOption) -> Unit
) {
    LazyColumn {
        item { FilterOptionRow(
            option = ProductFilterOption.ALL,
            label = "All Products",
            currentFilter = currentFilter,
            onSelected = onFilterSelected
        ) }

        item { FilterOptionRow(
            option = ProductFilterOption.IN_STOCK,
            label = "In Stock",
            currentFilter = currentFilter,
            onSelected = onFilterSelected
        ) }

        item { Text("Categories", style = MaterialTheme.typography.titleSmall) }

        val productCategory = ProductCategory.entries.toTypedArray()

        items(productCategory.size) { index ->
            FilterOptionRow(
                option = ProductFilterOption.BY_CATEGORY(productCategory[index]),
                label = productCategory[index].value,
                currentFilter = currentFilter,
                onSelected = onFilterSelected
            )
        }
    }
}

@Composable
private fun FilterOptionRow(
    option: ProductFilterOption,
    label: String,
    currentFilter: ProductFilterOption,
    onSelected: (ProductFilterOption) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .fillMaxWidth()
            .clickable { onSelected(option) }
            .padding(vertical = 8.dp)
    ) {
        RadioButton(
            selected = currentFilter == option ||
                    (option is ProductFilterOption.BY_CATEGORY &&
                            currentFilter is ProductFilterOption.BY_CATEGORY &&
                            currentFilter.category == option.category),
            onClick = { onSelected(option) }
        )
        Text(label, modifier = Modifier.padding(start = 8.dp))
    }
}

@Composable
private fun SortDialog(
    showDialog: Boolean,
    currentSort: ProductSortOption,
    onDismiss: () -> Unit,
    onSortSelected: (ProductSortOption) -> Unit
) {
    if (showDialog) {
        AlertDialog(
            onDismissRequest = onDismiss,
            title = { Text("Sort Products") },
            text = {
                SortDialogContent(
                    currentSort = currentSort,
                    onSortSelected = onSortSelected
                )
            },
            confirmButton = {
                TextButton(onClick = onDismiss) {
                    Text("Close")
                }
            }
        )
    }
}

@Composable
private fun SortDialogContent(
    currentSort: ProductSortOption,
    onSortSelected: (ProductSortOption) -> Unit
) {
    Column {
        ProductSortOption.entries
            .filter { it != ProductSortOption.DEFAULT }
            .forEach { option ->
                SortOptionRow(
                    option = option,
                    currentSort = currentSort,
                    onSelected = onSortSelected
                )
            }
    }
}

@Composable
private fun SortOptionRow(
    option: ProductSortOption,
    currentSort: ProductSortOption,
    onSelected: (ProductSortOption) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .fillMaxWidth()
            .clickable { onSelected(option) }
            .padding(vertical = 8.dp)
    ) {
        RadioButton(
            selected = currentSort == option,
            onClick = { onSelected(option) }
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
