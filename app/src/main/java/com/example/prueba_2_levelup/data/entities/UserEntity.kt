package com.example.prueba_2_levelup.data.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users")
data class UserEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val nombre: String,
    val apellido: String,
    // val rut: String, // No se pide explícitamente en el PDF para registro, considera si añadirlo
    val correo: String, // Debe ser único
    val contrasena: String,
    val fechaNacimiento: String, // Guardar como texto "YYYY-MM-DD" para simplicidad, o usar TypeConverter para Date/LocalDate
    val esDuoc: Boolean = false, // Determinar al registrar por el correo
    val puntosLevelUp: Int = 0 // Inicia en 0
)