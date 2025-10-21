package com.example.prueba_2_levelup

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.prueba_2_levelup.data.UserRepository
import com.example.prueba_2_levelup.screens.auth.LoginScreen
import com.example.prueba_2_levelup.screens.auth.RegistrationScreen
import com.example.prueba_2_levelup.screens.main.HomeScreen
import com.example.prueba_2_levelup.util.PreferencesManager
import com.example.prueba_2_levelup.viewModel.LoginViewModel
import com.example.prueba_2_levelup.viewModel.RegistrationViewModel
import com.example.prueba_2_levelup.viewModel.ViewModelFactory

@Composable
fun AppNavigator(
    navController: NavHostController,
    preferencesManager: PreferencesManager,
    userRepository: UserRepository,
    startDestination: String
) {
    // Crear instancia del Factory una sola vez
    val factory = ViewModelFactory(userRepository, preferencesManager)

    NavHost(navController = navController, startDestination = startDestination) {
        composable("login") {
            // Pasamos el factory al viewModel composable
            val loginViewModel: LoginViewModel = viewModel(factory = factory)
            LoginScreen(
                navController = navController,
                viewModel = loginViewModel
            )
        }
        composable("registration") {
            val registrationViewModel: RegistrationViewModel = viewModel(factory = factory)
            RegistrationScreen(
                navController = navController,
                viewModel = registrationViewModel
            )
        }
        composable("home") {
            // Pasamos el factory a HomeScreen también, para que pueda crear sus ViewModels
            HomeScreen(
                factory = factory,
                preferencesManager = preferencesManager, // Puede que no sea necesario aquí
                mainNavController = navController // Para poder hacer logout y volver al login
            )
        }
        // Puedes añadir rutas para detalles de producto si es necesario
        // composable("productDetail/{productId}") { backStackEntry -> ... }
    }
}