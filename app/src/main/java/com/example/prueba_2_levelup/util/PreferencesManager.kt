package com.example.prueba_2_levelup.util

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

val Context.dataStore by preferencesDataStore(name = "user_prefs")

class PreferencesManager(private val context: Context) {

    companion object {
        val IS_LOGGED_IN = booleanPreferencesKey("is_logged_in")
        val USER_ID_LONG = longPreferencesKey("user_id_long")
        val USER_EMAIL = stringPreferencesKey("user_email")
        val AUTH_TOKEN = stringPreferencesKey("auth_token")
        val USER_USERNAME = stringPreferencesKey("user_username") // NUEVA CLAVE
    }

    // --- Funciones de guardado ---

    suspend fun setLoggedIn(isLoggedIn: Boolean) { /* ... */ }
    suspend fun saveAuthToken(token: String) { /* ... */ }
    suspend fun saveUserId(id: Long) { /* ... */ }

    // NUEVA FUNCIÓN: Guarda el nombre de usuario
    suspend fun saveUsername(username: String) {
        context.dataStore.edit { preferences ->
            preferences[USER_USERNAME] = username
        }
    }

    // NUEVA FUNCIÓN: Guarda el email (actualizamos la existente si era necesario)
    suspend fun saveEmail(email: String) {
        context.dataStore.edit { preferences ->
            preferences[USER_EMAIL] = email
        }
    }

    suspend fun clearUserData() { /* ... */ }

    // --- Flujos de lectura ---

    val isLoggedInFlow: Flow<Boolean> = context.dataStore.data
        .map { preferences -> preferences[IS_LOGGED_IN] ?: false }

    val authTokenFlow: Flow<String?> = context.dataStore.data
        .map { preferences -> preferences[AUTH_TOKEN] }

    val userIdFlow: Flow<Long?> = context.dataStore.data
        .map { preferences -> preferences[USER_ID_LONG] }

    val userEmailFlow: Flow<String?> = context.dataStore.data
        .map { preferences -> preferences[USER_EMAIL] }

    val usernameFlow: Flow<String?> = context.dataStore.data // NUEVO FLUJO
        .map { preferences -> preferences[USER_USERNAME] }

    suspend fun logout() { /* ... */ }
}