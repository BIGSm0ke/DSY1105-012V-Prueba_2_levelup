package com.example.prueba_2_levelup.viewModel

// Import 'Uri' ya NO es necesario
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.prueba_2_levelup.data.UserRepository
import com.example.prueba_2_levelup.data.entities.ProductEntity
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

/**
 * Estado de la UI (SIN la imagen)
 */
data class ProductManagementUiState(
    val products: List<ProductEntity> = emptyList(),
    val selectedProductId: String? = null,
    val productName: String = "",
    val productDescription: String = "",
    val productPrice: String = "",
    // 'selectedImageUri' ha sido eliminado
    val isLoading: Boolean = false,
    val userMessage: String? = null
)

class ProductManagementViewModel(private val userRepository: UserRepository) : ViewModel() {

    private val _uiState = MutableStateFlow(ProductManagementUiState())
    val uiState: StateFlow<ProductManagementUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            userRepository.getAllProducts()
                .stateIn(viewModelScope)
                .collect { productList ->
                    _uiState.update { it.copy(products = productList) }
                }
        }
    }

    // --- Funciones para actualizar campos (sin cambios) ---

    fun onProductNameChange(newName: String) {
        _uiState.update { it.copy(productName = newName, userMessage = null) }
    }

    fun onProductDescriptionChange(newDesc: String) {
        _uiState.update { it.copy(productDescription = newDesc, userMessage = null) }
    }

    fun onProductPriceChange(newPrice: String) {
        val filteredPrice = newPrice.filter { char -> char.isDigit() || char == '.' }
        if (filteredPrice.count { it == '.' } <= 1) {
            _uiState.update { it.copy(productPrice = filteredPrice, userMessage = null) }
        }
    }

    // --- La función 'onImageSelected' ha sido eliminada ---

    /**
     * Carga los datos de un producto (SIN la imagen)
     */
    fun loadProductForEdit(product: ProductEntity) {
        _uiState.update {
            it.copy(
                selectedProductId = product.id,
                productName = product.nombre,
                productDescription = product.descripcion,
                productPrice = product.precio.toString()
                // La lógica de 'selectedImageUri' ha sido eliminada
            )
        }
    }

    /**
     * Limpia todos los campos (SIN la imagen)
     */
    fun clearFields() {
        _uiState.update {
            it.copy(
                selectedProductId = null,
                productName = "",
                productDescription = "",
                productPrice = "",
                // 'selectedImageUri' ha sido eliminado
                userMessage = null
            )
        }
    }

    fun dismissUserMessage() {
        _uiState.update { it.copy(userMessage = null) }
    }

    // --- Funciones CRUD (SIN la imagen) ---

    fun saveOrUpdateProduct() {
        val currentState = _uiState.value
        val priceDouble = currentState.productPrice.toDoubleOrNull()

        if (currentState.productName.isBlank() || priceDouble == null || priceDouble <= 0) {
            _uiState.update { it.copy(userMessage = "Error: Nombre y Precio son obligatorios.") }
            return
        }

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            try {
                // La lógica de 'finalImagePath' ha sido eliminada

                val product = ProductEntity(
                    id = currentState.selectedProductId ?: generateProductId(),
                    nombre = currentState.productName.trim(),
                    descripcion = currentState.productDescription.trim(),
                    precio = priceDouble,
                    // 'imageUrl' ha sido eliminado
                    categoria = "General"
                )

                if (currentState.selectedProductId == null) {
                    userRepository.insertProduct(product)
                } else {
                    userRepository.updateProduct(product)
                }

                _uiState.update { it.copy(userMessage = "Producto guardado con éxito") }
                clearFields()

            } catch (e: Exception) {
                _uiState.update { it.copy(userMessage = "Error al guardar: ${e.message}") }
            } finally {
                _uiState.update { it.copy(isLoading = false) }
            }
        }
    }

    fun deleteProduct() {
        val currentState = _uiState.value
        if (currentState.selectedProductId == null) return

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            try {
                val productToDelete = ProductEntity(
                    id = currentState.selectedProductId,
                    nombre = currentState.productName,
                    descripcion = currentState.productDescription,
                    precio = currentState.productPrice.toDoubleOrNull() ?: 0.0,
                    // 'imageUrl' ha sido eliminado
                    categoria = "General"
                )

                userRepository.deleteProduct(productToDelete)
                _uiState.update { it.copy(userMessage = "Producto eliminado") }
                clearFields()

            } catch (e: Exception) {
                _uiState.update { it.copy(userMessage = "Error al eliminar: ${e.message}") }
            } finally {
                _uiState.update { it.copy(isLoading = false) }
            }
        }
    }

    private fun generateProductId(): String {
        return "PROD_${System.currentTimeMillis()}"
    }
}