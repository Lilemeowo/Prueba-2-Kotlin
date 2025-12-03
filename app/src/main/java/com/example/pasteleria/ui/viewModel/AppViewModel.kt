package com.example.pasteleria.ui.viewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.pasteleria.data.DatosUsuario
import com.example.pasteleria.data.DataProductos
import com.example.pasteleria.data.Productos
import com.example.pasteleria.data.remote.OrdenRequest
import com.example.pasteleria.data.remote.PasteleriaRepository
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class AppViewModel(app: Application) : AndroidViewModel(app) {

    private val datos = DatosUsuario(app)
    private val repository = PasteleriaRepository()

    // Productos que vienen del backend
    private val _productos = MutableStateFlow<List<Productos>>(emptyList())
    val productos: StateFlow<List<Productos>> = _productos

    // Carrito (sigue igual)
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

    val ordenesCsv = datos.ordenesCsv.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5_000),
        ""
    )

    init {
        // Cargar carrito desde DataStore
        viewModelScope.launch {
            datos.carritoIds.collect { csv ->
                _carrito.value = if (csv.isBlank()) emptyList()
                else csv.split(",").mapNotNull { it.toLongOrNull() }
            }
        }

        // Cargar productos desde el backend
        cargarProductos()
    }

    private fun cargarProductos() = viewModelScope.launch {
        try {
            val remotos = repository.obtenerProductos()
            _productos.value = remotos
        } catch (e: Exception) {
            // Si el backend falla, usamos los productos locales
            _productos.value = DataProductos
        }
    }

    fun agregarEnCarrito(id: Long) {
        _carrito.value = _carrito.value + id
        persistCarrito()
    }

    fun limpiarCarrito() {
        _carrito.value = emptyList()
        persistCarrito()
    }

    private fun persistCarrito() = viewModelScope.launch {
        datos.guardarCarrito(_carrito.value.joinToString(","))
    }

    fun guardarPerfil(
        nombre: String,
        email: String,
        direccion: String = ""
    ) = viewModelScope.launch {
        datos.guardarUsuario(nombre, email, direccion)
    }

    fun logout() = viewModelScope.launch {
        datos.guardarUsuario("", "")
        datos.guardarCarrito("")
        _carrito.value = emptyList()
    }

    fun total(): Int {
        val listaProductos =
            if (_productos.value.isNotEmpty()) _productos.value else DataProductos

        return _carrito.value.sumOf { id ->
            listaProductos.firstOrNull { it.id == id }?.precio ?: 0
        }
    }

    fun pagarYGuardarOrden() = viewModelScope.launch {
        val subtotal = total()
        val totalIva = (subtotal * 1.19).toInt()
        val cantidad = _carrito.value.size
        val fecha = System.currentTimeMillis()

        // Guardar en DataStore como antes
        val linea = "$fecha;$cantidad;$subtotal;$totalIva"
        datos.agregarOrden(linea)

        // Enviar orden al backend
        try {
            val nombre = nombreUsuario.value.ifBlank { "Cliente" }
            val ordenRequest = OrdenRequest(
                nombreCliente = nombre,
                cantidadProductos = cantidad,
                subtotal = subtotal,
                total = totalIva
            )
            repository.crearOrden(ordenRequest)
        } catch (e: Exception) {
            // Si falla el backend, al menos queda guardado en DataStore
            // Podrías hacer un Log.e aquí si quieres
        }

        limpiarCarrito()
    }
}
