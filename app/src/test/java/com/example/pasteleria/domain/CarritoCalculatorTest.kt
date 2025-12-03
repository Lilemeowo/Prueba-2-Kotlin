package com.example.pasteleria.domain


import com.example.pasteleria.data.Productos
import org.junit.Assert.assertEquals
import org.junit.Test

class CarritoCalculatorTest {

    // Productos de ejemplo (simulan lo que tienes en DataProductos)
    private val productos = listOf(
        Productos(
            id = 1,
            nombre = "Torta Tres Leches",
            precio = 10000,
            descripcion = "Torta clásica"
        ),
        Productos(
            id = 2,
            nombre = "Pie de Limón",
            precio = 8000,
            descripcion = "Pie casero"
        ),
        Productos(
            id = 3,
            nombre = "Cheesecake",
            precio = 12000,
            descripcion = "Cheesecake frutos rojos"
        )
    )

    private val calculator = CarritoCalculator(productos)

    @Test
    fun `subtotal de carrito vacío es 0`() {
        val ids = emptyList<Long>()

        val subtotal = calculator.calcularSubtotal(ids)

        assertEquals(0, subtotal)
    }

    @Test
    fun `subtotal suma correctamente varios productos`() {
        val ids = listOf(1L, 2L)  // Torta (10000) + Pie (8000) = 18000

        val subtotal = calculator.calcularSubtotal(ids)

        assertEquals(18000, subtotal)
    }

    @Test
    fun `subtotal ignora ids que no existen`() {
        val ids = listOf(1L, 999L)  // id 999 no existe

        val subtotal = calculator.calcularSubtotal(ids)

        // Solo considera la Torta (10000)
        assertEquals(10000, subtotal)
    }

    @Test
    fun `total con IVA 19 porciento se calcula bien`() {
        val subtotal = 10000

        val totalConIva = calculator.calcularTotalConIva(subtotal)

        // 10000 * 1,19 = 11900
        assertEquals(11900, totalConIva)
    }

    @Test
    fun `total con IVA custom se calcula bien`() {
        val subtotal = 10000

        val totalConIva = calculator.calcularTotalConIva(subtotal, iva = 0.10)

        // 10000 * 1,10 = 11000
        assertEquals(11000, totalConIva)
    }
}
