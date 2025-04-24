package com.app.frontend.viewmodels

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

class NavigationViewModel : ViewModel() {
    private lateinit var _navController: NavController
    private val _currentRoute = mutableStateOf<String?>(null)
    val currentRoute: State<String?> = _currentRoute

    fun init(navController: NavController) {
        _navController = navController
        observeRoutes()
    }

    // Encapsulated navigation methods
    fun navigateTo(route: String) {
        _navController.navigate(route) {
            popUpTo(_navController.graph.startDestinationId) {
                saveState = true
            }
            launchSingleTop = true
            restoreState = true
        }
    }

    private fun observeRoutes() {
        _navController.currentBackStackEntryFlow
            .onEach { entry ->
                _currentRoute.value = entry.destination.route
            }
            .launchIn(viewModelScope)
    }
}