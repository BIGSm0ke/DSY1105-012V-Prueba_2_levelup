package com.example.prueba_2_levelup.data

import com.example.prueba_2_levelup.data.dao.CartDao
import com.example.prueba_2_levelup.data.dao.ProductDao
import com.example.prueba_2_levelup.data.dao.UserDao
import com.example.prueba_2_levelup.data.entities.CartItemEntity
import com.example.prueba_2_levelup.data.entities.ProductEntity
import com.example.prueba_2_levelup.data.entities.UserEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull // Importar para obtener el valor del Flow

// Repositorio centraliza el acceso a los DAOs
class UserRepository(
    private val userDao: UserDao,
    private val productDao: ProductDao,
    private val cartDao: CartDao
) {

    // --- User ---
    suspend fun insertUser(user: UserEntity): Boolean {
        return userDao.insertUser(user) != -1L
    }
    suspend fun getUserByEmail(email: String): UserEntity? = userDao.getUserByEmail(email)
    suspend fun login(email: String, pass: String): UserEntity? = userDao.getUserByCredentials(email, pass)
    suspend fun addPoints(userId: Int, points: Int) = userDao.addPoints(userId, points)

    // --- Product ---
    suspend fun insertProduct(product: ProductEntity) = productDao.insertProduct(product)
    suspend fun insertAllProducts(products: List<ProductEntity>) = productDao.insertAllProducts(products)
    fun getAllProducts(): Flow<List<ProductEntity>> = productDao.getAllProducts()
    fun getProductsByCategory(category: String): Flow<List<ProductEntity>> = productDao.getProductsByCategory(category)
    suspend fun getProductById(id: String): ProductEntity? = productDao.getProductById(id)
    suspend fun getProductCount(): Int = productDao.getProductCount()

    suspend fun updateProduct(product: ProductEntity) = productDao.updateProduct(product)
    suspend fun deleteProduct(product: ProductEntity) = productDao.deleteProduct(product)

    // --- Cart ---
    suspend fun addToCart(productId: String, quantity: Int = 1) {
        val existingItem = cartDao.getCartItemByProductId(productId)
        if (existingItem != null) {
            existingItem.quantity += quantity
            cartDao.updateCartItem(existingItem)
        } else {
            cartDao.insertCartItem(CartItemEntity(productId = productId, quantity = quantity))
        }
    }

    // --- CORRECCIÓN AQUÍ ---
    suspend fun updateCartItemQuantity(itemId: Int, newQuantity: Int) {
        // Necesitamos una función en CartDao para obtener un item por su ID
        // Asumamos que creamos: suspend fun getCartItemById(id: Int): CartItemEntity?
        val item = cartDao.getCartItemById(itemId) // Llama a la nueva función (que debes crear en CartDao)
        if (item != null) {
            if (newQuantity > 0) {
                item.quantity = newQuantity
                cartDao.updateCartItem(item) // Actualiza el item en la BD
            } else {
                // Si la nueva cantidad es 0 o menos, eliminamos el item
                removeFromCart(itemId)
            }
        }
        // Puedes añadir manejo de errores si item es null
    }
    // --- FIN CORRECCIÓN ---

    suspend fun removeFromCart(itemId: Int) = cartDao.deleteCartItemById(itemId)
    fun getCartItems() = cartDao.getCartItemsWithProductInfo() // Usar el que trae info del producto
    suspend fun clearCart() = cartDao.clearCart()
}