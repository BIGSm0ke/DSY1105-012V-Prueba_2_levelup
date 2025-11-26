package com.example.prueba_2_levelup.data.network

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class UsuarioDTO(
    val id: Long,
    val nombreUsuario: String,
    val email: String,
    val nombre: String? = null,
    val apellido: String? = null,
    val roles: List<String>? = null
)