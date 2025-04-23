package com.app.kotlin_oauth.activities

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.rememberNavController
import com.app.kotlin_oauth.components.AppNavigationBar
import com.app.kotlin_oauth.components.AppTopBar
import com.app.kotlin_oauth.ui.theme.AppTheme

abstract class BaseActivity : ComponentActivity() {
    open val showNavigationBar: Boolean = true
    open val showTopBar: Boolean = false

    @SuppressLint("StateFlowValueCalledInComposition", "UnusedMaterial3ScaffoldPaddingParameter")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AppTheme {
                val navController = rememberNavController()

                Scaffold(
                    topBar = {
                        if (showTopBar) {
                            AppTopBar(onBackPressed = { finish() })
                        }
                    },
                    bottomBar = {
                        if (showNavigationBar) {
                            AppNavigationBar(navController)
                        }
                    }
                ) { innerPadding ->
                    ActivityContent(
                        modifier = Modifier.padding(innerPadding).padding(horizontal = 16.dp, vertical = 8.dp).fillMaxSize(),
                    )
                }
            }
        }
    }

    @Composable
    abstract fun ActivityContent(modifier: Modifier = Modifier)
}