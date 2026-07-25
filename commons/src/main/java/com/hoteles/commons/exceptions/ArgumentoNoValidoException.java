package com.hoteles.commons.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class ArgumentoNoValidoException extends RuntimeException {
    public ArgumentoNoValidoException(String message) {
        super(message);
    }
}
