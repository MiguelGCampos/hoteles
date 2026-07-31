package com.hoteles.reservas.dto;

import com.hoteles.commons.dto.habitaciones.HabitacionResponse;
import com.hoteles.commons.dto.huespedes.HuespedResponse;

import java.time.LocalDate;

public record ReservaResponse(
        Long id,
        HuespedResponse huesped,
        HabitacionResponse habitacion,
        LocalDate fechaEntrada,
        LocalDate fechaSalida,
        String estadoReserva,
        String estadoRegistro
) {
}
