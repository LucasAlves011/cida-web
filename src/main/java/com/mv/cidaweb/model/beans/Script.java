package com.mv.cidaweb.model.beans;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

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

//    @Column(columnDefinition = "bigint default 0")
//    private long curtidas;

    @ManyToMany(fetch = FetchType.EAGER )
    @JoinTable(name = "curtidas_scripts", joinColumns = @JoinColumn(name = "script_id"), inverseJoinColumns = @JoinColumn(name = "pessoa_id"))
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
