package com.hoteles.reservas.mappers;

import com.hoteles.commons.dto.habitaciones.HabitacionResponse;
import com.hoteles.commons.dto.huespedes.HuespedResponse;
import com.hoteles.reservas.dto.ReservaRequest;
import com.hoteles.reservas.dto.ReservaResponse;
import com.hoteles.reservas.entities.Reserva;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ReservaMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "estadoReserva", ignore = true)
    @Mapping(target = "estadoRegistro", ignore = true)
    Reserva toEntity(ReservaRequest request);

    @Mapping(source = "reserva.id", target = "id")
    @Mapping(source = "reserva.fechaEntrada", target = "fechaEntrada")
    @Mapping(source = "reserva.fechaSalida", target = "fechaSalida")
    @Mapping(source = "reserva.estadoReserva.descripcion", target = "estadoReserva")
    @Mapping(source = "huesped", target = "huesped")
    @Mapping(source = "habitacion", target = "habitacion")
    ReservaResponse toResponse(Reserva reserva, HuespedResponse huesped, HabitacionResponse habitacion);
}
