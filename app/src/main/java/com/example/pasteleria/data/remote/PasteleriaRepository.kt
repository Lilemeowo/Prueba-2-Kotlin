package com.example.pasteleria.data.remote


import com.example.pasteleria.data.Productos

class PasteleriaRepository(
    private val api: PasteleriaApi = RetrofitInstance.api
) {

    suspend fun obtenerProductos(): List<Productos> = api.getProductos()

    suspend fun crearOrden(orden: OrdenRequest) {
        api.crearOrden(orden)
    }
}
