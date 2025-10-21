package com.example.prueba_2_levelup.screens.main

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.* // Importa las funciones de Google Maps Compose

// Data class simple para representar un evento (puedes expandirla)
data class GameEvent(
    val id: String,
    val name: String,
    val location: LatLng,
    val description: String? = null
)

// Lista de eventos de ejemplo (en una app real, vendrían de una BD o API)
val sampleEvents = listOf(
    GameEvent("evt1", "Torneo Gamer Fest", LatLng(-33.45694, -70.64827), "Gran torneo presencial"), // Santiago Centro (aprox)
    GameEvent("evt2", "Expo Videojuegos Retro", LatLng(-33.4184, -70.6033), "Exhibición en Providencia"), // Providencia (aprox)
    GameEvent("evt3", "Lanzamiento Nuevo Juego", LatLng(-33.3916, -70.5724), "Evento en Las Condes") // Las Condes (aprox)
)

@Composable
fun EventMapScreen(
    // Puedes añadir un ViewModel si necesitas lógica más compleja
    // eventMapViewModel: EventMapViewModel = viewModel()
) {
    // Estado para recordar la configuración de la cámara del mapa
    // Centrado inicialmente en Santiago (puedes ajustar lat/lng y zoom)
    val santiagoLatLng = LatLng(-33.4489, -70.6693)
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(santiagoLatLng, 10f) // Zoom inicial (10f es un buen nivel para ciudad)
    }

    // Propiedades y controles de UI para el mapa
    var mapProperties by remember {
        mutableStateOf(MapProperties(mapType = MapType.NORMAL)) // Puedes cambiar a SATELLITE, HYBRID, TERRAIN
    }
    val mapUiSettings by remember {
        mutableStateOf(MapUiSettings(zoomControlsEnabled = true)) // Habilita controles de zoom
    }

    Column(modifier = Modifier
        .fillMaxSize()
        .padding(top = 16.dp, start = 16.dp, end = 16.dp) // Añade padding superior
    ) {
        Text("Eventos Gamer Cercanos", style = MaterialTheme.typography.headlineMedium)
        Spacer(modifier = Modifier.height(16.dp))

        Box(modifier = Modifier.fillMaxSize()) {
            GoogleMap(
                modifier = Modifier.fillMaxSize(),
                cameraPositionState = cameraPositionState,
                properties = mapProperties,
                uiSettings = mapUiSettings
            ) {
                // Añade marcadores para cada evento
                sampleEvents.forEach { event ->
                    Marker(
                        state = MarkerState(position = event.location),
                        title = event.name,
                        snippet = event.description ?: "Evento Gamer" // Texto que aparece al tocar el marcador
                        // Puedes personalizar el ícono del marcador aquí:
                        // icon = BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)
                    )
                }

                // Opcional: Marcador para la ubicación actual del usuario
                // Necesitarías obtener la ubicación del dispositivo (requiere más lógica y permisos)
                // val userLocation = LatLng(...)
                // Marker(state = MarkerState(position = userLocation), title = "Tu ubicación")
            }

            // Opcional: Muestra un indicador de carga mientras el mapa se inicializa
            if (!cameraPositionState.isMoving && cameraPositionState.position.target == santiagoLatLng) { // Condición simple de carga
                // CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            }
        }
    }
}

// --- Preview (Opcional) ---
/*
@Preview(showBackground = true)
@Composable
fun EventMapScreenPreview() {
    Prueba_2_levelupTheme {
        // La preview del mapa puede no funcionar directamente en el editor
        // Es mejor probarla en un emulador o dispositivo real.
        Box(modifier = Modifier.fillMaxSize()) {
             Text("Vista previa del mapa no disponible en el editor.", modifier = Modifier.align(Alignment.Center))
        }
    }
}
*/