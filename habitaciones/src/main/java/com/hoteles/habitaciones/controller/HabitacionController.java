package com.hoteles.habitaciones.controller;

import com.hoteles.commons.controller.CommonController;
import com.hoteles.commons.dto.habitaciones.HabitacionRequest;
import com.hoteles.commons.dto.habitaciones.HabitacionResponse;
import com.hoteles.habitaciones.service.HabitacionService;
import jakarta.validation.constraints.Positive;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/habitaciones")
@Validated
public class HabitacionController extends CommonController<HabitacionRequest, HabitacionResponse, HabitacionService> {

    public HabitacionController(HabitacionService service) {
        super(service);
    }

    @GetMapping("/id-habitacion/{id}")
    public ResponseEntity<HabitacionResponse> obtenerHabitacionPorIdSinEstado(
            @PathVariable @Positive(message = "El ID debe ser positivo") Long id) {
        return ResponseEntity.ok(service.obtenerHabitacionPorIdSinEstado(id));
    }

    // Cambio de estado desde el frontend (ADMIN). Aplica la restricción de negocio
    // que impide pasar manualmente de OCUPADA a DISPONIBLE.
    @PutMapping("/{id}/estado/{idEstado}")
    public ResponseEntity<HabitacionResponse> actualizarEstado(
            @PathVariable @Positive(message = "El ID debe ser positivo") Long id,
            @PathVariable @Positive(message = "El código de estado debe ser positivo") Long idEstado) {
        return ResponseEntity.ok(service.actualizarEstadoHabitacion(id, idEstado));
    }

    // --- Endpoint interno, consumido por Reservas vía Feign ---
    // Usado para sincronizar el estado automáticamente en check-in/check-out/cancelación,
    // sin la restricción manual del endpoint público.
    @PutMapping("/internos/{id}/estado/{idEstado}")
    public ResponseEntity<HabitacionResponse> actualizarEstadoInterno(
            @PathVariable Long id,
            @PathVariable Long idEstado) {
        return ResponseEntity.ok(service.actualizarEstadoHabitacionInterno(id, idEstado));
    }
}
