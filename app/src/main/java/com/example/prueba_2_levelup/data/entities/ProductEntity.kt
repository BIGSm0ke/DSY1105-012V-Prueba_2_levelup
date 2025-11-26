package com.example.prueba_2_levelup.data.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.prueba_2_levelup.data.network.NetworkProduct

@Entity(tableName = "products")
data class ProductEntity(
    @PrimaryKey val id: Long, // CAMBIO CRÍTICO: De String a Long
    val codigo: String,
    val categoria: String,
    val nombre: String,
    val precio: Double,
    val precioOriginal: Double?,
    val imagen: String?,
    val descripcion: String,
    val oferta: Boolean,
    val stock: Int
)

// Función de mapeo (NetworkProduct -> ProductEntity)
fun NetworkProduct.toEntity(): ProductEntity {
    return ProductEntity(
        id = this.id, // Ahora Long
        codigo = this.codigo ?: "N/A",
        categoria = this.categoria ?: "General",
        nombre = this.nombre ?: "Producto Desconocido",
        precio = this.precio ?: 0.0,
        precioOriginal = this.precioOriginal,
        imagen = this.imagen,
        descripcion = this.descripcion ?: "Sin descripción",
        oferta = this.oferta ?: false,
        stock = this.stock ?: 0
    )
}

// Función de mapeo (ProductEntity -> NetworkProduct)
fun ProductEntity.toNetworkProduct(): NetworkProduct {
    return NetworkProduct(
        id = this.id, // Ahora Long
        codigo = this.codigo,
        categoria = this.categoria,
        nombre = this.nombre,
        precio = this.precio,
        precioOriginal = this.precioOriginal,
        imagen = this.imagen,
        descripcion = this.descripcion,
        descripcionProducto = this.descripcion,
        oferta = this.oferta,
        especificaciones = null,
        stock = this.stock,
        activo = true,
        porcentajeDescuento = null
    )
}