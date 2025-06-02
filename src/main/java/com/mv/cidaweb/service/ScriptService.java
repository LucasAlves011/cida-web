package com.mv.cidaweb.service;

import com.mv.cidaweb.model.beans.Pessoa;
import com.mv.cidaweb.model.beans.Script;
import com.mv.cidaweb.model.dtos.PessoaDTO;
import com.mv.cidaweb.model.dtos.ScriptComComentariosDTO;
import com.mv.cidaweb.model.dtos.ScriptDTO;
import com.mv.cidaweb.model.dtos.ScriptEntradaDTO;
import com.mv.cidaweb.model.exceptions.ObjectNotFoundException;
import com.mv.cidaweb.model.exceptions.PrivilegiosInsuficientesException;
import com.mv.cidaweb.model.repository.ScriptRepository;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ScriptService {

    private final ScriptRepository scriptRepository;
    private final ComentarioService comentarioService;
    private final PessoaService pessoaService;

    public ScriptService(ScriptRepository scriptRepository, PessoaService pessoaService, @Lazy ComentarioService comentarioService) {
        this.scriptRepository = scriptRepository;
        this.pessoaService = pessoaService;
        this.comentarioService = comentarioService;
    }

    public Optional<Script> findById(Long scriptId) {
        return scriptRepository.findById(scriptId);
    }

    public Script save(Script script) {
        return scriptRepository.save(script);
    }

    public ArrayList<ScriptDTO> getAllScripts() {
        return scriptRepository.findAll().stream().map(this::scriptToScriptDTO).collect(Collectors.toCollection(ArrayList::new));
    }

    public ScriptDTO getScriptById(Long id) {
        return scriptRepository.findById(id).map(this::scriptToScriptDTO).orElseThrow(() -> new ObjectNotFoundException(String.format("Script com idScript %d não encontrado", id)));
    }

    public ScriptDTO getScriptByNome(String nome) {
        return scriptRepository.findByTitulo(nome).map(this::scriptToScriptDTO).orElseThrow(() -> new ObjectNotFoundException(String.format("Script com nome %s não encontrado", nome)));
    }

    public ScriptDTO updateScript(ScriptEntradaDTO ScriptEntradaDTO , Long id) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Script script = scriptRepository.findById(id).orElseThrow(() -> new ObjectNotFoundException(String.format("Script com idScript %d não encontrado", id)));
        if (!script.getAutor().getLogin().equals(authentication.getName())) {
            throw new PrivilegiosInsuficientesException("Você não tem permissão para editar este script");
        }

        script.setTitulo(ScriptEntradaDTO.titulo());
        script.setConteudo(ScriptEntradaDTO.conteudo());
        script.setDescricao(ScriptEntradaDTO.descricao());
        script.setDataAtualizacao(LocalDateTime.now());

        var a = scriptRepository.save(script);
        return new ScriptDTO(a.getId(), new PessoaDTO(a.getAutor().getNome(), a.getAutor().getIdFoto()), a.getDataCriacao(), a.getTitulo(), a.getConteudo(),
                a.getDescricao(), a.getCurtidas(), verificarSePessoaCurtiuOuNao(a));
    }

    public ScriptDTO saveScript(ScriptEntradaDTO ScriptEntradaDTO) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        var pessoa = pessoaService.findByLogin(authentication.getName()).orElseThrow(() -> new ObjectNotFoundException(String.format("Pessoa com nome %s não encontrada", authentication.getName())));

        var script = new Script(ScriptEntradaDTO.titulo().trim(), ScriptEntradaDTO.conteudo(), ScriptEntradaDTO.descricao().trim(), LocalDateTime.now(), pessoa);
        var a = scriptRepository.save(script);
        return scriptToScriptDTO(a);
    }

    public ScriptComComentariosDTO getScriptByIdComComentarios(Long id) {
        var script = scriptRepository.findById(id).orElseThrow(() -> new ObjectNotFoundException(String.format("Script com idScript %d não encontrado", id)));
        return getScriptComComentariosDTO(script);
    }

    public ScriptComComentariosDTO getScriptByNomeComComentarios(String nome) {
        var script = scriptRepository.findByTitulo(nome).orElseThrow(() -> new ObjectNotFoundException(String.format("Script com nome %s não encontrado", nome)));
        return getScriptComComentariosDTO(script);
    }

    private ScriptComComentariosDTO getScriptComComentariosDTO(Script script) {
        var comentarios = comentarioService.getComentarios(script.getId());
        return new ScriptComComentariosDTO(script.getId(), new PessoaDTO(script.getAutor().getNome(), script.getAutor().getIdFoto()), script.getDataCriacao(),
                script.getTitulo(), script.getConteudo(), script.getDescricao(), script.getCurtidas(), comentarios, verificarSePessoaCurtiuOuNao(script));
    }

    public ArrayList<ScriptDTO> getAllScriptsByAutor(String nome) {
        var pessoa = pessoaService.findByNome(nome).orElseThrow(() -> new ObjectNotFoundException(String.format("Pessoa com nome %s não encontrada", nome)));
        return scriptRepository.findScriptByAutor(pessoa).stream().map(this::scriptToScriptDTO).collect(Collectors.toCollection(ArrayList::new));
    }

    public ScriptDTO curtirDescurtirScript(Long scriptId) {
        var script = scriptRepository.findById(scriptId).orElseThrow(() -> new ObjectNotFoundException(String.format("Script com idScript %d não encontrado", scriptId)));
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        var pessoa = pessoaService.findByLogin(authentication.getName()).orElseThrow(() -> new ObjectNotFoundException(String.format("Pessoa com nome %s não encontrada", authentication.getName())));

        if (script.getPessoasQueCurtiram().contains(pessoa)) {
            script.removerCurtida(pessoa);
            pessoa.removeScriptCurtido(script);
        } else {
            pessoa.addScriptCurtido(script);
            script.adicionarCurtida(pessoa);
        }

        pessoaService.save(pessoa);
        var a = scriptRepository.save(script);
        return new ScriptDTO(a.getId(), new PessoaDTO(a.getAutor().getNome(),a.getAutor().getIdFoto()), a.getDataCriacao(), a.getTitulo(), a.getConteudo(),
                a.getDescricao(), a.getCurtidas(), verificarSePessoaCurtiuOuNao(a));
    }

    private boolean verificarSePessoaCurtiuOuNao(Script script) {
        var loginPessoa = SecurityContextHolder.getContext().getAuthentication().getName();
        var pessoa = pessoaService.findByLogin(loginPessoa).orElseThrow(() -> new ObjectNotFoundException(String.format("Pessoa com nome %s não encontrada", loginPessoa)));
        return script.getPessoasQueCurtiram().contains(pessoa);
    }

    private ScriptDTO scriptToScriptDTO(Script script) {
        return new ScriptDTO(script.getId(), new PessoaDTO(script.getAutor().getNome(), script.getAutor().getIdFoto()),
                script.getDataCriacao(), script.getTitulo(), script.getConteudo(), script.getDescricao(), script.getCurtidas(), verificarSePessoaCurtiuOuNao(script));
    }

    public ArrayList<ScriptDTO> listarMeusScriptsEFavoritos() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        var pessoa = pessoaService.findByLogin(authentication.getName()).orElseThrow(() -> new ObjectNotFoundException(String.format("Pessoa com nome %s não encontrada", authentication.getName())));

        var meusScripts = pessoa.getScripts();
        meusScripts.addAll(pessoa.getScriptsCurtidos().stream().filter(f -> !f.getAutor().equals(pessoa)).collect(Collectors.toCollection(ArrayList::new)));

        return meusScripts.stream().map(this::scriptToScriptDTO).collect(Collectors.toCollection(ArrayList::new));
    }

    public Boolean deleteScript(Long id) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        var script = scriptRepository.findById(id).orElseThrow(() -> new ObjectNotFoundException(String.format("Script com idScript %d não encontrado", id)));
        if (!script.getAutor().getLogin().equals(authentication.getName())) {
            throw new PrivilegiosInsuficientesException("Você não tem permissão para deletar este script");
        }

        // 2. Remove todas as referências bidirecionais
        // Para cada pessoa que curtiu este script:
        for (Pessoa pessoa : new ArrayList<>(script.getPessoasQueCurtiram())) {
            pessoa.getScriptsCurtidos().remove(script);
            pessoaService.save(pessoa); // Sincroniza a mudança
        }
        script.getPessoasQueCurtiram().clear();

        // 3. Deleta o script (os comentários serão removidos pelo orphanRemoval)
        scriptRepository.delete(script);

        // 4. Força o flush para garantir sincronização
        scriptRepository.flush();

        scriptRepository.delete(script);
        return true;
    }
}
