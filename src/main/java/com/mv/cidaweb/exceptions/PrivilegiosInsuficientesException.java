package com.mv.cidaweb.exceptions;

import java.io.Serial;

public class PrivilegiosInsuficientesException extends RuntimeException {

    @Serial
    private static final long serialVersionUID = 1L;

    public PrivilegiosInsuficientesException(String message) {
        super(message);
    }

}
