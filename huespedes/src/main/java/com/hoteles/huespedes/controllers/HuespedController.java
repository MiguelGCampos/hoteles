package com.hoteles.huespedes.controllers;

import com.hoteles.commons.controller.CommonController;
import com.hoteles.commons.dto.huespedes.HuespedRequest;
import com.hoteles.commons.dto.huespedes.HuespedResponse;
import com.hoteles.huespedes.services.HuespedService;
import jakarta.validation.constraints.Positive;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/huespedes")
public class HuespedController extends CommonController<HuespedRequest, HuespedResponse, HuespedService> {

    public HuespedController(HuespedService service) {
        super(service);
    }

    @GetMapping("/id-huesped/{id}")
    public ResponseEntity<HuespedResponse> obtenerHuespedPorIdSinEstado(
            @PathVariable @Positive(message = "El ID debe ser positivo") Long id) {
        return ResponseEntity.ok(service.obtenerHuespedSinEstadoPorId(id));
    }

    // --- Endpoints internos, consumidos por Reservas vía Feign ---

    @GetMapping("/internos/{id}/activo")
    public ResponseEntity<HuespedResponse> obtenerHuespedActivoPorId(@PathVariable Long id) {
        return ResponseEntity.ok(service.obtenerHuespedActivoPorId(id));
    }

    @GetMapping("/internos/{id}")
    public ResponseEntity<HuespedResponse> obtenerHuespedSinEstadoPorId(@PathVariable Long id) {
        return ResponseEntity.ok(service.obtenerHuespedSinEstadoPorId(id));
    }

}
