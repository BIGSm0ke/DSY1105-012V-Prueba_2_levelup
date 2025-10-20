package com.example.prueba_2_levelup.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.prueba_2_levelup.data.UserRepository
import com.example.prueba_2_levelup.data.entities.ProductEntity
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class CatalogViewModel(private val userRepository: UserRepository) : ViewModel() {

    // Expone el flujo de productos como StateFlow para la UI
    val products: StateFlow<List<ProductEntity>> = userRepository.getAllProducts()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000L), // Mantiene el flujo activo 5s después de que no haya suscriptores
            initialValue = emptyList() // Valor inicial mientras carga
        )

    // Función para añadir al carrito
    fun addToCart(productId: String) {
        viewModelScope.launch {
            try {
                userRepository.addToCart(productId)
                // Opcional: Mostrar un mensaje de éxito (ej. con un Snackbar)
            } catch (e: Exception) {
                // Manejar error al añadir al carrito
            }
        }
    }
}