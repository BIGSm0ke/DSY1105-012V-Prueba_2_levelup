// app/src/main/java/com/example/prueba_2_levelup/data/network/AuthResponse.kt
package com.example.prueba_2_levelup.data.network

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class AuthResponse(
    val token: String,
    val tipo: String,
    val id: Long,
    val nombreUsuario: String,
    val email: String,
    val roles: List<String>
)