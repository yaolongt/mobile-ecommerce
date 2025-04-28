package com.app.frontend.components

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import coil3.compose.AsyncImage
import com.app.frontend.R
import java.net.URLEncoder
import kotlin.text.replace

@Composable
fun CustomImageDisplay(imageUrlPath: String, modifier: Modifier = Modifier) {
    if (imageUrlPath.isNotEmpty()) {
    val imageUrl = remember(imageUrlPath) {
        // First remove any existing encoding
        val cleanPath = imageUrlPath.replace("%2F", "/").replace("+", " ")
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
        contentDescription = "Product Image $imageUrlPath",
        contentScale = ContentScale.Crop,
        modifier = modifier,
        onLoading = {
            "Loading"
        },
        onError = { state ->
            Log.e("ImageLoading", "Failed to load image: ${state.result.throwable}")
        }
    )
    } else {
        Image(
            painter = painterResource(R.drawable.carlos_sainz),
            contentDescription = "Placeholder Image",
            contentScale = ContentScale.Crop,
            modifier = modifier
        )
    }
}