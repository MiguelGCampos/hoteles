package com.hoteles.commons.enums;

import com.hoteles.commons.exceptions.RecursoNoEncontradoException;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum TipoDocumento {
    CREDENCIAL_PARA_VOTAR(1L, "Credencial para votar"),
    PASAPORTE(2L, "Pasaporte"),
    CARTILLA_MILITAR(3L, "Cartilla militar"),
    LICENCIA_DE_CONDUCIR(4L, "Licencia de conducir");

    private final Long codigo;
    private final String descripcion;

    public static TipoDocumento obtenerTipoDocumentoPorCodigo(Long codigo) {
        for (TipoDocumento d : values()) {
            if (d.codigo == codigo) {
                return d;
            }
        }
        throw new RecursoNoEncontradoException("Código de tipo de documento no válido: " + codigo);
    }
}
