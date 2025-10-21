package com.example.prueba_2_levelup.screens.main

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle // Icono para aumentar
import androidx.compose.material.icons.filled.Delete // Icono para eliminar item
import androidx.compose.material.icons.filled.RemoveCircle // Icono para disminuir
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.prueba_2_levelup.data.dao.CartItemWithProductInfo
import com.example.prueba_2_levelup.viewModel.CartUiState
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
        Text("Carrito de Compras", style = MaterialTheme.typography.headlineMedium)
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
                    Divider() // Separador entre items
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
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Opcional: Imagen del producto aquí si la tienes
        // AsyncImage(...)

        // Nombre y Precio Unitario
        Column(modifier = Modifier.weight(1f)) {
            Text(item.nombre, fontWeight = FontWeight.Bold)
            Text(format.format(item.precio))
        }

        // Controles de Cantidad
        Row(verticalAlignment = Alignment.CenterVertically) {
            IconButton(onClick = onDecreaseQuantity, enabled = item.quantity > 0) { // Deshabilitar si la cantidad es 0? O dejar que el VM lo elimine
                Icon(Icons.Filled.RemoveCircle, contentDescription = "Disminuir cantidad")
            }
            Text(item.quantity.toString(), modifier = Modifier.padding(horizontal = 8.dp))
            IconButton(onClick = onIncreaseQuantity) {
                Icon(Icons.Filled.AddCircle, contentDescription = "Aumentar cantidad")
            }
        }

        // Botón Eliminar
        IconButton(onClick = onRemoveItem) {
            Icon(Icons.Filled.Delete, contentDescription = "Eliminar item", tint = MaterialTheme.colorScheme.error)
        }
    }
}

@Composable
fun CartSummary(totalPrice: Double, onClearCart: () -> Unit) {
    val format = NumberFormat.getCurrencyInstance(Locale("es", "CL"))
    format.maximumFractionDigits = 0

    Column(modifier = Modifier.fillMaxWidth()) {
        Divider()
        Spacer(modifier = Modifier.height(8.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("Total:", style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
            Text(format.format(totalPrice), style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
        }
        Spacer(modifier = Modifier.height(16.dp))
        // Botón para vaciar carrito (Opcional)
        OutlinedButton(
            onClick = onClearCart,
            modifier = Modifier.align(Alignment.End),
            colors = ButtonDefaults.outlinedButtonColors(contentColor = MaterialTheme.colorScheme.error),
            border = ButtonDefaults.outlinedButtonBorder.copy(
                brush = androidx.compose.ui.graphics.SolidColor(MaterialTheme.colorScheme.error)
            )

        ) {
            Icon(Icons.Filled.Delete, contentDescription = "Vaciar Carrito", modifier = Modifier.size(ButtonDefaults.IconSize))
            Spacer(Modifier.size(ButtonDefaults.IconSpacing))
            Text("Vaciar Carrito")
        }
        Spacer(modifier = Modifier.height(8.dp))
        // Botón Proceder al Pago (Añadir lógica después)
        Button(onClick = { /* TODO: Navegar a pantalla de pago */ }, modifier = Modifier.fillMaxWidth()) {
            Text("Proceder al Pago")
        }
    }
}

// --- Previews (Opcional) ---
/*
@Preview(showBackground = true)
@Composable
fun CartItemRowPreview() {
    Prueba_2_levelupTheme {
        CartItemRow(
            item = CartItemWithProductInfo(1, "p1", 2, "PlayStation 5", 549990.0),
            onIncreaseQuantity = {},
            onDecreaseQuantity = {},
            onRemoveItem = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
fun CartScreenPreview_Empty() {
    Prueba_2_levelupTheme {
        // Necesitarías un ViewModel mock que devuelva estado vacío
        // CartScreen(viewModel = mockViewModelEmpty)
         Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
             Text("Carrito de Compras", style = MaterialTheme.typography.headlineMedium)
             Spacer(modifier = Modifier.height(16.dp))
             Box(modifier = Modifier.weight(1f), contentAlignment = Alignment.Center) {
                 Text("Tu carrito está vacío.")
             }
         }
    }
}

@Preview(showBackground = true)
@Composable
fun CartScreenPreview_WithItems() {
    Prueba_2_levelupTheme {
         // Necesitarías un ViewModel mock con items
        // CartScreen(viewModel = mockViewModelWithItems)
         val items = listOf(
             CartItemWithProductInfo(1, "p1", 1, "PlayStation 5", 549990.0),
             CartItemWithProductInfo(2, "p2", 2, "Controlador Xbox", 59990.0)
         )
         val total = items.sumOf { it.precio * it.quantity }
         Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
             Text("Carrito de Compras", style = MaterialTheme.typography.headlineMedium)
             Spacer(modifier = Modifier.height(16.dp))
             LazyColumn(modifier = Modifier.weight(1f)) {
                 items(items, key = { it.id }) { item ->
                     CartItemRow(
                         item = item,
                         onIncreaseQuantity = { },
                         onDecreaseQuantity = { },
                         onRemoveItem = { }
                     )
                     Divider()
                 }
             }
             Spacer(modifier = Modifier.height(16.dp))
             CartSummary(totalPrice = total, onClearCart = {})
         }
    }
}
*/