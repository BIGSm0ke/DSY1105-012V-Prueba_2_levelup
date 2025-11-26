package com.example.prueba_2_levelup.data

import com.example.prueba_2_levelup.data.dao.CartDao
import com.example.prueba_2_levelup.data.dao.ProductDao
import com.example.prueba_2_levelup.data.dao.UserDao
import com.example.prueba_2_levelup.data.entities.CartItemEntity
import com.example.prueba_2_levelup.data.entities.ProductEntity
import com.example.prueba_2_levelup.data.entities.UserEntity
import com.example.prueba_2_levelup.data.entities.toEntity
import com.example.prueba_2_levelup.data.entities.toNetworkProduct
import com.example.prueba_2_levelup.data.network.ProductoApiService
import com.example.prueba_2_levelup.data.network.LoginRequest
import com.example.prueba_2_levelup.data.network.AuthResponse
import com.example.prueba_2_levelup.util.PreferencesManager
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.firstOrNull
import java.io.IOException

class UserRepository(
    private val userDao: UserDao,
    private val productDao: ProductDao,
    private val cartDao: CartDao,
    // DEPENDENCIAS PARA LA INTEGRACIÓN DE LA API Y SESIÓN
    private val preferencesManager: PreferencesManager,
    private val productoApiService: ProductoApiService
) {

    // ------------------------------------
    // --- User (Lógica de API y Room) ---
    // ------------------------------------

    suspend fun insertUser(user: UserEntity): Boolean {
        // Usar lógica de registro local si no hay API de registro implementada
        return userDao.insertUser(user) != -1L
    }

    /**
     * Realiza el login usando la API de Spring Boot y guarda el token y los datos de perfil.
     */
    suspend fun login(nombreUsuario: String, pass: String): AuthResponse? {
        val request = LoginRequest(nombreUsuario, pass)
        return try {
            val response = productoApiService.login(request)

            // 1. Guardar el token para el Interceptor (seguridad)
            preferencesManager.saveAuthToken(response.token)

            // 2. Guardar el ID (Long)
            preferencesManager.saveUserId(response.id)

            // 3. Guardar nombre de usuario y email para la pantalla de perfil
            preferencesManager.saveUsername(response.nombreUsuario)
            preferencesManager.saveEmail(response.email)

            response
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    // Método getProfileData ELIMINADO: La información del perfil se lee ahora desde PreferencesManager.

    suspend fun getUserByEmail(email: String): UserEntity? = userDao.getUserByEmail(email)
    suspend fun addPoints(userId: Int, points: Int) = userDao.addPoints(userId, points)


    // ------------------------------------
    // --- Product (Lógica de API y Room como Caché) ---
    // ------------------------------------

    // GET ALL: Obtiene productos de la API
    fun getProductsFromApi(): Flow<List<ProductEntity>> = flow {
        try {
            val networkProducts = productoApiService.getProductos()
            val productEntities = networkProducts.map { it.toEntity() }

            productDao.insertAllProducts(productEntities)

            emit(productEntities)
        } catch (e: Exception) {
            throw e
        }
    }

    // POST/INSERTAR (API + Room)
    suspend fun insertProduct(product: ProductEntity): ProductEntity {
        val networkProduct = product.toNetworkProduct()
        val createdNetworkProduct = productoApiService.crearProducto(networkProduct)

        val createdEntity = createdNetworkProduct.toEntity()
        productDao.insertProduct(createdEntity)
        return createdEntity
    }

    // PUT/ACTUALIZAR (API + Room)
    suspend fun updateProduct(product: ProductEntity) {
        val networkProduct = product.toNetworkProduct()
        val apiId = product.id

        if (apiId == 0L) {
            throw IllegalStateException("No se pudo obtener un ID Long válido para actualizar.")
        }

        val updatedNetworkProduct = productoApiService.actualizarProducto(apiId, networkProduct)

        val updatedEntity = updatedNetworkProduct.toEntity()
        productDao.updateProduct(updatedEntity)
    }

    // DELETE/ELIMINAR (API + Room)
    suspend fun deleteProduct(product: ProductEntity) {
        val apiId = product.id

        if (apiId == 0L) {
            throw IllegalStateException("No se pudo obtener un ID Long válido para eliminar.")
        }

        productoApiService.eliminarProducto(apiId)

        productDao.deleteProduct(product)
    }

    // --- Product (Métodos Locales para compatibilidad y caché) ---
    suspend fun insertAllProducts(products: List<ProductEntity>) = productDao.insertAllProducts(products)
    fun getAllProducts(): Flow<List<ProductEntity>> = productDao.getAllProducts()
    fun getProductsByCategory(category: String): Flow<List<ProductEntity>> = productDao.getProductsByCategory(category)
    suspend fun getProductById(id: Long): ProductEntity? = productDao.getProductById(id)
    suspend fun getProductCount(): Int = productDao.getProductCount()


    // ------------------------------------
    // --- Cart (Lógica Local) ---
    // ------------------------------------

    // productId usa Long
    suspend fun addToCart(productId: Long, quantity: Int = 1) {
        val existingItem = cartDao.getCartItemByProductId(productId)
        if (existingItem != null) {
            existingItem.quantity += quantity
            cartDao.updateCartItem(existingItem)
        } else {
            cartDao.insertCartItem(CartItemEntity(productId = productId, quantity = quantity))
        }
    }

    suspend fun updateCartItemQuantity(itemId: Int, newQuantity: Int) {
        // Asumiendo que CartDao tiene getCartItemById(itemId: Int)
        val item = cartDao.getCartItemById(itemId)
        if (item != null) {
            if (newQuantity > 0) {
                item.quantity = newQuantity
                cartDao.updateCartItem(item)
            } else {
                removeFromCart(itemId)
            }
        }
    }

    suspend fun removeFromCart(itemId: Int) = cartDao.deleteCartItemById(itemId)
    fun getCartItems() = cartDao.getCartItemsWithProductInfo()
    suspend fun clearCart() = cartDao.clearCart()
}