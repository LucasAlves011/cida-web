package com.mv.cidaweb.model.beans;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
public class Script {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String titulo;

    private String conteudo;
    private String descricao;
    private LocalDateTime dataCriacao;
    private LocalDateTime dataAtualizacao;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "autor_id")
    private Pessoa autor;

    @Column(columnDefinition = "bigint default 0")
    private long curtidas;

    @OneToMany(mappedBy = "script", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Comentario> comentarios = new ArrayList<>();

    public Script(String titulo, String conteudo, String descricao, LocalDateTime dataCriacao, Pessoa autor) {
        this.titulo = titulo;
        this.conteudo = conteudo;
        this.descricao = descricao;
        this.dataCriacao = dataCriacao;
        this.autor = autor;
    }

    public Script() {
    }

    public void addComentario(Comentario comentario) {
        this.comentarios.add(comentario);
        comentario.setScript(this);
    }
}
