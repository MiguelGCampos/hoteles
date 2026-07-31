package com.hoteles.commons.dto.huespedes;

import com.hoteles.commons.enums.EstadoRegistro;
import com.hoteles.commons.enums.TipoDocumento;

public record HuespedResponse(
        Long id,
        String nombre,
        String apellidoPaterno,
        String apellidoMaterno,
        String email,
        String telefono,
        TipoDocumento tipoDocumento,
        String nacionalidad,
        EstadoRegistro estadoRegistro
) {
}
