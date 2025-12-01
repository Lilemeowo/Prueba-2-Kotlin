package com.example.pasteleria.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.pasteleria.data.DataProductos
import com.example.pasteleria.ui.viewModel.AppViewModel
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Divider

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CarritoScreen(onBack: () -> Unit) {
    val vm: AppViewModel = viewModel()
    val ids by vm.carrito.collectAsState()

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Carrito") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Volver")
                    }
                }
            )
        }
    ) { inner ->
        Column(
            modifier = Modifier
                .padding(inner)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            if (ids.isEmpty()) {
                Text("Tu carrito está vacío.")
            } else {
                ids.forEach { id ->
                    val p = DataProductos.first { it.id == id }
                    Text("${p.nombre} — CLP ${p.precio}")
                }

                Divider()

                val subtotal = vm.total()
                val totalConIva = (subtotal * 1.19).toInt()

                Text(
                    text = "Total: CLP $subtotal",
                    style = MaterialTheme.typography.titleLarge


                )

                Text(
                    text = "Total + IVA (19%): CLP $totalConIva",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.secondary
                )

                Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    Button(onClick = { vm.limpiarCarrito() }) {
                        Text("Vaciar carrito")
                    }

                    Button(
                        onClick = { vm.pagarYGuardarOrden() },
                        enabled = ids.isNotEmpty()
                    ) {
                        Text("Pagar")
                    }
                }
            }
        }
    }
}
