package com.mv.cidaweb.service;

import com.mv.cidaweb.model.beans.Pessoa;
import com.mv.cidaweb.model.beans.UserRole;
import com.mv.cidaweb.model.dtos.PessoaDTO;
import com.mv.cidaweb.model.dtos.RegisterDTO;
import com.mv.cidaweb.model.exceptions.CredenciaisInvalidasException;
import com.mv.cidaweb.model.repository.PessoaRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;
import java.util.UUID;

@Service
public class PessoaService {

    private final PasswordEncoder passwordEncoder;
    private final PessoaRepository pessoaRepository;

    @Value("${app.pasta-fotos}")
    private String UPLOAD_DIR;

    public PessoaService(PasswordEncoder passwordEncoder, PessoaRepository pessoaRepository) {
        this.passwordEncoder = passwordEncoder;
        this.pessoaRepository = pessoaRepository;
    }

    public PessoaDTO cadastrarPessoa(RegisterDTO registerDTO, String idFoto) {
        if (pessoaRepository.findByLogin(registerDTO.login()).isPresent()) {
            throw new CredenciaisInvalidasException("Login já está sendo usado!");
        }
        if (registerDTO.password() == null || registerDTO.password().isBlank() || registerDTO.password().length() < 5) {
            throw new CredenciaisInvalidasException("Senha inválida!");
        }
        var pessoa = pessoaRepository.save(new Pessoa(registerDTO.nomeSobrenome(), registerDTO.login(), passwordEncoder.encode(registerDTO.password()), UserRole.USER, idFoto));
        return new PessoaDTO(pessoa.getNome(), pessoa.getIdFoto());
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

    public byte[] getImage(UUID id) {

        try {
            Path filePath = Paths.get(UPLOAD_DIR).resolve(id.toString() + ".png").normalize();
            byte[] imageBytes = Files.readAllBytes(filePath);

            if (imageBytes.length == 0) {
                throw new RuntimeException("Arquivo vazio");
            }

            return imageBytes;
        } catch (IOException e) {
            throw new RuntimeException("Erro ao ler a imagem: " + e.getMessage());
        }

    }
}
