package com.hoteles.authorization.services;

import com.hoteles.authorization.dto.UsuarioRequest;
import com.hoteles.authorization.dto.UsuarioResponse;

import java.util.Set;

public interface UsuarioService {

    Set<UsuarioResponse> listar();

    UsuarioResponse registrar(UsuarioRequest request);

    UsuarioResponse eliminar(String username);
}
