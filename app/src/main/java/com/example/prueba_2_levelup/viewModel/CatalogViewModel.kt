// app/src/main/java/com/example/prueba_2_levelup/viewModel/CatalogViewModel.kt
package com.example.prueba_2_levelup.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.prueba_2_levelup.data.UserRepository
import com.example.prueba_2_levelup.data.entities.ProductEntity
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.catch // <--- NUEVA IMPORTACIÓN NECESARIA

class CatalogViewModel(private val userRepository: UserRepository) : ViewModel() {

    // --- Canal para eventos de UI (como mensajes) ---
    private val _uiEvent = Channel<UiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    // MODIFICACIÓN: Expone el flujo de productos trayendo la información desde la API de Spring
    val products: StateFlow<List<ProductEntity>> = userRepository.getProductsFromApi()
        .catch { e ->
            // Manejo de errores de flujo/red
            e.printStackTrace()
            // Referencia correcta: UiEvent.ShowSnackbar
            _uiEvent.send(UiEvent.ShowSnackbar("Error al cargar productos: ${e.message}"))
            emit(emptyList())
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000L),
            initialValue = emptyList()
        )

    // Función para añadir al carrito (Uso correcto original)
    fun addToCart(productId: Long) { // CAMBIO: De String a Long
        viewModelScope.launch {
            try {
                userRepository.addToCart(productId)
                // ...
            } catch (e: Exception) {
                // ...
            }
        }
    }

    // --- Clase sellada para definir los tipos de eventos de UI ---
    sealed class UiEvent {
        data class ShowSnackbar(val message: String) : UiEvent()
        // Puedes añadir otros eventos si los necesitas (ej. Navigate)
    }
}