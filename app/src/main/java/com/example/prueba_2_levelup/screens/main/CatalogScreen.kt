package com.example.prueba_2_levelup.screens.main

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage // Necesitarás añadir Coil para cargar imágenes si usas imageUrl
import coil.request.ImageRequest
import com.example.prueba_2_levelup.data.entities.ProductEntity
import com.example.prueba_2_levelup.viewModel.CatalogViewModel
import java.text.NumberFormat
import java.util.Locale

@Composable
fun CatalogScreen(
    viewModel: CatalogViewModel,
    // onProductClick: (String) -> Unit // Opcional: para navegar a detalles
) {
    // Observa el estado de los productos desde el ViewModel
    val products by viewModel.products.collectAsState()

    if (products.isEmpty()) {
        // Muestra un indicador de carga o mensaje si no hay productos
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            // CircularProgressIndicator() // O un texto
            Text("Cargando productos...")
        }
    } else {
        // Muestra los productos en una cuadrícula
        LazyVerticalGrid(
            columns = GridCells.Adaptive(minSize = 160.dp), // Ajusta el tamaño mínimo de cada celda
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier.fillMaxSize()
        ) {
            items(products) { product ->
                ProductCard(
                    product = product,
                    onAddToCartClick = { viewModel.addToCart(product.id) }
                    // onClick = { onProductClick(product.id) } // Para detalles
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductCard(
    product: ProductEntity,
    onAddToCartClick: () -> Unit,
    // onClick: () -> Unit // Para detalles
) {
    // Formateador para moneda local (Peso Chileno)
    val format = NumberFormat.getCurrencyInstance(Locale("es", "CL"))
    format.maximumFractionDigits = 0 // Sin decimales para CLP

    Card(
        modifier = Modifier
            .fillMaxWidth()
        // .clickable { onClick() } // Para navegar a detalles
        ,
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        // Usa los colores del tema si están definidos
        // colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
    ) {
        Column(modifier = Modifier.padding(8.dp)) {
            // --- Opcional: Imagen del Producto ---
            /* // Descomenta si añades imageUrl a ProductEntity y la dependencia Coil
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(product.imageUrl ?: "URL_POR_DEFECTO_SI_ES_NULL") // Añade una URL por defecto
                    .crossfade(true)
                    .build(),
                contentDescription = product.nombre,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp), // Altura fija para la imagen
                contentScale = ContentScale.Crop // Escala la imagen
            )
            Spacer(modifier = Modifier.height(8.dp))
            */

            // --- Nombre del Producto ---
            Text(
                text = product.nombre,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis // Corta el texto si es muy largo
            )
            Spacer(modifier = Modifier.height(4.dp))

            // --- Categoría ---
            Text(
                text = product.categoria,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(modifier = Modifier.height(8.dp))

            // --- Precio ---
            Text(
                text = format.format(product.precio), // Formatea el precio como CLP
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.SemiBold
            )
            Spacer(modifier = Modifier.height(8.dp))

            // --- Botón Añadir al Carrito ---
            Button(
                onClick = onAddToCartClick,
                modifier = Modifier.align(Alignment.End) // Alinea el botón a la derecha
            ) {
                Text("Añadir")
            }
        }
    }
}

// --- Preview (Opcional) ---
/*
@Preview(showBackground = true)
@Composable
fun ProductCardPreview() {
    Prueba_2_levelupTheme {
        ProductCard(
            product = ProductEntity(id = "prev1", categoria = "Accesorios", nombre = "Auriculares Gamer HyperX", precio = 79990.0, descripcion = ""),
            onAddToCartClick = {}
        )
    }
}

@Preview(showBackground = true, widthDp = 380)
@Composable
fun CatalogScreenPreview() {
     Prueba_2_levelupTheme {
        // Necesitarías un ViewModel mock para la preview completa
        // CatalogScreen(viewModel = mockViewModel)
         val previewProducts = listOf(
             ProductEntity(id = "p1", categoria = "Consolas", nombre = "PlayStation 5", precio = 549990.0, ""),
             ProductEntity(id = "p2", categoria = "Accesorios", nombre = "Controlador Xbox", precio = 59990.0, ""),
             ProductEntity(id = "p3", categoria = "Juegos de Mesa", nombre = "Catan", precio = 29990.0, "")
         )
         LazyVerticalGrid(
             columns = GridCells.Adaptive(minSize = 160.dp),
             contentPadding = PaddingValues(16.dp),
             verticalArrangement = Arrangement.spacedBy(16.dp),
             horizontalArrangement = Arrangement.spacedBy(16.dp),
         ) {
             items(previewProducts) { product ->
                 ProductCard(product = product, onAddToCartClick = {})
             }
         }
     }
}
*/

// --- Dependencia Coil (si usas imágenes) ---
// Añade esto a tu app/build.gradle.kts si decides usar AsyncImage:
// implementation("io.coil-kt:coil-compose:2.5.0") // O la versión más reciente