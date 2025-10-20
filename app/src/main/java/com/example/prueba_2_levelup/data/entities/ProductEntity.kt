package com.example.prueba_2_levelup.data.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "products")
data class ProductEntity(
    @PrimaryKey val id: String, // "JM001", "AC001", etc.
    val categoria: String,
    val nombre: String,
    val precio: Double,
    val descripcion: String
    // Podrías añadir un campo para URL de imagen si quieres mostrarlas
    // val imageUrl: String? = null
)