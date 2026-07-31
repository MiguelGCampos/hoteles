package com.hoteles.commons.dto.habitaciones;


import java.math.BigDecimal;

/**
 * Resumen de la habitación, usado dentro de la respuesta de una Reserva.
 */
public record DatosHabitacion(
        Integer numero,
        String tipo,
        BigDecimal precio
) {
}