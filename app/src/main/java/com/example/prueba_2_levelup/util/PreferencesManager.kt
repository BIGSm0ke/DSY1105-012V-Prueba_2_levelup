package com.example.prueba_2_levelup.util

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

// Extensión para crear el DataStore
val Context.dataStore by preferencesDataStore(name = "user_prefs")

class PreferencesManager(private val context: Context) {

    companion object {
        // Claves para guardar los datos
        val IS_LOGGED_IN = booleanPreferencesKey("is_logged_in")
        val USER_ID = intPreferencesKey("user_id") // Guardar ID para saber quién es
        val USER_EMAIL = stringPreferencesKey("user_email")
        // Podrías guardar nombre/apellido aquí o recuperarlos de la BD usando el ID
    }

    // Guarda el estado de login y datos básicos del usuario
    suspend fun saveLoginState(isLoggedIn: Boolean, userId: Int = -1, email: String = "") {
        context.dataStore.edit { preferences ->
            preferences[IS_LOGGED_IN] = isLoggedIn
            if (isLoggedIn) {
                preferences[USER_ID] = userId
                preferences[USER_EMAIL] = email
            } else {
                // Si cierra sesión, limpiamos los datos
                preferences.remove(USER_ID)
                preferences.remove(USER_EMAIL)
            }
        }
    }

    // Flujo para saber si el usuario está logueado
    val isLoggedInFlow: Flow<Boolean> = context.dataStore.data
        .map { preferences ->
            preferences[IS_LOGGED_IN] ?: false
        }

    // Flujo para obtener el ID del usuario actual (si está logueado)
    val userIdFlow: Flow<Int?> = context.dataStore.data
        .map { preferences ->
            preferences[USER_ID]
        }

    // Flujo para obtener el email del usuario actual (si está logueado)
    val userEmailFlow: Flow<String?> = context.dataStore.data
        .map { preferences ->
            preferences[USER_EMAIL]
        }

    // Función específica para logout
    suspend fun logout() {
        saveLoginState(false)
    }
}