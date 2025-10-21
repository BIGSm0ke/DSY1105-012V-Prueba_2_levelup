package com.example.prueba_2_levelup.screens.main

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.DateRange // Placeholder for Map/Events
import androidx.compose.material.icons.filled.Home // Placeholder for Catalog
import androidx.compose.material.icons.filled.Info // Placeholder for Support
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.*
import com.example.prueba_2_levelup.util.PreferencesManager // Importa PreferencesManager
import com.example.prueba_2_levelup.viewModel.* // Importa todos tus ViewModels y el Factory

// Define las rutas internas de la Home
sealed class HomeSections(val route: String, val label: String, val icon: ImageVector) {
    object Catalog : HomeSections("catalog", "Catálogo", Icons.Filled.Home)
    object Cart : HomeSections("cart", "Carrito", Icons.Filled.ShoppingCart)
    object Profile : HomeSections("profile", "Perfil", Icons.Filled.AccountCircle)
    object EventMap : HomeSections("event_map", "Eventos", Icons.Filled.DateRange)
    object Support : HomeSections("support", "Soporte", Icons.Filled.Info)
}

// Lista de secciones para la barra de navegación
val homeSections = listOf(
    HomeSections.Catalog,
    HomeSections.Cart,
    HomeSections.Profile,
    HomeSections.EventMap,
    HomeSections.Support
)

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter") // El padding se usa en el NavHost interno
@Composable
fun HomeScreen(
    factory: ViewModelFactory, // Recibe el Factory para crear ViewModels
    preferencesManager: PreferencesManager, // Podría ser necesario para alguna lógica directa
    mainNavController: NavHostController // El controlador principal para logout
) {
    // Controlador de navegación *interno* para las secciones de Home
    val homeNavController = rememberNavController()

    // --- Instancia tus ViewModels usando el Factory ---
    val catalogViewModel: CatalogViewModel = viewModel(factory = factory)
    val cartViewModel: CartViewModel = viewModel(factory = factory)
    val profileViewModel: ProfileViewModel = viewModel(factory = factory)
    // Añade instancias para EventMapViewModel y SupportViewModel si los creas y necesitan el factory

    Scaffold(
        bottomBar = {
            // Barra de navegación inferior
            BottomNavigationBar(navController = homeNavController)
        }
    ) { innerPadding ->
        // NavHost *interno* que ocupa el espacio del Scaffold
        NavHost(
            navController = homeNavController,
            startDestination = HomeSections.Catalog.route, // Empieza en el Catálogo
            modifier = Modifier.padding(innerPadding) // Aplica el padding del Scaffold
        ) {
            // Define qué pantalla mostrar para cada ruta interna
            composable(HomeSections.Catalog.route) {
                CatalogScreen(
                    viewModel = catalogViewModel,
                    // onProductClick = { productId -> /* Navegar a detalle si lo implementas */ }
                )
            }
            composable(HomeSections.Cart.route) {
                CartScreen(viewModel = cartViewModel)
            }
            composable(HomeSections.Profile.route) {
                ProfileScreen(
                    viewModel = profileViewModel,
                    onLogout = {
                        // Usa el controlador principal para volver al Login
                        mainNavController.navigate("login") {
                            popUpTo("home") { inclusive = true } // Limpia la pila hasta Home
                        }
                    }
                )
            }
            composable(HomeSections.EventMap.route) {
                // Si EventMapScreen necesita ViewModel con factory, créalo aquí
                EventMapScreen(/* viewModel = viewModel(factory = factory) */)
            }
            composable(HomeSections.Support.route) {
                // Si SupportScreen necesita ViewModel con factory, créalo aquí
                SupportScreen(/* viewModel = viewModel(factory = factory) */)
            }
        }
    }
}

// Composable para la barra de navegación inferior
@Composable
fun BottomNavigationBar(navController: NavHostController) {
    NavigationBar {
        // Obtiene la entrada actual de la pila de navegación para saber qué item resaltar
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentDestination = navBackStackEntry?.destination

        // Itera sobre las secciones definidas
        homeSections.forEach { section ->
            NavigationBarItem(
                icon = { Icon(section.icon, contentDescription = section.label) }, // Ícono de la sección
                label = { Text(section.label) }, // Texto de la sección
                // Determina si esta sección es la actualmente seleccionada
                selected = currentDestination?.hierarchy?.any { it.route == section.route } == true,
                // Acción al hacer clic en un item de la barra
                onClick = {
                    navController.navigate(section.route) {
                        // Vuelve al inicio del gráfico de navegación para evitar acumular pantallas
                        popUpTo(navController.graph.findStartDestination().id) {
                            saveState = true // Guarda el estado de la pantalla al salir
                        }
                        // Evita lanzar la misma pantalla múltiples veces si se presiona rápido
                        launchSingleTop = true
                        // Restaura el estado guardado al volver a una pantalla
                        restoreState = true
                    }
                }
            )
        }
    }
}