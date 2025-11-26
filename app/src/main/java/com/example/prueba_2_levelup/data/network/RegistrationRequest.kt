// app/src/main/java/com/example/prueba_2_levelup/data/network/RegistrationRequest.kt
package com.example.prueba_2_levelup.data.network

import com.squareup.moshi.Json

data class RegistrationRequest(
    @Json(name = "nombreUsuario") val nombreUsuario: String,
    @Json(name = "email") val email: String,
    @Json(name = "password") val password: String,
    @Json(name = "nombre") val nombre: String,
    @Json(name = "apellido") val apellido: String,
    @Json(name = "telefono") val telefono: String,
    @Json(name = "direccion") val direccion: String,
    // El rol se asigna automáticamente y no se pide en el formulario
    @Json(name = "roles") val roles: List<String> = listOf("ROL_USUARIO")
)