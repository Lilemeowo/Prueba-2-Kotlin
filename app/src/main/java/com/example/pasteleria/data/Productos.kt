package com.example.pasteleria.data

data class Productos(
    val id: Long,
    val nombre: String,
    val precio: Int,
    val descripcion: String
)

val DataProductos = listOf(
    Productos(1, "Torta Tres Leches", 14_990, "Bizcocho húmedo, crema y merengue"),
    Productos(2, "Torta Chocolate Intenso", 18_990, "Relleno de ganache y cobertura de cacao"),
    Productos(3, "Torta Frutos Rojos", 16_990, "Crema chantilly con mezcla de berries"),
    Productos(4, "Torta Mil Hojas", 15_990, "Hojarasca tradicional con manjar casero"),
    Productos(5, "Torta Selva Negra", 19_990, "Bizcocho de chocolate, crema y cerezas")
)

