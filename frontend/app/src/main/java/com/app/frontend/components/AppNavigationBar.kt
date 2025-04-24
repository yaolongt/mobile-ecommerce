package com.app.frontend.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.lifecycle.viewmodel.compose.viewModel
import com.app.frontend.viewmodels.NavigationViewModel
import androidx.compose.runtime.getValue

@Composable
fun AppNavigationBar() {
    val viewModel: NavigationViewModel = viewModel()
    val currentRoute by viewModel.currentRoute // Observe route changes

    val items = listOf(
        NavigationItem("Home", "home", Icons.Filled.Home, Icons.Outlined.Home),
        NavigationItem("User", "user", Icons.Filled.Person, Icons.Outlined.Person)
    )

    NavigationBar {
        items.forEach { item ->
            NavigationBarItem(
                icon = {
                    Icon(
                        imageVector = if (currentRoute == item.route) item.selectedIcon else item.unselectedIcon,
                        contentDescription = item.label
                    )
                },
                label = { Text(item.label) },
                selected = currentRoute == item.route,
                onClick = {
                    viewModel.navigateTo(item.route)
                }
            )
        }
    }
}

data class NavigationItem(
    val label: String,
    val route: String,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector
)
