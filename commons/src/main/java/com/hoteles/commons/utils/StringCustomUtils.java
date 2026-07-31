package com.hoteles.commons.utils;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class StringCustomUtils {

    public static void validarTamanio(String valor, int min, int max, String mensaje) {
        if (valor == null || valor.trim().length() < min || valor.trim().length() > max) {
            throw new IllegalArgumentException(mensaje);
        }
    }
}
