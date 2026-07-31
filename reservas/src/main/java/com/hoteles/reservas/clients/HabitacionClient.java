package com.hoteles.reservas.clients;

import com.hoteles.commons.dto.habitaciones.HabitacionResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;

// El "name" debe coincidir con spring.application.name / eureka.instance.appname
// del microservicio de habitaciones ("habitaciones"), no con un alias arbitrario.
@FeignClient(name = "habitaciones")
public interface HabitacionClient {

    /**
     * Obtiene una habitación por su ID, sin importar su estado de registro.
     */
    @GetMapping("/api/habitaciones/id-habitacion/{id}")
    HabitacionResponse obtenerHabitacionPorIdSinEstado(@PathVariable("id") Long id);

    /**
     * Actualiza el estado de una habitación desde el flujo automático de Reservas
     * (check-in, check-out, cancelación). Usa el endpoint INTERNO, que no aplica
     * la restricción de "no volver a DISPONIBLE manualmente si está OCUPADA",
     * ya que es precisamente Reservas quien debe liberar la habitación.
     */
    @PutMapping("/api/habitaciones/internos/{idHabitacion}/estado/{idEstado}")
    void actualizarEstadoHabitacion(@PathVariable("idHabitacion") Long idHabitacion, @PathVariable("idEstado") Long idEstado);
}
