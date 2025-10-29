package com.example.prueba_2_levelup.data.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "products")
data class ProductEntity(
    @PrimaryKey val id: String,
    val nombre: String,
    val precio: Double,
    val descripcion: String,
    val categoria: String,
    // El campo 'imageUrl' ha sido eliminado
)