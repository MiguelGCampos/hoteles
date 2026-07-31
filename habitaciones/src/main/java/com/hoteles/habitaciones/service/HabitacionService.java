package com.hoteles.habitaciones.service;

import com.hoteles.commons.dto.habitaciones.HabitacionRequest;
import com.hoteles.commons.dto.habitaciones.HabitacionResponse;
import com.hoteles.commons.service.CrudService;

public interface HabitacionService extends CrudService<HabitacionRequest, HabitacionResponse> {

    HabitacionResponse obtenerHabitacionPorIdSinEstado(Long id);

    /**
     * Cambio de estado solicitado por el ADMIN (frontend). Aplica la regla de negocio
     * que impide pasar manualmente a DISPONIBLE una habitación OCUPADA.
     */
    HabitacionResponse actualizarEstadoHabitacion(Long idHabitacion, Long idEstadoHabitacion);

    /**
     * Cambio de estado disparado automáticamente por el microservicio de Reservas
     * (check-in, check-out, cancelación). No aplica la restricción manual anterior,
     * ya que es el propio flujo de negocio el que libera/ocupa la habitación.
     */
    HabitacionResponse actualizarEstadoHabitacionInterno(Long idHabitacion, Long idEstadoHabitacion);
}
