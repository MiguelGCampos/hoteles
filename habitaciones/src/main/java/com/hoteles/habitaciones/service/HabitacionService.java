package com.hoteles.habitaciones.service;

import com.hoteles.commons.dto.habitaciones.HabitacionRequest;
import com.hoteles.commons.dto.habitaciones.HabitacionResponse;
import com.hoteles.commons.service.CrudService;
import org.springframework.transaction.annotation.Transactional;

public interface HabitacionService extends CrudService<HabitacionRequest, HabitacionResponse> {
    HabitacionResponse obtenerHabitacionPorIdSinEstado(Long id);

    void actualizarEstadoHabitacion(Long idHabitacion, Long idEstadoHabitacion);

    @Transactional(readOnly = true)
    HabitacionResponse obtenerHabitacionActivaDisponiblePorId(Long id);

    void sincronizarEstado(Long id, Long idEstado);
}