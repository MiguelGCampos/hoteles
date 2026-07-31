package com.hoteles.reservas.dto;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public record ReservaRequest(
        @NotNull(message = "El ID del huésped no puede ser nulo.")
        Long idHuesped,

        @NotNull(message = "El ID de la habitación no puede ser nulo.")
        Long idHabitacion,

        @NotNull(message = "La fecha de entrada no puede ser nula.")
        @FutureOrPresent(message = "La fecha de entrada no puede ser en el pasado.")
        LocalDate fechaEntrada,

        @NotNull(message = "La fecha de salida no puede ser nula.")
        LocalDate fechaSalida
) {
}
