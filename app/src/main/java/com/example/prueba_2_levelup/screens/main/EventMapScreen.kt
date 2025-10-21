package com.example.prueba_2_levelup.screens.main

import android.content.Context // Necesario para Intent
import android.content.Intent // Necesario para Intent
import android.net.Uri // Necesario para Uri
import android.widget.Toast // Para mensajes de error
import androidx.compose.foundation.clickable // Para hacer clickeable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items // Necesario para LazyColumn items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext // Para obtener el contexto
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.google.android.gms.maps.model.LatLng // Aún usamos LatLng para los datos

// Data class simple para representar un evento (igual que antes)
data class GameEvent(
    val id: String,
    val name: String,
    val location: LatLng,
    val description: String? = null,
    val address: String? = null // Opcional: Añadir dirección para el Intent
)

// Lista de eventos de ejemplo (igual que antes, puedes añadir address)
val sampleEvents = listOf(
    GameEvent("evt1", "Torneo Gamer Fest", LatLng(-33.45694, -70.64827), "Gran torneo presencial", "Plaza de Armas, Santiago"),
    GameEvent("evt2", "Expo Videojuegos Retro", LatLng(-33.4184, -70.6033), "Exhibición en Providencia", "Av. Providencia 123, Santiago"),
    GameEvent("evt3", "Lanzamiento Nuevo Juego", LatLng(-33.3916, -70.5724), "Evento en Las Condes", "Av. Apoquindo 456, Las Condes")
)

@Composable
fun EventMapScreen( // Podrías renombrarlo a EventListScreen si prefieres
    // Puedes añadir un ViewModel si necesitas lógica más compleja
) {
    val context = LocalContext.current // Obtenemos el contexto actual

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text("Próximos Eventos Gamer", style = MaterialTheme.typography.headlineMedium)
        Spacer(modifier = Modifier.height(16.dp))

        // Lista de eventos
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(8.dp) // Espacio entre items
        ) {
            items(sampleEvents) { event ->
                EventListItem(event = event) { selectedEvent ->
                    // Acción al hacer click: Abrir mapa externo
                    openMapForEvent(context, selectedEvent)
                }
                Divider() // Línea divisoria
            }
        }
    }
}

// Composable para mostrar cada item de la lista de eventos
@Composable
fun EventListItem(event: GameEvent, onClick: (GameEvent) -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick(event) } // Hace toda la columna clickeable
            .padding(vertical = 12.dp)
    ) {
        Text(event.name, fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleMedium)
        event.description?.let {
            Text(it, style = MaterialTheme.typography.bodyMedium)
        }
        event.address?.let {
            Text("Dirección: $it", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
        // Podrías mostrar lat/lng si quieres:
        // Text("Coords: ${event.location.latitude}, ${event.location.longitude}", style = MaterialTheme.typography.bodySmall)
    }
}

// Función auxiliar para crear y lanzar el Intent del mapa
fun openMapForEvent(context: Context, event: GameEvent) {
    // Intenta crear una URI geo con latitud y longitud + un marcador con el nombre
    val gmmIntentUri = Uri.parse("geo:${event.location.latitude},${event.location.longitude}?q=${Uri.encode(event.name)}")

    // Alternativa: Si tienes dirección, puedes intentar buscarla (puede ser menos preciso)
    // val gmmIntentUri = Uri.parse("geo:0,0?q=${Uri.encode(event.address ?: event.name)}")

    val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)
    // Opcional: Especificar el paquete de Google Maps si quieres forzarlo,
    // pero es mejor dejar que el usuario elija su app de mapas preferida.
    // mapIntent.setPackage("com.google.android.apps.maps")

    try {
        // Verifica si hay alguna aplicación que pueda manejar este Intent
        if (mapIntent.resolveActivity(context.packageManager) != null) {
            context.startActivity(mapIntent)
        } else {
            Toast.makeText(context, "No se encontró una aplicación de mapas.", Toast.LENGTH_LONG).show()
        }
    } catch (e: Exception) {
        Toast.makeText(context, "Error al intentar abrir el mapa.", Toast.LENGTH_LONG).show()
    }
}


// --- Preview (Opcional) ---
/*
@Preview(showBackground = true)
@Composable
fun EventListScreenPreview() {
    Prueba_2_levelupTheme {
        EventMapScreen() // Renombrar si cambiaste el nombre
    }
}
*/