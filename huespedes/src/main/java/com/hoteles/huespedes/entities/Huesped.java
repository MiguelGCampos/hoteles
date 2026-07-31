package com.hoteles.huespedes.entities;

import com.hoteles.commons.enums.EstadoRegistro;
import com.hoteles.commons.enums.TipoDocumento;
import com.hoteles.commons.utils.StringCustomUtils;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "HUESPEDES")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@Getter
public class Huesped {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_HUESPED")
    private Long id;

    @Column(name = "NOMBRE", nullable = false, length = 50)
    private String nombre;

    @Column(name = "APELLIDO_PATERNO", nullable = false, length = 50)
    private String apellidoPaterno;

    @Column(name = "APELLIDO_MATERNO", nullable = false, length = 50)
    private String apellidoMaterno;

    @Column(name = "EMAIL", nullable = false, length = 100)
    private String email;

    @Column(name = "TELEFONO", nullable = false, length = 10)
    private String telefono;

    @Enumerated(EnumType.STRING)
    @Column(name = "TIPO_DOCUMENTO", nullable = false, length = 30)
    private TipoDocumento tipoDocumento;

    @Column(name = "NACIONALIDAD", nullable = false, length = 50)
    private String nacionalidad;

    @Enumerated(EnumType.STRING)
    @Column(name = "ESTADO_REGISTRO", nullable = false, length = 20)
    private EstadoRegistro estadoRegistro;

    public static Huesped crear(String nombre, String apellidoPaterno, String apellidoMaterno,
                                 String email, String telefono, TipoDocumento tipoDocumento, String nacionalidad) {

        validarDatos(nombre, apellidoPaterno, apellidoMaterno, email, telefono, nacionalidad);

        return Huesped.builder()
                .nombre(nombre.trim())
                .apellidoPaterno(apellidoPaterno.trim())
                .apellidoMaterno(apellidoMaterno.trim())
                .email(email.toLowerCase().trim())
                .telefono(telefono.trim())
                .tipoDocumento(tipoDocumento)
                .nacionalidad(nacionalidad.trim())
                .estadoRegistro(EstadoRegistro.ACTIVO)
                .build();
    }

    public void actualizar(String nombre, String apellidoPaterno, String apellidoMaterno,
                            String email, String telefono, TipoDocumento tipoDocumento, String nacionalidad) {

        validarNoEliminado();
        validarDatos(nombre, apellidoPaterno, apellidoMaterno, email, telefono, nacionalidad);

        this.nombre = nombre.trim();
        this.apellidoPaterno = apellidoPaterno.trim();
        this.apellidoMaterno = apellidoMaterno.trim();
        this.email = email.toLowerCase().trim();
        this.telefono = telefono.trim();
        this.tipoDocumento = tipoDocumento;
        this.nacionalidad = nacionalidad.trim();
    }

    public void eliminar() {
        validarNoEliminado();
        this.estadoRegistro = EstadoRegistro.ELIMINADO;
    }

    private void validarNoEliminado() {
        if (this.estadoRegistro == EstadoRegistro.ELIMINADO) {
            throw new IllegalStateException("El huésped ya está eliminado");
        }
    }

    private static void validarDatos(String nombre, String apellidoPaterno, String apellidoMaterno,
                                      String email, String telefono, String nacionalidad) {

        StringCustomUtils.validarTamanio(nombre, 2, 50, "El nombre es requerido y debe tener entre 2 y 50 caracteres");
        StringCustomUtils.validarTamanio(apellidoPaterno, 2, 50, "El apellido paterno es requerido y debe tener entre 2 y 50 caracteres");
        StringCustomUtils.validarTamanio(apellidoMaterno, 2, 50, "El apellido materno es requerido y debe tener entre 2 y 50 caracteres");
        StringCustomUtils.validarTamanio(email, 1, 100, "El email es requerido y debe tener máximo 100 caracteres");
        if (telefono == null || !telefono.trim().matches("^[0-9]{10}$")) {
            throw new IllegalArgumentException("El teléfono es requerido y debe tener exactamente 10 dígitos");
        }
        StringCustomUtils.validarTamanio(nacionalidad, 1, 50, "La nacionalidad es requerida y debe tener máximo 50 caracteres");
    }
}
