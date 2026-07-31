package com.hoteles.reservas.repositories;

import com.hoteles.commons.enums.EstadoRegistro;
import com.hoteles.commons.enums.EstadoReserva;
import com.hoteles.reservas.entities.Reserva;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ReservaRepository extends JpaRepository<Reserva, Long> {

    List<Reserva> findByEstadoRegistro(EstadoRegistro estado);

    Optional<Reserva> findByIdAndEstadoRegistro(Long id, EstadoRegistro estado);

    /**
     * Verifica si una habitación tiene reservas activas en una lista de estados.
     * Crucial para saber si una habitación está "ocupada" en un sentido amplio.
     */
    boolean existsByIdHabitacionAndEstadoRegistroAndEstadoReservaIn(Long idHabitacion, EstadoRegistro estadoRegistro, List<EstadoReserva> estados);

    /**
     * Verifica si un huésped tiene reservas activas en una lista de estados.
     * Usado para el endpoint que consultará el microservicio de huéspedes.
     */
    boolean existsByIdHuespedAndEstadoRegistroAndEstadoReservaIn(Long idHuesped, EstadoRegistro estadoRegistro, List<EstadoReserva> estados);
}
