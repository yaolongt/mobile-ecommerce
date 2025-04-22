package com.app.kotlin_oauth.activities

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.app.kotlin_oauth.components.AppNavigationBar
import com.app.kotlin_oauth.ui.theme.AppTheme

abstract class BaseActivity : ComponentActivity() {
    open val showNavigationBar: Boolean = true

    @SuppressLint("StateFlowValueCalledInComposition")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AppTheme {
                val navController = rememberNavController()

                Scaffold(
                    bottomBar = {
                        if (showNavigationBar) {
                            AppNavigationBar(navController)
                        }
                    }
                ) { innerPadding ->
                    Box(Modifier.padding(innerPadding)) {
                        ActivityContent()
                    }
                }
            }
        }
    }

    @Composable
    abstract fun ActivityContent()
}