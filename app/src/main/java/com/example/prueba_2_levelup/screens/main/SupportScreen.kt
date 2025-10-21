package com.example.prueba_2_levelup.screens.main

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info // Ícono de información
import androidx.compose.material.icons.filled.Send // Ícono alternativo
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

@Composable
fun SupportScreen() {
    val context = LocalContext.current
    // IMPORTANTE: Reemplaza este número!
    val supportPhoneNumber = "56912345678" // Incluye código de país

    // Fondo negro, texto blanco por el tema
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(32.dp), // Más padding general
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // Ícono grande Verde Neón
        Icon(
            Icons.Filled.Info, // O Icons.Filled.SupportAgent, etc.
            contentDescription = "Soporte",
            modifier = Modifier.size(80.dp), // Más grande
            tint = MaterialTheme.colorScheme.secondary // Verde Neón
        )
        Spacer(modifier = Modifier.height(24.dp))

        // Título con Orbitron
        Text(
            "Soporte Técnico",
            style = MaterialTheme.typography.headlineMedium // Orbitron
        )
        Spacer(modifier = Modifier.height(12.dp))

        // Texto con Roboto Regular, Gris Claro
        Text(
            "¿Necesitas ayuda? Contáctanos directamente a través de WhatsApp.",
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant // Gris Claro
        )
        Spacer(modifier = Modifier.height(40.dp))

        // Botón Azul Eléctrico
        Button(
            onClick = {
                openWhatsAppChat(context, supportPhoneNumber)
            },
            modifier = Modifier.fillMaxWidth(0.9f), // Un poco más ancho
            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary) // Azul
        ) {
            // Icon(Icons.Filled.Send, contentDescription = "WhatsApp") // Ícono opcional
            // Spacer(Modifier.size(ButtonDefaults.IconSpacing))
            // Texto del botón Blanco
            Text("Contactar por WhatsApp", style = MaterialTheme.typography.labelLarge)
        }
    }
}

// Función auxiliar para abrir WhatsApp (sin cambios)
fun openWhatsAppChat(context: Context, phoneNumber: String) {
    val url = "https://api.whatsapp.com/send?phone=$phoneNumber"
    try {
        val intent = Intent(Intent.ACTION_VIEW).apply {
            data = Uri.parse(url)
        }
        context.startActivity(intent)
    } catch (e: Exception) {
        Toast.makeText(context, "No se pudo abrir WhatsApp. ¿Está instalado?", Toast.LENGTH_LONG).show()
    }
}