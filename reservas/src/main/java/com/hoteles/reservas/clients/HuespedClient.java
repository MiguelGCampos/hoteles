package com.hoteles.reservas.clients;

import com.hoteles.commons.dto.huespedes.HuespedResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

// El "name" debe coincidir con spring.application.name / eureka.instance.appname
// del microservicio de huéspedes ("huespedes").
@FeignClient(name = "huespedes")
public interface HuespedClient {

    /**
     * Obtiene un huésped por su ID, sin importar su estado de registro.
     */
    @GetMapping("/api/huespedes/id-huesped/{id}")
    HuespedResponse obtenerHuespedPorIdSinEstado(@PathVariable("id") Long id);
}
