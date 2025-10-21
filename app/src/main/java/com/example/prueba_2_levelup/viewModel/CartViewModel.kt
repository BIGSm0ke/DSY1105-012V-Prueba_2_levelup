package com.example.prueba_2_levelup.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.prueba_2_levelup.data.UserRepository
import com.example.prueba_2_levelup.data.dao.CartItemWithProductInfo // Asegúrate que este import esté
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

// Data class para representar el estado del carrito en la UI
data class CartUiState(
    val items: List<CartItemWithProductInfo> = emptyList(),
    val totalPrice: Double = 0.0,
    val error: String? = null // Para mostrar errores si fallan las operaciones
)

class CartViewModel(private val userRepository: UserRepository) : ViewModel() {

    // Expone el estado del carrito (items y total)
    val cartState: StateFlow<CartUiState> = userRepository.getCartItems()
        .map { items ->
            val total = items.sumOf { it.precio * it.quantity }
            CartUiState(items = items, totalPrice = total)
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000L), // Mantener activo 5s
            initialValue = CartUiState() // Estado inicial vacío
        )

    // Función para eliminar un item del carrito
    fun removeFromCart(itemId: Int) {
        viewModelScope.launch {
            try {
                userRepository.removeFromCart(itemId)
            } catch (e: Exception) {
                // TODO: Manejar el error, quizás actualizando el state con un mensaje
                // _cartState.update { it.copy(error = "Error al eliminar: ${e.message}") }
            }
        }
    }

    // --- CORRECCIÓN AQUÍ ---
    // Función para actualizar la cantidad de un item
    fun updateQuantity(itemId: Int, newQuantity: Int) {
        // La lógica en CartScreen ya asegura que no se llame con 0 o menos si se usa el botón '-'
        // Pero validamos aquí por si acaso se llama desde otro lugar.
        if (newQuantity <= 0) {
            removeFromCart(itemId) // Si la cantidad es 0 o menos, simplemente lo eliminamos
        } else {
            viewModelScope.launch {
                try {
                    // Llama a la función del repositorio que actualiza la cantidad en la BD
                    userRepository.updateCartItemQuantity(itemId, newQuantity) // <-- DESCOMENTADO Y FUNCIONAL
                } catch (e: Exception) {
                    // TODO: Manejar el error
                    // _cartState.update { it.copy(error = "Error al actualizar cantidad: ${e.message}") }
                }
            }
        }
    }
    // --- FIN CORRECCIÓN ---

    // Función para vaciar todo el carrito
    fun clearCart() {
        viewModelScope.launch {
            try {
                userRepository.clearCart()
            } catch (e: Exception) {
                // TODO: Manejar el error
                // _cartState.update { it.copy(error = "Error al vaciar carrito: ${e.message}") }
            }
        }
    }
}