package com.example.prueba_2_levelup.screens.auth

// ELIMINADAS: import android.app.DatePickerDialog
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions // AÑADIDA
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType // AÑADIDA
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.prueba_2_levelup.viewModel.RegistrationViewModel
// ELIMINADAS: import java.time.LocalDate, import java.time.format.DateTimeFormatter, import java.util.Calendar

@Composable
fun RegistrationScreen(
    navController: NavController,
    viewModel: RegistrationViewModel
) {
    // ELIMINADO todo el código del DatePickerDialog y manejo de fecha de nacimiento

    // --- Surface para el fondo negro (del tema oscuro) ---
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 32.dp, vertical = 16.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("Crear Cuenta", style = MaterialTheme.typography.headlineMedium)
            Spacer(modifier = Modifier.height(32.dp))

            // --- Definición de colores ---
            val textFieldColors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = MaterialTheme.colorScheme.primary,
                unfocusedBorderColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.4f),
                focusedLabelColor = MaterialTheme.colorScheme.primary,
                cursorColor = MaterialTheme.colorScheme.primary
            )

            // --- Campo Nombre ---
            OutlinedTextField(
                value = viewModel.nombre,
                onValueChange = { viewModel.nombre = it; viewModel.validateNombre() },
                label = { Text("Nombre") },
                isError = viewModel.nombreError != null,
                modifier = Modifier.fillMaxWidth(),
                colors = textFieldColors
            )
            viewModel.nombreError?.let {
                Text(it, color = MaterialTheme.colorScheme.error, style = MaterialTheme.typography.bodySmall)
            }
            Spacer(modifier = Modifier.height(12.dp))

            // --- Campo Apellido ---
            OutlinedTextField(
                value = viewModel.apellido,
                onValueChange = { viewModel.apellido = it; viewModel.validateApellido() },
                label = { Text("Apellido") },
                isError = viewModel.apellidoError != null,
                modifier = Modifier.fillMaxWidth(),
                colors = textFieldColors
            )
            viewModel.apellidoError?.let { Text(it, color = MaterialTheme.colorScheme.error, style = MaterialTheme.typography.bodySmall) }
            Spacer(modifier = Modifier.height(12.dp))

            // --- NUEVO CAMPO: Nombre de Usuario ---
            OutlinedTextField(
                value = viewModel.nombreUsuario,
                onValueChange = { viewModel.nombreUsuario = it; viewModel.validateNombreUsuario() },
                label = { Text("Nombre de Usuario") },
                isError = viewModel.nombreUsuarioError != null,
                modifier = Modifier.fillMaxWidth(),
                colors = textFieldColors
            )
            viewModel.nombreUsuarioError?.let { Text(it, color = MaterialTheme.colorScheme.error, style = MaterialTheme.typography.bodySmall) }
            Spacer(modifier = Modifier.height(12.dp))

            // --- Campo Email ---
            OutlinedTextField(
                value = viewModel.email,
                onValueChange = { viewModel.email = it; viewModel.validateEmail() },
                label = { Text("Correo Electrónico") },
                isError = viewModel.emailError != null,
                modifier = Modifier.fillMaxWidth(),
                colors = textFieldColors
            )
            viewModel.emailError?.let { Text(it, color = MaterialTheme.colorScheme.error, style = MaterialTheme.typography.bodySmall) }
            // Lógica para mostrar descuento
            if (viewModel.email.endsWith("@duoc.cl", true) || viewModel.email.endsWith("@profesor.duoc.cl", true)) {
                Text("¡Correo Duoc detectado! Recibirás 20% dcto.", color = MaterialTheme.colorScheme.secondary, style = MaterialTheme.typography.bodySmall)
            }
            Spacer(modifier = Modifier.height(12.dp))

            // --- ELIMINADO: TextField de Fecha de Nacimiento ---

            // --- NUEVO CAMPO: Teléfono ---
            OutlinedTextField(
                value = viewModel.telefono,
                onValueChange = { viewModel.telefono = it; viewModel.validateTelefono() },
                label = { Text("Teléfono") },
                isError = viewModel.telefonoError != null,
                modifier = Modifier.fillMaxWidth(),
                colors = textFieldColors,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone) // Teclado numérico
            )
            viewModel.telefonoError?.let { Text(it, color = MaterialTheme.colorScheme.error, style = MaterialTheme.typography.bodySmall) }
            Spacer(modifier = Modifier.height(12.dp))

            // --- NUEVO CAMPO: Dirección ---
            OutlinedTextField(
                value = viewModel.direccion,
                onValueChange = { viewModel.direccion = it; viewModel.validateDireccion() },
                label = { Text("Dirección") },
                isError = viewModel.direccionError != null,
                modifier = Modifier.fillMaxWidth(),
                colors = textFieldColors
            )
            viewModel.direccionError?.let { Text(it, color = MaterialTheme.colorScheme.error, style = MaterialTheme.typography.bodySmall) }
            Spacer(modifier = Modifier.height(12.dp))

            // --- Campo Password ---
            OutlinedTextField(
                value = viewModel.password,
                onValueChange = { viewModel.password = it; viewModel.validatePassword() },
                label = { Text("Contraseña") },
                isError = viewModel.passwordError != null,
                visualTransformation = PasswordVisualTransformation(),
                modifier = Modifier.fillMaxWidth(),
                colors = textFieldColors
            )
            viewModel.passwordError?.let { Text(it, color = MaterialTheme.colorScheme.error, style = MaterialTheme.typography.bodySmall) }
            Spacer(modifier = Modifier.height(12.dp))

            // --- Campo Confirmar Password ---
            OutlinedTextField(
                value = viewModel.confirmPassword,
                onValueChange = { viewModel.confirmPassword = it; viewModel.validateConfirmPassword() },
                label = { Text("Confirmar Contraseña") },
                isError = viewModel.confirmPasswordError != null,
                visualTransformation = PasswordVisualTransformation(),
                modifier = Modifier.fillMaxWidth(),
                colors = textFieldColors
            )
            viewModel.confirmPasswordError?.let { Text(it, color = MaterialTheme.colorScheme.error, style = MaterialTheme.typography.bodySmall) }
            Spacer(modifier = Modifier.height(24.dp))

            viewModel.registrationError?.let { error ->
                Text(error, color = MaterialTheme.colorScheme.error)
                Spacer(modifier = Modifier.height(8.dp))
            }

            // --- Botón Registrar ---
            Button(
                onClick = {
                    viewModel.registerUser {
                        // Navega hacia atrás (a Login) SÓLO si el registro fue exitoso
                        navController.popBackStack()
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.secondary)
            ) {
                Text("Registrar", style = MaterialTheme.typography.labelLarge, color = MaterialTheme.colorScheme.onSecondary)
            }
            Spacer(modifier = Modifier.height(16.dp))
            TextButton(onClick = { navController.popBackStack() }) { Text("¿Ya tienes cuenta? Inicia Sesión") }
        }
    }
}