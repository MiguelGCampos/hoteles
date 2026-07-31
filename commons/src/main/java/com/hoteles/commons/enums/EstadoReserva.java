package com.hoteles.commons.enums;

import com.hoteles.commons.exceptions.RecursoNoEncontradoException;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.EnumSet;
import java.util.Objects;
import java.util.Set;

@Getter
@AllArgsConstructor
public enum EstadoReserva {
    CONFIRMADA(1L, "Reserva creada", true, true) {
        @Override
        public Set<EstadoReserva> puedeCambiar() {
            return EnumSet.of(EN_CURSO, CANCELADA);
        }
    },
    EN_CURSO(2L, "Check-in realizado", true, false){
        @Override
        public Set<EstadoReserva> puedeCambiar() { return EnumSet.of(FINALIZADA);}
    },
    FINALIZADA(3L, "Check-out realizado", false, false){
        @Override
        public Set<EstadoReserva> puedeCambiar() { return EnumSet.noneOf(EstadoReserva.class);}
    },
    CANCELADA(4L, "Reserva cancelada", false, false){
        @Override
        public Set<EstadoReserva> puedeCambiar() { return EnumSet.noneOf(EstadoReserva.class);}
    };

    private final Long codigo;
    private final String descripcion;
    private final boolean actualizable;

    private final boolean eliminable;

    public abstract Set<EstadoReserva> puedeCambiar();

    public boolean puedeCambiarA(EstadoReserva nuevoEstado){
        return this.puedeCambiar().contains(nuevoEstado);
    }

    public static EstadoReserva obtenerEstadoCitaPorCodigo(Long codigo){
        for(EstadoReserva e : values()){
            if(Objects.equals(e.codigo, codigo)){
                return e;
            }
        }
        throw new RecursoNoEncontradoException("Código de cita no válido: "+codigo);
    }

    public boolean esActivo() {
        return this == CONFIRMADA || this == EN_CURSO;
    }
}
