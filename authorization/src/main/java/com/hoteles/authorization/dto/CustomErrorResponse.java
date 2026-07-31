package com.hoteles.authorization.dto;

public record CustomErrorResponse(
        int codigo,
        String mensaje
) {

}
