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
import com.example.prueba_2_levelup.data.UserRepository
import com.example.prueba_2_levelup.util.PreferencesManager
import com.example.prueba_2_levelup.viewModel.* // Importa todos tus ViewModels

// Define las rutas internas de la Home
sealed class HomeSections(val route: String, val label: String, val icon: ImageVector) {
    object Catalog : HomeSections("catalog", "Catálogo", Icons.Filled.Home)
    object Cart : HomeSections("cart", "Carrito", Icons.Filled.ShoppingCart)
    object Profile : HomeSections("profile", "Perfil", Icons.Filled.AccountCircle)
    object EventMap : HomeSections("event_map", "Eventos", Icons.Filled.DateRange)
    object Support : HomeSections("support", "Soporte", Icons.Filled.Info)
}

val homeSections = listOf(
    HomeSections.Catalog,
    HomeSections.Cart,
    HomeSections.Profile,
    HomeSections.EventMap,
    HomeSections.Support
)

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter") // Padding se usa en el NavHost interno
@Composable
fun HomeScreen(
    userRepository: UserRepository, // Pasa las dependencias necesarias
    preferencesManager: PreferencesManager,
    mainNavController: NavHostController // El controlador principal para logout
    // Podrías necesitar pasar un ViewModelFactory si lo creaste
) {
    // Controlador de navegación *interno* para las secciones de Home
    val homeNavController = rememberNavController()

    // --- Instancia tus ViewModels ---
    // Idealmente, usa un ViewModelFactory para inyectar userRepository y preferencesManager
    // val factory = ViewModelFactory(userRepository, preferencesManager)
    val catalogViewModel: CatalogViewModel = viewModel() // viewModel(factory = factory)
    val cartViewModel: CartViewModel = viewModel()       // viewModel(factory = factory)
    val profileViewModel: ProfileViewModel = viewModel()   // viewModel(factory = factory)
    // Añade instancias para EventMapViewModel y SupportViewModel si los creas

    Scaffold(
        bottomBar = {
            BottomNavigationBar(navController = homeNavController)
        }
    ) { innerPadding ->
        // NavHost *interno* que ocupa el espacio del Scaffold
        NavHost(
            navController = homeNavController,
            startDestination = HomeSections.Catalog.route, // Empieza en el Catálogo
            modifier = Modifier.padding(innerPadding) // Aplica el padding del Scaffold
        ) {
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
                        // Usar el controlador principal para volver al Login
                        mainNavController.navigate("login") {
                            popUpTo("home") { inclusive = true } // Limpia la pila hasta Home
                        }
                    }
                )
            }
            composable(HomeSections.EventMap.route) {
                EventMapScreen(/* Pasa ViewModel si es necesario */)
            }
            composable(HomeSections.Support.route) {
                SupportScreen(/* Pasa ViewModel si es necesario */)
            }
        }
    }
}

@Composable
fun BottomNavigationBar(navController: NavHostController) {
    NavigationBar {
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentDestination = navBackStackEntry?.destination

        homeSections.forEach { section ->
            NavigationBarItem(
                icon = { Icon(section.icon, contentDescription = section.label) },
                label = { Text(section.label) },
                selected = currentDestination?.hierarchy?.any { it.route == section.route } == true,
                onClick = {
                    navController.navigate(section.route) {
                        // Pop up to the start destination of the graph to avoid building up a large
                        // stack of destinations on the back stack as users select items
                        popUpTo(navController.graph.findStartDestination().id) {
                            saveState = true
                        }
                        // Avoid multiple copies of the same destination when reselecting the same item
                        launchSingleTop = true
                        // Restore state when reselecting a previously selected item
                        restoreState = true
                    }
                }
            )
        }
    }
}

// --- Previews (Opcional, pero útil) ---
/*
@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
    Prueba_2_levelupTheme {
        // Necesitarías mocks o instancias falsas para previsualizar HomeScreen
        // HomeScreen( userRepoMock, prefsMock, rememberNavController())
    }
}
*/