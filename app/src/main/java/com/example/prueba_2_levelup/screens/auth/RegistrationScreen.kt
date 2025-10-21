package com.example.prueba_2_levelup.screens.auth

import android.app.DatePickerDialog
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.prueba_2_levelup.viewModel.RegistrationViewModel
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Calendar

@Composable
fun RegistrationScreen(
    navController: NavController,
    viewModel: RegistrationViewModel
) {
    val context = LocalContext.current
    val displayDateFormat = DateTimeFormatter.ofPattern("dd/MM/yyyy")

    // --- Configuración DatePickerDialog (sin cambios) ---
    val currentCalendar = Calendar.getInstance()
    val datePickerDialog = DatePickerDialog(
        context,
        { _, year, month, dayOfMonth ->
            viewModel.fechaNacimiento = LocalDate.of(year, month + 1, dayOfMonth)
            viewModel.validateFechaNacimiento()
        },
        viewModel.fechaNacimiento?.year ?: currentCalendar.get(Calendar.YEAR),
        viewModel.fechaNacimiento?.monthValue?.minus(1) ?: currentCalendar.get(Calendar.MONTH),
        viewModel.fechaNacimiento?.dayOfMonth ?: currentCalendar.get(Calendar.DAY_OF_MONTH)
    )
    datePickerDialog.datePicker.maxDate = System.currentTimeMillis()
    // --- Fin DatePickerDialog ---

    // --- FIX 3: Envolver en Surface para el color de fondo ---
    // Surface aplicará el color MaterialTheme.colorScheme.background.
    // Si tu app usa el tema oscuro (Dark Theme), este fondo será negro
    // y el texto (como "Crear Cuenta") se volverá blanco automáticamente.
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize() // Rellena la Surface
                .padding(horizontal = 32.dp, vertical = 16.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            // --- Contenido de la Columna (Tu código anterior, sin cambios) ---

            Text("Crear Cuenta", style = MaterialTheme.typography.headlineMedium)
            Spacer(modifier = Modifier.height(32.dp))

            val textFieldColors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = MaterialTheme.colorScheme.primary,
                unfocusedBorderColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.4f),
                focusedLabelColor = MaterialTheme.colorScheme.primary,
                cursorColor = MaterialTheme.colorScheme.primary
            )

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

            OutlinedTextField(
                value = viewModel.email,
                onValueChange = { viewModel.email = it; viewModel.validateEmail() },
                label = { Text("Correo Electrónico") },
                isError = viewModel.emailError != null,
                modifier = Modifier.fillMaxWidth(),
                colors = textFieldColors
            )
            viewModel.emailError?.let { Text(it, color = MaterialTheme.colorScheme.error, style = MaterialTheme.typography.bodySmall) }
            if (viewModel.email.endsWith("@duoc.cl", true) || viewModel.email.endsWith("@profesor.duoc.cl", true)) {
                Text("¡Correo Duoc detectado! Recibirás 20% dcto.", color = MaterialTheme.colorScheme.secondary, style = MaterialTheme.typography.bodySmall)
            }
            Spacer(modifier = Modifier.height(12.dp))

            OutlinedTextField(
                value = viewModel.fechaNacimiento?.format(displayDateFormat) ?: "",
                onValueChange = {},
                label = { Text("Fecha de Nacimiento (Click aquí)") },
                readOnly = true,
                isError = viewModel.fechaNacimientoError != null,
                modifier = Modifier.fillMaxWidth().clickable { datePickerDialog.show() },
                colors = textFieldColors
            )
            viewModel.fechaNacimientoError?.let { Text(it, color = MaterialTheme.colorScheme.error, style = MaterialTheme.typography.bodySmall) }
            Spacer(modifier = Modifier.height(12.dp))

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

            Button(
                onClick = { viewModel.registerUser { /* ... */ } },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.secondary)
            ) {
                Text("Registrar", style = MaterialTheme.typography.labelLarge, color = MaterialTheme.colorScheme.onSecondary)
            }
            Spacer(modifier = Modifier.height(16.dp))
            TextButton(onClick = { navController.popBackStack() }) { Text("¿Ya tienes cuenta? Inicia Sesión") }

        } // Fin Column
    } // Fin Surface
}