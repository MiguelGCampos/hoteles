package com.hoteles.habitaciones.repository;

import com.hoteles.commons.enums.EstadoRegistro;
import com.hoteles.habitaciones.enitty.Habitacion;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface HabitacionRepository extends JpaRepository<Habitacion, Long> {

    List<Habitacion> findByEstadoRegistro(EstadoRegistro estadoRegistro);
    Optional<Habitacion> findByIdAndEstadoRegistro(Long id, EstadoRegistro estadoRegistro);

    boolean existsByNumeroAndEstadoRegistro(Integer numero, EstadoRegistro estadoRegistro);

    boolean existsByNumeroAndEstadoRegistroAndIdNot(Integer numero, EstadoRegistro estadoRegistro, Long id);
}
