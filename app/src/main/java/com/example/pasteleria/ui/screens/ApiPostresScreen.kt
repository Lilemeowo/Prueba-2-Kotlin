package com.example.pasteleria.ui.screens



import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.pasteleria.ui.viewModel.PostresApiViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ApiPostresScreen(onBack: () -> Unit) {
    val vm: PostresApiViewModel = viewModel()

    val postres by vm.postres.collectAsState()
    val cargando by vm.cargando.collectAsState()
    val error by vm.error.collectAsState()

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Postres (API externa)") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Volver")
                    }
                }
            )
        }
    ) { inner ->
        Column(
            Modifier
                .padding(inner)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {

            when {
                cargando -> {
                    CircularProgressIndicator()
                }

                error != null -> {
                    Text(
                        text = error ?: "Error",
                        color = MaterialTheme.colorScheme.error
                    )
                    Button(onClick = { vm.cargarPostres() }) {
                        Text("Reintentar")
                    }
                }

                else -> {
                    LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        items(postres) { postre ->
                            Card(Modifier.fillMaxWidth()) {
                                Column(Modifier.padding(12.dp)) {
                                    Text(
                                        text = postre.strMeal,
                                        style = MaterialTheme.typography.titleMedium
                                    )
                                    // Solo mostramos la URL de la imagen para que se note que viene de la API
                                    Text(
                                        text = postre.strMealThumb,
                                        style = MaterialTheme.typography.bodySmall
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
