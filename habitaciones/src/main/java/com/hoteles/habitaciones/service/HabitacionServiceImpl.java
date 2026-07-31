package com.hoteles.habitaciones.service;


import com.hoteles.commons.dto.habitaciones.HabitacionRequest;
import com.hoteles.commons.dto.habitaciones.HabitacionResponse;
import com.hoteles.commons.enums.EstadoHabitacion;
import com.hoteles.commons.enums.EstadoRegistro;
import com.hoteles.commons.exceptions.RecursoNoEncontradoException;
import com.hoteles.habitaciones.enitty.Habitacion;
import com.hoteles.habitaciones.mapper.HabitacionMapper;
import com.hoteles.habitaciones.repository.HabitacionRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@AllArgsConstructor
@Transactional
@Slf4j
public class HabitacionServiceImpl implements HabitacionService {

    private final HabitacionRepository habitacionRepository;
    private final HabitacionMapper habitacionMapper;

    @Override
    @Transactional(readOnly = true)
    public List<HabitacionResponse> listar() {
        log.info("Listando habitaciones activas...");
        return habitacionRepository.findByEstadoRegistro(EstadoRegistro.ACTIVO).stream()
                .map(habitacionMapper::entidadAResponse).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public HabitacionResponse obtenerPorId(Long id) {
        log.info("Buscando habitación activa con id {}", id);
        return habitacionMapper.entidadAResponse(obtenerActivaOException(id));
    }

    @Override
    @Transactional(readOnly = true)
    public HabitacionResponse obtenerHabitacionPorIdSinEstado(Long id) {
        log.info("Buscando habitación sin filtro de estado con id {}", id);
        return habitacionMapper.entidadAResponse(habitacionRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Habitación no encontrada con id " + id)));
    }

    @Override
    public void actualizarEstadoHabitacion(Long idHabitacion, Long idEstadoHabitacion) {
        Habitacion habitacion = obtenerActivaOException(idHabitacion);

        log.info("Cambiando estado de la habitación con id {}", idHabitacion);

        EstadoHabitacion nuevoEstado = EstadoHabitacion.obtenerEstadoPorCodigo(idEstadoHabitacion);

        habitacion.cambiarEstado(nuevoEstado);

        log.info("Estado de la habitación {} actualizado a {}", idHabitacion, nuevoEstado);
    }

    @Override
    public HabitacionResponse registrar(HabitacionRequest request) {
        log.info("Registrando nueva habitación número {}", request.numero());

        validarNumeroUnico(request.numero(), null);

        Habitacion habitacion = habitacionMapper.requestAEntidad(request);

        habitacionRepository.save(habitacion);

        log.info("Habitación registrada con éxito: {}", habitacion.getNumeroHabitacion());

        return habitacionMapper.entidadAResponse(habitacion);
    }

    @Override
    public HabitacionResponse actualizar(HabitacionRequest request, Long id) {
        Habitacion habitacion = obtenerActivaOException(id);

        log.info("Actualizando habitación con id: {}", id);

        validarNumeroUnico(request.numero(), id);

        habitacion.actualizar(request.numero(), request.tipo(), request.precio(), request.capacidad());

        log.info("Habitación actualizada con éxito: {}", habitacion.getNumeroHabitacion());

        return habitacionMapper.entidadAResponse(habitacion);
    }

    @Override
    public void eliminar(Long id) {
        Habitacion habitacion = obtenerActivaOException(id);

        log.info("Eliminando habitación con id {}", id);

        habitacion.eliminar();

        log.info("Habitación con id {} ha sido eliminada", id);
    }


    @Transactional(readOnly = true)
    @Override
    public HabitacionResponse obtenerHabitacionActivaDisponiblePorId(Long id) {
        log.info("Buscando habitación activa y disponible con id {}", id);

        Habitacion habitacion = habitacionRepository.findByIdAndEstadoRegistro(id, EstadoRegistro.ACTIVO)
                .orElseThrow(() -> new RecursoNoEncontradoException("Habitación activa no encontrada con id " + id));

        if (habitacion.getEstadoHabitacion() != EstadoHabitacion.DISPONIBLE) {
            throw new IllegalStateException("La habitación con id " + id + " no está disponible actualmente");
        }

        return habitacionMapper.entidadAResponse(habitacion);
    }

    @Override
    public void sincronizarEstado(Long id, Long idEstado) {
        Habitacion habitacion = obtenerActivaOException(id);

        EstadoHabitacion nuevoEstado = EstadoHabitacion.obtenerEstadoPorCodigo(idEstado);

        log.info("Sincronizando estado de la habitación {} a {} (disparado por Reservas)", id, nuevoEstado);

        habitacion.sincronizarEstado(nuevoEstado);
    }

    private Habitacion obtenerActivaOException(Long id) {
        return habitacionRepository.findByIdAndEstadoRegistro(id, EstadoRegistro.ACTIVO)
                .orElseThrow(() -> new RecursoNoEncontradoException("Habitación activa no encontrada con id " + id));
    }

    private void validarNumeroUnico(Integer numero, Long idExcluir) {
        log.info("Validando número de habitación único...");

        boolean existe = (idExcluir == null)
                ? habitacionRepository.existsByNumeroAndEstadoRegistro(numero, EstadoRegistro.ACTIVO)
                : habitacionRepository.existsByNumeroAndEstadoRegistroAndIdNot(numero, EstadoRegistro.ACTIVO, idExcluir);

        if (existe) {
            throw new IllegalArgumentException("Ya existe una habitación activa registrada con el número: " + numero);
        }
    }

}