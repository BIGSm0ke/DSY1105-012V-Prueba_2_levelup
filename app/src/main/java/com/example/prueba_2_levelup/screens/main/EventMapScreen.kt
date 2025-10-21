package com.example.prueba_2_levelup.screens.main

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn // Ícono de mapa
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.google.android.gms.maps.model.LatLng

// Data class simple para representar un evento (igual que antes)
data class GameEvent(
    val id: String,
    val name: String,
    val location: LatLng,
    val description: String? = null,
    val address: String? = null // Opcional: Añadir dirección para el Intent
)

// Lista de eventos de ejemplo (igual que antes)
val sampleEvents = listOf(
    GameEvent("evt1", "Torneo Gamer Fest", LatLng(-33.45694, -70.64827), "Gran torneo presencial", "Plaza de Armas, Santiago"),
    GameEvent("evt2", "Expo Videojuegos Retro", LatLng(-33.4184, -70.6033), "Exhibición en Providencia", "Av. Providencia 123, Santiago"),
    GameEvent("evt3", "Lanzamiento Nuevo Juego", LatLng(-33.3916, -70.5724), "Evento en Las Condes", "Av. Apoquindo 456, Las Condes")
)

@Composable
fun EventMapScreen( // O EventListScreen
) {
    val context = LocalContext.current

    // Fondo negro y texto blanco por el tema
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // Título con Orbitron
        Text("Próximos Eventos", style = MaterialTheme.typography.headlineMedium)
        Spacer(modifier = Modifier.height(16.dp))

        // Lista de eventos
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(sampleEvents, key = {it.id}) { event ->
                EventListItem(event = event) { selectedEvent ->
                    openMapForEvent(context, selectedEvent)
                }
                HorizontalDivider(color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.12f))
            }
        }
    }
}

// Composable para cada item de la lista
@Composable
fun EventListItem(event: GameEvent, onClick: (GameEvent) -> Unit) {
    Row( // Usar Row para alinear ícono y texto
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick(event) }
            .padding(vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically // Alinear verticalmente
    ) {
        // Ícono de ubicación (Azul Eléctrico)
        Icon(
            Icons.Filled.LocationOn,
            contentDescription = "Ubicación",
            tint = MaterialTheme.colorScheme.primary,
            modifier = Modifier.size(40.dp).padding(end = 12.dp) // Tamaño y espacio
        )
        Column { // Columna para el texto
            // Nombre (Roboto Bold, Blanco)
            Text(event.name, fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleMedium)
            // Descripción (Roboto Regular, Gris Claro)
            event.description?.let {
                Text(it, style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
            // Dirección (Roboto Regular, Gris Claro)
            event.address?.let {
                Text(it, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
        }
    }
}

// Función auxiliar para abrir el mapa (sin cambios)
fun openMapForEvent(context: Context, event: GameEvent) {
    val gmmIntentUri = Uri.parse("geo:${event.location.latitude},${event.location.longitude}?q=${Uri.encode(event.name)}")
    val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)
    try {
        if (mapIntent.resolveActivity(context.packageManager) != null) {
            context.startActivity(mapIntent)
        } else {
            Toast.makeText(context, "No se encontró una aplicación de mapas.", Toast.LENGTH_LONG).show()
        }
    } catch (e: Exception) {
        Toast.makeText(context, "Error al intentar abrir el mapa.", Toast.LENGTH_LONG).show()
    }
}