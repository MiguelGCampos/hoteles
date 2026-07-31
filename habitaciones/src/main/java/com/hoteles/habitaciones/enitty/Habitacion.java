package com.hoteles.habitaciones.enitty;

import com.hoteles.commons.enums.EstadoHabitacion;
import com.hoteles.commons.enums.EstadoRegistro;
import com.hoteles.commons.enums.TipoHabitacion;
import com.hoteles.commons.utils.StringCustomUtils;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
@Entity
@Table(name = "HABITACIONES")
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
@ToString
public class Habitacion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_HABITACION")
    private Long id;

    @Column(name = "NUM_HABITACION", nullable = false)
    private Integer numeroHabitacion;

    @Enumerated(EnumType.STRING)
    @Column(name = "TIPO_HABITACION", nullable = false, length = 30)
    private TipoHabitacion tipoHabitacion;

    @Column(name = "PRECIO", nullable = false, precision = 10, scale = 2)
    private BigDecimal precio;

    @Column(name = "CAPACIDAD", nullable = false)
    private Integer capacidad;

    @Enumerated(EnumType.STRING)
    @Column(name = "ESTADO_HABITACION", nullable = false, length = 20)
    private EstadoHabitacion estadoHabitacion;

    @Enumerated(EnumType.STRING)
    @Column(name = "ESTADO_REGISTRO", nullable = false, length = 30)
    private EstadoRegistro estadoRegistro;

    // Método de creación
    public static Habitacion crear(Integer numero, TipoHabitacion tipo, BigDecimal precio, Integer capacidad) {
        validarDatos(numero, tipo, precio, capacidad);

        return Habitacion.builder()
                .numeroHabitacion(numero)
                .tipoHabitacion(tipo)
                .precio(precio)
                .capacidad(capacidad)
                .estadoHabitacion(EstadoHabitacion.DISPONIBLE)
                .estadoRegistro(EstadoRegistro.ACTIVO)
                .build();
    }

    // Método de actualización
    public void actualizar(Integer numero, TipoHabitacion tipo, BigDecimal precio, Integer capacidad) {
        validarNoEliminada();
        validarDatos(numero, tipo, precio, capacidad);

        this.numeroHabitacion = numero;
        this.tipoHabitacion = tipo;
        this.precio = precio;
        this.capacidad = capacidad;
    }

    public void cambiarEstado(EstadoHabitacion nuevoEstado) {
        validarNoEliminada();
        if (nuevoEstado == null) {
            throw new IllegalArgumentException("El nuevo estado de la habitación es requerido");
        }
        if (this.estadoHabitacion == EstadoHabitacion.OCUPADA && nuevoEstado == EstadoHabitacion.DISPONIBLE) {
            throw new IllegalStateException("No se puede cambiar manualmente a DISPONIBLE una habitación OCUPADA");
        }
        this.estadoHabitacion = nuevoEstado;
    }

    public void sincronizarEstado(EstadoHabitacion nuevoEstado) {
        validarNoEliminada();
        if (nuevoEstado == null) {
            throw new IllegalArgumentException("El nuevo estado de la habitación es requerido");
        }
        this.estadoHabitacion = nuevoEstado;
    }

    public void eliminar() {
        validarNoEliminada();
        if (this.estadoHabitacion == EstadoHabitacion.OCUPADA) {
            throw new IllegalStateException("No se puede eliminar una habitación OCUPADA");
        }
        this.estadoRegistro = EstadoRegistro.ELIMINADO;
    }

    private void validarNoEliminada() {
        if (this.estadoRegistro == EstadoRegistro.ELIMINADO) {
            throw new IllegalStateException("La habitación ya está eliminada");
        }
    }

    private static void validarDatos(Integer numero, TipoHabitacion tipo, BigDecimal precio, Integer capacidad) {
        if (numero == null || numero <= 0) {
            throw new IllegalArgumentException("El número de habitación es requerido y debe ser mayor a 0");
        }
        if (tipo == null) {
            throw new IllegalArgumentException("El tipo de habitación es requerido");
        }
        if (precio == null || precio.signum() <= 0) {
            throw new IllegalArgumentException("El precio es requerido y debe ser mayor a 0");
        }
        if (capacidad == null || capacidad < 1) {
            throw new IllegalArgumentException("La capacidad es requerida y debe ser mínimo 1");
        }
    }
}
