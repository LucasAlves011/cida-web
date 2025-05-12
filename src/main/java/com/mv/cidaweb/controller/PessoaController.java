package com.mv.cidaweb.controller;

import com.mv.cidaweb.model.dtos.PessoaDTO;
import com.mv.cidaweb.model.dtos.RegisterDTO;
import com.mv.cidaweb.service.PessoaService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@RestController()
@RequestMapping("")
public class PessoaController {

    private final PessoaService pessoaService;

    @Value("${app.pasta-fotos}")
    private String UPLOAD_DIR;

    public PessoaController(PessoaService pessoaService) {
        this.pessoaService = pessoaService;
    }

    @PostMapping(value = "/cadastrar", consumes = {"multipart/form-data"})
    public ResponseEntity<?> cadastrarPessoa(@RequestPart String nomeSobrenome,
                                             @RequestPart String login,
                                             @RequestPart String password,
                                             @RequestPart String email,
                                             @RequestPart MultipartFile image) throws IOException {
        System.out.println(nomeSobrenome);

        if (image.isEmpty()) {
            return ResponseEntity.badRequest().body("Arquivo vazio");
        }

        if (login == null || login.isBlank() || password == null || password.isBlank() || email == null || email.isBlank()) {
            return ResponseEntity.badRequest().body("Login, senha ou email inválidos");
        }

        String nomeImagem = UUID.randomUUID() + ".png";
        try {

            byte[] bytes = image.getBytes();
            Path path = Paths.get(UPLOAD_DIR + nomeImagem);
            Files.write(path, bytes);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        var pessoa = pessoaService.cadastrarPessoa(new RegisterDTO(nomeSobrenome, login, password, email), nomeImagem);

        return ResponseEntity.ok().body(new PessoaDTO(pessoa.nome(), nomeImagem.replace(".png", "")));
    }

    @GetMapping("image/{id}")
    public ResponseEntity<byte[]> getFoto(@PathVariable UUID id) {
        return ResponseEntity.ok().body(pessoaService.getImage(id));
    }
}
