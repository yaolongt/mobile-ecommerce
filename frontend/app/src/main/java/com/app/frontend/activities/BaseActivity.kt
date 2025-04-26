package com.app.frontend.activities

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.rememberNavController
import com.app.frontend.components.AppNavigationBar
import com.app.frontend.ui.theme.AppTheme
import com.app.frontend.viewmodels.NavigationViewModel

abstract class BaseActivity : ComponentActivity() {
    protected val navViewModel: NavigationViewModel by viewModels()
    open val showNavigationBar: Boolean = true

    @SuppressLint("StateFlowValueCalledInComposition", "UnusedMaterial3ScaffoldPaddingParameter")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AppTheme {
                val navController = rememberNavController()

                // Initialize ViewModel with NavController
                LaunchedEffect(Unit) {
                    navViewModel.init(navController)
                }

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