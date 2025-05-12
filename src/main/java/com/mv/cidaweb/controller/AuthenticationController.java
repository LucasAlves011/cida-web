package com.mv.cidaweb.controller;

import com.mv.cidaweb.model.dtos.TokenDTO;
import com.mv.cidaweb.service.AuthenticationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("")
public class AuthenticationController {

    @Autowired
    private AuthenticationService authenticationService;

    @GetMapping("/authenticate")
    public TokenDTO authenticate(Authentication authentication) {
        return authenticationService.authenticate(authentication);
    }
}
