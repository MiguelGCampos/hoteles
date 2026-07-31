package com.hoteles.commons.clients;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "reservas", path = "/api/reservas")
public interface ReservaClient {

    @GetMapping("/huesped/{idHuesped}/tiene-reservas-bloqueantes")
    Boolean tieneHuespedReservasBloqueantes(@PathVariable("idHuesped") Long idHuesped);
}
