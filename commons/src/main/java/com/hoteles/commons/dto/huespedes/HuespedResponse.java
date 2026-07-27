package com.hoteles.commons.dto.huespedes;

public record HuespedResponse(
        Long id,
        String nombre,
        String email,
        String telefono,
        String documento,
        String nacionalidad
) {
}
