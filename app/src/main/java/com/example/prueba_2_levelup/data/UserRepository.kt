package com.example.prueba_2_levelup.data

import com.example.prueba_2_levelup.data.dao.CartDao
import com.example.prueba_2_levelup.data.dao.ProductDao
import com.example.prueba_2_levelup.data.dao.UserDao
import com.example.prueba_2_levelup.data.entities.CartItemEntity
import com.example.prueba_2_levelup.data.entities.ProductEntity
import com.example.prueba_2_levelup.data.entities.UserEntity
import kotlinx.coroutines.flow.Flow

// Repositorio centraliza el acceso a los DAOs
class UserRepository(
    private val userDao: UserDao,
    private val productDao: ProductDao,
    private val cartDao: CartDao
) {

    // --- User ---
    suspend fun insertUser(user: UserEntity) = userDao.insertUser(user)
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
    suspend fun updateCartItemQuantity(itemId: Int, newQuantity: Int) {
        val item = cartDao.getCartItemsWithProductInfo(). // Need to fetch the item first (or add a getById method)

    }
    suspend fun removeFromCart(itemId: Int) = cartDao.deleteCartItemById(itemId)
    fun getCartItems() = cartDao.getCartItemsWithProductInfo() // Usar el que trae info del producto
    suspend fun clearCart() = cartDao.clearCart()
}