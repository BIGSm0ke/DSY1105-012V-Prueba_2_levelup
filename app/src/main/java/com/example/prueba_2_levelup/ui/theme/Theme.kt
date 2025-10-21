package com.example.prueba_2_levelup.ui.theme

import android.os.Build
import androidx.compose.ui.graphics.Color
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext

// Define el esquema de colores OSCURO usando la paleta Level-Up
private val DarkColorScheme = darkColorScheme(
    primary = LevelUpElectricBlue,       // Color primario (botones principales, acentos)
    secondary = LevelUpNeonGreen,      // Color secundario (otros acentos)
    tertiary = LevelUpLightGray,       // Color terciario (podría ser para bordes o elementos menos importantes)
    background = LevelUpBlack,         // Color de fondo de las pantallas
    surface = LevelUpBlack,            // Color de superficies como Cards, BottomNav (puede ser un gris oscuro si prefieres contraste)
    onPrimary = LevelUpWhite,          // Color del texto sobre el color primario (ej. texto en botón azul)
    onSecondary = LevelUpBlack,          // Color del texto sobre el color secundario (ej. texto en botón verde)
    onTertiary = LevelUpBlack,         // Color del texto sobre el color terciario
    onBackground = LevelUpWhite,       // Color del texto principal sobre el fondo
    onSurface = LevelUpWhite,          // Color del texto sobre superficies
    surfaceVariant = Color(0xFF1A1A1A), // Un gris oscuro para superficies alternativas si es necesario
    onSurfaceVariant = LevelUpLightGray, // Texto secundario sobre surfaceVariant
    error = Color(0xFFFF5252),         // Un color rojo para errores
    onError = LevelUpBlack             // Texto sobre color de error
)

// Opcional: Podrías definir un LightColorScheme si quisieras dar la opción de tema claro
// private val LightColorScheme = lightColorScheme(...)

@Composable
fun Prueba_2_levelupTheme(
    darkTheme: Boolean = true, // Forzamos el tema oscuro como pide el PDF
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = false, // Desactivamos color dinámico para mantener la paleta fija
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
        // Siempre usamos nuestro DarkColorScheme personalizado
        else -> DarkColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography, // Asegúrate que Type.kt esté configurado
        content = content
    )
}