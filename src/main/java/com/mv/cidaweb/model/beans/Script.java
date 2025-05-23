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

    @Column(columnDefinition = "CHARACTER LARGE OBJECT", length = 40000)
    private String conteudo;
    private String descricao;
    private LocalDateTime dataCriacao;
    private LocalDateTime dataAtualizacao;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "autor_id")
    private Pessoa autor;

    @ManyToMany(mappedBy = "scriptsCurtidos")
    private List<Pessoa> pessoasQueCurtiram = new ArrayList<>();

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

    public long getCurtidas() {
        return this.pessoasQueCurtiram.size();
    }

    public void adicionarCurtida(Pessoa pessoa) {
        this.pessoasQueCurtiram.add(pessoa);
    }

    public void removerCurtida(Pessoa pessoa) {
        this.pessoasQueCurtiram.remove(pessoa);
    }

    public void addComentario(Comentario comentario) {
        this.comentarios.add(comentario);
        comentario.setScript(this);
    }
}
