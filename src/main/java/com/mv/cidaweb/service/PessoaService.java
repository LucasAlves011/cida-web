package com.mv.cidaweb.service;

import com.mv.cidaweb.model.beans.Pessoa;
import com.mv.cidaweb.model.beans.UserRole;
import com.mv.cidaweb.model.dtos.PessoaDTO;
import com.mv.cidaweb.model.dtos.RegisterDTO;
import com.mv.cidaweb.model.repository.PessoaRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class PessoaService {

    private final PasswordEncoder passwordEncoder;
    private final PessoaRepository pessoaRepository;

    public PessoaService(PasswordEncoder passwordEncoder, PessoaRepository pessoaRepository) {
        this.passwordEncoder = passwordEncoder;
        this.pessoaRepository = pessoaRepository;
    }

    public PessoaDTO cadastrarPessoa(RegisterDTO registerDTO) {
        if (pessoaRepository.findByLogin(registerDTO.login()).isPresent()) {
            throw new RuntimeException("Login já está sendo usado!");
        }
        if (registerDTO.password() == null || registerDTO.password().isBlank() || registerDTO.password().length() < 5) {
            throw new RuntimeException("Senha inválida!");
        }
        var pessoa = pessoaRepository.save(new Pessoa(registerDTO.nome(), registerDTO.login(), passwordEncoder.encode(registerDTO.password()), UserRole.USER));
        return new PessoaDTO(pessoa.getNome(), pessoa.getCorAvatar());
    }

    public Pessoa save(Pessoa pessoa) {
        return pessoaRepository.save(pessoa);
    }

    public Optional<Pessoa> findByLogin(String name) {
        return pessoaRepository.findByLogin(name);
    }

    public Optional<Pessoa> findByNome(String nome) {
        return pessoaRepository.findByNome(nome);
    }
}
