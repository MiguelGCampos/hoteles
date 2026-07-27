package com.hoteles.commons.dto.huespedes;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record HuespedRequest(
        @NotBlank(message = "El nombre es requerido")
        @Size(min=1, max=50, message = "El nombre es requerido y debe tener entre 1 y 50 caracteres")
        String nombre,

        @NotBlank(message = "El apellido paterno es requerido")
        @Size(min=1, max=50, message = "El apellido paterno es requerido y debe tener entre 1 y 50 caracteres")
        String apellidoPaterno,

        @NotBlank(message = "El apellido materno es requerido")
        @Size(min=1, max=50, message = "El apellido materno es requerido y debe tener entre 1 y 50 caracteres")
        String apellidoMaterno,

        @NotBlank(message = "El email es requerido")
        @Size(min=1, max=100, message = "El email es requerido y debe tener entre 1 y 100 caracteres")
        @Email(message = "El email debe tener un formato valido (ejemplo@dominio.com)")
        String email,

        @NotBlank(message = "El teléfono es requerido")
        @Pattern(regexp = "^\\d{10}$", message = "El teléfono solo debe contener números")
        String telefono,

        @NotBlank(message = "El documento de identificación es requerido")
        @Size(min=1, max=50, message = "El documento de identificación debe tener entre 1 y 50 caracteres")
        String documento,

        @NotBlank(message = "La nacionalidad es requerida")
        @Size(min=1, max=50, message = "El documento de identificación debe tener entre 1 y 50 caracteres")
        String nacionalidad
) {
}
