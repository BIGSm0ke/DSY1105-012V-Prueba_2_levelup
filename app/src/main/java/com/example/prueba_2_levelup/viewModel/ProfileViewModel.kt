package com.example.prueba_2_levelup.viewModel


import kotlinx.coroutines.flow.flow
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.prueba_2_levelup.data.UserRepository
import com.example.prueba_2_levelup.data.entities.UserEntity
import com.example.prueba_2_levelup.util.PreferencesManager
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

data class ProfileUiState(
    val userEmail: String = "",
    val userName: String = "",
    val userLastName: String = "",
    val levelUpPoints: Int = 0,
    val isLoading: Boolean = true
)

class ProfileViewModel(
    private val userRepository: UserRepository,
    private val preferencesManager: PreferencesManager
) : ViewModel() {

    // Combina el flujo del ID de usuario de DataStore con la consulta a la BD
    val profileState: StateFlow<ProfileUiState> = preferencesManager.userEmailFlow
        .filterNotNull() // Solo procede si hay un email guardado (usuario logueado)
        .flatMapLatest { email ->
            // Cuando el email cambia, busca el usuario en la BD
            // Nota: Sería mejor buscar por ID si lo guardas en PreferencesManager
            // O crear un flujo en UserRepository que devuelva el UserEntity basado en el ID/email
            flow { emit(userRepository.getUserByEmail(email)) } // Asumiendo que getUserByEmail es suspend
        }
        .map { user ->
            if (user != null) {
                ProfileUiState(
                    userEmail = user.correo,
                    userName = user.nombre,
                    userLastName = user.apellido,
                    levelUpPoints = user.puntosLevelUp,
                    isLoading = false
                )
            } else {
                ProfileUiState(isLoading = false) // Usuario no encontrado o error
            }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000L),
            initialValue = ProfileUiState() // Estado inicial cargando
        )

    fun logout(onLogoutComplete: () -> Unit) {
        viewModelScope.launch {
            preferencesManager.logout()
            onLogoutComplete() // Llama al callback para navegar al login
        }
    }
}

// Necesitarás importar kotlinx.coroutines.flow.flow
