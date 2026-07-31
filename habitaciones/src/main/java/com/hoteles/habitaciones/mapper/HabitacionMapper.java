package com.hoteles.habitaciones.mapper;

import com.hoteles.commons.dto.habitaciones.HabitacionRequest;
import com.hoteles.commons.dto.habitaciones.HabitacionResponse;
import com.hoteles.commons.enums.EstadoHabitacion;
import com.hoteles.commons.enums.EstadoRegistro;
import com.hoteles.commons.enums.TipoHabitacion;
import com.hoteles.commons.mappers.CommonMapper;
import com.hoteles.habitaciones.enitty.Habitacion;
import org.springframework.stereotype.Component;

@Component
public class HabitacionMapper implements CommonMapper<HabitacionRequest, HabitacionResponse, Habitacion> {

    @Override
    public Habitacion requestAEntidad(HabitacionRequest request) {
        if (request == null) return null;

        return Habitacion.builder()
                .numeroHabitacion(request.numero())
                .tipoHabitacion(TipoHabitacion.obtenerTipoHabitacionPorCodigo(request.tipoCodigo()))
                .precio(request.precio())
                .capacidad(request.capacidad())
                .estadoHabitacion(EstadoHabitacion.DISPONIBLE)
                .estadoRegistro(EstadoRegistro.ACTIVO)
                .build();
    }

    @Override
    public HabitacionResponse entidadAResponse(Habitacion entidad) {
        if (entidad == null) return null;

        return new HabitacionResponse(
                entidad.getId(),
                entidad.getNumeroHabitacion(),// ✅ código del tipo
                entidad.getTipoHabitacion().getDescripcion(), // ✅ descripción del tipo
                entidad.getPrecio(),
                entidad.getCapacidad(),
                entidad.getEstadoHabitacion().getDescripcion()
        );
    }
}
