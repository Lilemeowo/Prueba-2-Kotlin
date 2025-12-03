package com.example.pasteleria.domain


import com.example.pasteleria.data.Productos

class CarritoCalculator(
    private val productos: List<Productos>
) {

    // Suma el precio de todos los productos cuyo id está en el carrito
    fun calcularSubtotal(idsCarrito: List<Long>): Int {
        return idsCarrito.sumOf { id ->
            productos.firstOrNull { it.id == id }?.precio ?: 0
        }
    }

    // Aplica IVA (por defecto 19%)
    fun calcularTotalConIva(subtotal: Int, iva: Double = 0.19): Int {
        return (subtotal * (1.0 + iva)).toInt()
    }
}
