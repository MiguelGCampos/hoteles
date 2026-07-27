package com.hoteles.commons.dto.habitaciones;

import jakarta.validation.constraints.*;

import java.math.BigDecimal;

public record HabitacionRequest(
        @NotNull(message = "El número es requerido.")
        @Positive(message = "El número debe ser positivo.")
        Integer numero,

        @NotBlank(message = "El tipo es requerido.")
        String tipo,

        @NotNull(message = "El precio es requerido")
        @Positive(message = "El precio debe ser positivo")
        BigDecimal precio,

        @NotNull(message = "La capacidad es requerida")
        @Min(value = 1, message = "La capacidad debe ser igual o mayor a 1")
        Integer capacidad
) {
}
