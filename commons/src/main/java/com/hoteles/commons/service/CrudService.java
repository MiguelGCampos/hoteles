package com.hoteles.commons.service;

import java.util.List;

/**
 * Interfaz genérica para operaciones CRUD básicas que será implementada por los servicios de cada microservicio.
 * @param <RQ> DTO para las solicitudes de creación/actualización (Request).
 * @param <RS> DTO para las respuestas (Response).
 * @param <ID> El tipo del ID de la entidad (Ej. Long, Integer, String).
 */
public interface CrudService<RQ, RS, ID> {

    /**
     * Devuelve una lista de todos los recursos.
     * @return Lista de DTOs de respuesta.
     */
    List<RS> listar();

    /**
     * Busca un recurso por su ID.
     * @param id El ID del recurso.
     * @return El DTO de respuesta del recurso encontrado.
     */
    RS obtenerPorId(ID id);

    /**
     * Registra un nuevo recurso.
     * @param request El DTO con los datos para la creación.
     * @return El DTO de respuesta del recurso creado.
     */
    RS registrar(RQ request);

    /**
     * Actualiza un recurso existente.
     * @param request El DTO con los datos para la actualización.
     * @param id El ID del recurso a actualizar.
     * @return El DTO de respuesta del recurso actualizado.
     */
    RS actualizar(RQ request, ID id);

    /**
     * Elimina un recurso por su ID.
     * @param id El ID del recurso a eliminar.
     */
    void eliminar(ID id);
}
