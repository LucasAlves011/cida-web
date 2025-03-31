package com.mv.cidaweb.service;

import com.mv.cidaweb.model.beans.Comentario;
import com.mv.cidaweb.model.dtos.ComentarioDTO;
import com.mv.cidaweb.model.dtos.PessoaDTO;
import com.mv.cidaweb.model.exceptions.ObjectNotFoundException;
import com.mv.cidaweb.model.repository.ComentarioRepository;
import jakarta.transaction.Transactional;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.stream.Collectors;

@Service
public class ComentarioService {

    private final ComentarioRepository comentarioRepository;
    private final PessoaService pessoaService;
    private final ScriptService scriptService;

    public ComentarioService(ComentarioRepository comentarioRepository, PessoaService pessoaService, ScriptService scriptService) {
        this.comentarioRepository = comentarioRepository;
        this.pessoaService = pessoaService;
        this.scriptService = scriptService;
    }

    @Transactional
    public ComentarioDTO addComentario(String comentario, Long scriptId) {
        var script = scriptService.findById(scriptId).orElseThrow(() -> new ObjectNotFoundException(String.format("Script com id %d não encontrado", scriptId)));
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        var pessoa = pessoaService.findByLogin(authentication.getName()).orElseThrow(() -> new ObjectNotFoundException(String.format("Pessoa com nome %s não encontrada", authentication.getName())));
        var novoComentario = new Comentario(comentario, LocalDateTime.now(), pessoa);
        script.addComentario(novoComentario);
        scriptService.save(script);
        return new ComentarioDTO(pessoa.getId(), comentario, novoComentario.getDataHora(), new PessoaDTO(pessoa.getNome(), pessoa.getCorAvatar()), 0, false);
    }

    public ArrayList<ComentarioDTO> getComentarios(Long scriptId) {
        var script = scriptService.findById(scriptId).orElseThrow(() -> new ObjectNotFoundException(String.format("Script com id %d não encontrado", scriptId)));
        return script.getComentarios().stream().map(a ->
                new ComentarioDTO(a.getId().toString(), a.getComentario(), a.getDataHora(), new PessoaDTO(a.getAutor().getNome(), a.getAutor().getCorAvatar()), a.getCurtidas(), verificarSePessoaCurtiuOuNao(a))
        ).collect(Collectors.toCollection(ArrayList::new));
    }

    public ComentarioDTO getComentario(Long comentario_id) {
        var comentario = comentarioRepository.findById(comentario_id).orElseThrow(() -> new ObjectNotFoundException(String.format("Comentário com id %d não encontrado", comentario_id)));
        return new ComentarioDTO(comentario.getId().toString(), comentario.getComentario(), comentario.getDataHora(), new PessoaDTO(comentario.getAutor().getNome(), comentario.getAutor().getCorAvatar()), comentario.getCurtidas(), verificarSePessoaCurtiuOuNao(comentario));
    }

    public ComentarioDTO curtirDescurtirComentario(Long comentarioId) {
        var comentario = comentarioRepository.findById(comentarioId).orElseThrow(() -> new ObjectNotFoundException(String.format("Comentário com id %d não encontrado", comentarioId)));
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        var pessoa = pessoaService.findByLogin(authentication.getName()).orElseThrow(() -> new ObjectNotFoundException(String.format("Pessoa com nome %s não encontrada", authentication.getName())));

        if (comentario.getPessoasQueCurtiram().contains(pessoa)) {
            comentario.removerCurtida(pessoa);
            pessoa.removeComentarioCurtido(comentario);
        } else {
            pessoa.addComentarioCurtido(comentario);
            comentario.adicionarCurtida(pessoa);
        }

        pessoaService.save(pessoa);
        var a = comentarioRepository.save(comentario);
        return new ComentarioDTO(a.getId().toString(), a.getComentario(), a.getDataHora(), new PessoaDTO(a.getAutor().getNome(), a.getAutor().getCorAvatar()), a.getCurtidas(), verificarSePessoaCurtiuOuNao(a));
    }

    private boolean verificarSePessoaCurtiuOuNao(Comentario comentario) {
        var loginPessoa = SecurityContextHolder.getContext().getAuthentication().getName();
        var pessoa = pessoaService.findByLogin(loginPessoa).orElseThrow(() -> new ObjectNotFoundException(String.format("Pessoa com nome %s não encontrada", loginPessoa)));
        return comentario.getPessoasQueCurtiram().contains(pessoa);
    }
}
