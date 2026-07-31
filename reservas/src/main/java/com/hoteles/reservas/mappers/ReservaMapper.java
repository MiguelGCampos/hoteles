package com.hoteles.reservas.mappers;

import com.hoteles.commons.dto.habitaciones.DatosHabitacion;
import com.hoteles.commons.dto.habitaciones.HabitacionResponse;
import com.hoteles.commons.dto.huespedes.DatosHuesped;
import com.hoteles.commons.dto.huespedes.HuespedResponse;
import com.hoteles.commons.mappers.CommonMapper;
import com.hoteles.reservas.dto.ReservaRequest;
import com.hoteles.reservas.dto.ReservaResponse;
import com.hoteles.reservas.entities.Reserva;
import org.springframework.stereotype.Component;

@Component
public class ReservaMapper implements CommonMapper<ReservaRequest, ReservaResponse, Reserva> {

    @Override
    public Reserva requestAEntidad(ReservaRequest request) {
        if (request == null) return null;

        return Reserva.crear(
                request.idHabitacion(),
                request.idHuesped(),
                request.fechaEntrada(),
                request.fechaSalida()
        );
    }

    @Override
    public ReservaResponse entidadAResponse(Reserva entidad) {
        if (entidad == null) return null;

        return new ReservaResponse(
                entidad.getId(),
                null,
                null,
                entidad.getFechaEntrada(),
                entidad.getFechaSalida(),
                entidad.getEstadoReserva() == null ? null : entidad.getEstadoReserva().name(),
                entidad.getEstadoRegistro() == null ? null : entidad.getEstadoRegistro().name()
        );
    }

    // -- Sobrecarga que enriquece la respuesta con los datos de Huésped y Habitación (vía Feign)

    public ReservaResponse entidadAResponse(Reserva entidad, HuespedResponse huesped, HabitacionResponse habitacion) {
        if (entidad == null) return null;

        return new ReservaResponse(
                entidad.getId(),
                huesped,
                habitacion,
                entidad.getFechaEntrada(),
                entidad.getFechaSalida(),
                entidad.getEstadoReserva() == null ? null : entidad.getEstadoReserva().name(),
                entidad.getEstadoRegistro() == null ? null : entidad.getEstadoRegistro().name()
        );
    }

    private DatosHuesped huespedResponseADatosHuesped(HuespedResponse huesped) {
        if (huesped == null) return null;

        // Concatenamos nombre y apellido paterno para el nombre completo en el DTO incrustado
        String nombreCompleto = huesped.nombre() + " " + huesped.apellidoPaterno();

        // Convertimos el Enum TipoDocumento a String
        String tipoDoc = huesped.tipoDocumento() != null ? huesped.tipoDocumento().name() : null;

        return new DatosHuesped(
                nombreCompleto,
                tipoDoc,
                huesped.telefono()
        );
    }

    private DatosHabitacion habitacionResponseADatosHabitacion(HabitacionResponse habitacion) {
        if (habitacion == null) return null;

        return new DatosHabitacion(
                habitacion.numero(),
                habitacion.tipoHabitacion().getDescripcion(),
                habitacion.precio()
        );
    }

} // FIN DE LA CLASE RESERVAMAPPER