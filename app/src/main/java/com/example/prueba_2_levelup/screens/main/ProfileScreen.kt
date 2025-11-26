// app/src/main/java/com/example/prueba_2_levelup/screens/main/ProfileScreen.kt
package com.example.prueba_2_levelup.screens.main

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.prueba_2_levelup.viewModel.ProfileViewModel
import androidx.compose.runtime.collectAsState // Importación necesaria

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    navController: NavController,
    viewModel: ProfileViewModel
) {
    // 1. Observar el estado de login (dispara la navegación fuera de Home)
    val isLoggedIn by viewModel.preferencesManager.isLoggedInFlow.collectAsState(initial = true)

    // 2. Redirección (Se activa cuando isLoggedIn cambia a false, ej. al presionar el botón)
    LaunchedEffect(isLoggedIn) {
        if (!isLoggedIn) {
            navController.navigate("login") {
                // Limpia la pila de Home y va a Login
                popUpTo("home") { inclusive = true }
            }
        }
    }

    Scaffold(
        topBar = { TopAppBar(title = { Text("Menú de Sesión") }) }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 32.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center // Centrar el contenido
        ) {
            Text(
                "Gestión de Sesión",
                style = MaterialTheme.typography.headlineSmall,
                modifier = Modifier.padding(bottom = 32.dp)
            )

            // --- BOTÓN DE CERRAR SESIÓN ---
            Button(
                onClick = { viewModel.logout() },
                modifier = Modifier.fillMaxWidth().height(50.dp),
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)
            ) {
                Icon(Icons.AutoMirrored.Filled.Logout, contentDescription = "Cerrar sesión")
                Spacer(Modifier.size(ButtonDefaults.IconSpacing))
                Text("CERRAR SESIÓN")
            }
        }
    }
}

// Nota: Se han eliminado los composables ProfileDetail y ProfileInfoCard.