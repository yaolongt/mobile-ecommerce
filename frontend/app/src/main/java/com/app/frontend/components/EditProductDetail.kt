package com.app.frontend.components

import android.app.Activity
import android.content.pm.PackageManager
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import coil3.compose.AsyncImage
import android.Manifest
import android.os.Build
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontStyle
import com.app.frontend.models.Product
import com.app.frontend.models.ProductCategory

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditProductDetail(
    product: Product,
    onDismiss: () -> Unit,
    onSave: (Product, List<Uri>?) -> Unit
) {
    var editedProduct by remember { mutableStateOf(product.copy()) }
    var selectedImageUris by remember { mutableStateOf<List<Uri>>(emptyList()) }
    var selectedIndices by remember { mutableStateOf<Set<Int>>(emptySet()) }
    var isUploading by remember { mutableStateOf(false) }
    var uploadError by remember { mutableStateOf<String?>(null) }
    val context = LocalContext.current

    // Image picker launcher
    val imagePicker = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetMultipleContents()
    ) { uris: List<Uri> ->
        selectedImageUris += uris
    }

    val storagePermission = remember {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            Manifest.permission.READ_MEDIA_IMAGES
        } else {
            Manifest.permission.READ_EXTERNAL_STORAGE
        }
    }

    val permissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) imagePicker.launch("image/*")
    }

    fun selectImages() {
        when {
            ContextCompat.checkSelfPermission(
                context,
                storagePermission
            ) == PackageManager.PERMISSION_GRANTED -> {
                imagePicker.launch("image/*")
            }
            ActivityCompat.shouldShowRequestPermissionRationale(
                context as Activity,
                storagePermission
            ) -> {
                // Show rationale dialog
                uploadError = "Storage permission is required to select images"
            }
            else -> {
                permissionLauncher.launch(storagePermission)
            }
        }
    }

    fun toggleImageSelection(index: Int) {
        selectedIndices = if (selectedIndices.contains(index)) {
            selectedIndices - index
        } else {
            selectedIndices + index
        }
    }

    fun removeSelectedImages() {
        selectedImageUris = selectedImageUris.filterIndexed { index, _ ->
            !selectedIndices.contains(index)
        }
        selectedIndices = emptySet()
    }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Edit Product") },
        text = {
            Column(
                modifier = Modifier.verticalScroll(rememberScrollState())
            ) {// Add this image upload section at the top
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // Image preview with selection
                    LazyRow(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        itemsIndexed(selectedImageUris) { index, uri ->
                            Box(
                                modifier = Modifier
                                    .size(100.dp)
                                    .clip(MaterialTheme.shapes.small)
                                    .border(
                                        width = if (selectedIndices.contains(index)) 2.dp else 0.dp,
                                        color = if (selectedIndices.contains(index)) MaterialTheme.colorScheme.primary else Color.Transparent,
                                        shape = MaterialTheme.shapes.small
                                    )
                                    .clickable { toggleImageSelection(index) }
                            ) {
                                AsyncImage(
                                    model = uri,
                                    contentDescription = "Selected Image",
                                    contentScale = ContentScale.Crop,
                                    modifier = Modifier.fillMaxSize()
                                )

                                if (selectedIndices.contains(index)) {
                                    Box(
                                        modifier = Modifier
                                            .align(Alignment.TopEnd)
                                            .padding(4.dp)
                                            .background(MaterialTheme.colorScheme.primary, CircleShape)
                                            .size(20.dp)
                                    ) {
                                        Text(
                                            text = "✓",
                                            color = MaterialTheme.colorScheme.onPrimary,
                                            modifier = Modifier.align(Alignment.Center)
                                        )
                                    }
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = "⚠\uFE0F Uploading images will override the current images.",
                        style = MaterialTheme.typography.bodySmall,
                        fontStyle = FontStyle.Italic
                    )

                    // Action buttons
                    Button(
                        onClick = { selectImages() },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Select More Images")
                    }

                    if (selectedImageUris.isNotEmpty()) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            TextButton(
                                onClick = {
                                    if (selectedIndices.isNotEmpty()) {
                                        removeSelectedImages()
                                    } else {
                                        selectedImageUris = emptyList()
                                    }
                                },
                                modifier = Modifier.weight(1f)
                            ) {
                                Text(
                                    text = if (selectedIndices.isNotEmpty()) "Remove Selected" else "Remove All",
                                    color = MaterialTheme.colorScheme.error
                                )
                            }

                            if (selectedIndices.isNotEmpty()) {
                                TextButton(
                                    onClick = { selectedIndices = emptySet() },
                                    modifier = Modifier.weight(1f)
                                ) {
                                    Text("Clear Selection")
                                }
                            }
                        }
                    }
                }


                Spacer(modifier = Modifier.height(16.dp))

                OutlinedTextField(
                    value = editedProduct.name,
                    onValueChange = { editedProduct = editedProduct.copy(name = it) },
                    label = { Text("Product Name") },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(16.dp))

                OutlinedTextField(
                    value = editedProduct.price.toString(),
                    onValueChange = {
                        editedProduct = editedProduct.copy(price = it.toFloatOrNull() ?: 0f)
                    },
                    label = { Text("Price") },
                    keyboardOptions = KeyboardOptions.Default.copy(
                        keyboardType = KeyboardType.Number
                    ),
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(16.dp))

                OutlinedTextField(
                    value = editedProduct.inventory.toString(),
                    onValueChange = {
                        editedProduct = editedProduct.copy(inventory = it.toIntOrNull() ?: 0)
                    },
                    label = { Text("Inventory") },
                    keyboardOptions = KeyboardOptions.Default.copy(
                        keyboardType = KeyboardType.Number
                    ),
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Category dropdown
                var expanded by remember { mutableStateOf(false) }
                ExposedDropdownMenuBox(
                    expanded = expanded,
                    onExpandedChange = { expanded = !expanded }
                ) {
                    OutlinedTextField(
                        value = editedProduct.category.value,
                        onValueChange = {},
                        label = { Text("Category") },
                        readOnly = true,
                        trailingIcon = {
                            ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .menuAnchor()
                    )

                    ExposedDropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { expanded = false }
                    ) {
                        ProductCategory.entries.forEach { category ->
                            DropdownMenuItem(
                                text = { Text(category.value) },
                                onClick = {
                                    editedProduct = editedProduct.copy(category = category)
                                    expanded = false
                                }
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                OutlinedTextField(
                    value = editedProduct.description ?: "",
                    onValueChange = {
                        editedProduct = editedProduct.copy(description = it.ifEmpty { null })
                    },
                    label = { Text("Description") },
                    modifier = Modifier.fillMaxWidth(),
                    maxLines = 4
                )
            }
        },
        confirmButton = {
            Button(
                onClick = { onSave(editedProduct, selectedImageUris.takeIf {it.isNotEmpty()}) },
                enabled = !isUploading,
            ) {
                if (isUploading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(24.dp),
                        color = MaterialTheme.colorScheme.onPrimary,
                        strokeWidth = 2.dp
                    )
                } else {
                    Text("Save")
                }
            }
        },
        dismissButton = {
            TextButton(
                onClick = onDismiss
            ) {
                Text("Cancel")
            }
        }
    )
}
