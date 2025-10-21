package com.example.prueba_2_levelup.screens.auth

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.prueba_2_levelup.viewModel.LoginViewModel

@Composable
fun LoginScreen(
    navController: NavController,
    viewModel: LoginViewModel
) {
    // El tema oscuro (fondo negro, texto blanco) debería aplicarse automáticamente
    // Si el fondo NO es negro, asegúrate que MainActivity aplica Prueba_2_levelupTheme
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 32.dp, vertical = 16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Título con fuente Orbitron
        Text("LEVEL-UP GAMER", style = MaterialTheme.typography.headlineMedium)
        Spacer(modifier = Modifier.height(48.dp))

        // --- Campos de texto con colores explícitos del tema ---
        OutlinedTextField(
            value = viewModel.email,
            onValueChange = { viewModel.email = it },
            label = { Text("Correo Electrónico") },
            modifier = Modifier.fillMaxWidth(),
            // Aplicamos colores explícitamente desde el tema
            colors = OutlinedTextFieldDefaults.colors(
                focusedTextColor = MaterialTheme.colorScheme.onBackground, // Texto al escribir (Blanco)
                unfocusedTextColor = MaterialTheme.colorScheme.onBackground, // Texto sin foco (Blanco)
                cursorColor = MaterialTheme.colorScheme.primary, // Cursor (Azul Eléctrico)
                focusedBorderColor = MaterialTheme.colorScheme.primary, // Borde con foco (Azul Eléctrico)
                unfocusedBorderColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.3f), // Borde sin foco (Blanco Transparente)
                focusedLabelColor = MaterialTheme.colorScheme.primary, // Etiqueta con foco (Azul Eléctrico)
                unfocusedLabelColor = MaterialTheme.colorScheme.onSurfaceVariant // Etiqueta sin foco (Gris Claro)
            )
        )
        Spacer(modifier = Modifier.height(16.dp))
        OutlinedTextField(
            value = viewModel.password,
            onValueChange = { viewModel.password = it },
            label = { Text("Contraseña") },
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier.fillMaxWidth(),
            // Aplicamos colores explícitamente desde el tema
            colors = OutlinedTextFieldDefaults.colors(
                focusedTextColor = MaterialTheme.colorScheme.onBackground,
                unfocusedTextColor = MaterialTheme.colorScheme.onBackground,
                cursorColor = MaterialTheme.colorScheme.primary,
                focusedBorderColor = MaterialTheme.colorScheme.primary,
                unfocusedBorderColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.3f),
                focusedLabelColor = MaterialTheme.colorScheme.primary,
                unfocusedLabelColor = MaterialTheme.colorScheme.onSurfaceVariant
            )
        )
        Spacer(modifier = Modifier.height(24.dp))

        // Mostrar error de login si existe
        viewModel.loginError?.let { error ->
            Text(error, color = MaterialTheme.colorScheme.error) // Rojo
            Spacer(modifier = Modifier.height(8.dp))
        }

        // Botón principal con color primario (Azul Eléctrico)
        Button(
            onClick = {
                viewModel.attemptLogin { userId, nombre, apellido ->
                    navController.navigate("home") {
                        popUpTo("login") { inclusive = true }
                    }
                }
            },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary) // Azul
        ) {
            // Texto del botón usará onPrimary (Blanco)
            Text("Iniciar Sesión", style = MaterialTheme.typography.labelLarge)
        }
        Spacer(modifier = Modifier.height(16.dp))

        // Botón secundario (TextButton) usará color primario (Azul Eléctrico)
        TextButton(onClick = { navController.navigate("registration") }) {
            Text("¿No tienes cuenta? Regístrate") // El color es primary por defecto
        }
    }
}