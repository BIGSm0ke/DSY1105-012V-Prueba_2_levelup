package com.example.prueba_2_levelup.screens.main

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExitToApp // Ícono para Logout
import androidx.compose.material.icons.filled.Star // Ícono para Puntos
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.prueba_2_levelup.viewModel.ProfileViewModel

@Composable
fun ProfileScreen(
    viewModel: ProfileViewModel,
    onLogout: () -> Unit
) {
    val profileState by viewModel.profileState.collectAsState()

    // Fondo negro y texto blanco por el tema
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween // Distribuye espacio (info arriba, botón abajo)
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) { // Columna para la info
            // Título con Orbitron
            Text("Mi Perfil", style = MaterialTheme.typography.headlineMedium)
            Spacer(modifier = Modifier.height(32.dp))

            if (profileState.isLoading) {
                CircularProgressIndicator(color = MaterialTheme.colorScheme.primary) // Indicador azul
            } else {
                // Info del usuario
                ProfileInfoCard(profileState)
            }
        }

        // Botón Logout siempre visible abajo (si no está cargando)
        if (!profileState.isLoading) {
            Button(
                onClick = { viewModel.logout(onLogout) },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp), // Margen inferior
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error) // Rojo
            ) {
                Icon(Icons.Filled.ExitToApp, contentDescription = "Cerrar Sesión")
                Spacer(Modifier.size(ButtonDefaults.IconSpacing))
                Text("Cerrar Sesión", style = MaterialTheme.typography.labelLarge)
            }
        }
    }
}

// Card para agrupar la información del perfil
@Composable
fun ProfileInfoCard(profileState: com.example.prueba_2_levelup.viewModel.ProfileUiState) { // Usa el nombre completo para evitar ambigüedad
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant), // Gris oscuro
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            ProfileInfoRow(label = "Nombre:", value = profileState.userName)
            ProfileInfoRow(label = "Apellido:", value = profileState.userLastName)
            HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp), color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.1f))
            ProfileInfoRow(label = "Correo:", value = profileState.userEmail)
            HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp), color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.1f))
            ProfileInfoRow(label = "Puntos LevelUp:", value = profileState.levelUpPoints.toString(), isHighlight = true, icon = Icons.Filled.Star) // Con ícono
        }
    }
}


@Composable
fun ProfileInfoRow(label: String, value: String, isHighlight: Boolean = false, icon: androidx.compose.ui.graphics.vector.ImageVector? = null) { // Tipo de Icon
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 10.dp), // Más espacio vertical
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) { // Agrupa ícono y label
            if (icon != null) {
                Icon(
                    icon,
                    contentDescription = label,
                    modifier = Modifier.size(20.dp),
                    tint = if (isHighlight) MaterialTheme.colorScheme.secondary else MaterialTheme.colorScheme.primary // Verde o Azul
                )
                Spacer(modifier = Modifier.width(8.dp))
            }
            Text(label, fontWeight = FontWeight.SemiBold, style = MaterialTheme.typography.bodyLarge) // Texto un poco más grande
        }
        Text(
            value,
            fontWeight = if (isHighlight) FontWeight.Bold else FontWeight.Normal,
            fontSize = if (isHighlight) 20.sp else 16.sp, // Destaca más los puntos
            color = if (isHighlight) MaterialTheme.colorScheme.secondary else MaterialTheme.colorScheme.onSurface // Verde o Blanco
        )
    }
}