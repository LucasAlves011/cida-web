package com.mv.cidaweb.model.repository;

import com.mv.cidaweb.model.beans.Pessoa;
import com.mv.cidaweb.model.beans.Script;
import com.mv.cidaweb.model.beans.UserRole;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.time.LocalDateTime;

@DataJpaTest
@Profile("test")
class ScriptRepositoryTest {

    @Autowired
    ScriptRepository scriptRepository;

    @Autowired
    PessoaRepository pessoaRepository;

    @Autowired
    EntityManager entityManager;

    @MockitoBean
    PasswordEncoder passwordEncoder;

    @Test
    @DisplayName("Deve retornar verdadeiro quando existir um script na base com o titulo informado")
    void findByTitulo() {
        Pessoa pessoa = new Pessoa("Lucas Matheus", "lucas" ,"lucas123", UserRole.USER);
        entityManager.persist(pessoa);
        Script script = new Script("titulo", "conteudo","descricao", LocalDateTime.now(),pessoa);
        entityManager.persist(script);

        boolean result = scriptRepository.findByTitulo("titulo").isPresent();
        assert(result);
    }

    @Test
    void findScriptByAutor() {
    }
}