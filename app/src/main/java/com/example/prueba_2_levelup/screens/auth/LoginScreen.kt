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
    // --- FIX 1: Envolver en Surface para aplicar el fondo negro ---
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 32.dp, vertical = 16.dp), // Padding
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Título con fuente Orbitron (aplicado por el tema)
            Text("LEVEL-UP GAMER", style = MaterialTheme.typography.headlineMedium)
            Spacer(modifier = Modifier.height(48.dp))

            // --- FIX 2: Definir los colores una sola vez ---
            // (Estos son los mismos colores que ya tenías, pero en una variable)
            val textFieldColors = OutlinedTextFieldDefaults.colors(
                focusedTextColor = MaterialTheme.colorScheme.onBackground, // Texto al escribir (Blanco)
                unfocusedTextColor = MaterialTheme.colorScheme.onBackground, // Texto sin foco (Blanco)
                cursorColor = MaterialTheme.colorScheme.primary, // Cursor (Azul Eléctrico)
                focusedBorderColor = MaterialTheme.colorScheme.primary, // Borde con foco (Azul Eléctrico)
                unfocusedBorderColor = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f), // Borde sin foco (Gris Oscuro Semi-Transparente)
                focusedLabelColor = MaterialTheme.colorScheme.primary, // Etiqueta con foco (Azul Eléctrico)
                unfocusedLabelColor = MaterialTheme.colorScheme.onSurfaceVariant // Etiqueta sin foco (Gris Claro)
            )
            // --- Fin FIX 2 ---

            // --- Campos de texto ---
            OutlinedTextField(
                value = viewModel.email,
                onValueChange = { viewModel.email = it },
                label = { Text("Correo Electrónico") },
                modifier = Modifier.fillMaxWidth(),
                colors = textFieldColors // Aplicamos la variable
            )
            Spacer(modifier = Modifier.height(16.dp))
            OutlinedTextField(
                value = viewModel.password,
                onValueChange = { viewModel.password = it },
                label = { Text("Contraseña") },
                visualTransformation = PasswordVisualTransformation(),
                modifier = Modifier.fillMaxWidth(),
                colors = textFieldColors // Aplicamos la variable
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

            // Botón secundario (TextButton) usará color primario (Azul Eléctrico) por defecto
            TextButton(onClick = { navController.navigate("registration") }) {
                Text("¿No tienes cuenta? Regístrate")
            }
        }
    } // --- Fin Surface (FIX 1) ---
}