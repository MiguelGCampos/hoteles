package com.hoteles.commons.controller;

import com.hoteles.commons.service.CrudService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


public abstract class CommonController<RQ, RS, S extends CrudService<RQ, RS, ID>, ID> {

    @Autowired
    protected S service;

    @GetMapping
    public ResponseEntity<List<RS>> listar() {
        return ResponseEntity.ok(service.listar());
    }

    @GetMapping("/{id}")
    public ResponseEntity<RS> obtenerPorId(@PathVariable ID id) {
        return ResponseEntity.ok(service.obtenerPorId(id));
    }

    @PostMapping
    public ResponseEntity<RS> registrar(@RequestBody RQ request) {
        RS response = service.registrar(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<RS> actualizar(@RequestBody RQ request, @PathVariable ID id) {
        return ResponseEntity.ok(service.actualizar(request, id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable ID id) {
        service.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
