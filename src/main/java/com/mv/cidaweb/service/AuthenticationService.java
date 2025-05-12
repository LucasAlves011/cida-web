package com.mv.cidaweb.service;

import com.mv.cidaweb.config.security.JwtService;
import com.mv.cidaweb.model.dtos.TokenDTO;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationService {

    private final JwtService jwtService;

    public AuthenticationService(JwtService jwtService) {
        this.jwtService = jwtService;
    }

    public TokenDTO authenticate(Authentication authentication) {
        return jwtService.generateToken(authentication);
    }
}
