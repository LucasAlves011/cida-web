package com.mv.cidaweb.model.exceptions;

import java.io.Serial;

public class CredenciaisInvalidasException extends RuntimeException{
    @Serial
    private static final long serialVersionUID = 1L;

    public CredenciaisInvalidasException(String message) {
        super(message);
    }

    public CredenciaisInvalidasException(String message, Throwable cause) {
        super(message, cause);
    }
}
