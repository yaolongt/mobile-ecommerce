package com.app.frontend.components

import androidx.compose.runtime.getValue
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.app.frontend.R

@Composable
fun ProductDetailPager() {
    val PLACEHOLDER_IMAGE = Array(5) { _ -> R.drawable.carlos_sainz }
    val pagerState = rememberPagerState(pageCount = { PLACEHOLDER_IMAGE.size })

    Column {
        HorizontalPager(
            state = pagerState,
            modifier = Modifier
                .fillMaxWidth()
                .height(250.dp)
        ) { page ->
            val isFocused = page == pagerState.currentPage
            val scale by animateFloatAsState(
                targetValue = if (isFocused) 1f else 0.9f,
                animationSpec = tween(300)
            )

            Box(
                modifier = Modifier
                    .padding(horizontal = 8.dp) // Space between items
                    .graphicsLayer {
                        scaleX = scale
                        scaleY = scale
                    }
                    .clip(RoundedCornerShape(10.dp))
            ) {
                Image(
                    painter = painterResource(id = PLACEHOLDER_IMAGE[page]),
                    contentDescription = "Product Image $page",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }}

@Preview
@Composable
private fun PreviewProductDetails() {
    ProductDetailPager()
}