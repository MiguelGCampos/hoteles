package com.hoteles.habitaciones.service;

import com.hoteles.commons.dto.habitaciones.HabitacionRequest;
import com.hoteles.commons.dto.habitaciones.HabitacionResponse;
import com.hoteles.commons.enums.EstadoHabitacion;
import com.hoteles.commons.enums.EstadoRegistro;
import com.hoteles.commons.enums.TipoHabitacion;
import com.hoteles.commons.exceptions.ArgumentoNoValidoException;
import com.hoteles.commons.exceptions.RecursoNoEncontradoException;
import com.hoteles.commons.exceptions.ReglaNegocioException;
import com.hoteles.habitaciones.enitty.Habitacion;
import com.hoteles.habitaciones.mapper.HabitacionMapper;
import com.hoteles.habitaciones.repository.HabitacionRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Slf4j
@Transactional
@AllArgsConstructor
public class HabitacionServiceImpl implements HabitacionService {

    private final HabitacionRepository habitacionRepository;
    private final HabitacionMapper habitacionMapper;

    @Override
    @Transactional(readOnly = true)
    public HabitacionResponse obtenerHabitacionPorIdSinEstado(Long id) {
        log.info("Buscando habitación sin filtro de estado con id {}", id);

        return habitacionMapper.entidadAResponse(habitacionRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException(
                        "Habitación no encontrada con id: " + id)));
    }

    @Override
    public HabitacionResponse actualizarEstadoHabitacion(Long idHabitacion, Long idEstadoHabitacion) {
        Habitacion habitacion = obtenerHabitacionActivaOException(idHabitacion);
        EstadoHabitacion nuevoEstado = EstadoHabitacion.obtenerEstadoPorCodigo(idEstadoHabitacion);

        // Regla de negocio: no se puede cambiar manualmente a DISPONIBLE si está OCUPADA.
        // Ese cambio solo lo puede disparar automáticamente el microservicio de Reservas
        // (vía el endpoint interno) al hacer check-out o cancelar.
        if (nuevoEstado == EstadoHabitacion.DISPONIBLE && habitacion.getEstadoHabitacion() == EstadoHabitacion.OCUPADA) {
            throw new ReglaNegocioException(
                    "No se puede cambiar manualmente a DISPONIBLE una habitación OCUPADA. " +
                            "Esta transición solo ocurre automáticamente al finalizar o cancelar su reserva.");
        }

        return cambiarEstado(habitacion, nuevoEstado, idHabitacion);
    }

    @Override
    public HabitacionResponse actualizarEstadoHabitacionInterno(Long idHabitacion, Long idEstadoHabitacion) {
        Habitacion habitacion = obtenerHabitacionActivaOException(idHabitacion);
        EstadoHabitacion nuevoEstado = EstadoHabitacion.obtenerEstadoPorCodigo(idEstadoHabitacion);
        return cambiarEstado(habitacion, nuevoEstado, idHabitacion);
    }

    private HabitacionResponse cambiarEstado(Habitacion habitacion, EstadoHabitacion nuevoEstado, Long idHabitacion) {
        if (habitacion.getEstadoHabitacion() == nuevoEstado) {
            return habitacionMapper.entidadAResponse(habitacion);
        }

        EstadoHabitacion anteriorEstado = habitacion.getEstadoHabitacion();
        habitacion.setEstadoHabitacion(nuevoEstado);

        log.info("Estado de la habitación con id {} cambió de {} a {}", idHabitacion, anteriorEstado, nuevoEstado);
        return habitacionMapper.entidadAResponse(habitacion);
    }

    @Override
    @Transactional(readOnly = true)
    public List<HabitacionResponse> listar() {
        log.info("Listado de todas las habitaciones activas solicitado");
        return habitacionRepository.findByEstadoRegistro(EstadoRegistro.ACTIVO).stream()
                .map(habitacionMapper::entidadAResponse).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public HabitacionResponse obtenerPorId(Long id) {
        return habitacionMapper.entidadAResponse(obtenerHabitacionActivaOException(id));
    }

    @Override
    public HabitacionResponse registrar(HabitacionRequest request) {
        log.info("Registrando nueva habitación con número {}", request.numero());

        validarNumeroUnico(request.numero());

        Habitacion habitacion = habitacionMapper.requestAEntidad(request);
        habitacion.setEstadoHabitacion(EstadoHabitacion.DISPONIBLE);

        habitacionRepository.save(habitacion);

        log.info("Habitación registrada con éxito: {}", habitacion.getNumeroHabitacion());
        return habitacionMapper.entidadAResponse(habitacion);
    }

    @Override
    public HabitacionResponse actualizar(HabitacionRequest request, Long id) {
        Habitacion habitacion = obtenerHabitacionActivaOException(id);
        log.info("Actualizando habitación con id: {}", id);

        validarCambiosUnicos(request, habitacion);

        // El estado de la habitación NO se modifica desde este endpoint,
        // solo desde PUT /{id}/estado/{idEstado}.
        habitacion.actualizar(
                request.numero(),
                TipoHabitacion.obtenerTipoHabitacionPorCodigo(request.tipoCodigo()),
                request.precio(),
                request.capacidad(),
                habitacion.getEstadoHabitacion()
        );

        habitacionRepository.save(habitacion);

        log.info("Habitación actualizada con éxito: {}", id);
        return habitacionMapper.entidadAResponse(habitacion);
    }

    @Override
    public void eliminar(Long id) {
        Habitacion habitacion = obtenerHabitacionActivaOException(id);
        log.info("Eliminando habitación con id: {}", id);

        if (habitacion.getEstadoHabitacion() == EstadoHabitacion.OCUPADA) {
            throw new ReglaNegocioException(
                    "No se puede eliminar la habitación con id " + id + " porque está OCUPADA.");
        }

        habitacion.eliminar();
        log.info("Habitación con id {} ha sido eliminada", id);
    }

    private Habitacion obtenerHabitacionActivaOException(Long id) {
        log.info("Buscando habitación con estado {} con id: {}", EstadoRegistro.ACTIVO, id);
        return habitacionRepository.findByIdAndEstadoRegistro(id, EstadoRegistro.ACTIVO).orElseThrow(() ->
                new RecursoNoEncontradoException("Habitación activa no encontrada con el id: " + id));
    }

    private void validarNumeroUnico(Integer numeroHabitacion) {
        if (habitacionRepository.existsByNumeroHabitacionAndEstadoRegistro(
                numeroHabitacion, EstadoRegistro.ACTIVO)) {

            throw new ArgumentoNoValidoException(
                    "Ya existe una habitación activa registrada con el número: " + numeroHabitacion);
        }
    }

    private void validarCambiosUnicos(HabitacionRequest request, Habitacion habitacion) {
        if (!habitacion.getNumeroHabitacion().equals(request.numero()) &&
                habitacionRepository.existsByNumeroHabitacionAndEstadoRegistroAndIdNot(
                        request.numero(), EstadoRegistro.ACTIVO, habitacion.getId())) {

            throw new ArgumentoNoValidoException(
                    "Ya existe una habitación activa registrada con el número: " + request.numero());
        }
    }
}
