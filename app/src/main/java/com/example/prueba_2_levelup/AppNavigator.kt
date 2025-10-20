package com.example.prueba_2_levelup

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.prueba_2_levelup.data.UserRepository
import com.example.prueba_2_levelup.screens.auth.LoginScreen
import com.example.prueba_2_levelup.screens.auth.RegistrationScreen
import com.example.prueba_2_levelup.screens.main.HomeScreen // Asumiendo que HomeScreen maneja la navegación interna
import com.example.prueba_2_levelup.util.PreferencesManager
import com.example.prueba_2_levelup.viewModel.LoginViewModel
import com.example.prueba_2_levelup.viewModel.RegistrationViewModel
// Importa tus otras ViewModels y Screens aquí

// Necesitarás crear un ViewModelFactory para inyectar dependencias
// import com.example.prueba_2_levelup.viewModel.ViewModelFactory

@Composable
fun AppNavigator(
    navController: NavHostController,
    preferencesManager: PreferencesManager,
    userRepository: UserRepository,
    startDestination: String
) {
    // Crear instancia del Factory (ajusta según tu implementación)
    // val factory = ViewModelFactory(userRepository, preferencesManager)

    NavHost(navController = navController, startDestination = startDestination) {
        composable("login") {
            // Pasamos el factory al viewModel composable
            val loginViewModel: LoginViewModel = viewModel() // = viewModel(factory = factory)
            LoginScreen(
                navController = navController,
                viewModel = loginViewModel
            )
        }
        composable("registration") {
            val registrationViewModel: RegistrationViewModel = viewModel() // = viewModel(factory = factory)
            RegistrationScreen(
                navController = navController,
                viewModel = registrationViewModel
            )
        }
        composable("home") { // La pantalla principal que contiene el BottomNavBar
            // HomeScreen necesitará acceso a varios ViewModels o un ViewModel principal
            HomeScreen(
                userRepository = userRepository, // Pasa lo necesario
                preferencesManager = preferencesManager,
                mainNavController = navController // Para poder hacer logout y volver al login
            )
        }
        // Puedes añadir rutas para detalles de producto si es necesario
        // composable("productDetail/{productId}") { backStackEntry -> ... }
    }
}