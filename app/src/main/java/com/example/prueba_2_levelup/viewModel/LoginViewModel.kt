package com.example.prueba_2_levelup.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.prueba_2_levelup.data.UserRepository
import com.example.prueba_2_levelup.util.PreferencesManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

/**
 * Estado de la UI para la pantalla de inicio de sesión.
 */
data class LoginUiState(
    val username: String = "",
    val password: String = "",
    val isLoading: Boolean = false,
    val isLoggedIn: Boolean = false,
    val userMessage: String? = null
)

class LoginViewModel(
    private val userRepository: UserRepository,
    private val preferencesManager: PreferencesManager
) : ViewModel() {

    private val _uiState = MutableStateFlow(LoginUiState())
    val uiState: StateFlow<LoginUiState> = _uiState.asStateFlow()

    // --- Handlers de Campos ---

    fun onUsernameChange(newUsername: String) {
        _uiState.update { it.copy(username = newUsername, userMessage = null) }
    }

    fun onPasswordChange(newPassword: String) {
        _uiState.update { it.copy(password = newPassword, userMessage = null) }
    }

    fun dismissUserMessage() {
        _uiState.update { it.copy(userMessage = null) }
    }

    // --- Lógica de Login (Usando API) ---

    fun login() {
        val currentState = _uiState.value

        if (currentState.username.isBlank() || currentState.password.isBlank()) {
            _uiState.update { it.copy(userMessage = "Por favor, ingresa usuario y contraseña.") }
            return
        }

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, userMessage = null) }

            try {
                // Llama al método del repositorio que usa la API y guarda el token
                val authResponse = userRepository.login(currentState.username, currentState.password)

                if (authResponse != null) {
                    // Login exitoso

                    // El token y el ID ya están guardados en UserRepository.login().
                    // CORREGIDO: Usar la función setLoggedIn de PreferencesManager
                    preferencesManager.setLoggedIn(true)

                    _uiState.update { it.copy(
                        isLoggedIn = true,
                        userMessage = "Login exitoso. Bienvenido, ${authResponse.nombreUsuario}"
                    ) }

                } else {
                    // Falla de autenticación
                    _uiState.update { it.copy(userMessage = "Credenciales incorrectas. Inténtalo de nuevo.") }
                }
            } catch (e: Exception) {
                e.printStackTrace()
                _uiState.update { it.copy(userMessage = "Error de conexión: Verifica que el servidor Spring esté activo.") }
            } finally {
                _uiState.update { it.copy(isLoading = false) }
            }
        }
    }
}