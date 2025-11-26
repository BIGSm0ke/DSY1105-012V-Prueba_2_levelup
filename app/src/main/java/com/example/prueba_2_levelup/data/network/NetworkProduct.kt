package com.example.prueba_2_levelup.data.network

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class NetworkProduct(
    // Nota: Usamos 'Long' para el ID, igual que en el backend (ProductoDTO.java)
    val id: Long,
    val codigo: String?,
    val categoria: String?,
    val nombre: String?,
    val precio: Double?,
    val precioOriginal: Double?,
    val imagen: String?,
    val descripcion: String?,
    val descripcionProducto: String?,
    val oferta: Boolean?,
    val especificaciones: String?,
    val stock: Int?,
    val activo: Boolean?,
    val porcentajeDescuento: Int?
)