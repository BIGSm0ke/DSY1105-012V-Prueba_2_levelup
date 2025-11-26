package com.example.prueba_2_levelup.screens.auth

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.prueba_2_levelup.R
import com.example.prueba_2_levelup.ui.theme.Orbitron
import com.example.prueba_2_levelup.viewModel.LoginViewModel

@Composable
fun LoginScreen(
    navController: NavController,
    viewModel: LoginViewModel
) {
    val uiState by viewModel.uiState.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }

    // Manejar la navegación después del login exitoso
    LaunchedEffect(uiState.isLoggedIn) {
        if (uiState.isLoggedIn) {
            navController.navigate("home") {
                // Limpia la pila de navegación para que el usuario no pueda volver al login
                popUpTo("login") { inclusive = true }
            }
        }
    }

    // Manejar mensajes del usuario (éxito/error)
    LaunchedEffect(uiState.userMessage) {
        val message = uiState.userMessage
        if (message != null) {
            snackbarHostState.showSnackbar(
                message = message,
                duration = SnackbarDuration.Short
            )
            viewModel.dismissUserMessage()
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .background(MaterialTheme.colorScheme.background),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Card(
                modifier = Modifier
                    .fillMaxWidth(0.9f)
                    .padding(16.dp),
                shape = RoundedCornerShape(12.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
            ) {
                Column(
                    modifier = Modifier.padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // BLOQUE COMENTADO PARA EVITAR EL CRASH POR RECURSO NO SOPORTADO
                    /*
                    Image(
                        painter = painterResource(id = R.mipmap.ic_launcher),
                        contentDescription = "Logo",
                        modifier = Modifier.size(96.dp).padding(bottom = 16.dp)
                    )
                    */
                    // Se agrega un Spacer para reemplazar el espacio ocupado por el logo
                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        text = "LEVEL UP",
                        style = MaterialTheme.typography.headlineLarge.copy(
                            fontWeight = FontWeight.Bold,
                            fontFamily = Orbitron
                        ),
                        color = MaterialTheme.colorScheme.primary
                    )
                    Spacer(modifier = Modifier.height(32.dp))

                    // Campo de Usuario
                    OutlinedTextField(
                        value = uiState.username,
                        onValueChange = { viewModel.onUsernameChange(it) },
                        label = { Text("Usuario / Email") },
                        leadingIcon = { Icon(Icons.Filled.Email, contentDescription = null) },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true,
                        shape = RoundedCornerShape(8.dp)
                    )
                    Spacer(modifier = Modifier.height(16.dp))

                    // Campo de Contraseña
                    OutlinedTextField(
                        value = uiState.password,
                        onValueChange = { viewModel.onPasswordChange(it) },
                        label = { Text("Contraseña") },
                        leadingIcon = { Icon(Icons.Filled.Lock, contentDescription = null) },
                        visualTransformation = PasswordVisualTransformation(),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true,
                        shape = RoundedCornerShape(8.dp)
                    )
                    Spacer(modifier = Modifier.height(32.dp))

                    Button(
                        onClick = { viewModel.login() },
                        modifier = Modifier.fillMaxWidth().height(50.dp),
                        enabled = !uiState.isLoading,
                        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
                    ) {
                        if (uiState.isLoading) {
                            CircularProgressIndicator(color = Color.White, modifier = Modifier.size(24.dp))
                        } else {
                            Text("INICIAR SESIÓN", fontWeight = FontWeight.Bold)
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    TextButton(
                        onClick = { navController.navigate("registration") },
                        enabled = !uiState.isLoading
                    ) {
                        Text("¿No tienes cuenta? Regístrate aquí.")
                    }
                }
            }
        }
    }
}