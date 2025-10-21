package com.example.prueba_2_levelup

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.collectAsState
import kotlinx.coroutines.CoroutineScope
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope // Necesario para launch en LaunchedEffect
import androidx.navigation.compose.rememberNavController
import com.example.prueba_2_levelup.data.AppDatabase
import com.example.prueba_2_levelup.data.UserRepository
import com.example.prueba_2_levelup.data.entities.ProductEntity
import com.example.prueba_2_levelup.ui.theme.Prueba_2_levelupTheme
import com.example.prueba_2_levelup.util.PreferencesManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // 1. Inicializar DataStore (para gestión de sesión)
        val preferencesManager = PreferencesManager(this)

        // 2. Inicializar Room (Base de datos) y Repositorio
        val database = AppDatabase.getDatabase(this)
        val userRepository = UserRepository(
            userDao = database.userDao(),
            productDao = database.productDao(),
            cartDao = database.cartDao()
        )

        // 3. Pre-poblar la base de datos de productos (si está vacía)
        // Se ejecuta en un hilo secundario para no bloquear el principal
        val initialProductSetupScope = CoroutineScope(Dispatchers.IO)
        initialProductSetupScope.launch {
            if (userRepository.getProductCount() == 0) {
                // Añadir productos de ejemplo del PDF
                userRepository.insertAllProducts(
                    listOf(
                        ProductEntity(id = "JM001", categoria = "Juegos de Mesa", nombre = "Catan", precio = 29990.0, descripcion = "Un clásico juego de estrategia donde los jugadores compiten por colonizar..."),
                        ProductEntity(id = "JM002", categoria = "Juegos de Mesa", nombre = "Carcassonne", precio = 24990.0, descripcion = "Un juego de colocación de fichas donde los jugadores construyen el paisaje..."),
                        ProductEntity(id = "AC001", categoria = "Accesorios", nombre = "Controlador Inalámbrico Xbox Series X", precio = 59990.0, descripcion = "Ofrece una experiencia de juego cómoda con botones mapeables..."),
                        ProductEntity(id = "AC002", categoria = "Accesorios", nombre = "Auriculares Gamer HyperX Cloud II", precio = 79990.0, descripcion = "Proporcionan un sonido envolvente de calidad con un micrófono desmontable..."),
                        ProductEntity(id = "CO001", categoria = "Consolas", nombre = "PlayStation 5", precio = 549990.0, descripcion = "La consola de última generación de Sony, que ofrece gráficos impresionantes..."),
                        ProductEntity(id = "CG001", categoria = "Computadores Gamers", nombre = "PC Gamer ASUS ROG Strix", precio = 1299990.0, descripcion = "Un potente equipo diseñado para los gamers más exigentes..."),
                        ProductEntity(id = "SG001", categoria = "Sillas Gamers", nombre = "Silla Gamer Secretlab Titan", precio = 349990.0, descripcion = "Diseñada para el máximo confort, esta silla ofrece un soporte ergonómico..."),
                        ProductEntity(id = "MS001", categoria = "Mouse", nombre = "Mouse Gamer Logitech G502 HERO", precio = 49990.0, descripcion = "Con sensor de alta precisión y botones personalizables..."),
                        ProductEntity(id = "MP001", categoria = "Mousepad", nombre = "Mousepad Razer Goliathus Extended Chroma", precio = 29990.0, descripcion = "Ofrece un área de juego amplia con iluminación RGB personalizable..."),
                        ProductEntity(id = "PP001", categoria = "Poleras Personalizadas", nombre = "Polera Gamer Personalizada 'Level-Up'", precio = 14990.0, descripcion = "Una camiseta cómoda y estilizada, con la posibilidad de personalizarla...")
                        // Añade aquí los Polerones y Servicio Técnico si los consideras productos
                    )
                )
            }
        }

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