package com.example.prueba_2_levelup.screens.auth

import android.app.DatePickerDialog
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
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
    val calendar = Calendar.getInstance()
    // Inicializa el DatePickerDialog
    val datePickerDialog = DatePickerDialog(
        context,
        { _, year, month, dayOfMonth ->
            viewModel.fechaNacimiento = LocalDate.of(year, month + 1, dayOfMonth)
            viewModel.validateFechaNacimiento() // Validar al seleccionar
        },
        calendar.get(Calendar.YEAR),
        calendar.get(Calendar.MONTH),
        calendar.get(Calendar.DAY_OF_MONTH)
    )
    // Limita la fecha máxima seleccionable a hoy
    datePickerDialog.datePicker.maxDate = System.currentTimeMillis()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Registro", style = MaterialTheme.typography.headlineMedium)
        Spacer(modifier = Modifier.height(16.dp))

        // --- Campos del Formulario ---
        OutlinedTextField(value = viewModel.nombre, onValueChange = { viewModel.nombre = it; viewModel.validateNombre() }, label = { Text("Nombre") }, isError = viewModel.nombreError != null, modifier = Modifier.fillMaxWidth())
        viewModel.nombreError?.let { Text(it, color = MaterialTheme.colorScheme.error, style = MaterialTheme.typography.bodySmall) }
        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(value = viewModel.apellido, onValueChange = { viewModel.apellido = it; viewModel.validateApellido() }, label = { Text("Apellido") }, isError = viewModel.apellidoError != null, modifier = Modifier.fillMaxWidth())
        viewModel.apellidoError?.let { Text(it, color = MaterialTheme.colorScheme.error, style = MaterialTheme.typography.bodySmall) }
        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(value = viewModel.email, onValueChange = { viewModel.email = it; viewModel.validateEmail() }, label = { Text("Correo Electrónico") }, isError = viewModel.emailError != null, modifier = Modifier.fillMaxWidth())
        viewModel.emailError?.let { Text(it, color = MaterialTheme.colorScheme.error, style = MaterialTheme.typography.bodySmall) }
        // Podrías añadir un mensaje si es correo Duoc
        if (viewModel.email.endsWith("@duoc.cl", true) || viewModel.email.endsWith("@profesor.duoc.cl", true)) {
            Text("¡Correo Duoc detectado! Recibirás 20% dcto.", color = MaterialTheme.colorScheme.primary, style = MaterialTheme.typography.bodySmall)
        }
        Spacer(modifier = Modifier.height(8.dp))

        // Campo Fecha Nacimiento (no editable, abre DatePicker)
        OutlinedTextField(
            value = viewModel.fechaNacimiento?.format(DateTimeFormatter.ISO_DATE) ?: "",
            onValueChange = {}, // No permitir escribir
            label = { Text("Fecha de Nacimiento (Click aquí)") },
            readOnly = true,
            isError = viewModel.fechaNacimientoError != null,
            modifier = Modifier
                .fillMaxWidth()
                .clickable { datePickerDialog.show() } // Mostrar selector al hacer click
        )
        viewModel.fechaNacimientoError?.let { Text(it, color = MaterialTheme.colorScheme.error, style = MaterialTheme.typography.bodySmall) }
        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(value = viewModel.password, onValueChange = { viewModel.password = it; viewModel.validatePassword() }, label = { Text("Contraseña") }, isError = viewModel.passwordError != null, visualTransformation = PasswordVisualTransformation(), modifier = Modifier.fillMaxWidth())
        viewModel.passwordError?.let { Text(it, color = MaterialTheme.colorScheme.error, style = MaterialTheme.typography.bodySmall) }
        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(value = viewModel.confirmPassword, onValueChange = { viewModel.confirmPassword = it; viewModel.validateConfirmPassword() }, label = { Text("Confirmar Contraseña") }, isError = viewModel.confirmPasswordError != null, visualTransformation = PasswordVisualTransformation(), modifier = Modifier.fillMaxWidth())
        viewModel.confirmPasswordError?.let { Text(it, color = MaterialTheme.colorScheme.error, style = MaterialTheme.typography.bodySmall) }
        Spacer(modifier = Modifier.height(16.dp))

        // Mensaje de error general de registro
        viewModel.registrationError?.let { error ->
            Text(error, color = MaterialTheme.colorScheme.error)
            Spacer(modifier = Modifier.height(8.dp))
        }

        Button(
            onClick = {
                viewModel.registerUser {
                    // Navegar a Login al éxito
                    navController.navigate("login") {
                        popUpTo("registration") { inclusive = true } // Opcional: limpiar pantalla registro
                    }
                }
            },
            enabled = viewModel.isFormValid, // Habilitar solo si el form es válido (o validar al clickear)
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Registrar")
        }
        Spacer(modifier = Modifier.height(8.dp))
        TextButton(onClick = { navController.popBackStack() }) { // Volver al Login
            Text("¿Ya tienes cuenta? Inicia Sesión")
        }
    }
}