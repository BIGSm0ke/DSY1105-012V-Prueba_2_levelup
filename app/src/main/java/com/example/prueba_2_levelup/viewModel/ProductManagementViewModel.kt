package com.example.prueba_2_levelup.viewModel

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
import java.lang.Exception // Asegúrate de manejar correctamente las excepciones

// Asumimos que esta es la estructura completa del DTO y ProductEntity
val availableCategories = listOf(
    "General", "Juegos de Mesa", "Accesorios", "Consolas",
    "Computadores Gamers", "Sillas Gamers", "Mouse", "Mousepad",
    "Poleras Personalizadas"
)

data class ProductManagementUiState(
    val products: List<ProductEntity> = emptyList(),
    val selectedProductId: Long? = null, // CORREGIDO a Long?

    // Campos de Producto
    val productName: String = "",
    val productDescription: String = "",
    val productPrice: String = "",
    val productCategory: String = availableCategories.first(),
    val productCode: String = "",
    val productOriginalPrice: String = "",
    val productImage: String? = null,
    val isOffer: Boolean = false,
    val productStock: String = "1",

    val isLoading: Boolean = false,
    val userMessage: String? = null
)

class ProductManagementViewModel(private val userRepository: UserRepository) : ViewModel() {

    private val _uiState = MutableStateFlow(ProductManagementUiState())
    val uiState: StateFlow<ProductManagementUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            // Usamos getProductsFromApi() para forzar la carga desde el backend
            userRepository.getProductsFromApi()
                .stateIn(viewModelScope)
                .collect { productList ->
                    _uiState.update { it.copy(products = productList) }
                }
        }
    }

    // --- Handlers de campos ---
    // (Incluyendo los handlers de los 9 campos del DTO)

    fun onProductNameChange(newName: String) {
        _uiState.update { it.copy(productName = newName, userMessage = null) }
    }

    fun onProductDescriptionChange(newDesc: String) {
        _uiState.update { it.copy(productDescription = newDesc, userMessage = null) }
    }

    fun onProductCodeChange(newCode: String) {
        _uiState.update { it.copy(productCode = newCode, userMessage = null) }
    }

    fun onProductPriceChange(newPrice: String) {
        val filteredPrice = newPrice.filter { char -> char.isDigit() || char == '.' }
        if (filteredPrice.count { it == '.' } <= 1) {
            _uiState.update { it.copy(productPrice = filteredPrice, userMessage = null) }
        }
    }

    fun onProductOriginalPriceChange(newPrice: String) {
        val filteredPrice = newPrice.filter { char -> char.isDigit() || char == '.' }
        if (filteredPrice.count { it == '.' } <= 1) {
            _uiState.update { it.copy(productOriginalPrice = filteredPrice, userMessage = null) }
        }
    }

    fun onProductCategoryChange(newCategory: String) {
        _uiState.update { it.copy(productCategory = newCategory, userMessage = null) }
    }

    fun onProductStockChange(newStock: String) {
        val filteredStock = newStock.filter { it.isDigit() }
        _uiState.update { it.copy(productStock = filteredStock, userMessage = null) }
    }

    fun onIsOfferChange(isOffer: Boolean) {
        _uiState.update { it.copy(isOffer = isOffer, userMessage = null) }
    }

    fun onProductImageChange(newImage: String?) {
        _uiState.update { it.copy(productImage = newImage, userMessage = null) }
    }

    /**
     * Carga los datos de un producto (Completo)
     */
    fun loadProductForEdit(product: ProductEntity) {
        _uiState.update {
            it.copy(
                selectedProductId = product.id, // ID Long
                productName = product.nombre,
                productDescription = product.descripcion,
                productPrice = product.precio.toString(),
                productCategory = product.categoria,
                productCode = product.codigo,
                productOriginalPrice = product.precioOriginal?.toString() ?: "",
                productImage = product.imagen,
                isOffer = product.oferta,
                productStock = product.stock.toString()
            )
        }
    }

    /**
     * Limpia todos los campos
     */
    fun clearFields() {
        _uiState.update {
            it.copy(
                selectedProductId = null,
                productName = "",
                productDescription = "",
                productPrice = "",
                productCategory = availableCategories.first(),
                productCode = "",
                productOriginalPrice = "",
                productImage = null,
                isOffer = false,
                productStock = "1",
                userMessage = null
            )
        }
    }

    fun dismissUserMessage() {
        _uiState.update { it.copy(userMessage = null) }
    }

    // --- Funciones CRUD (API Call) ---

    fun saveOrUpdateProduct() {
        val currentState = _uiState.value
        val priceDouble = currentState.productPrice.toDoubleOrNull()
        val originalPriceDouble = currentState.productOriginalPrice.toDoubleOrNull()
        val stockInt = currentState.productStock.toIntOrNull()

        // Validación
        if (currentState.productName.isBlank() || priceDouble == null || priceDouble <= 0 || stockInt == null || stockInt < 0 || currentState.productCategory.isBlank()) {
            _uiState.update { it.copy(userMessage = "Error: Datos obligatorios incompletos o inválidos.") }
            return
        }

        // Si es una creación (POST), el ID inicial es 0L. Si es actualización (PUT), es el ID real.
        val initialId = currentState.selectedProductId ?: 0L
        val finalCode = currentState.productCode.ifBlank { initialId.toString() }

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            try {

                val product = ProductEntity(
                    id = initialId,
                    nombre = currentState.productName.trim(),
                    descripcion = currentState.productDescription.trim(),
                    precio = priceDouble,
                    categoria = currentState.productCategory,
                    codigo = finalCode,
                    precioOriginal = if (currentState.productOriginalPrice.isNotBlank()) originalPriceDouble else null,
                    imagen = currentState.productImage,
                    oferta = currentState.isOffer,
                    stock = stockInt
                )

                if (currentState.selectedProductId == null) {
                    // POST (Creación): Obtiene el producto con el ID real del backend
                    val createdProduct = userRepository.insertProduct(product)
                    _uiState.update { it.copy(userMessage = "Producto creado (ID: ${createdProduct.id}) con éxito") }
                } else {
                    // PUT (Actualización)
                    userRepository.updateProduct(product)
                    _uiState.update { it.copy(userMessage = "Producto actualizado con éxito") }
                }

                // Forzar una recarga del catálogo para obtener el producto actualizado/creado
                userRepository.getProductsFromApi().collect { }

                clearFields()

            } catch (e: Exception) {
                // Aquí se capturan los errores HTTP (como el 401)
                _uiState.update { it.copy(userMessage = "Error al actualizar (401/Red): ${e.message}") }
            } finally {
                _uiState.update { it.copy(isLoading = false) }
            }
        }
    }

    fun deleteProduct() {
        val currentState = _uiState.value
        if (currentState.selectedProductId == null || currentState.selectedProductId == 0L) return

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            try {
                // Se construye el objeto ProductEntity mínimo necesario para la eliminación
                val productToDelete = ProductEntity(
                    id = currentState.selectedProductId,
                    nombre = currentState.productName,
                    descripcion = currentState.productDescription,
                    precio = currentState.productPrice.toDoubleOrNull() ?: 0.0,
                    categoria = currentState.productCategory,
                    codigo = currentState.productCode.ifBlank { currentState.selectedProductId.toString() },
                    precioOriginal = currentState.productOriginalPrice.toDoubleOrNull(),
                    imagen = currentState.productImage,
                    oferta = currentState.isOffer,
                    stock = currentState.productStock.toIntOrNull() ?: 0
                )

                userRepository.deleteProduct(productToDelete)
                _uiState.update { it.copy(userMessage = "Producto eliminado") }

                // Forzar una recarga del catálogo
                userRepository.getProductsFromApi().collect { }

                clearFields()

            } catch (e: Exception) {
                _uiState.update { it.copy(userMessage = "Error al eliminar (401/Red): ${e.message}") }
            } finally {
                _uiState.update { it.copy(isLoading = false) }
            }
        }
    }
}