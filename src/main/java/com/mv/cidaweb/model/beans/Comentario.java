package com.mv.cidaweb.model.beans;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Schema(name = "Comentario")
public class Comentario {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String comentario;
    private LocalDateTime dataHora;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "autor_id")
    private Pessoa autor;

    @Column(columnDefinition = "bigint default 0")
    private long curtidas;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "script_id")
    private Script script;

    public Comentario(String comentario, LocalDateTime dataHora, Pessoa autor, long curtidas) {
        this.comentario = comentario;
        this.dataHora = dataHora;
        this.autor = autor;
        this.curtidas = curtidas;
    }

    public Comentario() {
    }
}
