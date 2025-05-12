package com.mv.cidaweb.config.security;

import com.mv.cidaweb.model.dtos.TokenDTO;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.stream.Collectors;

@Service
public class JwtService {
    private static final long TEMPO_DE_VIDA_TOKEN_SEGUNDOS = 36000L;
    private final JwtEncoder encoder;

    public JwtService(JwtEncoder encoder) {
        this.encoder = encoder;
    }

    public TokenDTO generateToken(Authentication authentication) {
        Instant now = Instant.now();
        String scope = authentication
                .getAuthorities().stream().map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(" "));

        JwtClaimsSet claims = JwtClaimsSet.builder()
                .issuer("spring-security-jwt")
                .issuedAt(now)
                .expiresAt(now.plusSeconds(TEMPO_DE_VIDA_TOKEN_SEGUNDOS))
                .subject(authentication.getName())
                .claim("scope", scope)
                .build();

        var token =  encoder.encode(JwtEncoderParameters.from(claims)).getTokenValue();
        return new TokenDTO(token, "jwt", TEMPO_DE_VIDA_TOKEN_SEGUNDOS);
    }

}