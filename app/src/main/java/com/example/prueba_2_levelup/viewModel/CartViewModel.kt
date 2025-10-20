package com.example.prueba_2_levelup.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.prueba_2_levelup.data.UserRepository
import com.example.prueba_2_levelup.data.dao.CartItemWithProductInfo
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

// Data class para representar el estado del carrito en la UI
data class CartUiState(
    val items: List<CartItemWithProductInfo> = emptyList(),
    val totalPrice: Double = 0.0
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
            started = SharingStarted.WhileSubscribed(5000L),
            initialValue = CartUiState() // Estado inicial vacío
        )

    fun removeFromCart(itemId: Int) {
        viewModelScope.launch {
            try {
                userRepository.removeFromCart(itemId)
            } catch (e: Exception) {
                // Manejar error
            }
        }
    }

    fun updateQuantity(itemId: Int, newQuantity: Int) {
        if (newQuantity <= 0) {
            removeFromCart(itemId) // Si la cantidad es 0 o menos, eliminar
        } else {
            viewModelScope.launch {
                try {
                    // Nota: Necesitas ajustar UserRepository/CartDao para tener una forma
                    // eficiente de actualizar la cantidad por ID.
                    // Por ahora, asumimos que existe userRepository.updateCartItemQuantity(itemId, newQuantity)
                    // userRepository.updateCartItemQuantity(itemId, newQuantity) // Descomentar cuando implementes
                } catch (e: Exception) {
                    // Manejar error
                }
            }
        }
    }

    fun clearCart() {
        viewModelScope.launch {
            try {
                userRepository.clearCart()
            } catch (e: Exception) {
                // Manejar error
            }
        }
    }
}