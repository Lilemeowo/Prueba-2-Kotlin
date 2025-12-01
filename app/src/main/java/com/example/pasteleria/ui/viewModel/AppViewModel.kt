package com.example.pasteleria.ui.viewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import com.example.pasteleria.data.DatosUsuario
import com.example.pasteleria.data.DataProductos

class AppViewModel(app: Application): AndroidViewModel(app) {
    private val datos = DatosUsuario(app)

    private val _carrito = MutableStateFlow<List<Long>>(emptyList())
    val carrito: StateFlow<List<Long>> = _carrito

    val nombreUsuario: StateFlow<String> = datos.nombreUsuario.stateIn(
        viewModelScope, SharingStarted.WhileSubscribed(5_000), ""
    )
    val emailUsuario: StateFlow<String> = datos.emailUsuario.stateIn(
        viewModelScope, SharingStarted.WhileSubscribed(5_000), ""
    )
    val direccionUsuario: StateFlow<String> = datos.direccionUsuario.stateIn(
        viewModelScope, SharingStarted.WhileSubscribed(5_000), ""
    )

    val estaLogueado: StateFlow<Boolean> = nombreUsuario
        .map { it.isNotBlank() }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), false)

    val ordenesCsv = datos.ordenesCsv.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), "")

    init {
        viewModelScope.launch {
            datos.carritoIds.collect { csv ->
                _carrito.value = if (csv.isBlank()) emptyList()
                else csv.split(",").mapNotNull { it.toLongOrNull() }
            }
        }
    }

    fun agregarEnCarrito(id: Long) { _carrito.value = _carrito.value + id; persistCarrito() }
    fun limpiarCarrito() { _carrito.value = emptyList(); persistCarrito() }
    private fun persistCarrito() = viewModelScope.launch {
        datos.guardarCarrito(_carrito.value.joinToString(","))
    }

    fun guardarPerfil(
        nombre: String,
        email: String,
        direccion: String = "" ) = viewModelScope.launch {
        datos.guardarUsuario(nombre, email, direccion)
    }

    fun logout() = viewModelScope.launch {
        datos.guardarUsuario("", "")
        datos.guardarCarrito("")
        _carrito.value = emptyList()
    }

    fun total(): Int = _carrito.value.sumOf { id ->
        DataProductos.firstOrNull { it.id == id }?.precio ?: 0
    }
    fun pagarYGuardarOrden() = viewModelScope.launch {
        val subtotal = total()
        val totalIva = (subtotal * 1.19).toInt()
        val cantidad = _carrito.value.size
        val fecha = System.currentTimeMillis()

        val linea = "$fecha;$cantidad;$subtotal;$totalIva"

        datos.agregarOrden(linea)
        limpiarCarrito()
    }

}