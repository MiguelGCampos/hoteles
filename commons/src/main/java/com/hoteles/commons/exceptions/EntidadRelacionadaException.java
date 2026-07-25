package com.hoteles.commons.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class EntidadRelacionadaException extends RuntimeException {
    public EntidadRelacionadaException(String message) {
        super(message);
    }
}
