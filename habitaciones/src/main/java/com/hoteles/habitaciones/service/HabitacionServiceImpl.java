package com.hoteles.habitaciones.service;

import com.hoteles.commons.dto.habitaciones.HabitacionRequest;
import com.hoteles.commons.dto.habitaciones.HabitacionResponse;
import com.hoteles.commons.exceptions.RecursoNoEncontradoException;
import com.hoteles.habitaciones.mapper.HabitacionMapper;
import com.hoteles.habitaciones.repository.HabitacionRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Slf4j
@Transactional
@AllArgsConstructor
public class HabitacionServiceImpl implements HabitacionService{
    private final HabitacionRepository habitacionRepository;
    private final HabitacionMapper habitacionMapper;

    @Override
    public HabitacionResponse obtenerHabitacionPorIdSinEstado(Long id) {
        log.info("Buscando médico sin estado con id {}", id);

        return habitacionMapper.entidadAResponse(habitacionRepository.findById(id)
                .orElseThrow(()->new RecursoNoEncontradoException(
                        "Medico sin estado no encontrado con id: {}"+id)));
    }

    @Override
    public List<HabitacionResponse> listar() {
        return List.of();
    }

    @Override
    public HabitacionResponse obtenerPorId(Long id) {
        return null;
    }

    @Override
    public HabitacionResponse registrar(HabitacionRequest request) {
        return null;
    }

    @Override
    public HabitacionResponse actualizar(HabitacionRequest request, Long id) {
        return null;
    }

    @Override
    public void eliminar(Long id) {

    }
}
