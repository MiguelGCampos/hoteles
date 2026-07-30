package com.hoteles.habitaciones.service;

import com.hoteles.commons.dto.habitaciones.HabitacionRequest;
import com.hoteles.commons.dto.habitaciones.HabitacionResponse;
import com.hoteles.commons.enums.EstadoHabitacion;
import com.hoteles.commons.enums.EstadoRegistro;
import com.hoteles.commons.enums.TipoHabitacion;
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
@Slf4j
@Transactional
@AllArgsConstructor
public class HabitacionServiceImpl implements HabitacionService{
    private final HabitacionRepository habitacionRepository;
    private final HabitacionMapper habitacionMapper;

    @Override
    @Transactional(readOnly = true)
    public HabitacionResponse obtenerHabitacionPorIdSinEstado(Long id) {
        log.info("Buscando médico sin estado con id {}", id);

        return habitacionMapper.entidadAResponse(habitacionRepository.findById(id)
                .orElseThrow(()->new RecursoNoEncontradoException(
                        "Medico sin estado no encontrado con id: {}"+id)));
    }

    @Override
    public void actualizarEstadoHabitacion(Long idHabitacion, Long idEstadoHabitacion) {
        Habitacion habitacion = obtenerHabitacionActivaOException(idHabitacion);

        EstadoHabitacion nuevoEstado = EstadoHabitacion.
                obtenerEstadoPorCodigo(idEstadoHabitacion);

        if (habitacion.getEstadoHabitacion() == nuevoEstado) return;

        EstadoHabitacion anteriorEstado = habitacion.getEstadoHabitacion();

        habitacion.setEstadoHabitacion(nuevoEstado);

        log.info("Disponibilidad del médico con id {} cambió de {} a {}",
                idHabitacion, anteriorEstado, nuevoEstado);
    }

    @Override
    @Transactional(readOnly = true)
    public List<HabitacionResponse> listar() {
        log.info("Listado de todos las habitaciones activos solicitado");
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
        log.info("Registrando nueva habitación");

        validarNumeroUnico(request.numero());

        Habitacion habitacion = habitacionMapper.requestAEntidad(request);

        habitacion.setEstadoHabitacion(EstadoHabitacion.DISPONIBLE);

        habitacionRepository.save(habitacion);

        log.info("Médico registrado con éxito: {}", habitacion.getNumeroHabitacion());
        return habitacionMapper.entidadAResponse(habitacion);
    }

    @Override
    public HabitacionResponse actualizar(HabitacionRequest request, Long id) {
        Habitacion habitacion = obtenerHabitacionActivaOException(id);
        log.info("Actualizando Habitación con id: {}", id);

        validarCambiosUnicos(request, habitacion);

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
        log.info("Eliminando Médico con id: {}", id);

        //medicoTieneCitasAsignadas(id);

        habitacion.eliminar();
        log.info("Médico con id {} ha sido eliminado", id);
    }

    private Habitacion obtenerHabitacionActivaOException(Long id) {
        log.info("Buscando Habitación con estado {} con id: {}", EstadoRegistro.ACTIVO, id);
        return habitacionRepository.findByIdAndEstadoRegistro(id, EstadoRegistro.ACTIVO).orElseThrow(() ->
                new RecursoNoEncontradoException("Habitación activa no encontrado con el id: " + id));
    }

    private void validarNumeroUnico(Integer numeroHabitacion) {
        if (habitacionRepository.existsByNumeroHabitacionIgnoreCaseAndEstadoRegistro(
                numeroHabitacion, EstadoRegistro.ACTIVO)) {

            throw new IllegalArgumentException(
                    "Ya existe una Habitación registrada con el numero: " + numeroHabitacion);
        }
    }

    private void validarCambiosUnicos(HabitacionRequest request, Habitacion habitacion) {

        if (!habitacion.getNumeroHabitacion().equals(request.numero()) &&
                habitacionRepository.existsByNumeroHabitacionIgnoreCaseAndEstadoRegistro(
                        request.numero(), EstadoRegistro.ACTIVO)) {

            throw new IllegalArgumentException(
                    "Ya existe un Habitación registrado con el numero: " + request.numero());
        }
    }

    /*private void medicoTieneCitasAsignadas(Long id) {
        citaClient.medicoTieneCitasAsignadas(id);
    }*/

}
