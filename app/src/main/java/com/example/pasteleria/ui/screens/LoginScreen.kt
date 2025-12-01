package com.example.pasteleria.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.pasteleria.ui.viewModel.AppViewModel
import androidx.compose.ui.text.input.PasswordVisualTransformation

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(onBack: () -> Unit, onLoginSuccess: () -> Unit) {
    val vm: AppViewModel = viewModel()

    var usuario by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var pass by remember { mutableStateOf("") }
    var direccion by remember { mutableStateOf("") }
    var error by remember { mutableStateOf<String?>(null) }

    Scaffold(topBar = {
        CenterAlignedTopAppBar(
            title = { Text("Login") }
        )
    }) { inner ->
        Column(
            Modifier
                .padding(inner)
                .padding(30.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            OutlinedTextField(
                value = usuario,
                onValueChange = { usuario = it },
                label = { Text("Nombre Usuario") },
                singleLine = true
            )
            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = { Text("Email") },
                singleLine = true
            )
            OutlinedTextField(
                value = pass,
                onValueChange = { pass = it },
                label = { Text("Contraseña") },
                singleLine = true,
                visualTransformation = PasswordVisualTransformation()
            )

            Button(onClick = {
                error = when {
                    usuario.isBlank() -> "Usuario requerido"
                    !email.contains("@") -> "Email inválido, debe contener @"
                    pass.length < 6 -> "La contraseña debe tener al menos 6 caracteres"
                    else -> null
                }
                if (error == null) {
                    vm.guardarPerfil(usuario, email, direccion)
                    onLoginSuccess()
                }
            }) { Text("Ingresar") }

            error?.let { Text(it, color = MaterialTheme.colorScheme.error) }
        }
    }
}
