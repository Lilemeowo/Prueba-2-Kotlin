package com.example.pasteleria.data

data class Sucursal(
    val id: Int,
    val nombre: String,
    val direccion: String,
    val horario: String,
    val telefono: String
)

val DataSucursales = listOf(
    Sucursal(
        id = 1,
        nombre = "Pastelería 1000 Sabores - Centro",
        direccion = "Av. O'Higgins 1234, Santiago Centro",
        horario = "Lun a Dom 09:00 - 21:00",
        telefono = "+56 9 5555 1111"
    ),
    Sucursal(
        id = 2,
        nombre = "Pastelería 1000 Sabores - Maipú",
        direccion = "Camino a Melipilla 7890, Maipú",
        horario = "Lun a Sáb 08:30 - 20:00",
        telefono = "+56 9 5555 2222"
    ),
    Sucursal(
        id = 3,
        nombre = "Pastelería 1000 Sabores - Viña del Mar",
        direccion = "Calle Álvarez 456, Viña del Mar",
        horario = "Lun a Dom 10:00 - 21:30",
        telefono = "+56 9 5555 3333"
    )
)
