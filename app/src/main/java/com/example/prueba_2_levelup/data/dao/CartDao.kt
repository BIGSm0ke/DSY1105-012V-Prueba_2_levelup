package com.example.prueba_2_levelup.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.prueba_2_levelup.data.entities.CartItemEntity
import kotlinx.coroutines.flow.Flow

// Clase de datos para combinar CartItem y Product (para mostrar en la UI del carrito)
data class CartItemWithProductInfo(
    val id: Int,            // ID del CartItem
    val productId: String,  // ID del Producto
    var quantity: Int,      // Cantidad en el carrito
    val nombre: String,     // Nombre del Producto
    val precio: Double,     // Precio del Producto
    // Podrías añadir imageUrl si la tienes en ProductEntity
    // val imageUrl: String?
)


@Dao
interface CartDao {
    // Inserta un item, ignora si el producto ya está (gracias al índice en CartItemEntity)
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertCartItem(item: CartItemEntity): Long // Devuelve -1 si falla

    // Actualiza un item existente (ej. cambiar cantidad)
    @Update
    suspend fun updateCartItem(item: CartItemEntity)

    // Obtiene un CartItem por el ID del producto (para ver si ya existe)
    @Query("SELECT * FROM cart_items WHERE productId = :productId LIMIT 1")
    suspend fun getCartItemByProductId(productId: String): CartItemEntity?

    // Obtiene un CartItem por su propio ID (para actualizar cantidad o borrar)
    @Query("SELECT * FROM cart_items WHERE id = :itemId LIMIT 1")
    suspend fun getCartItemById(itemId: Int): CartItemEntity?

    // Obtiene todos los items del carrito junto con la información del producto asociado
    // Usa un JOIN para combinar las tablas cart_items y products
    @Query("SELECT ci.id, ci.productId, ci.quantity, p.nombre, p.precio " +
            "FROM cart_items ci JOIN products p ON ci.productId = p.id " +
            "ORDER BY p.nombre") // Ordenar por nombre de producto
    fun getCartItemsWithProductInfo(): Flow<List<CartItemWithProductInfo>>

    // Elimina un item del carrito por su ID
    @Query("DELETE FROM cart_items WHERE id = :itemId")
    suspend fun deleteCartItemById(itemId: Int)

    // Elimina todos los items del carrito
    @Query("DELETE FROM cart_items")
    suspend fun clearCart()
}