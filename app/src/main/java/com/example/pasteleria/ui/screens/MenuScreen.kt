package com.example.pasteleria.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.pasteleria.ui.theme.*
import com.example.pasteleria.ui.viewModel.AppViewModel
import com.example.pasteleria.ui.navigation.NavRoutes
import com.example.pasteleria.ui.components.BotonMenu

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MenuScreen(onNavigate: (String) -> Unit) {
    val vm: AppViewModel = viewModel()
    val name by vm.nombreUsuario.collectAsState()
    val email by vm.emailUsuario.collectAsState()

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(title = { Text("Pasteleria — Menú") },
                actions = {
                    IconButton(onClick = { vm.logout() }) {
                        Icon(Icons.Default.Logout, contentDescription = "Salir")
                    }
                }
            )
        }
    ) { inner ->
        Column(
            Modifier
                .fillMaxSize()
                .padding(inner)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Card(
                colors = CardDefaults.cardColors(containerColor = CardBlack, contentColor = TextPrimary),
                elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    Text(

                        "Bienvenido, $name ",
                        style = MaterialTheme.typography.titleLarge,
                        color = SecondaryBlue
                    )
                    Text(
                        email,
                        style = MaterialTheme.typography.bodyMedium,
                        color = TextSecondary
                    )
                    Button(
                        onClick = { onNavigate(NavRoutes.ApiPostres) },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Ver postres desde API externa")
                    }

                }
            }

            BotonMenu("Productos / Tienda", Icons.Filled.Store) { onNavigate(NavRoutes.Productos) }
            BotonMenu("Carrito", Icons.Filled.ShoppingCart) { onNavigate(NavRoutes.Carrito) }
            BotonMenu("Perfil", Icons.Filled.Person) { onNavigate(NavRoutes.Perfil) }
            BotonMenu("Sucursales", Icons.Filled.LocationOn) { onNavigate(NavRoutes.Sucursales) }
        }
    }
}

