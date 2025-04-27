package com.app.frontend.activities

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.app.frontend.components.AppTopBar
import com.app.frontend.components.EditProductDetail
import com.app.frontend.components.ProductDetail
import com.app.frontend.components.ProductDetailSkeleton
import com.app.frontend.viewmodels.ProductViewModel

class ProductDetailActivity : BaseActivity() {
    override val showNavigationBar: Boolean = false

    @Composable
    override fun ActivityContent(modifier: Modifier) {
        val viewModel: ProductViewModel = viewModel()
        var productId = intent.getIntExtra("productId", -1)
        val product by viewModel.product.collectAsState()
        var showEditDialog by remember { mutableStateOf(false) }

        LaunchedEffect(productId) {
            viewModel.fetchProduct(productId)
        }

        Scaffold(
            topBar = {
                AppTopBar("Product Detail", onBackPressed = { finish() }, onEditPressed = { showEditDialog = true })
            },
        ) { innerPadding ->
            Column(
                modifier = modifier.padding(innerPadding),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                if (viewModel.isLoading || product == null) {
                    ProductDetailSkeleton()
                } else {
                    ProductDetail(product = product!!)
                }
            }
        }

        // Edit Dialog
        if (showEditDialog && product != null) {
            EditProductDetail(
                product = product!!,
                onDismiss = { showEditDialog = false },
                onSave = { updatedProduct ->
                    viewModel.updateProduct(updatedProduct)
                    showEditDialog = false
                    setResult(RESULT_OK)
                }
            )
        }
    }

    @Preview
    @Composable
    override fun PreviewActivityContent() {
        ActivityContent()
    }
}
