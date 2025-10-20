package com.example.prueba_2_levelup.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.prueba_2_levelup.data.entities.ProductEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ProductDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE) // Reemplazar si se actualiza info del producto
    suspend fun insertProduct(product: ProductEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllProducts(products: List<ProductEntity>) // Para poblar la BD

    @Query("SELECT * FROM products ORDER BY categoria, nombre")
    fun getAllProducts(): Flow<List<ProductEntity>> // Flow para observar cambios

    @Query("SELECT * FROM products WHERE categoria = :category ORDER BY nombre")
    fun getProductsByCategory(category: String): Flow<List<ProductEntity>>

    @Query("SELECT * FROM products WHERE id = :productId LIMIT 1")
    suspend fun getProductById(productId: String): ProductEntity?

    @Query("SELECT COUNT(*) FROM products")
    suspend fun getProductCount(): Int // Para verificar si ya se poblaron los datos
}