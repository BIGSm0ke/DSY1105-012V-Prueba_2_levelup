// app/src/main/java/com/example/prueba_2_levelup/data/network/LoginRequest.kt
package com.example.prueba_2_levelup.data.network

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class LoginRequest(
    val nombreUsuario: String,
    val password: String
)