package com.mv.cidaweb.service;

import com.mv.cidaweb.exceptions.ObjectNotFoundException;
import com.mv.cidaweb.exceptions.PrivilegiosInsuficientesException;
import com.mv.cidaweb.model.beans.Comentario;
import com.mv.cidaweb.model.beans.Script;
import com.mv.cidaweb.model.dtos.ComentarioDTO;
import com.mv.cidaweb.model.dtos.PessoaDTO;
import com.mv.cidaweb.model.dtos.ScriptComComentariosDTO;
import com.mv.cidaweb.model.dtos.ScriptDTO;
import com.mv.cidaweb.model.repository.ComentarioRepository;
import com.mv.cidaweb.model.repository.PessoaRepository;
import com.mv.cidaweb.model.repository.ScriptRepository;
import jakarta.transaction.Transactional;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.stream.Collectors;

@Service
public class ScriptService {

    private final ScriptRepository scriptRepository;
    private final PessoaRepository pessoaRepository;
    private final ComentarioRepository comentarioRepository;

    public ScriptService(ScriptRepository scriptRepository, PessoaRepository pessoaRepository, ComentarioRepository comentarioRepository) {
        this.scriptRepository = scriptRepository;
        this.pessoaRepository = pessoaRepository;
        this.comentarioRepository = comentarioRepository;
    }

    public ArrayList<ScriptDTO> getAllScripts() {
        return scriptRepository.findAll().stream().map(a ->
                new ScriptDTO(a.getId(), new PessoaDTO(a.getAutor().getNome(), a.getAutor().getCorAvatar()), a.getDataCriacao(), a.getTitulo(), a.getConteudo(), a.getDescricao(), a.getCurtidas(), verificarSePessoaCurtiuOuNao(a))
        ).collect(Collectors.toCollection(ArrayList::new));
    }

    public ScriptDTO getScriptById(Long id) {
        return scriptRepository.findById(id).map(a ->
                new ScriptDTO(a.getId(), new PessoaDTO(a.getAutor().getNome(), a.getAutor().getCorAvatar()), a.getDataCriacao(), a.getTitulo(), a.getConteudo(), a.getDescricao(), a.getCurtidas(), verificarSePessoaCurtiuOuNao(a))
        ).orElseThrow(() -> new ObjectNotFoundException(String.format("Script com id %d não encontrado", id)));
    }

    public ScriptDTO getScriptByNome(String nome) {
        return scriptRepository.findByTitulo(nome).map(a ->
                new ScriptDTO(a.getId(), new PessoaDTO(a.getAutor().getNome(), a.getAutor().getCorAvatar()), a.getDataCriacao(), a.getTitulo(), a.getConteudo(), a.getDescricao(), a.getCurtidas(), verificarSePessoaCurtiuOuNao(a))
        ).orElseThrow(() -> new ObjectNotFoundException(String.format("Script com nome %s não encontrado", nome)));
    }

    public ScriptDTO updateScript(ScriptDTO scriptDTO, Long id) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Script script = scriptRepository.findById(id).orElseThrow(() -> new ObjectNotFoundException(String.format("Script com id %d não encontrado", id)));
        if (!script.getAutor().getNome().equals(authentication.getName())) {
            throw new PrivilegiosInsuficientesException("Você não tem permissão para editar este script");
        }

        script.setTitulo(scriptDTO.titulo());
        script.setConteudo(scriptDTO.conteudo());
        var a = scriptRepository.save(script);
        return new ScriptDTO(a.getId(), new PessoaDTO(a.getAutor().getNome(), a.getAutor().getCorAvatar()), a.getDataCriacao(), a.getTitulo(), a.getConteudo(), a.getDescricao(), a.getCurtidas(), verificarSePessoaCurtiuOuNao(a));
    }

    @Transactional
    public ComentarioDTO addComentario(String comentario, Long scriptId) {
        var script = scriptRepository.findById(scriptId).orElseThrow(() -> new ObjectNotFoundException(String.format("Script com id %d não encontrado", scriptId)));
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        var pessoa = pessoaRepository.findByLogin(authentication.getName()).orElseThrow(() -> new ObjectNotFoundException(String.format("Pessoa com nome %s não encontrada", authentication.getName())));
        var novoComentario = new Comentario(comentario, LocalDateTime.now(), pessoa);
        script.addComentario(novoComentario);
        scriptRepository.save(script);
        return new ComentarioDTO(pessoa.getId(), comentario, novoComentario.getDataHora(), new PessoaDTO(pessoa.getNome(), pessoa.getCorAvatar()), 0,false);
    }

    public ArrayList<ComentarioDTO> getComentarios(Long scriptId) {
        var script = scriptRepository.findById(scriptId).orElseThrow(() -> new ObjectNotFoundException(String.format("Script com id %d não encontrado", scriptId)));
        return script.getComentarios().stream().map(a ->
                new ComentarioDTO(a.getId().toString(), a.getComentario(), a.getDataHora(), new PessoaDTO(a.getAutor().getNome(), a.getAutor().getCorAvatar()), a.getCurtidas(),verificarSePessoaCurtiuOuNao(a))
        ).collect(Collectors.toCollection(ArrayList::new));
    }

    public ComentarioDTO getComentario(Long scriptId) {
        var script = scriptRepository.findById(scriptId).orElseThrow(() -> new ObjectNotFoundException(String.format("Script com id %d não encontrado", scriptId)));
        var comentario = script.getComentarios().getLast();
        return new ComentarioDTO(comentario.getId().toString(), comentario.getComentario(), comentario.getDataHora(), new PessoaDTO(comentario.getAutor().getNome(), comentario.getAutor().getCorAvatar()), comentario.getCurtidas(),verificarSePessoaCurtiuOuNao(comentario));
    }

    public ScriptDTO saveScript(ScriptDTO scriptDTO) {
        var pessoa = pessoaRepository.findByNome(scriptDTO.autor().nome()).orElseThrow(() -> new ObjectNotFoundException(String.format("Pessoa com nome %s não encontrada", scriptDTO.autor().nome())));
        var script = new Script(scriptDTO.titulo(), scriptDTO.conteudo(), scriptDTO.descricao(), LocalDateTime.now(), pessoa);
        var a = scriptRepository.save(script);
        return new ScriptDTO(a.getId(), new PessoaDTO(a.getAutor().getNome(), a.getAutor().getCorAvatar()), a.getDataCriacao(), a.getTitulo(), a.getConteudo(), a.getDescricao(), a.getCurtidas(), verificarSePessoaCurtiuOuNao(a));
    }

    public ScriptComComentariosDTO getScriptByIdComComentarios(Long id) {
        var script = scriptRepository.findById(id).orElseThrow(() -> new ObjectNotFoundException(String.format("Script com id %d não encontrado", id)));
        return getScriptComComentariosDTO(script);
    }

    public ScriptComComentariosDTO getScriptByNomeComComentarios(String nome) {
        var script = scriptRepository.findByTitulo(nome).orElseThrow(() -> new ObjectNotFoundException(String.format("Script com nome %s não encontrado", nome)));
        return getScriptComComentariosDTO(script);
    }

    private ScriptComComentariosDTO getScriptComComentariosDTO(Script script) {
        return new ScriptComComentariosDTO(script.getId(), new PessoaDTO(script.getAutor().getNome(), script.getAutor().getCorAvatar()), script.getDataCriacao(), script.getTitulo(), script.getConteudo(), script.getDescricao(), script.getCurtidas(), script.getComentarios()
                .stream().map(a -> new ComentarioDTO(a.getId().toString(), a.getComentario(), a.getDataHora(), new PessoaDTO(a.getAutor().getNome(), a.getAutor().getCorAvatar()), a.getCurtidas(),verificarSePessoaCurtiuOuNao(a))
        ).collect(Collectors.toCollection(ArrayList::new)));
    }

    public ArrayList<ScriptDTO> getAllScriptsByAutor(String nome) {
        var pessoa = pessoaRepository.findByNome(nome).orElseThrow(() -> new ObjectNotFoundException(String.format("Pessoa com nome %s não encontrada", nome)));
        return scriptRepository.findScriptByAutor(pessoa).stream().map(a ->
                new ScriptDTO(a.getId(), new PessoaDTO(a.getAutor().getNome(), a.getAutor().getCorAvatar()), a.getDataCriacao(), a.getTitulo(), a.getConteudo(), a.getDescricao(), a.getCurtidas(), verificarSePessoaCurtiuOuNao(a))
        ).collect(Collectors.toCollection(ArrayList::new));
    }

    public ScriptDTO curtirDescurtirScript(Long scriptId) {
        var script = scriptRepository.findById(scriptId).orElseThrow(() -> new ObjectNotFoundException(String.format("Script com id %d não encontrado", scriptId)));
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        var pessoa = pessoaRepository.findByLogin(authentication.getName()).orElseThrow(() -> new ObjectNotFoundException(String.format("Pessoa com nome %s não encontrada", authentication.getName())));

        if (script.getPessoasQueCurtiram().contains(pessoa)) {
            script.removerCurtida(pessoa);
            pessoa.removeScriptCurtido(script);
        } else {
            pessoa.addScriptCurtido(script);
            script.adicionarCurtida(pessoa);
        }

        pessoaRepository.save(pessoa);
        var a = scriptRepository.save(script);
        return new ScriptDTO(a.getId(), new PessoaDTO(a.getAutor().getNome(), a.getAutor().getCorAvatar()), a.getDataCriacao(), a.getTitulo(), a.getConteudo(), a.getDescricao(), a.getCurtidas(), verificarSePessoaCurtiuOuNao(a));
    }

    private boolean verificarSePessoaCurtiuOuNao(Script script) {
        var loginPessoa = SecurityContextHolder.getContext().getAuthentication().getName();
        var pessoa = pessoaRepository.findByLogin(loginPessoa).orElseThrow(() -> new ObjectNotFoundException(String.format("Pessoa com nome %s não encontrada", loginPessoa)));
        return script.getPessoasQueCurtiram().contains(pessoa);
    }

    public ComentarioDTO curtirDescurtirComentario(Long comentarioId) {
        var comentario = comentarioRepository.findById(comentarioId).orElseThrow(() -> new ObjectNotFoundException(String.format("Comentário com id %d não encontrado", comentarioId)));
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        var pessoa = pessoaRepository.findByLogin(authentication.getName()).orElseThrow(() -> new ObjectNotFoundException(String.format("Pessoa com nome %s não encontrada", authentication.getName())));

        if (comentario.getPessoasQueCurtiram().contains(pessoa)) {
            comentario.removerCurtida(pessoa);
            pessoa.removeComentarioCurtido(comentario);
        } else {
            pessoa.addComentarioCurtido(comentario);
            comentario.adicionarCurtida(pessoa);
        }

        pessoaRepository.save(pessoa);
        var a = comentarioRepository.save(comentario);
        return new ComentarioDTO(a.getId().toString(), a.getComentario(), a.getDataHora(), new PessoaDTO(a.getAutor().getNome(), a.getAutor().getCorAvatar()), a.getCurtidas(), verificarSePessoaCurtiuOuNao(a));
    }

    private boolean verificarSePessoaCurtiuOuNao(Comentario comentario) {
        var loginPessoa = SecurityContextHolder.getContext().getAuthentication().getName();
        var pessoa = pessoaRepository.findByLogin(loginPessoa).orElseThrow(() -> new ObjectNotFoundException(String.format("Pessoa com nome %s não encontrada", loginPessoa)));
        return comentario.getPessoasQueCurtiram().contains(pessoa);
    }

    public ArrayList<ScriptDTO> listarMeusScriptsEFavoritos() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        var pessoa = pessoaRepository.findByLogin(authentication.getName()).orElseThrow(() -> new ObjectNotFoundException(String.format("Pessoa com nome %s não encontrada", authentication.getName())));

        var meusScripts = pessoa.getScripts();
        meusScripts.addAll(pessoa.getScriptsCurtidos().stream().filter(f -> !f.getAutor().equals(pessoa)).collect(Collectors.toCollection(ArrayList::new)));

        return meusScripts.stream().map(a ->
                new ScriptDTO(a.getId(), new PessoaDTO(a.getAutor().getNome(), a.getAutor().getCorAvatar()), a.getDataCriacao(), a.getTitulo(), a.getConteudo(), a.getDescricao(), a.getCurtidas(), verificarSePessoaCurtiuOuNao(a))
        ).collect(Collectors.toCollection(ArrayList::new));
    }
}
