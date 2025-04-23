package com.app.kotlin_oauth.activities

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.app.kotlin_oauth.pages.HomePage

class MainActivity : BaseActivity() {
    @Composable
    override fun ActivityContent(modifier: Modifier) {
        val navController = rememberNavController()

        Column(
            modifier = modifier,
        ) {
            NavHost(
                navController = navController,
                startDestination = "home",
            ) {
                composable("home") {
                    HomePage()
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
    private fun PreviewMainActivity() {
        HomePage()
    }
}
