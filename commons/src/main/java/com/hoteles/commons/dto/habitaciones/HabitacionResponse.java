package com.hoteles.commons.dto.habitaciones;

import com.hoteles.commons.enums.EstadoHabitacion;
import com.hoteles.commons.enums.EstadoRegistro;
import com.hoteles.commons.enums.TipoHabitacion;

import java.math.BigDecimal;

public record HabitacionResponse(
        Long id,
        Integer numero,
        TipoHabitacion tipoHabitacion,
        BigDecimal precio,
        Integer capacidad,
        EstadoHabitacion estadoHabitacion,
        EstadoRegistro estadoRegistro
) {
}
