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
import java.time.LocalDate // Importar LocalDate
import java.time.Period    // Importar Period para calcular edad
import java.time.format.DateTimeFormatter // Importar DateTimeFormatter

class RegistrationViewModel(private val userRepository: UserRepository) : ViewModel() {

    var nombre by mutableStateOf("")
    var apellido by mutableStateOf("")
    var email by mutableStateOf("")
    var password by mutableStateOf("")
    var confirmPassword by mutableStateOf("")
    // Usar LocalDate?
    var fechaNacimiento by mutableStateOf<LocalDate?>(null)

    var nombreError by mutableStateOf<String?>(null)
    var apellidoError by mutableStateOf<String?>(null)
    var emailError by mutableStateOf<String?>(null)
    var passwordError by mutableStateOf<String?>(null)
    var confirmPasswordError by mutableStateOf<String?>(null)
    var fechaNacimientoError by mutableStateOf<String?>(null)
    var registrationError by mutableStateOf<String?>(null)

    // Formateador estándar ISO para guardar "YYYY-MM-DD"
    private val dbDateFormat = DateTimeFormatter.ISO_LOCAL_DATE // YYYY-MM-DD

    val isFormValid: Boolean
        get() = nombreError == null && apellidoError == null && emailError == null &&
                passwordError == null && confirmPasswordError == null && fechaNacimientoError == null &&
                nombre.isNotBlank() && apellido.isNotBlank() && email.isNotBlank() &&
                password.isNotBlank() && confirmPassword.isNotBlank() && fechaNacimiento != null

    // --- Funciones de Validación ---
    fun validateNombre() { nombreError = if (nombre.isBlank()) "El nombre no puede estar vacío" else null }
    fun validateApellido() { apellidoError = if (apellido.isBlank()) "El apellido no puede estar vacío" else null }
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

    fun validateFechaNacimiento() {
        fechaNacimientoError = if (fechaNacimiento == null) { "Debes seleccionar una fecha" }
        // Usa la función isOver18 con LocalDate
        else if (!isOver18(fechaNacimiento!!)) { "Debes ser mayor de 18 años" }
        else null
    }

    // Función isOver18 usando LocalDate y Period
    private fun isOver18(birthDate: LocalDate): Boolean {
        // Calcula el período entre la fecha de nacimiento y hoy, y obtiene los años
        return Period.between(birthDate, LocalDate.now()).years >= 18
    }

    // --- FUNCIÓN CORREGIDA ---
    // Función para verificar correo @duoc.cl o @profesor.duoc.cl
    private fun isDuocEmail(email: String): Boolean {
        // Asegura que coincida con la lógica de la UI
        return email.endsWith("@duoc.cl", ignoreCase = true) ||
                email.endsWith("@profesor.duoc.cl", ignoreCase = true)
    }
    // --- FIN CORRECCIÓN ---


    fun registerUser(onRegistrationSuccess: () -> Unit) {
        // Ejecuta todas las validaciones
        validateNombre(); validateApellido(); validateEmail(); validatePassword(); validateConfirmPassword(); validateFechaNacimiento()

        // Si el formulario no es válido, muestra error y detiene
        if (!isFormValid) {
            registrationError = "Por favor, corrige los errores en el formulario."
            return
        }

        // Lanza corutina para operaciones de BD
        viewModelScope.launch {
            registrationError = null // Limpia error previo
            try {
                // Verifica si el correo ya existe
                if (userRepository.getUserByEmail(email) != null) {
                    emailError = "Este correo ya está registrado."
                    registrationError = "Este correo ya está registrado."
                    return@launch // Detiene si ya existe
                }

                // Crea la entidad con los datos validados
                val newUser = UserEntity(
                    nombre = nombre,
                    apellido = apellido,
                    correo = email,
                    contrasena = password, // ¡Considera hashear en producción!
                    // Formatea LocalDate a String "YYYY-MM-DD" para guardar
                    fechaNacimiento = fechaNacimiento!!.format(dbDateFormat),
                    esDuoc = isDuocEmail(email) // Usa la lógica corregida
                )
                // Intenta insertar y verifica si tuvo éxito
                val success = userRepository.insertUser(newUser)
                if(success) {
                    // Llama al callback (que contiene el popBackStack()) si fue exitoso
                    onRegistrationSuccess()
                } else {
                    // Caso de conflicto (ej. email ya existe, detectado por BD)
                    emailError = "Conflicto: El correo ya existe en la base de datos."
                    registrationError = "Conflicto: El correo ya existe."
                }

            } catch (e: Exception) {
                // Captura otros errores
                registrationError = "Error inesperado al registrar: ${e.message}"
            }
        }
    }
}