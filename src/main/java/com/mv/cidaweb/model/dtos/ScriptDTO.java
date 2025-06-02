package com.mv.cidaweb.model.dtos;

import java.time.LocalDateTime;

public record ScriptDTO(long idScript, PessoaDTO autor, LocalDateTime dataHoraCriacao, String titulo, String conteudo, String descricao, long curtidas, boolean curtidoPorUsuario) {
}
