package com.hoteles.commons.enums;

import com.hoteles.commons.exceptions.RecursoNoEncontradoException;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum EstadoHabitacion {
    DISPONIBLE(1L, "Lista para asignarse"),
    OCUPADA(2L, "Asignada a una reserva"),
    LIMPIEZA(3L, "En limpieza"),
    MANTENIMIENTO(4L, "En repareación");

    private final Long codigo;
    private final String descripcion;

    public static EstadoHabitacion obtenerEstadoPorCodigo(Long codigo) {
        for (EstadoHabitacion e : values()) {
            if (e.codigo == codigo) {
                return e;
            }
        }
        throw new RecursoNoEncontradoException("Código de especialidad no válido: " + codigo);
    }
}
