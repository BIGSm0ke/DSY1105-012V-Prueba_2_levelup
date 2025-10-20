package com.example.prueba_2_levelup.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.prueba_2_levelup.data.entities.CartItemEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface CartDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertCartItem(item: CartItemEntity)

    @Update
    suspend fun updateCartItem(item: CartItemEntity)

    @Query("SELECT * FROM cart_items WHERE productId = :productId LIMIT 1")
    suspend fun getCartItemByProductId(productId: String): CartItemEntity?

    @Query("SELECT ci.*, p.nombre, p.precio FROM cart_items ci JOIN products p ON ci.productId = p.id")
    fun getCartItemsWithProductInfo(): Flow<List<CartItemWithProductInfo>> // Para mostrar en el carrito

    @Query("DELETE FROM cart_items WHERE id = :itemId")
    suspend fun deleteCartItemById(itemId: Int)

    @Query("DELETE FROM cart_items")
    suspend fun clearCart() // Para vaciar el carrito
}

// Clase de datos para combinar CartItem y Product (para mostrar en la UI del carrito)
data class CartItemWithProductInfo(
    val id: Int,
    val productId: String,
    var quantity: Int,
    val nombre: String,
    val precio: Double
)