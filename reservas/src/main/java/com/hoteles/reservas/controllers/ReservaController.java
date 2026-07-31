package com.hoteles.reservas.controllers;

import com.hoteles.reservas.dto.ReservaRequest;
import com.hoteles.reservas.dto.ReservaResponse;
import com.hoteles.reservas.services.ReservaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/reservas")
@RequiredArgsConstructor
public class ReservaController {

    private final ReservaService reservaService;

    @PostMapping
    public ResponseEntity<ReservaResponse> registrar(@Valid @RequestBody ReservaRequest request) {
        ReservaResponse response = reservaService.registrar(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    public ResponseEntity<List<ReservaResponse>> listar() {
        return ResponseEntity.ok(reservaService.listar());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ReservaResponse> obtenerPorId(@PathVariable Long id) {
        return ResponseEntity.ok(reservaService.obtenerPorId(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ReservaResponse> actualizar(@PathVariable Long id, @Valid @RequestBody ReservaRequest request) {
        return ResponseEntity.ok(reservaService.actualizar(id, request));
    }

    @PatchMapping("/{idReserva}/estado/{idEstado}")
    public ResponseEntity<Void> actualizarEstado(@PathVariable Long idReserva, @PathVariable Long idEstado) {
        reservaService.actualizarEstado(idReserva, idEstado);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        reservaService.eliminar(id);
        return ResponseEntity.noContent().build();
    }


    // Consumido por el microservicio de huéspedes vía commons.ReservaClient.
    @GetMapping("/huesped/{huespedId}/tiene-reservas-bloqueantes")
    public ResponseEntity<Boolean> huespedTieneReservasEnCurso(@PathVariable Long huespedId) {
        return ResponseEntity.ok(reservaService.huespedTieneReservasEnCurso(huespedId));
    }
}
