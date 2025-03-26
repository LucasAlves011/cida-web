package com.mv.cidaweb.model.dtos;

import java.time.LocalDateTime;

public record ScriptDTO(long id, PessoaDTO autor, LocalDateTime dataHoraCriacao, String titulo, String conteudo, String descricao, long curtidas) {
}
