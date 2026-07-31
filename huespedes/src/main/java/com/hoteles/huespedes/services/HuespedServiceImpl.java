package com.hoteles.huespedes.services;

import com.hoteles.commons.clients.ReservaClient;
import com.hoteles.commons.dto.huespedes.HuespedRequest;
import com.hoteles.commons.dto.huespedes.HuespedResponse;
import com.hoteles.commons.enums.EstadoRegistro;
import com.hoteles.commons.enums.TipoDocumento;
import com.hoteles.commons.exceptions.ArgumentoNoValidoException;
import com.hoteles.commons.exceptions.EntidadRelacionadaException;
import com.hoteles.commons.exceptions.RecursoNoEncontradoException;
import com.hoteles.huespedes.entities.Huesped;
import com.hoteles.huespedes.mappers.HuespedMapper;
import com.hoteles.huespedes.repositories.HuespedRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@AllArgsConstructor
@Transactional
@Slf4j
public class HuespedServiceImpl implements HuespedService {

    private final HuespedRepository huespedRepository;
    private final HuespedMapper huespedMapper;
    private final ReservaClient reservaClient;

    @Override
    @Transactional(readOnly = true)
    public HuespedResponse obtenerHuespedActivoPorId(Long id) {
        log.info("Buscando huésped activo con id {}", id);
        return huespedMapper.entidadAResponse(obtenerHuespedActivoOException(id));
    }

    @Override
    @Transactional(readOnly = true)
    public HuespedResponse obtenerHuespedSinEstadoPorId(Long id) {
        log.info("Buscando huésped sin filtro de estado con id {}", id);
        return huespedMapper.entidadAResponse(huespedRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Huésped no encontrado con id " + id)));
    }

    @Override
    @Transactional(readOnly = true)
    public List<HuespedResponse> listar() {
        log.info("Listando huéspedes activos...");
        return huespedRepository.findByEstadoRegistro(EstadoRegistro.ACTIVO).stream()
                .map(huespedMapper::entidadAResponse).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public HuespedResponse obtenerPorId(Long id) {
        log.info("Buscando huésped activo con id {}", id);
        return huespedMapper.entidadAResponse(obtenerHuespedActivoOException(id));
    }

    @Override
    public HuespedResponse registrar(HuespedRequest request) {
        log.info("Registrando nuevo huésped: {}", request.nombre());

        TipoDocumento tipoDocumento = TipoDocumento.obtenerTipoDocumentoPorCodigo(request.idTipoDocumento());
        validarDatosUnicos(request);

        Huesped huesped = Huesped.crear(
                request.nombre(),
                request.apellidoPaterno(),
                request.apellidoMaterno(),
                request.email(),
                request.telefono(),
                tipoDocumento,
                request.nacionalidad()
        );

        huespedRepository.save(huesped);
        log.info("Huésped registrado con éxito: {}", huesped.getNombre());
        return huespedMapper.entidadAResponse(huesped);
    }

    @Override
    public HuespedResponse actualizar(HuespedRequest request, Long id) {
        Huesped huesped = obtenerHuespedActivoOException(id);
        log.info("Actualizando huésped con id: {}", id);

        TipoDocumento tipoDocumento = TipoDocumento.obtenerTipoDocumentoPorCodigo(request.idTipoDocumento());
        validarCambiosUnicos(request, id);

        huesped.actualizar(
                request.nombre(),
                request.apellidoPaterno(),
                request.apellidoMaterno(),
                request.email(),
                request.telefono(),
                tipoDocumento,
                request.nacionalidad());

        log.info("Huésped actualizado con éxito: {}", huesped.getNombre());
        return huespedMapper.entidadAResponse(huesped);
    }

    @Override
    public void eliminar(Long id) {
        Huesped huesped = obtenerHuespedActivoOException(id);
        log.info("Eliminando huésped con id {}", id);
        validarSinReservasBloqueantes(id);
        huesped.eliminar();
        log.info("Huésped con id {} ha sido eliminado", id);
    }

    private void validarSinReservasBloqueantes(Long idHuesped) {
        log.info("Validando que el huésped {} no tenga reservas EN_CURSO...", idHuesped);
        if (Boolean.TRUE.equals(reservaClient.tieneHuespedReservasBloqueantes(idHuesped))) {
            throw new EntidadRelacionadaException("No se puede eliminar al huésped con id " + idHuesped + " porque tiene una reserva en estado EN_CURSO");
        }
    }

    private Huesped obtenerHuespedActivoOException(Long id) {
        return huespedRepository.findByIdAndEstadoRegistro(id, EstadoRegistro.ACTIVO)
                .orElseThrow(() -> new RecursoNoEncontradoException("Huésped activo no encontrado con id " + id));
    }

    private void validarDatosUnicos(HuespedRequest request) {
        log.info("Validando datos únicos...");
        if (huespedRepository.existsByEmailIgnoreCaseAndEstadoRegistro(request.email().trim(), EstadoRegistro.ACTIVO)) {
            throw new ArgumentoNoValidoException("Ya existe un huésped activo registrado con el email: " + request.email());
        }
        if (huespedRepository.existsByTelefonoAndEstadoRegistro(request.telefono().trim(), EstadoRegistro.ACTIVO)) {
            throw new ArgumentoNoValidoException("Ya existe un huésped activo registrado con el teléfono: " + request.telefono());
        }
    }

    private void validarCambiosUnicos(HuespedRequest request, Long id) {
        log.info("Validando cambios únicos...");
        if (huespedRepository.existsByEmailIgnoreCaseAndEstadoRegistroAndIdNot(request.email().trim(), EstadoRegistro.ACTIVO, id)) {
            throw new ArgumentoNoValidoException("Ya existe un huésped activo registrado con el email: " + request.email());
        }
        if (huespedRepository.existsByTelefonoAndEstadoRegistroAndIdNot(request.telefono().trim(), EstadoRegistro.ACTIVO, id)) {
            throw new ArgumentoNoValidoException("Ya existe un huésped activo registrado con el teléfono: " + request.telefono());
        }
    }
}
