package com.example.prueba_2_levelup.viewModel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.prueba_2_levelup.data.UserRepository
import com.example.prueba_2_levelup.util.PreferencesManager
import kotlinx.coroutines.launch

class LoginViewModel(
    private val userRepository: UserRepository,
    private val preferencesManager: PreferencesManager
) : ViewModel() {

    var email by mutableStateOf("")
    var password by mutableStateOf("")
    var loginError by mutableStateOf<String?>(null) // Para mostrar mensaje de error

    fun attemptLogin(onLoginSuccess: (userId: Int, nombre: String, apellido: String) -> Unit) {
        viewModelScope.launch {
            loginError = null // Resetea el error
            try {
                val user = userRepository.login(email, password)
                if (user != null) {
                    // Guardar estado de login en DataStore
                    preferencesManager.saveLoginState(true, user.id, user.correo)
                    // Llamar al callback para navegar
                    onLoginSuccess(user.id, user.nombre, user.apellido)
                } else {
                    loginError = "Correo o contraseña incorrecta."
                }
            } catch (e: Exception) {
                // Manejo básico de errores de red o BD
                loginError = "Error al intentar iniciar sesión: ${e.message}"
            }
        }
    }
}

// Necesitarás un ViewModelFactory para inyectar dependencias
// (UserRepository, PreferencesManager) a los ViewModels.
// Puedes crear un archivo ViewModelFactory.kt