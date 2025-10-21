package com.example.prueba_2_levelup.screens.main

import androidx.compose.foundation.layout.*
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
    onLogout: () -> Unit // Lambda para ejecutar la navegación de logout
) {
    // Observa el estado del perfil desde el ViewModel
    val profileState by viewModel.profileState.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top // Alinea al top para mostrar info primero
    ) {
        Text("Mi Perfil", style = MaterialTheme.typography.headlineMedium)
        Spacer(modifier = Modifier.height(32.dp))

        // Muestra indicador de carga mientras se obtienen los datos
        if (profileState.isLoading) {
            CircularProgressIndicator()
        } else {
            // Muestra la información del usuario
            ProfileInfoRow(label = "Nombre:", value = profileState.userName)
            ProfileInfoRow(label = "Apellido:", value = profileState.userLastName)
            ProfileInfoRow(label = "Correo:", value = profileState.userEmail)
            Spacer(modifier = Modifier.height(16.dp))
            Divider()
            Spacer(modifier = Modifier.height(16.dp))
            ProfileInfoRow(label = "Puntos LevelUp:", value = profileState.levelUpPoints.toString(), isHighlight = true)

            Spacer(modifier = Modifier.weight(1f)) // Empuja el botón hacia abajo

            // Botón para cerrar sesión
            Button(
                onClick = { viewModel.logout(onLogout) }, // Llama al logout del VM y luego a la navegación
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error) // Color rojo para logout
            ) {
                Text("Cerrar Sesión")
            }
        }
    }
}

@Composable
fun ProfileInfoRow(label: String, value: String, isHighlight: Boolean = false) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween // Separa etiqueta y valor
    ) {
        Text(label, fontWeight = FontWeight.SemiBold)
        Text(
            value,
            fontWeight = if (isHighlight) FontWeight.Bold else FontWeight.Normal,
            fontSize = if (isHighlight) 18.sp else 16.sp, // Destaca los puntos
            color = if (isHighlight) MaterialTheme.colorScheme.primary else LocalContentColor.current
        )
    }
}

// --- Preview (Opcional) ---
/*
@Preview(showBackground = true)
@Composable
fun ProfileScreenPreview() {
    Prueba_2_levelupTheme {
        // Necesitarías un ViewModel mock para la preview
        // ProfileScreen(viewModel = mockProfileViewModel, onLogout = {})

        // Preview manual simple:
         Column(
             modifier = Modifier.fillMaxSize().padding(16.dp),
             horizontalAlignment = Alignment.CenterHorizontally,
             verticalArrangement = Arrangement.Top
         ) {
             Text("Mi Perfil", style = MaterialTheme.typography.headlineMedium)
             Spacer(modifier = Modifier.height(32.dp))
             ProfileInfoRow(label = "Nombre:", value = "Juan")
             ProfileInfoRow(label = "Apellido:", value = "Pérez")
             ProfileInfoRow(label = "Correo:", value = "juan.perez@email.com")
             Spacer(modifier = Modifier.height(16.dp))
             Divider()
             Spacer(modifier = Modifier.height(16.dp))
             ProfileInfoRow(label = "Puntos LevelUp:", value = "1500", isHighlight = true)
             Spacer(modifier = Modifier.weight(1f))
             Button(onClick = {}, modifier = Modifier.fillMaxWidth(), colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)) {
                 Text("Cerrar Sesión")
             }
         }
    }
}
*/