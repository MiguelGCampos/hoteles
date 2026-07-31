package com.hoteles.huespedes.clients;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "reservas-service")
public interface ReservaClient {

    @GetMapping("/api/reservas/huesped/{huespedId}/en-curso")
    boolean huespedTieneReservasEnCurso(@PathVariable("huespedId") Long huespedId);

}
