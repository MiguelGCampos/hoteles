package com.hoteles.commons.dto.huespedes;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record HuespedRequest(
        @NotBlank(message = "El nombre no puede estar vacío")
        @Size(min = 2, max = 50, message = "El nombre debe tener entre 2 y 50 caracteres")
        String nombre,

        @NotBlank(message = "El apellido paterno no puede estar vacío")
        @Size(min = 2, max = 50, message = "El apellido paterno debe tener entre 2 y 50 caracteres")
        String apellidoPaterno,

        @NotBlank(message = "El apellido materno no puede estar vacío")
        @Size(min = 2, max = 50, message = "El apellido materno debe tener entre 2 y 50 caracteres")
        String apellidoMaterno,

        @NotBlank(message = "El email no puede estar vacío")
        @Email(message = "El formato del email no es válido")
        @Size(max = 100, message = "El email no puede exceder los 100 caracteres")
        String email,

        @NotBlank(message = "El teléfono no puede estar vacío")
        @Size(min = 10, max = 10, message = "El teléfono debe tener 10 dígitos")
        String telefono,

        @NotNull(message = "El id del tipo de documento no puede ser nulo")
        Long idTipoDocumento,

        @NotBlank(message = "La nacionalidad no puede estar vacía")
        @Size(max = 50, message = "La nacionalidad no puede exceder los 50 caracteres")
        String nacionalidad
) {
}
