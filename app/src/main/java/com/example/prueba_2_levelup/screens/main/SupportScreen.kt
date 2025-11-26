package com.example.prueba_2_levelup.screens.main

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.prueba_2_levelup.viewModel.ProfileViewModel

@Composable
fun SupportScreen(
    viewModel: ProfileViewModel,
    onLogoutSuccess: () -> Unit // NUEVA LAMBDA: Se llama después de borrar el token
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text("Soporte Técnico", style = MaterialTheme.typography.headlineMedium)
            Spacer(modifier = Modifier.height(32.dp))
            Text("Para asistencia, por favor contacte a:", style = MaterialTheme.typography.bodyLarge)
            Spacer(modifier = Modifier.height(8.dp))
            Text("soporte@levelup.com", style = MaterialTheme.typography.bodyLarge, color = MaterialTheme.colorScheme.primary)
            Spacer(modifier = Modifier.height(32.dp))
        }

        // --- Botón de Cerrar Sesión ---
        Button(
            // Al hacer clic, primero llamamos al logout del ViewModel
            onClick = { viewModel.logout(); onLogoutSuccess() },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp),
            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)
        ) {
            Icon(Icons.AutoMirrored.Filled.Logout, contentDescription = "Cerrar sesión")
            Spacer(Modifier.size(ButtonDefaults.IconSpacing))
            Text("Cerrar Sesión")
        }
    }
}