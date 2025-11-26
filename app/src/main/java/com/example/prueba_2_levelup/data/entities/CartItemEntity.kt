package com.example.prueba_2_levelup.data.entities

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "cart_items",
    foreignKeys = [ForeignKey(
        entity = ProductEntity::class,
        parentColumns = ["id"],
        childColumns = ["productId"],
        onDelete = ForeignKey.CASCADE
    )]
)
data class CartItemEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val productId: Long, // CAMBIO CRÍTICO: De String a Long
    var quantity: Int
)