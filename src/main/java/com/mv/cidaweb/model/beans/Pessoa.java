package com.mv.cidaweb.model.beans;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

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
    private String idFoto;

    @Column(unique = true)
    private String email;

    @OneToMany(mappedBy = "autor", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Script> scripts = new ArrayList<>();

    @OneToMany(mappedBy = "autor", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Comentario> comentarios = new ArrayList<>();

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "curtidas_scripts", joinColumns = @JoinColumn(name = "pessoa_id"), inverseJoinColumns = @JoinColumn(name = "script_id"))
    private List<Script> scriptsCurtidos = new ArrayList<>();

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "curtidas_comentarios", joinColumns = @JoinColumn(name = "pessoa_id"), inverseJoinColumns = @JoinColumn(name = "comentario_id"))
    private List<Comentario> comentariosCurtidos = new ArrayList<>();

    public Pessoa() {
    }

    public Pessoa(String nome, String login, String password, UserRole role, String email,String idFoto) {
        this.nome = nome;
        this.login = login;
        this.password = password;
        this.email = email;
        this.role = role;
        this.idFoto = idFoto;
    }

    public String getIdFoto() {
        return idFoto.replace(".png", "");
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

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;
        Pessoa pessoa = (Pessoa) object;
        return Objects.equals(id, pessoa.id) && Objects.equals(login, pessoa.login);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, login);
    }
}

