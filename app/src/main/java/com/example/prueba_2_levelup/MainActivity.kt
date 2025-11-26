package com.example.prueba_2_levelup

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.navigation.compose.rememberNavController
import com.example.prueba_2_levelup.data.AppDatabase
import com.example.prueba_2_levelup.data.UserRepository
import com.example.prueba_2_levelup.data.network.ApiClient // Importación necesaria
import com.example.prueba_2_levelup.ui.theme.Prueba_2_levelupTheme
import com.example.prueba_2_levelup.util.PreferencesManager

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // 1. Inicializar DataStore (para gestión de sesión)
        val preferencesManager = PreferencesManager(this)

        // PASO NUEVO: Inicializar la API Service usando el factory del ApiClient
        val productoApiService = ApiClient.createProductoService(this)

        // 2. Inicializar Room (Base de datos) y Repositorio
        val database = AppDatabase.getDatabase(this)

        // CORRECCIÓN: Pasar los dos parámetros faltantes (línea 28)
        val userRepository = UserRepository(
            userDao = database.userDao(),
            productDao = database.productDao(),
            cartDao = database.cartDao(),
            preferencesManager = preferencesManager, // Parámetro añadido
            productoApiService = productoApiService // Parámetro añadido
        )

        // NOTA: Se ha eliminado el paso 3 (pre-poblar la BD)

        setContent {
            Prueba_2_levelupTheme { // Aplica el tema de la app
                // 4. Observar estado de login y determinar ruta inicial
                val isLoggedIn by preferencesManager.isLoggedInFlow.collectAsState(initial = false)

                val startDestination = if (isLoggedIn) {
                    "home" // Si ya inició sesión, va directo a la pantalla principal
                } else {
                    "login" // Si no, va a la pantalla de inicio de sesión
                }

                // 5. Inicializar el controlador de navegación
                val navController = rememberNavController()

                // 6. Llamar al Composable que contiene el NavHost (la lógica de navegación)
                AppNavigator(
                    navController = navController,
                    preferencesManager = preferencesManager,
                    userRepository = userRepository,
                    startDestination = startDestination
                )
            }
        }
    }
}