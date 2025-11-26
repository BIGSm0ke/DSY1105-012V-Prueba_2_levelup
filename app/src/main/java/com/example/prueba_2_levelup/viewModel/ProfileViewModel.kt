// app/src/main/java/com/example/prueba_2_levelup/viewModel/ProfileViewModel.kt
package com.example.prueba_2_levelup.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.prueba_2_levelup.data.UserRepository
import com.example.prueba_2_levelup.util.PreferencesManager
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow

// Nota: Eliminamos ProfileUiState y SimpleUserProfile

class ProfileViewModel(
    private val userRepository: UserRepository,
    val preferencesManager: PreferencesManager // Mantener para acceder al estado de sesión y token
) : ViewModel() {

    // Función principal requerida: Cerrar sesión
    fun logout() {
        viewModelScope.launch {
            // Llama al método que borra el token y establece isLoggedIn = false
            preferencesManager.logout()
            // La pantalla se redirigirá automáticamente al detectar el cambio en el Flow
        }
    }

    // Nota: El ViewModel ya no carga datos de perfil ni maneja estados complejos.
    // Si necesitas un estado de carga (isLoading), deberías reintroducir el patrón UiState.
    // Para cumplir con el requisito de 'solo un botón', este diseño es suficiente.
}