package com.example.pasteleria.ui.viewModel



import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pasteleria.data.api.PostreDto
import com.example.pasteleria.data.api.PostresRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class PostresApiViewModel : ViewModel() {

    private val repository = PostresRepository()

    private val _postres = MutableStateFlow<List<PostreDto>>(emptyList())
    val postres: StateFlow<List<PostreDto>> = _postres

    private val _cargando = MutableStateFlow(false)
    val cargando: StateFlow<Boolean> = _cargando

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    init {
        cargarPostres()
    }

    fun cargarPostres() {
        viewModelScope.launch {
            _cargando.value = true
            _error.value = null
            try {
                _postres.value = repository.obtenerPostres()
            } catch (e: Exception) {
                _error.value = "No se pudieron cargar los postres"
            } finally {
                _cargando.value = false
            }
        }
    }
}
