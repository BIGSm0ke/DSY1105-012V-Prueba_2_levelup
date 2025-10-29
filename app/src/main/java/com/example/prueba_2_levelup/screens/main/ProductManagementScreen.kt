package com.example.prueba_2_levelup.screens.main

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.prueba_2_levelup.data.entities.ProductEntity
import com.example.prueba_2_levelup.viewModel.ProductManagementViewModel


@Composable
fun ProductManagementScreen(
    viewModel: ProductManagementViewModel
) {
    val uiState by viewModel.uiState.collectAsState()

    val textFieldColors = OutlinedTextFieldDefaults.colors(
        focusedBorderColor = MaterialTheme.colorScheme.primary,
        unfocusedBorderColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.4f),
        focusedLabelColor = MaterialTheme.colorScheme.primary,
        cursorColor = MaterialTheme.colorScheme.primary
    )

    val snackbarHostState = remember { SnackbarHostState() }

    // --- BLOQUE CORREGIDO ---
    // (Aquí estaba el error "Smart cast")
    LaunchedEffect(key1 = uiState.userMessage) {
        // 1. Guardamos el mensaje en una variable local
        val message = uiState.userMessage

        // 2. Comprobamos la variable local (esto SÍ permite el smart-cast)
        if (message != null) {
            snackbarHostState.showSnackbar(
                message = message, // 3. Usamos la variable local
                duration = SnackbarDuration.Short
            )
            viewModel.dismissUserMessage()
        }
    }
    // --- FIN DEL BLOQUE CORREGIDO ---

    Scaffold(
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            // --- SECCIÓN 1: FORMULARIO ---
            item {
                Text("Gestionar Productos", style = MaterialTheme.typography.headlineMedium, modifier = Modifier.padding(vertical = 16.dp))

                OutlinedTextField(
                    value = uiState.productName,
                    onValueChange = { viewModel.onProductNameChange(it) },
                    label = { Text("Nombre del Producto") },
                    modifier = Modifier.fillMaxWidth(),
                    colors = textFieldColors,
                    singleLine = true
                )
                Spacer(modifier = Modifier.height(16.dp))

                OutlinedTextField(
                    value = uiState.productDescription,
                    onValueChange = { viewModel.onProductDescriptionChange(it) },
                    label = { Text("Descripción") },
                    modifier = Modifier.fillMaxWidth().height(120.dp),
                    colors = textFieldColors
                )
                Spacer(modifier = Modifier.height(16.dp))

                OutlinedTextField(
                    value = uiState.productPrice,
                    onValueChange = { viewModel.onProductPriceChange(it) },
                    label = { Text("Precio") },
                    modifier = Modifier.fillMaxWidth(),
                    colors = textFieldColors,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                    singleLine = true
                )
                Spacer(modifier = Modifier.height(24.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    Button(
                        onClick = { viewModel.saveOrUpdateProduct() },
                        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
                        enabled = !uiState.isLoading
                    ) {
                        Text(if (uiState.selectedProductId == null) "Crear Producto" else "Actualizar")
                    }

                    OutlinedButton(
                        onClick = { viewModel.clearFields() }
                    ) {
                        Text(if (uiState.selectedProductId == null) "Limpiar" else "Cancelar")
                    }

                    if (uiState.selectedProductId != null) {
                        Button(
                            onClick = { viewModel.deleteProduct() },
                            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error),
                            enabled = !uiState.isLoading
                        ) {
                            Text("Eliminar")
                        }
                    }
                }

                if (uiState.isLoading) {
                    CircularProgressIndicator(modifier = Modifier.padding(top = 16.dp))
                }

                Spacer(modifier = Modifier.height(32.dp))
                Divider()
                Text("Productos Existentes", style = MaterialTheme.typography.titleMedium, modifier = Modifier.padding(vertical = 16.dp))
            }

            // --- SECCIÓN 2: LISTA DE PRODUCTOS ---
            if (uiState.products.isEmpty()) {
                item {
                    Text("No hay productos creados.", modifier = Modifier.padding(16.dp))
                }
            } else {
                items(uiState.products) { product ->
                    ProductListItem(
                        product = product,
                        onClick = {
                            viewModel.loadProductForEdit(product)
                        }
                    )
                    Divider()
                }
            }
        }
    }
}

@Composable
fun ProductListItem(product: ProductEntity, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(product.nombre, style = MaterialTheme.typography.titleMedium)
            Text(product.descripcion, style = MaterialTheme.typography.bodySmall, maxLines = 2)
        }
        Spacer(modifier = Modifier.width(16.dp))
        Text("$${product.precio}", style = MaterialTheme.typography.titleMedium)
    }
}