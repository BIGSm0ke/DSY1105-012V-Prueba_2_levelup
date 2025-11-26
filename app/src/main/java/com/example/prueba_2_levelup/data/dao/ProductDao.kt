package com.example.prueba_2_levelup.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.prueba_2_levelup.data.entities.ProductEntity
import kotlinx.coroutines.flow.Flow

// ... imports

@Dao
interface ProductDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProduct(product: ProductEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllProducts(products: List<ProductEntity>)

    @Update
    suspend fun updateProduct(product: ProductEntity)

    @Delete
    suspend fun deleteProduct(product: ProductEntity)

    @Query("SELECT * FROM products ORDER BY categoria, nombre")
    fun getAllProducts(): Flow<List<ProductEntity>>

    @Query("SELECT * FROM products WHERE categoria = :category ORDER BY nombre")
    fun getProductsByCategory(category: String): Flow<List<ProductEntity>>

    @Query("SELECT * FROM products WHERE id = :productId LIMIT 1")
    suspend fun getProductById(productId: Long): ProductEntity? // CAMBIO: De String a Long

    @Query("SELECT COUNT(*) FROM products")
    suspend fun getProductCount(): Int
}