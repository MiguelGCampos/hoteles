package com.hoteles.commons.exceptions;

import com.hoteles.commons.dto.CustomErrorResponse;
import feign.FeignException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.stream.Collectors;


@RestControllerAdvice
@Slf4j
public class GlobalHandlerException {

    // --- 400: Validación incorrecta ---
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<CustomErrorResponse> handleValidationExceptions(MethodArgumentNotValidException ex) {
        String message = ex.getBindingResult().getFieldErrors().stream()
                .map(error -> "'" + error.getField() + "': " + error.getDefaultMessage())
                .collect(Collectors.joining(", "));
        log.warn("Error de validación (400): {}", message);
        return buildErrorResponse(HttpStatus.BAD_REQUEST, "Datos de entrada inválidos: " + message);
    }

    // --- 404: Recurso no encontrado ---
    @ExceptionHandler(RecursoNoEncontradoException.class)
    public ResponseEntity<CustomErrorResponse> handleResourceNotFound(RecursoNoEncontradoException ex) {
        log.warn("Recurso no encontrado (404): {}", ex.getMessage());
        return buildErrorResponse(HttpStatus.NOT_FOUND, ex.getMessage());
    }

    // --- 409: Violación de regla de negocio ---
    @ExceptionHandler(ReglaNegocioException.class)
    public ResponseEntity<CustomErrorResponse> handleBusinessRuleViolation(ReglaNegocioException ex) {
        log.warn("Violación de regla de negocio (409): {}", ex.getMessage());
        return buildErrorResponse(HttpStatus.CONFLICT, ex.getMessage());
    }

    // --- Errores de comunicación entre servicios (Feign) ---
    @ExceptionHandler(FeignException.class)
    public ResponseEntity<CustomErrorResponse> handleFeignException(FeignException ex) {
        HttpStatus status = HttpStatus.resolve(ex.status());
        if (status == null) {
            status = HttpStatus.INTERNAL_SERVER_ERROR;
        }
        log.error("Error de Feign ({}): {}", status.value(), ex.getMessage());

        String message = switch (status) {
            case NOT_FOUND -> "El recurso solicitado no fue encontrado en el servicio remoto.";
            case CONFLICT -> "Conflicto de negocio detectado en el servicio remoto.";
            case BAD_REQUEST -> "Solicitud incorrecta enviada al servicio remoto.";
            default -> "Error en la comunicación con un servicio interno.";
        };
        return buildErrorResponse(status, message);
    }

    // --- 500: Error interno del servidor ---
    @ExceptionHandler(Exception.class)
    public ResponseEntity<CustomErrorResponse> handleGeneralException(Exception ex) {
        log.error("Error interno del servidor (500): {}", ex.getMessage(), ex);
        return buildErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Ocurrió un error inesperado en el servidor.");
    }

    // --- Método de ayuda para construir la respuesta ---
    private ResponseEntity<CustomErrorResponse> buildErrorResponse(HttpStatus status, String message) {
        CustomErrorResponse errorResponse = new CustomErrorResponse(status.value(), message);
        return new ResponseEntity<>(errorResponse, status);
    }
}
