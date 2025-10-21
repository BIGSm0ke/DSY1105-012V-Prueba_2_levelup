package com.example.prueba_2_levelup.screens.main

import androidx.compose.foundation.BorderStroke // <-- IMPORTACIÓN AÑADIDA
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.SolidColor // <-- Asegúrate que también esté este import
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.prueba_2_levelup.data.dao.CartItemWithProductInfo
import com.example.prueba_2_levelup.viewModel.CartViewModel
import java.text.NumberFormat
import java.util.Locale

@Composable
fun CartScreen(viewModel: CartViewModel) {
    // Observa el estado del carrito (items y total) desde el ViewModel
    val cartState by viewModel.cartState.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text("Carrito", style = MaterialTheme.typography.headlineMedium)
        Spacer(modifier = Modifier.height(16.dp))

        if (cartState.items.isEmpty()) {
            // Mensaje si el carrito está vacío
            Box(modifier = Modifier.weight(1f), contentAlignment = Alignment.Center) {
                Text("Tu carrito está vacío.")
            }
        } else {
            // Lista de items en el carrito
            LazyColumn(modifier = Modifier.weight(1f)) {
                items(cartState.items, key = { it.id }) { item ->
                    CartItemRow(
                        item = item,
                        onIncreaseQuantity = { viewModel.updateQuantity(item.id, item.quantity + 1) },
                        onDecreaseQuantity = { viewModel.updateQuantity(item.id, item.quantity - 1) },
                        onRemoveItem = { viewModel.removeFromCart(item.id) }
                    )
                    HorizontalDivider(color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.12f)) // Separador entre items
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Resumen del total
            CartSummary(
                totalPrice = cartState.totalPrice,
                onClearCart = { viewModel.clearCart() }
            )
        }
    }
}

@Composable
fun CartItemRow(
    item: CartItemWithProductInfo,
    onIncreaseQuantity: () -> Unit,
    onDecreaseQuantity: () -> Unit,
    onRemoveItem: () -> Unit
) {
    val format = NumberFormat.getCurrencyInstance(Locale("es", "CL"))
    format.maximumFractionDigits = 0

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Nombre y Precio Unitario
        Column(modifier = Modifier.weight(1f)) {
            Text(item.nombre, fontWeight = FontWeight.Bold, style = MaterialTheme.typography.bodyLarge)
            Text(format.format(item.precio), style = MaterialTheme.typography.bodyMedium)
        }

        Spacer(modifier = Modifier.width(8.dp))

        // Controles de Cantidad
        Row(verticalAlignment = Alignment.CenterVertically) {
            IconButton(onClick = onDecreaseQuantity) {
                Icon(Icons.Filled.RemoveCircleOutline, contentDescription = "Disminuir", tint = MaterialTheme.colorScheme.primary)
            }
            Text(item.quantity.toString(), modifier = Modifier.padding(horizontal = 12.dp), fontWeight = FontWeight.Medium)
            IconButton(onClick = onIncreaseQuantity) {
                Icon(Icons.Filled.AddCircleOutline, contentDescription = "Aumentar", tint = MaterialTheme.colorScheme.secondary)
            }
        }

        Spacer(modifier = Modifier.width(8.dp))

        // Botón Eliminar
        IconButton(onClick = onRemoveItem) {
            Icon(Icons.Filled.DeleteOutline, contentDescription = "Eliminar", tint = MaterialTheme.colorScheme.error)
        }
    }
}

@Composable
fun CartSummary(totalPrice: Double, onClearCart: () -> Unit) {
    val format = NumberFormat.getCurrencyInstance(Locale("es", "CL"))
    format.maximumFractionDigits = 0

    Column(modifier = Modifier.fillMaxWidth()) {
        HorizontalDivider(color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.12f))
        Spacer(modifier = Modifier.height(12.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("Total:", style = MaterialTheme.typography.headlineSmall)
            Text(format.format(totalPrice), style = MaterialTheme.typography.headlineSmall)
        }
        Spacer(modifier = Modifier.height(20.dp))
        // Botón para vaciar carrito
        OutlinedButton(
            onClick = onClearCart,
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.outlinedButtonColors(contentColor = MaterialTheme.colorScheme.error),
            // AQUÍ SE USA BorderStroke
            border = BorderStroke(1.dp, MaterialTheme.colorScheme.error)

        ) {
            Icon(Icons.Filled.RemoveShoppingCart, contentDescription = "Vaciar Carrito", modifier = Modifier.size(ButtonDefaults.IconSize))
            Spacer(Modifier.size(ButtonDefaults.IconSpacing))
            Text("Vaciar Carrito")
        }
        Spacer(modifier = Modifier.height(12.dp))
        // Botón Proceder al Pago
        Button(
            onClick = { /* TODO: Navegar a pantalla de pago */ },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
        ) {
            Text("Proceder al Pago", style = MaterialTheme.typography.labelLarge)
        }
    }
}

// --- Previews ---
// ... (Tus previews si las tienes)