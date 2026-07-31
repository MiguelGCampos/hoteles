package com.hoteles.authorization.controllers;

import com.hoteles.authorization.dto.UsuarioRequest;
import com.hoteles.authorization.dto.UsuarioResponse;
import com.hoteles.authorization.services.UsuarioService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RestController
@RequestMapping("/admin/usuarios")
@AllArgsConstructor
public class UsuarioController {

    private final UsuarioService usuarioService;

    @GetMapping
    public ResponseEntity<Set<UsuarioResponse>> listar() {
        return ResponseEntity.ok(usuarioService.listar());
    }

    @PostMapping
    public ResponseEntity<UsuarioResponse> registrar(@Valid @RequestBody UsuarioRequest request) {
        return ResponseEntity.ok(usuarioService.registrar(request));
    }

    @DeleteMapping("/{username}")
    public ResponseEntity<UsuarioResponse> eliminar(@PathVariable String username) {
        return ResponseEntity.ok(usuarioService.eliminar(username));
    }
}