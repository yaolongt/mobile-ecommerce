package com.app.kotlin_oauth.activities

import android.annotation.SuppressLint
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.app.kotlin_oauth.pages.HomePage

class MainActivity : BaseActivity() {
    @Composable
    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter", "StateFlowValueCalledInComposition")
    override fun ActivityContent() {
        val navController = rememberNavController()

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
