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
import java.util.Base64;
import java.util.Map;
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
    public ResponseEntity<?> getFoto(@PathVariable UUID id) {
        byte[] imageBytes = pessoaService.getImage(id);
        return ResponseEntity.ok().body(Base64.getEncoder().encodeToString(imageBytes));
    }

    @GetMapping("/user/image")
    public ResponseEntity<Map<String,String>> getFotoUser() {
        return ResponseEntity.ok().body(pessoaService.getImageUser());
    }
}
