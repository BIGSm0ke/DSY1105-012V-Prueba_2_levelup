package com.example.prueba_2_levelup.screens.main

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.prueba_2_levelup.R // Importa R si usas imagen por defecto
import com.example.prueba_2_levelup.data.entities.ProductEntity
import com.example.prueba_2_levelup.viewModel.CatalogViewModel
import kotlinx.coroutines.flow.collectLatest
import java.text.NumberFormat
import java.util.Locale

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun CatalogScreen(
    viewModel: CatalogViewModel,
    // onProductClick: (String) -> Unit
) {
    val products by viewModel.products.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(key1 = true) {
        viewModel.uiEvent.collectLatest { event ->
            when (event) {
                is CatalogViewModel.UiEvent.ShowSnackbar -> {
                    snackbarHostState.showSnackbar(
                        message = event.message,
                        duration = SnackbarDuration.Short
                    )
                }
            }
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) }
        // Puedes añadir un TopAppBar aquí si quieres
        // topBar = { TopAppBar(title = { Text("Catálogo", fontFamily = Orbitron) }) }
    ) { paddingValues ->

        if (products.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                contentAlignment = Alignment.Center
            ) {
                // Texto blanco sobre fondo negro por el tema
                Text("Cargando productos...")
            }
        } else {
            LazyVerticalGrid(
                columns = GridCells.Adaptive(minSize = 160.dp),
                contentPadding = PaddingValues(
                    start = 16.dp,
                    end = 16.dp,
                    top = paddingValues.calculateTopPadding() + 16.dp,
                    bottom = paddingValues.calculateBottomPadding() + 16.dp
                ),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier.fillMaxSize()
            ) {
                items(products, key = { it.id }) { product -> // Añadir key para mejor rendimiento
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductCard(
    product: ProductEntity,
    onAddToCartClick: () -> Unit,
    // onClick: () -> Unit
) {
    val format = NumberFormat.getCurrencyInstance(Locale("es", "CL"))
    format.maximumFractionDigits = 0

    // Card usará surfaceVariant (Gris Oscuro) si lo definiste, o surface (Negro)
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant) // Gris oscuro para contraste
    ) {
        Column(modifier = Modifier.padding(12.dp)) { // Más padding interno
            // --- Imagen --- (Descomenta si la usas)
            /*
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(product.imageUrl ?: R.drawable.placeholder_image) // Usa un placeholder si es null
                    .crossfade(true)
                    .build(),
                contentDescription = product.nombre,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(130.dp),
                contentScale = ContentScale.Crop
            )
            Spacer(modifier = Modifier.height(12.dp))
            */

            // --- Nombre (Roboto Bold) ---
            Text(
                text = product.nombre,
                style = MaterialTheme.typography.titleMedium, // Ya es Bold por Type.kt
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                color = MaterialTheme.colorScheme.onSurface // Blanco
            )
            Spacer(modifier = Modifier.height(4.dp))

            // --- Categoría (Roboto Regular, Gris Claro) ---
            Text(
                text = product.categoria,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant // Gris claro
            )
            Spacer(modifier = Modifier.height(8.dp))

            // --- Precio (Roboto SemiBold, Blanco) ---
            Text(
                text = format.format(product.precio),
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.SemiBold, // Puedes definir un estilo específico si quieres
                color = MaterialTheme.colorScheme.onSurface // Blanco
            )
            Spacer(modifier = Modifier.height(12.dp))

            // --- Botón Añadir (Verde Neón) ---
            Button(
                onClick = onAddToCartClick,
                modifier = Modifier.align(Alignment.End),
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.secondary) // Verde Neón
            ) {
                // Texto usará onSecondary (Negro)
                Text("Añadir", color = MaterialTheme.colorScheme.onSecondary)
            }
        }
    }
}