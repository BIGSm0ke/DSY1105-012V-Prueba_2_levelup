package com.example.prueba_2_levelup.screens.main

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.prueba_2_levelup.data.entities.ProductEntity
import com.example.prueba_2_levelup.viewModel.ProductManagementViewModel
import com.example.prueba_2_levelup.viewModel.availableCategories


@Composable
fun ProductManagementScreen(
    viewModel: ProductManagementViewModel
) {
    val uiState by viewModel.uiState.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }
    var expanded by remember { mutableStateOf(false) } // Estado para el Dropdown

    val textFieldColors = OutlinedTextFieldDefaults.colors(
        focusedBorderColor = MaterialTheme.colorScheme.primary,
        unfocusedBorderColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.4f),
        focusedLabelColor = MaterialTheme.colorScheme.primary,
        cursorColor = MaterialTheme.colorScheme.primary
    )

    LaunchedEffect(key1 = uiState.userMessage) {
        val message = uiState.userMessage
        if (message != null) {
            snackbarHostState.showSnackbar(
                message = message,
                duration = SnackbarDuration.Short
            )
            viewModel.dismissUserMessage()
        }
    }

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

            // --- SECCIÓN 1: FORMULARIO (CAMPOS EXTENDIDOS) ---
            item {
                Text("Gestionar Productos", style = MaterialTheme.typography.headlineMedium, modifier = Modifier.padding(vertical = 16.dp))

                // CAMPO: ID (solo visualización)
                if (uiState.selectedProductId != null) {
                    Text("ID: ${uiState.selectedProductId}", style = MaterialTheme.typography.bodySmall, modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp))
                }

                // CAMPO: Código
                OutlinedTextField(
                    value = uiState.productCode,
                    onValueChange = { viewModel.onProductCodeChange(it) },
                    label = { Text("Código de Producto") },
                    modifier = Modifier.fillMaxWidth(),
                    colors = textFieldColors,
                    singleLine = true
                )
                Spacer(modifier = Modifier.height(16.dp))

                // CAMPO: Nombre
                OutlinedTextField(
                    value = uiState.productName,
                    onValueChange = { viewModel.onProductNameChange(it) },
                    label = { Text("Nombre del Producto") },
                    modifier = Modifier.fillMaxWidth(),
                    colors = textFieldColors,
                    singleLine = true
                )
                Spacer(modifier = Modifier.height(16.dp))

                // CAMPO: Categoría (Dropdown)
                OutlinedTextField(
                    value = uiState.productCategory,
                    onValueChange = {}, // No permitir edición directa
                    label = { Text("Categoría") },
                    readOnly = true,
                    trailingIcon = {
                        Icon(Icons.Filled.ArrowDropDown, contentDescription = "Dropdown", Modifier.clickable { expanded = true })
                    },
                    modifier = Modifier.fillMaxWidth(),
                    colors = textFieldColors,
                )
                DropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false },
                    modifier = Modifier.fillMaxWidth(0.9f) // Ajustar ancho
                ) {
                    availableCategories.forEach { category ->
                        DropdownMenuItem(
                            text = { Text(category) },
                            onClick = {
                                viewModel.onProductCategoryChange(category)
                                expanded = false
                            }
                        )
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))

                // CAMPO: Descripción
                OutlinedTextField(
                    value = uiState.productDescription,
                    onValueChange = { viewModel.onProductDescriptionChange(it) },
                    label = { Text("Descripción") },
                    modifier = Modifier.fillMaxWidth().heightIn(min = 80.dp, max = 120.dp),
                    colors = textFieldColors
                )
                Spacer(modifier = Modifier.height(16.dp))

                // FILA: Precio Normal y Precio Original
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    OutlinedTextField(
                        value = uiState.productPrice,
                        onValueChange = { viewModel.onProductPriceChange(it) },
                        label = { Text("Precio Venta") },
                        modifier = Modifier.weight(1f),
                        colors = textFieldColors,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                        singleLine = true
                    )
                    Spacer(modifier = Modifier.width(16.dp))
                    OutlinedTextField(
                        value = uiState.productOriginalPrice,
                        onValueChange = { viewModel.onProductOriginalPriceChange(it) },
                        label = { Text("Precio Original") },
                        modifier = Modifier.weight(1f),
                        colors = textFieldColors,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                        singleLine = true
                    )
                }
                Spacer(modifier = Modifier.height(16.dp))

                // FILA: Stock y Oferta
                Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                    OutlinedTextField(
                        value = uiState.productStock,
                        onValueChange = { viewModel.onProductStockChange(it) },
                        label = { Text("Stock") },
                        modifier = Modifier.weight(1f),
                        colors = textFieldColors,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        singleLine = true
                    )
                    Spacer(modifier = Modifier.width(16.dp))
                    Row(
                        modifier = Modifier.weight(1f),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Checkbox(
                            checked = uiState.isOffer,
                            onCheckedChange = { viewModel.onIsOfferChange(it) }
                        )
                        Text("En Oferta", style = MaterialTheme.typography.bodyLarge)
                    }
                }
                Spacer(modifier = Modifier.height(24.dp))

                // --- BOTONES ---
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
                items(uiState.products, key = { it.id }) { product ->
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
            Text("Categoría: ${product.categoria} | Stock: ${product.stock}", style = MaterialTheme.typography.bodySmall)
            Text(product.descripcion, style = MaterialTheme.typography.bodySmall, maxLines = 2)
        }
        Spacer(modifier = Modifier.width(16.dp))
        // Mostrar precio original si está en oferta
        Column(horizontalAlignment = Alignment.End) {
            if (product.oferta && product.precioOriginal != null) {
                Text("$${product.precioOriginal}", style = MaterialTheme.typography.bodySmall.copy(
                    // Aquí se usaría un estilo tachado, se simula con color para simplicidad
                ), color = MaterialTheme.colorScheme.error)
            }
            Text("$${product.precio}", style = MaterialTheme.typography.titleMedium)
        }
    }
}