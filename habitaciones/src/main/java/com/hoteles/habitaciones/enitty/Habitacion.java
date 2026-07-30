package com.hoteles.habitaciones.enitty;

import com.hoteles.commons.enums.EstadoHabitacion;
import com.hoteles.commons.enums.EstadoRegistro;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Entity
@Table(name = "HABITACIONES")
@AllArgsConstructor
@NoArgsConstructor
@Builder @Getter
public class Habitacion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_HABITACION")
    private Long id;

    @Column(name = "NUM_HABITACION")
    private Integer numeroHabitacion;

    @Column(name = "TIPO_HABITACION")
    private String tipoHabitacion;

    @Column(name = "PRECIO")
    private BigDecimal precio;

    @Column(name = "CAPACIDAD")
    private Integer capacidad;

    @Column(name = "ESTADO_HABITACION")
    private EstadoHabitacion estadoHabitacion;

    @Enumerated(EnumType.STRING)
    @Column(name = "ESTADO_REGISTRO", nullable = false)
    private EstadoRegistro estadoRegistro;
}
