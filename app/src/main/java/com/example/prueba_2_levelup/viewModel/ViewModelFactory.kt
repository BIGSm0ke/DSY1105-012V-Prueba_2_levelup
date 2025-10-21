package com.example.prueba_2_levelup.viewModel // O el paquete donde lo crees

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.prueba_2_levelup.data.UserRepository
import com.example.prueba_2_levelup.util.PreferencesManager

// Esta clase sabe cómo crear tus ViewModels específicos
class ViewModelFactory(
    private val userRepository: UserRepository,
    private val preferencesManager: PreferencesManager
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            // Si se pide un LoginViewModel, créalo con sus dependencias
            modelClass.isAssignableFrom(LoginViewModel::class.java) -> {
                LoginViewModel(userRepository, preferencesManager) as T
            }
            // Si se pide un RegistrationViewModel...
            modelClass.isAssignableFrom(RegistrationViewModel::class.java) -> {
                RegistrationViewModel(userRepository) as T
            }
            // Si se pide un CatalogViewModel...
            modelClass.isAssignableFrom(CatalogViewModel::class.java) -> {
                CatalogViewModel(userRepository) as T
            }
            // Si se pide un CartViewModel...
            modelClass.isAssignableFrom(CartViewModel::class.java) -> {
                CartViewModel(userRepository) as T
            }
            // Si se pide un ProfileViewModel...
            modelClass.isAssignableFrom(ProfileViewModel::class.java) -> {
                ProfileViewModel(userRepository, preferencesManager) as T
            }
            // Si no es ninguno de los anteriores, lanza una excepción
            else -> throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
        }
    }
}