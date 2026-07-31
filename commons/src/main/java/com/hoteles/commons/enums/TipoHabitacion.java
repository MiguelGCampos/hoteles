package com.hoteles.commons.enums;

import com.hoteles.commons.exceptions.RecursoNoEncontradoException;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum TipoHabitacion {
    INDIVIDUAL(1L, "Una cama individual"),
    DOBLE(2L, "Doble King/Queen"),
    TWIN(3L, "Dos camas individuales"),
    TRIPLE(4L, "Tres camas");

    private final Long codigo;
    private final String descripcion;

    public static TipoHabitacion obtenerTipoHabitacionPorCodigo(Long codigo) {
        for (TipoHabitacion d : values()) {
            if (d.codigo == codigo) {
                return d;
            }
        }
        throw new RecursoNoEncontradoException("Código de tipo de habitación no válido: " + codigo);
    }
}
