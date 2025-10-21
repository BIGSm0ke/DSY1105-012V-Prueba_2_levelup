
package com.example.prueba_2_levelup.viewModel

import android.util.Patterns
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.prueba_2_levelup.data.UserRepository
import com.example.prueba_2_levelup.data.entities.UserEntity
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.Period
import java.time.format.DateTimeParseException

class RegistrationViewModel(private val userRepository: UserRepository) : ViewModel() {

    var nombre by mutableStateOf("")
    var apellido by mutableStateOf("")
    var email by mutableStateOf("")
    var password by mutableStateOf("")
    var confirmPassword by mutableStateOf("")
    var fechaNacimiento by mutableStateOf<LocalDate?>(null) // Usar LocalDate

    var nombreError by mutableStateOf<String?>(null)
    var apellidoError by mutableStateOf<String?>(null)
    var emailError by mutableStateOf<String?>(null)
    var passwordError by mutableStateOf<String?>(null)
    var confirmPasswordError by mutableStateOf<String?>(null)
    var fechaNacimientoError by mutableStateOf<String?>(null)
    var registrationError by mutableStateOf<String?>(null)

    val isFormValid: Boolean
        get() = nombreError == null && apellidoError == null && emailError == null &&
                passwordError == null && confirmPasswordError == null && fechaNacimientoError == null &&
                nombre.isNotBlank() && apellido.isNotBlank() && email.isNotBlank() &&
                password.isNotBlank() && confirmPassword.isNotBlank() && fechaNacimiento != null


    fun validateNombre() {
        nombreError = if (nombre.isBlank()) "El nombre no puede estar vacío" else null
    }
    fun validateApellido() {
        apellidoError = if (apellido.isBlank()) "El apellido no puede estar vacío" else null
    }
    fun validateEmail() {
        emailError = if (email.isBlank()) {
            "El correo no puede estar vacío"
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            "Formato de correo inválido"
        } else null
    }
    fun validatePassword() {
        passwordError = if (password.isBlank()) {
            "La contraseña no puede estar vacía"
        } else if (password.length < 6) {
            "La contraseña debe tener al menos 6 caracteres"
        } else null
        validateConfirmPassword() // Validar confirmación si la contraseña cambia
    }
    fun validateConfirmPassword() {
        confirmPasswordError = if (confirmPassword != password) "Las contraseñas no coinciden" else null
    }
    fun validateFechaNacimiento() {
        fechaNacimientoError = if (fechaNacimiento == null) {
            "Debes seleccionar una fecha de nacimiento"
        } else if (!isOver18(fechaNacimiento!!)) {
            "Debes ser mayor de 18 años"
        } else null
    }

    private fun isOver18(birthDate: LocalDate): Boolean {
        return Period.between(birthDate, LocalDate.now()).years >= 18
    }

    private fun isDuocEmail(email: String): Boolean {
        return email.endsWith("@duoc.cl", ignoreCase = true) || email.endsWith("@profesor.duoc.cl", ignoreCase = true)
    }

    fun registerUser(onRegistrationSuccess: () -> Unit) {
        // Ejecutar todas las validaciones
        validateNombre()
        validateApellido()
        validateEmail()
        validatePassword()
        validateConfirmPassword()
        validateFechaNacimiento()

        if (!isFormValid) {
            registrationError = "Por favor, corrige los errores en el formulario."
            return
        }

        viewModelScope.launch {
            registrationError = null
            try {
                // Verificar si el correo ya existe
                if (userRepository.getUserByEmail(email) != null) {
                    emailError = "Este correo ya está registrado."
                    registrationError = "Este correo ya está registrado."
                    return@launch
                }

                val newUser = UserEntity(
                    nombre = nombre,
                    apellido = apellido,
                    correo = email,
                    contrasena = password, // En una app real, ¡hashear la contraseña!
                    fechaNacimiento = fechaNacimiento.toString(), // Guardar como YYYY-MM-DD
                    esDuoc = isDuocEmail(email)
                    // puntosLevelUp se inicializa en 0 por defecto
                )
                userRepository.insertUser(newUser)
                onRegistrationSuccess() // Llama al callback para navegar

            } catch (e: Exception) {
                registrationError = "Error al registrar: ${e.message}"
            }
        }
    }
}