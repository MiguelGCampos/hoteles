package com.hoteles.reservas.services;

import com.hoteles.commons.dto.habitaciones.HabitacionResponse;
import com.hoteles.commons.dto.huespedes.HuespedResponse;
import com.hoteles.commons.enums.EstadoHabitacion;
import com.hoteles.commons.enums.EstadoRegistro;
import com.hoteles.commons.enums.EstadoReserva;
import com.hoteles.commons.exceptions.RecursoNoEncontradoException;
import com.hoteles.commons.exceptions.ReglaNegocioException;
import com.hoteles.reservas.clients.HabitacionClient;
import com.hoteles.reservas.clients.HuespedClient;
import com.hoteles.reservas.dto.ReservaRequest;
import com.hoteles.reservas.dto.ReservaResponse;
import com.hoteles.reservas.entities.Reserva;
import com.hoteles.reservas.mappers.ReservaMapper;
import com.hoteles.reservas.repositories.ReservaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReservaServiceImpl implements ReservaService {

    private final ReservaRepository reservaRepository;
    private final ReservaMapper reservaMapper;
    private final HuespedClient huespedClient;
    private final HabitacionClient habitacionClient;

    @Override
    @Transactional
    public ReservaResponse registrar(ReservaRequest request) {
        // Validaciones de negocio
        validarFechas(request.fechaEntrada(), request.fechaSalida());
        HuespedResponse huesped = huespedClient.obtenerHuespedPorIdSinEstado(request.idHuesped());
        if (huesped.estadoRegistro() != EstadoRegistro.ACTIVO) {
            throw new ReglaNegocioException("El huésped con ID " + request.idHuesped() + " no está activo.");
        }
        HabitacionResponse habitacion = habitacionClient.obtenerHabitacionPorIdSinEstado(request.idHabitacion());
        if (habitacion.estadoRegistro() != EstadoRegistro.ACTIVO || habitacion.estadoHabitacion() != EstadoHabitacion.DISPONIBLE) {
            throw new ReglaNegocioException("La habitación con ID " + request.idHabitacion() + " no está activa y disponible.");
        }

        // Creación y persistencia
        Reserva reserva = reservaMapper.requestAEntidad(request);
        reserva.setEstadoReserva(EstadoReserva.CONFIRMADA);
        reserva.setEstadoRegistro(EstadoRegistro.ACTIVO);
        Reserva reservaGuardada = reservaRepository.save(reserva);

        // Sincronización de estado con el microservicio de habitaciones
        habitacionClient.actualizarEstadoHabitacion(habitacion.id(), EstadoHabitacion.OCUPADA.getCodigo());

        return reservaMapper.entidadAResponse(reservaGuardada, huesped, habitacion);
    }

    @Override
    @Transactional
    public ReservaResponse actualizar(Long id, ReservaRequest request) {
        Reserva reserva = findReservaActivaById(id);
        validarFechas(request.fechaEntrada(), request.fechaSalida());

        switch (reserva.getEstadoReserva()) {
            case CONFIRMADA:
                reserva.setFechaEntrada(request.fechaEntrada());
                reserva.setFechaSalida(request.fechaSalida());
                break;
            case EN_CURSO:
                if (!reserva.getFechaEntrada().isEqual(request.fechaEntrada())) {
                    throw new ReglaNegocioException("No se puede modificar la fecha de entrada de una reserva EN_CURSO.");
                }
                reserva.setFechaSalida(request.fechaSalida());
                break;
            default:
                throw new ReglaNegocioException("No se pueden modificar reservas en estado " + reserva.getEstadoReserva());
        }

        Reserva reservaActualizada = reservaRepository.save(reserva);
        HuespedResponse huesped = huespedClient.obtenerHuespedPorIdSinEstado(reservaActualizada.getIdHuesped());
        HabitacionResponse habitacion = habitacionClient.obtenerHabitacionPorIdSinEstado(reservaActualizada.getIdHabitacion());
        return reservaMapper.entidadAResponse(reservaActualizada, huesped, habitacion);
    }

    @Override
    @Transactional
    public void actualizarEstado(Long idReserva, Long idEstado) {
        Reserva reserva = findReservaActivaById(idReserva);
        EstadoReserva nuevoEstado = EstadoReserva.obtenerEstadoCitaPorCodigo(idEstado);

        if (!reserva.getEstadoReserva().puedeCambiarA(nuevoEstado)) {
            throw new ReglaNegocioException("No se puede cambiar el estado de " + reserva.getEstadoReserva() + " a " + nuevoEstado);
        }
        reserva.setEstadoReserva(nuevoEstado);

        // Si la reserva termina (finalizada o cancelada), liberar la habitación
        if (nuevoEstado == EstadoReserva.FINALIZADA || nuevoEstado == EstadoReserva.CANCELADA) {
            habitacionClient.actualizarEstadoHabitacion(reserva.getIdHabitacion(), EstadoHabitacion.DISPONIBLE.getCodigo());
        }

        reservaRepository.save(reserva);
    }

    @Override
    @Transactional
    public void eliminar(Long id) {
        Reserva reserva = findReservaActivaById(id);
        if (reserva.getEstadoReserva() != EstadoReserva.CONFIRMADA) {
            throw new ReglaNegocioException("Solo se pueden eliminar (cancelar) reservas en estado CONFIRMADA.");
        }
        reserva.setEstadoRegistro(EstadoRegistro.ELIMINADO); // Borrado lógico
        reservaRepository.save(reserva);

        // Liberar la habitación
        habitacionClient.actualizarEstadoHabitacion(reserva.getIdHabitacion(), EstadoHabitacion.DISPONIBLE.getCodigo());
    }

    @Override
    @Transactional(readOnly = true)
    public List<ReservaResponse> listar() {
        return reservaRepository.findByEstadoRegistro(EstadoRegistro.ACTIVO).stream()
                .map(this::buildResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public ReservaResponse obtenerPorId(Long id) {
        Reserva reserva = findReservaActivaById(id);
        return buildResponse(reserva);
    }

    @Override
    public boolean huespedTieneReservasEnCurso(Long huespedId) {
        return reservaRepository.existsByIdHuespedAndEstadoRegistroAndEstadoReservaIn(
                huespedId,
                EstadoRegistro.ACTIVO,
                List.of(EstadoReserva.EN_CURSO)
        );
    }

    private Reserva findReservaActivaById(Long id) {
        return reservaRepository.findByIdAndEstadoRegistro(id, EstadoRegistro.ACTIVO)
                .orElseThrow(() -> new RecursoNoEncontradoException("Reserva activa con ID " + id + " no encontrada."));
    }

    private ReservaResponse buildResponse(Reserva reserva) {
        HuespedResponse huesped = huespedClient.obtenerHuespedPorIdSinEstado(reserva.getIdHuesped());
        HabitacionResponse habitacion = habitacionClient.obtenerHabitacionPorIdSinEstado(reserva.getIdHabitacion());
        return reservaMapper.entidadAResponse(reserva, huesped, habitacion);
    }

    private void validarFechas(LocalDate fechaEntrada, LocalDate fechaSalida) {
        if (fechaEntrada.isAfter(fechaSalida) || fechaEntrada.isEqual(fechaSalida)) {
            throw new ReglaNegocioException("La fecha de entrada debe ser anterior a la fecha de salida.");
        }
    }
}