package com.example.pasteleria.data.remote



data class OrdenRequest(
    val nombreCliente: String,
    val cantidadProductos: Int,
    val subtotal: Int,
    val total: Int
)
