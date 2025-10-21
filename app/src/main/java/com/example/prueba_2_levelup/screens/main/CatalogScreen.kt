package com.example.prueba_2_levelup.screens.main

import android.annotation.SuppressLint // Importar para Scaffold
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.*
import androidx.compose.runtime.* // Necesario para LaunchedEffect, remember, etc.
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.prueba_2_levelup.data.entities.ProductEntity
import com.example.prueba_2_levelup.viewModel.CatalogViewModel
import kotlinx.coroutines.flow.collectLatest // Importar para el Channel
import java.text.NumberFormat
import java.util.Locale

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter") // Padding se usa en LazyVerticalGrid
@Composable
fun CatalogScreen(
    viewModel: CatalogViewModel,
    // onProductClick: (String) -> Unit
) {
    val products by viewModel.products.collectAsState()

    // --- Estado para manejar el Snackbar ---
    val snackbarHostState = remember { SnackbarHostState() } // Crea el estado del Snackbar

    // --- Escucha los eventos de UI del ViewModel ---
    LaunchedEffect(key1 = true) { // Se ejecuta una vez cuando el Composable entra en la composición
        viewModel.uiEvent.collectLatest { event -> // Colecta los eventos del Channel
            when (event) {
                is CatalogViewModel.UiEvent.ShowSnackbar -> {
                    snackbarHostState.showSnackbar(
                        message = event.message,
                        duration = SnackbarDuration.Short // Duración corta para el mensaje
                    )
                }
                // Maneja otros eventos si los añades
            }
        }
    }

    // --- Envuelve el contenido en un Scaffold para poder mostrar el Snackbar ---
    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) } // Añade el host del Snackbar
    ) { paddingValues -> // paddingValues proporcionado por Scaffold

        if (products.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues), // Aplica padding del Scaffold
                contentAlignment = Alignment.Center
            ) {
                Text("Cargando productos...")
            }
        } else {
            LazyVerticalGrid(
                columns = GridCells.Adaptive(minSize = 160.dp),
                contentPadding = PaddingValues(
                    start = 16.dp,
                    end = 16.dp,
                    top = paddingValues.calculateTopPadding() + 16.dp, // Combina padding del Scaffold y el nuestro
                    bottom = paddingValues.calculateBottomPadding() + 16.dp // Combina padding del Scaffold y el nuestro
                ),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier.fillMaxSize()
            ) {
                items(products) { product ->
                    ProductCard(
                        product = product,
                        onAddToCartClick = { viewModel.addToCart(product.id) }
                        // onClick = { onProductClick(product.id) }
                    )
                }
            }
        }
    }
}

// --- ProductCard (sin cambios respecto al código anterior) ---
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductCard(
    product: ProductEntity,
    onAddToCartClick: () -> Unit,
    // onClick: () -> Unit
) {
    val format = NumberFormat.getCurrencyInstance(Locale("es", "CL"))
    format.maximumFractionDigits = 0

    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
    ) {
        Column(modifier = Modifier.padding(8.dp)) {
            // ... (Imagen opcional, Nombre, Categoría, Precio - sin cambios)
            Text(
                text = product.nombre,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = product.categoria,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = format.format(product.precio),
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.SemiBold
            )
            Spacer(modifier = Modifier.height(8.dp))

            Button(
                onClick = onAddToCartClick,
                modifier = Modifier.align(Alignment.End)
            ) {
                Text("Añadir")
            }
        }
    }
}

// ... (Previews opcionales) ...