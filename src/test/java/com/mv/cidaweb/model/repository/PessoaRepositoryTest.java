package com.mv.cidaweb.model.repository;

import com.mv.cidaweb.model.beans.Pessoa;
import com.mv.cidaweb.model.beans.UserRole;
import com.mv.cidaweb.model.dtos.PessoaDTO;
import jakarta.persistence.EntityManager;
import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
class PessoaRepositoryTest {

    @Autowired
    PessoaRepository pessoaRepository;

    @Autowired
    EntityManager entityManager;

    @MockitoBean
    PasswordEncoder passwordEncoder;

    @Test
    @DisplayName("Deve retornar verdadeiro quando existir uma pessoa na base com o login informado")
    void findByNome1() {
        PessoaDTO pessoa = new PessoaDTO("fulano", "#0000FF");
        this.createPessoa(pessoa);
        boolean result = pessoaRepository.findByNome("fulano").isPresent();
        assert(result);
    }

    @Test
    @DisplayName("Deve retornar verdadeira quando existir uma pessoa na base com o login informado")
    void findByLogin() {
        Pessoa pessoa = new Pessoa("Lucas Matheus", "lucas" ,"lucas123", UserRole.USER, "FOTO");
        entityManager.persist(pessoa);
        Pessoa result = pessoaRepository.findByLogin("lucas").get();
        assertEquals(pessoa, result);
    }

    private Pessoa createPessoa(PessoaDTO pessoaDTO){
        Pessoa pessoa = new Pessoa();
        pessoa.setNome(pessoaDTO.nome());
        this.entityManager.persist(pessoa);
        return pessoa;
    }
}