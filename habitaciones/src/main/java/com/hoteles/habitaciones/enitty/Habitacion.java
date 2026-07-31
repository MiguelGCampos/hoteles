package com.hoteles.habitaciones.enitty;

import com.hoteles.commons.enums.EstadoHabitacion;
import com.hoteles.commons.enums.EstadoRegistro;
import com.hoteles.commons.enums.TipoHabitacion;
import com.hoteles.commons.utils.ValoresNumericosUtils;
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

    @Column(name = "NUM_HABITACION")
    private Integer numeroHabitacion;

    @Enumerated(EnumType.STRING) // guarda el nombre del enum en la BD
    @Column(name = "TIPO_HABITACION", nullable = false)
    private TipoHabitacion tipoHabitacion;

    @Column(name = "PRECIO")
    private BigDecimal precio;

    @Column(name = "CAPACIDAD")
    private Integer capacidad;

    @Enumerated(EnumType.STRING)
    @Column(name = "ESTADO_HABITACION")
    private EstadoHabitacion estadoHabitacion;

    @Enumerated(EnumType.STRING)
    @Column(name = "ESTADO_REGISTRO", nullable = false)
    private EstadoRegistro estadoRegistro;

    public void actualizar(
            Integer numeroHabitacion, TipoHabitacion tipoHabitacion, BigDecimal precio, Integer capacidad,
            EstadoHabitacion estadoHabitacion) {

        validarNoEliminado();

        validarDatos(
                numeroHabitacion, tipoHabitacion, precio, capacidad, estadoHabitacion);

        this.numeroHabitacion = numeroHabitacion;
        this.tipoHabitacion = tipoHabitacion;
        this.precio = precio;
        this.capacidad = capacidad;
        this.estadoHabitacion = estadoHabitacion;
    }

    private void validarNoEliminado() {
        if (this.estadoRegistro == EstadoRegistro.ELIMINADO)
            throw new IllegalStateException("La habitación ya está eliminada");
    }

    public void eliminar() {
        validarNoEliminado();
        this.estadoRegistro = EstadoRegistro.ELIMINADO;
    }

    // 🔹 Ahora valida TipoHabitacion en lugar de String
    private void validarDatos(
            Integer numeroHabitacion, TipoHabitacion tipoHabitacion, BigDecimal precio, Integer capacidad,
            EstadoHabitacion estadoHabitacion) {

        ValoresNumericosUtils.validarEnteroPositivo(numeroHabitacion,
                "El número de la habitación es requerido y debe ser positivo");

        if (tipoHabitacion == null)
            throw new IllegalArgumentException("El tipo de la habitación es requerido");

        ValoresNumericosUtils.validarBigDecimalPositivo(precio,
                "El precio es requerido y debe ser positivo");

        ValoresNumericosUtils.validarRangoIntegerMinimo(capacidad, 1,
                "La capacidad es requerida y debe valer mínimo 1");

        if (estadoHabitacion == null)
            throw new IllegalArgumentException("El estado de la habitación es requerido");
    }
}
