package com.example.prueba_2_levelup.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.prueba_2_levelup.data.entities.UserEntity

@Dao
interface UserDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE) // Ignorar si el correo ya existe
    suspend fun insertUser(user: UserEntity)

    @Query("SELECT * FROM users WHERE correo = :correo LIMIT 1")
    suspend fun getUserByEmail(correo: String): UserEntity? // Para verificar si existe al registrar

    @Query("SELECT * FROM users WHERE correo = :correo AND contrasena = :contrasena LIMIT 1")
    suspend fun getUserByCredentials(correo: String, contrasena: String): UserEntity? // Para login

    // Podrías añadir funciones para actualizar puntos, etc.
    @Query("UPDATE users SET puntosLevelUp = puntosLevelUp + :points WHERE id = :userId")
    suspend fun addPoints(userId: Int, points: Int)
}