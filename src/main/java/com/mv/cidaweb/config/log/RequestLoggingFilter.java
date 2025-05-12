package com.mv.cidaweb.config.log;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import java.io.IOException;

@Component
public class RequestLoggingFilter extends OncePerRequestFilter {

    private static final Logger logger = LoggerFactory.getLogger(RequestLoggingFilter.class);

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        // Log da requisição recebida
        logger.info("Requisição recebida: {} {} - Parâmetros: {} - Headers: {}",
                request.getMethod(),
                request.getRequestURI(),
                request.getParameterMap(),
                getHeadersAsString(request));

        try {
            filterChain.doFilter(request, response);

            // Log da resposta bem-sucedida
            logger.info("Resposta enviada: Status {} - Headers: {}",
                    response.getStatus(),
                    getResponseHeadersAsString(response));

        } catch (Exception e) {
            // Log do erro
            logger.error("Erro ao processar requisição: {} {} - Erro: {}",
                    request.getMethod(),
                    request.getRequestURI(),
                    e.getMessage(), e);

            throw e;
        }
    }

    private String getHeadersAsString(HttpServletRequest request) {
        // Implementação para extrair headers
        StringBuilder headers = new StringBuilder();
        request.getHeaderNames().asIterator()
                .forEachRemaining(headerName ->
                        headers.append(headerName).append(": ")
                                .append(request.getHeader(headerName)).append("; "));
        return headers.toString();
    }

    private String getResponseHeadersAsString(HttpServletResponse response) {
        // Implementação similar para headers de resposta
        return response.getHeaderNames().toString();
    }
}