package com.example.pasteleria.ui.screens

import android.graphics.Bitmap
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.pasteleria.ui.viewModel.AppViewModel
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.foundation.verticalScroll

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(onBack: () -> Unit) {
    val vm: AppViewModel = viewModel()
    val nombreUsuario by vm.nombreUsuario.collectAsState()
    val emailUsuario by vm.emailUsuario.collectAsState()
    val direccionUsuario by vm.direccionUsuario.collectAsState()

    var editarNombre by remember(nombreUsuario) { mutableStateOf(nombreUsuario) }
    var editarEmail by remember(emailUsuario) { mutableStateOf(emailUsuario) }
    var editarDireccion by remember(direccionUsuario) { mutableStateOf(direccionUsuario) }
    var photo by remember { mutableStateOf<Bitmap?>(null) }

    val cameraLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.TakePicturePreview())
    { bmp ->
        photo = bmp
    }

    Scaffold(topBar = {
        CenterAlignedTopAppBar(
            title = { Text("Perfil Usuario") },
            navigationIcon = { IconButton(onClick = onBack){ Icon(Icons.Default.ArrowBack, "Volver") } }
        )
    }) { inner ->
        val scrollState = rememberScrollState()

        Column(
            Modifier
                .padding(inner)
                .fillMaxSize()
                .verticalScroll(scrollState)
                .imePadding()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {

            photo?.let {
                Image(
                    it.asImageBitmap(),
                    contentDescription = null,
                    modifier = Modifier.size(120.dp)
                )
            }
            Button(onClick = { cameraLauncher.launch(null) }) { Text("Tomar foto") }

            OutlinedTextField(
                value = editarNombre,
                onValueChange = { editarNombre = it },
                label = { Text("Nombre") },
                singleLine = true
            )
            OutlinedTextField(value = editarEmail,
                onValueChange = { editarEmail = it },
                label = { Text("Email") },
                singleLine = true
            )
            OutlinedTextField(value = editarDireccion,
                onValueChange = { editarDireccion = it },
                label = { Text("Direccion") },
                singleLine = true
            )

            Button(onClick = { vm.guardarPerfil(editarNombre, editarEmail, editarDireccion) }) { Text("Guardar") }
            Text(
                "Guardado: $nombreUsuario • $emailUsuario • $editarDireccion",
                style = MaterialTheme.typography.bodyMedium)

            HistorialDeCompras(vm)
        }
    }
}
@Composable
fun HistorialDeCompras(vm: AppViewModel = viewModel()) {
    val csv by vm.ordenesCsv.collectAsState()
    val ordenes = remember(csv) {
        if (csv.isBlank()) emptyList()
        else csv.split("||").filter { it.isNotBlank() }
    }

    Spacer(Modifier.height(16.dp))
    Text("Historial de compras", style = MaterialTheme.typography.titleLarge)

    if (ordenes.isEmpty()) {
        Text("Sin compras registradas aún.")
    } else {
        ordenes.forEach { linea ->

            val partes = linea.split(";")
            val fechaMs = partes.getOrNull(0)?.toLongOrNull() ?: 0L
            val cant = partes.getOrNull(1) ?: "0"
            val sub = partes.getOrNull(2) ?: "0"
            val tot = partes.getOrNull(3) ?: "0"

            val fecha = java.text.SimpleDateFormat("dd-MM-yyyy HH:mm")
                .format(java.util.Date(fechaMs))

            Text("• $fecha — $cant item(s) — Subtotal $sub — Total $tot")
        }
    }
}