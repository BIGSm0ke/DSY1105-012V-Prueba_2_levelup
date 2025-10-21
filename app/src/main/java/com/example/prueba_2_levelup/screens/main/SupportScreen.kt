package com.example.prueba_2_levelup.screens.main

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

@Composable
fun SupportScreen(
    // Puedes añadir un ViewModel si necesitas lógica adicional
) {
    val context = LocalContext.current
    // IMPORTANTE: Reemplaza este número con el número de teléfono real de soporte, incluyendo código de país (ej. 569 para Chile Mobile)
    val supportPhoneNumber = "56912345678" // Ejemplo para Chile +56 9 1234 5678

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center // Centra el contenido
    ) {
        Icon(
            Icons.Filled.Info, // O usa un ícono específico de soporte/chat
            contentDescription = "Soporte",
            modifier = Modifier.size(64.dp),
            tint = MaterialTheme.colorScheme.primary
        )
        Spacer(modifier = Modifier.height(16.dp))

        Text(
            "Soporte Técnico",
            style = MaterialTheme.typography.headlineMedium
        )
        Spacer(modifier = Modifier.height(8.dp))

        Text(
            "¿Necesitas ayuda? Contáctanos directamente a través de WhatsApp.",
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.bodyLarge
        )
        Spacer(modifier = Modifier.height(32.dp))

        Button(
            onClick = {
                openWhatsAppChat(context, supportPhoneNumber)
            },
            modifier = Modifier.fillMaxWidth(0.8f) // Botón un poco más angosto
        ) {
            // Icon(Icons.Filled.Call, contentDescription = "WhatsApp Icon") // Reemplaza con ícono de WhatsApp si lo tienes
            Spacer(Modifier.size(ButtonDefaults.IconSpacing))
            Text("Contactar por WhatsApp")
        }
    }
}

// Función auxiliar para abrir WhatsApp
fun openWhatsAppChat(context: Context, phoneNumber: String) {
    val url = "https://api.whatsapp.com/send?phone=$phoneNumber"
    try {
        // Crea el Intent para abrir la URL de WhatsApp
        val intent = Intent(Intent.ACTION_VIEW).apply {
            data = Uri.parse(url)
            // Opcional: Especificar el paquete para asegurar que se abra WhatsApp si está instalado
            // setPackage("com.whatsapp")
        }
        context.startActivity(intent)
    } catch (e: Exception) {
        // Manejo de error si WhatsApp no está instalado o hay otro problema
        Toast.makeText(context, "No se pudo abrir WhatsApp. ¿Está instalado?", Toast.LENGTH_LONG).show()
        // O podrías intentar abrir la URL en un navegador como alternativa:
        // val webIntent = Intent(Intent.ACTION_VIEW, Uri.parse("https://wa.me/$phoneNumber"))
        // context.startActivity(webIntent)
    }
}

// --- Preview (Opcional) ---
/*
@Preview(showBackground = true)
@Composable
fun SupportScreenPreview() {
    Prueba_2_levelupTheme {
        SupportScreen()
    }
}
*/