package com.mv.cidaweb.model.dtos;

public record TokenDTO (String access_token, String type, long expires_in) {
}
