package com.example.prueba_2_levelup.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.prueba_2_levelup.data.UserRepository
import com.example.prueba_2_levelup.data.entities.ProductEntity
import kotlinx.coroutines.channels.Channel // Importa Channel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.receiveAsFlow // Importa receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class CatalogViewModel(private val userRepository: UserRepository) : ViewModel() {

    // --- Canal para eventos de UI (como mensajes) ---
    private val _uiEvent = Channel<UiEvent>() // Crea el canal
    val uiEvent = _uiEvent.receiveAsFlow() // Expone el canal como Flow

    // Expone el flujo de productos como StateFlow para la UI
    val products: StateFlow<List<ProductEntity>> = userRepository.getAllProducts()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000L),
            initialValue = emptyList()
        )

    // Función para añadir al carrito
    fun addToCart(productId: String) {
        viewModelScope.launch {
            try {
                userRepository.addToCart(productId)
                // --- Envía el evento de éxito ---
                _uiEvent.send(UiEvent.ShowSnackbar("¡Producto agregado al carrito!"))
            } catch (e: Exception) {
                // Envía un evento de error si falla
                _uiEvent.send(UiEvent.ShowSnackbar("Error al agregar: ${e.message}"))
            }
        }
    }

    // --- Clase sellada para definir los tipos de eventos de UI ---
    sealed class UiEvent {
        data class ShowSnackbar(val message: String) : UiEvent()
        // Puedes añadir otros eventos si los necesitas (ej. Navigate)
    }
}