package com.mv.cidaweb.model.dtos;

import java.time.LocalDateTime;

public record ComentarioDTO(String id, String conteudo, LocalDateTime dataHoraCriacao, PessoaDTO autor, long curtidas, boolean curtidoPorUsuario) {

}
