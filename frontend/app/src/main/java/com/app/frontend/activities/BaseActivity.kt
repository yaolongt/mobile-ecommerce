package com.app.frontend.activities

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.app.frontend.components.AppNavigationBar
import com.app.frontend.ui.theme.AppTheme

abstract class BaseActivity : ComponentActivity() {
    open val showNavigationBar: Boolean = true

    @SuppressLint("StateFlowValueCalledInComposition", "UnusedMaterial3ScaffoldPaddingParameter")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AppTheme {
                Scaffold(
                    bottomBar = {
                        if (showNavigationBar) {
                            AppNavigationBar()
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

    @Preview
    @Composable
    open fun PreviewActivityContent() {}
}