package com.hoteles.authorization.services;

import com.hoteles.authorization.dto.LoginRequest;
import com.hoteles.authorization.dto.TokenResponse;

public interface AuthService {

    TokenResponse autenticar(LoginRequest request) throws Exception;
}