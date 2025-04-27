package com.app.frontend.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp

@Composable
fun ProductDetailSkeleton(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Image placeholder
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(350.dp)
                .background(MaterialTheme.colorScheme.surfaceVariant)
        )

        // Product info section
        Column(
            modifier = Modifier.padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            // Title placeholder
            Box(
                modifier = Modifier
                    .fillMaxWidth(0.8f)
                    .height(32.dp)
                    .background(MaterialTheme.colorScheme.surfaceVariant)
            )

            // Category placeholder
            Box(
                modifier = Modifier
                    .fillMaxWidth(0.4f)
                    .height(24.dp)
                    .background(MaterialTheme.colorScheme.surfaceVariant)
            )

            // Rating placeholder
            Box(
                modifier = Modifier
                    .fillMaxWidth(0.6f)
                    .height(20.dp)
                    .background(MaterialTheme.colorScheme.surfaceVariant)
            )

            // Price placeholder
            Box(
                modifier = Modifier
                    .fillMaxWidth(0.3f)
                    .height(28.dp)
                    .background(MaterialTheme.colorScheme.surfaceVariant)
            )
        }

        // Description section
        Column(
            modifier = Modifier.padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            // Section title
            Box(
                modifier = Modifier
                    .fillMaxWidth(0.4f)
                    .height(24.dp)
                    .background(MaterialTheme.colorScheme.surfaceVariant)
            )

            // Description lines
            repeat(4) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(16.dp)
                        .background(MaterialTheme.colorScheme.surfaceVariant)
                )
            }
        }

        // Reviews section
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            // Section title
            Box(
                modifier = Modifier
                    .fillMaxWidth(0.4f)
                    .height(24.dp)
                    .background(MaterialTheme.colorScheme.surfaceVariant)
            )

            // Review card
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp)
                    .background(MaterialTheme.colorScheme.surfaceVariant)
                    .clip(RoundedCornerShape(8.dp))
            )
        }
    }
}