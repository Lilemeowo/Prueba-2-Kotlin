package com.example.pasteleria.data.remote


import com.example.pasteleria.data.Productos
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface PasteleriaApi {

    @GET("productos")
    suspend fun getProductos(): List<Productos>

    @POST("ordenes")
    suspend fun crearOrden(@Body orden: OrdenRequest)
}
