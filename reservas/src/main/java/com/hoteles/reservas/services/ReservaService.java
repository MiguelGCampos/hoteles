package com.hoteles.reservas.services;

import com.hoteles.reservas.dto.ReservaRequest;
import com.hoteles.reservas.dto.ReservaResponse;

import java.util.List;

public interface ReservaService {

    List<ReservaResponse> listar();

    ReservaResponse obtenerPorId(Long id);

    ReservaResponse registrar(ReservaRequest request);

    ReservaResponse actualizar(Long id, ReservaRequest request);

    void actualizarEstado(Long idReserva, Long idEstado);

    void eliminar(Long id);

    boolean huespedTieneReservasEnCurso(Long huespedId);
}
