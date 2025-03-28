package com.mv.cidaweb.model.beans;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@Schema(name = "Comentario")
public class Comentario {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

//    @Column(columnDefinition = "CHARACTER LARGE OBJECT")
    private String comentario;
    private LocalDateTime dataHora;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "autor_id")
    private Pessoa autor;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "script_id")
    private Script script;

    @ManyToMany(mappedBy = "comentariosCurtidos",fetch = FetchType.EAGER)
    private List<Pessoa> pessoasQueCurtiram = new ArrayList<>();

    public void adicionarCurtida(Pessoa pessoa) {
        this.pessoasQueCurtiram.add(pessoa);
    }

    public void removerCurtida(Pessoa pessoa) {
        this.pessoasQueCurtiram.remove(pessoa);
    }

    public long getCurtidas() {
        return this.pessoasQueCurtiram.size();
    }

    public Comentario(String comentario, LocalDateTime dataHora, Pessoa autor) {
        this.comentario = comentario;
        this.dataHora = dataHora;
        this.autor = autor;
    }

    public Comentario() {
    }
}
