package com.example.prueba_2_levelup.screens.main

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.DateRange // Placeholder for Map/Events
import androidx.compose.material.icons.filled.Home // Placeholder for Catalog
import androidx.compose.material.icons.filled.Info // Placeholder for Support
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.*
import com.example.prueba_2_levelup.util.PreferencesManager
import com.example.prueba_2_levelup.viewModel.* import com.example.prueba_2_levelup.viewModel.ViewModelFactory

// Define las rutas internas de la Home
sealed class HomeSections(val route: String, val label: String, val icon: ImageVector) {
    object Catalog : HomeSections("catalog", "Catálogo", Icons.Filled.Home)
    object Cart : HomeSections("cart", "Carrito", Icons.Filled.ShoppingCart)
    // Profile fue eliminado para evitar la redirección automática.
    object ProductManagement : HomeSections("product_management", "Productos", Icons.Filled.Edit)
    object Support : HomeSections("support", "Sesión", Icons.Filled.Info) // Renombrado para reflejar la función de Logout
}

// Lista de secciones para la barra de navegación (SIN PERFIL)
val homeSections = listOf(
    HomeSections.Catalog,
    HomeSections.Cart,
    HomeSections.ProductManagement,
    HomeSections.Support
)

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun HomeScreen(
    factory: ViewModelFactory,
    preferencesManager: PreferencesManager,
    mainNavController: NavHostController // El controlador principal (para salir al Login)
) {
    val homeNavController = rememberNavController()

    // --- Instancia tus ViewModels usando el Factory ---
    val catalogViewModel: CatalogViewModel = viewModel(factory = factory)
    val cartViewModel: CartViewModel = viewModel(factory = factory)
    val profileViewModel: ProfileViewModel = viewModel(factory = factory) // Contiene la lógica de logout
    val productManagementViewModel : ProductManagementViewModel = viewModel(factory = factory)

    Scaffold(
        bottomBar = {
            BottomNavigationBar(navController = homeNavController)
        }
    ) { innerPadding ->
        NavHost(
            navController = homeNavController,
            startDestination = HomeSections.Catalog.route,
            modifier = Modifier.padding(innerPadding)
        ) {

            composable(HomeSections.Catalog.route) {
                CatalogScreen(viewModel = catalogViewModel)
            }

            composable(HomeSections.Cart.route) {
                CartScreen(viewModel = cartViewModel)
            }

            // La ruta Profile (HomeSections.Profile.route) ha sido eliminada del NavHost.

            composable(HomeSections.ProductManagement.route) {
                ProductManagementScreen(viewModel = productManagementViewModel)
            }

            composable(HomeSections.Support.route) {
                SupportScreen(
                    viewModel = profileViewModel,
                    // CORRECCIÓN CLAVE: Definir la acción de navegación que usa el mainNavController
                    onLogoutSuccess = {
                        mainNavController.navigate("login") {
                            // Limpia la pila de navegación de Home y va al Login
                            popUpTo("home") { inclusive = true }
                        }
                    }
                )
            }
        }
    }
}

// Composable para la barra de navegación inferior
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
                        popUpTo(navController.graph.findStartDestination().id) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            )
        }
    }
}