package com.mv.cidaweb.model.beans;

import jakarta.persistence.*;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
public class Pessoa {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    private String nome;

    @Column(unique = true)
    private String login;

    private String password;
    private UserRole role;

    @OneToMany(mappedBy = "autor", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Script> scripts = new ArrayList<>();

    @OneToMany(mappedBy = "autor", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Comentario> comentarios = new ArrayList<>();

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "curtidas_scripts", joinColumns = @JoinColumn(name = "pessoa_id"), inverseJoinColumns = @JoinColumn(name = "script_id") )
    private List<Script> scriptsCurtidos = new ArrayList<>();

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "curtidas_comentarios", joinColumns = @JoinColumn(name = "pessoa_id"), inverseJoinColumns = @JoinColumn(name = "comentario_id"))
    private List<Comentario> comentariosCurtidos = new ArrayList<>();

    @Pattern(regexp = "^#([A-Fa-f0-9]{6}|[A-Fa-f0-9]{3})$")
    @Column(columnDefinition = "varchar(7) default '#2197D9'")
    private String corAvatar;

    public Pessoa() {
    }

    public Pessoa(String nome, String login, String password, UserRole role) {
        this.nome = nome;
        this.login = login;
        this.password = password;
        this.role = role;
    }

    // ADICIONAR E REMOVER CURTIDA DE SCRIPT
    public void removeScriptCurtido(Script script) {
        scriptsCurtidos.remove(script);
    }

    public void addScriptCurtido(Script script) {
        scriptsCurtidos.add(script);
    }

    // ADICIONAR E REMOVER CURTIDA DE COMENTARIO
    public void removeComentarioCurtido(Comentario comentario) {
        comentariosCurtidos.remove(comentario);
    }

    public void addComentarioCurtido(Comentario comentario) {
        comentariosCurtidos.add(comentario);
    }
}

