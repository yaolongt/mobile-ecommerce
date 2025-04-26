package com.app.frontend.components

import android.util.Log
import android.widget.Toast
import androidx.compose.runtime.getValue
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SuggestionChip
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.app.frontend.R
import com.app.frontend.models.Product
import com.app.frontend.models.ProductCategory
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import coil3.compose.AsyncImage
import java.net.URLEncoder

@Composable
fun ProductDetail(product: Product) {
    var scrollState = rememberScrollState()
    Column(
        modifier = Modifier.verticalScroll(scrollState).fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(10.dp),
    ) {
        // Image Gallery
        ProductImageGallery(product.images)

        // Product Info
        ProductInfoSection(product)

        // Description
        ProductDescriptionSection(product)

        // Reviews
        ProductReviewsSection()
    }
}

@Composable
fun ProductImageGallery(productImages: List<String>?) {
    val placeholderImageRes = R.drawable.carlos_sainz
    val images = productImages?.takeIf { it.isNotEmpty() } ?: listOf(placeholderImageRes.toString())
    val pagerState = rememberPagerState(pageCount = { images.size })

    Box(modifier = Modifier.fillMaxWidth()) {
        HorizontalPager(
            state = pagerState,
            modifier = Modifier
                .fillMaxWidth()
                .height(350.dp),
        ) { page ->
            val isFocused = page == pagerState.currentPage
            val scale by animateFloatAsState(
                targetValue = if (isFocused) 1f else 0.9f,
                animationSpec = tween(300)
            )

            Box(
                modifier = Modifier
                    .padding(horizontal = 8.dp)
                    .graphicsLayer {
                        scaleX = scale
                        scaleY = scale
                    }
                    .clip(RoundedCornerShape(10.dp))
            ) {
                if (productImages?.isNotEmpty() == true) {
                    val imageUrl = remember(images[page]) {
                        // First remove any existing encoding
                        val cleanPath = images[page].replace("%2F", "/").replace("+", " ")
                        // Then properly encode
                        val encodedPath = cleanPath.split("/").joinToString("/") {
                            URLEncoder.encode(it, "UTF-8").replace("+", "%20")
                        }
                        "http://10.0.2.2:9000/$encodedPath".also { url ->
                            Log.d("ImageLoading", "Final URL: $url")
                        }
                    }

                    AsyncImage(
                        model = imageUrl,
                        contentDescription = "Product Image $page",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize(),
                        onLoading = {
                            "Loading"
                        },
                        onError = { state ->
                            Log.e("ImageLoading", "Failed to load image: ${state.result.throwable}")
                        }
                    )
                } else {
                    Image(
                        painter = painterResource(id = placeholderImageRes),
                        contentDescription = "Placeholder Image",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                    )
                }
            }
        }

        // Image Indicator
        if (images.size > 1) {
            Row(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(bottom = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                repeat(pagerState.pageCount) { index ->
                    Box(
                        modifier = Modifier
                            .size(8.dp)
                            .clip(CircleShape)
                            .background(
                                if (index == pagerState.currentPage)
                                    MaterialTheme.colorScheme.primary
                                else
                                    MaterialTheme.colorScheme.onBackground.copy(alpha = 0.3f)
                            )
                    )
                }
            }
        }
    }
}

@Composable
fun ProductInfoSection(product: Product) {
    Column(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = product.name,
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.weight(0.8f)
            )

            val ctx = LocalContext.current
            IconButton (
                onClick = { Toast.makeText(ctx, "Shared", Toast.LENGTH_SHORT).show() },
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.surfaceVariant)
            ) {
                Icon(
                    imageVector = Icons.Default.Share,
                    contentDescription = "Share",
                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }

        SuggestionChip(
            enabled = false,
            label = {
            Text(
                text = "Category: ${product.category.value}",
                style = MaterialTheme.typography.bodySmall)
                               }, onClick = {})

        Spacer(modifier = Modifier.height(8.dp))

        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(vertical = 4.dp)
        ) {
            Icon(
                imageVector = Icons.Default.Star,
                contentDescription = "Rating",
                tint = Color(0xFFFFC107),
                modifier = Modifier.size(20.dp)
            )
            Text(
                text = "4.8",
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(start = 4.dp)
            )
            Text(
                text = "(120 reviews)",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                modifier = Modifier.padding(start = 8.dp)
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "$${"%.2f".format(product.price)}",
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary
        )

        if (product.inventory > 0) {
            Text(
                text = "In Stock: ${product.inventory}",
                color = Color(0xFF4CAF50),
                modifier = Modifier.padding(top = 4.dp)
            )
        } else {
            Text(
                text = "Out of Stock",
                color = Color(0xFFF44336),
                modifier = Modifier.padding(top = 4.dp)
            )
        }
    }
}

@Composable
fun ProductDescriptionSection(product: Product) {
    Column(
        modifier = Modifier
            .padding(horizontal = 16.dp)
            .fillMaxWidth()
    ) {
        Text(
            text = "Description",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        Text(
            text = product.description ?: "No description available",
            style = MaterialTheme.typography.bodyMedium,
            lineHeight = 22.sp
        )
    }
}

@Composable
fun ProductReviewsSection() {
    Column(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxWidth()
    ) {
        Text(
            text = "Reviews (120)",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        // Sample review
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(8.dp))
                .background(MaterialTheme.colorScheme.surfaceVariant)
                .padding(12.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = "Alex Johnson",
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier.weight(1f)
                )

                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Star,
                        contentDescription = "Rating",
                        tint = Color(0xFFFFC107),
                        modifier = Modifier.size(16.dp)
                    )
                    Text(
                        text = "4.5",
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier.padding(start = 4.dp)
                    )
                }
            }

            Text(
                text = "2 weeks ago",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                modifier = Modifier.padding(top = 2.dp, bottom = 8.dp)
            )

            Text(
                text = "This product exceeded my expectations. The quality is amazing and it looks even better in person than in the photos.",
                style = MaterialTheme.typography.bodyMedium,
                lineHeight = 20.sp
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        Button(
            onClick = { /* View all reviews */ },
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp),
            shape = RoundedCornerShape(8.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant,
                contentColor = MaterialTheme.colorScheme.onSurfaceVariant
            )
        ) {
            Text(text = "View All Reviews")
        }
    }
}

@Preview
@Composable
fun PreviewProductDetail() {
    val product = Product(
        id = 1,
        name = "Product 1",
        price = (12.34).toFloat(),
        inventory = 1,
        category = ProductCategory.ELECTRONICS,
        description = "Some description"
    )

    ProductDetail(product = product)
}