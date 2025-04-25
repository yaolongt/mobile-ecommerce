package com.app.frontend.activities

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.app.frontend.components.ProductDisplayList
import com.app.frontend.viewmodels.NavigationViewModel

class MainActivity : BaseActivity() {
    @Composable
    override fun ActivityContent(modifier: Modifier) {
        val navController = rememberNavController()
        val navViewModel: NavigationViewModel = viewModel()

        LaunchedEffect(Unit) {
            navViewModel.init(navController)
        }

        Column(
            modifier = modifier,
        ) {
            NavHost(
                navController = navController,
                startDestination = "home",
            ) {
                composable("home") {
                    ProductDisplayList()
                }
                composable("user") {
                    // You can create a dummy UserPage() if needed
                    Text("User Page") // Replace with actual screen
                }
            }
        }
    }

    @Preview
    @Composable
    override fun PreviewActivityContent() {
        ActivityContent()
    }
}
