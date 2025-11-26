package com.example.prueba_2_levelup.viewModel

import android.util.Patterns
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.prueba_2_levelup.data.UserRepository
import kotlinx.coroutines.launch
import java.lang.Exception // Asegurarse de manejar correctamente las excepciones

class RegistrationViewModel(private val userRepository: UserRepository) : ViewModel() {

    var nombre by mutableStateOf("")
    var apellido by mutableStateOf("")
    var nombreUsuario by mutableStateOf("") // NUEVO
    var email by mutableStateOf("")
    var password by mutableStateOf("")
    var confirmPassword by mutableStateOf("")
    var telefono by mutableStateOf("") // NUEVO
    var direccion by mutableStateOf("") // NUEVO
    // ELIMINADO: var fechaNacimiento...

    var nombreError by mutableStateOf<String?>(null)
    var apellidoError by mutableStateOf<String?>(null)
    var nombreUsuarioError by mutableStateOf<String?>(null) // NUEVO Error
    var emailError by mutableStateOf<String?>(null)
    var passwordError by mutableStateOf<String?>(null)
    var confirmPasswordError by mutableStateOf<String?>(null)
    var telefonoError by mutableStateOf<String?>(null) // NUEVO Error
    var direccionError by mutableStateOf<String?>(null) // NUEVO Error
    // ELIMINADO: var fechaNacimientoError...
    var registrationError by mutableStateOf<String?>(null)

    val isFormValid: Boolean
        get() = nombreError == null && apellidoError == null && nombreUsuarioError == null &&
                emailError == null && passwordError == null && confirmPasswordError == null &&
                telefonoError == null && direccionError == null &&
                nombre.isNotBlank() && apellido.isNotBlank() && nombreUsuario.isNotBlank() &&
                email.isNotBlank() && password.isNotBlank() && confirmPassword.isNotBlank() &&
                telefono.isNotBlank() && direccion.isNotBlank()


    // --- Funciones de Validación (Actualizadas) ---
    fun validateNombre() { nombreError = if (nombre.isBlank()) "El nombre no puede estar vacío" else null }
    fun validateApellido() { apellidoError = if (apellido.isBlank()) "El apellido no puede estar vacío" else null }
    fun validateNombreUsuario() { nombreUsuarioError = if (nombreUsuario.isBlank()) "El nombre de usuario no puede estar vacío" else null }
    fun validateTelefono() { telefonoError = if (telefono.isBlank()) "El teléfono no puede estar vacío" else null }
    fun validateDireccion() { direccionError = if (direccion.isBlank()) "La dirección no puede estar vacía" else null }

    fun validateEmail() {
        emailError = if (email.isBlank()) { "El correo no puede estar vacío" }
        else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) { "Formato de correo inválido" }
        else null
    }
    fun validatePassword() {
        passwordError = if (password.isBlank()) { "La contraseña no puede estar vacía" }
        else if (password.length < 6) { "La contraseña debe tener al menos 6 caracteres" }
        else null
        validateConfirmPassword()
    }
    fun validateConfirmPassword() { confirmPasswordError = if (confirmPassword != password) "Las contraseñas no coinciden" else null }

    // ELIMINADAS: validateFechaNacimiento(), isOver18(), isDuocEmail()

    // --- FUNCIÓN DE REGISTRO MODIFICADA para usar la API ---
    fun registerUser(onRegistrationSuccess: () -> Unit) {
        // Ejecuta todas las validaciones
        validateNombre(); validateApellido(); validateNombreUsuario(); validateEmail(); validatePassword(); validateConfirmPassword(); validateTelefono(); validateDireccion()

        // Si el formulario no es válido, muestra error y detiene
        if (!isFormValid) {
            registrationError = "Por favor, corrige los errores en el formulario."
            return
        }

        // Lanza corutina para operaciones de API
        viewModelScope.launch {
            registrationError = null // Limpia error previo
            try {

                // LLAMADA A LA API DE REGISTRO a través del UserRepository
                val authResponse = userRepository.registerUser(
                    nombreUsuario = nombreUsuario,
                    email = email,
                    password = password,
                    nombre = nombre,
                    apellido = apellido,
                    telefono = telefono,
                    direccion = direccion
                )

                if (authResponse != null) {
                    // Si la API devuelve una respuesta exitosa, ejecuta el callback para navegar
                    onRegistrationSuccess()
                } else {
                    // Manejo de error de la API (ej. nombre de usuario/email ya existe, o fallo 500)
                    registrationError = "Error al registrar. El nombre de usuario o correo podría estar en uso, o hubo un fallo en el servidor."
                }

            } catch (e: Exception) {
                // Captura errores de red o inesperados
                registrationError = "Error de conexión o inesperado al registrar: ${e.message}"
            }
        }
    }
}