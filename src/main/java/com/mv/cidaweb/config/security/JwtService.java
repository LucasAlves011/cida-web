package com.mv.cidaweb.config.security;

import com.mv.cidaweb.model.beans.Pessoa;
import com.mv.cidaweb.model.dtos.TokenDTO;
import com.mv.cidaweb.service.PessoaService;
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
    private final PessoaService pessoaService;

    public JwtService(JwtEncoder encoder, PessoaService pessoaService) {
        this.encoder = encoder;
        this.pessoaService = pessoaService;
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
        return new TokenDTO(token, "Bearer", TEMPO_DE_VIDA_TOKEN_SEGUNDOS);
    }

    public TokenDTO generatePermanentToken(Authentication authentication) {
        // Se já existir, retorna o token
        Pessoa pessoa = pessoaService.findByLogin(authentication.getName()).orElseThrow(
                () -> new RuntimeException("Usuário não encontrado")
        );

        if (pessoa.getTokenPermanent() != null) {
            return new TokenDTO(pessoa.getTokenPermanent(), "Bearer", -1);
        }

        Instant now = Instant.now();
        String scope = "/script/nome/*"; // Escopo permitido

        JwtClaimsSet claims = JwtClaimsSet.builder()
                .issuer("spring-security-jwt")
                .issuedAt(now)
                .subject(pessoa.getLogin())
                .claim("scope", scope)
                .build();

        String token = encoder.encode(JwtEncoderParameters.from(claims)).getTokenValue();

        // Salva o token na pessoa
        pessoa.setTokenPermanent(token);
        pessoaService.save(pessoa);

        return new TokenDTO(token, "Bearer", -1);
    }
}