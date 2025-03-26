package com.mv.cidaweb.model.dtos;

import java.time.LocalDateTime;
import java.util.ArrayList;

public record ScriptComComentariosDTO(long id, PessoaDTO autor, LocalDateTime dataHoraCriacao, String titulo,
                                      String conteudo, String descricao, long curtidas, ArrayList<ComentarioDTO> comentarios) {
}
