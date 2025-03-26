package com.mv.cidaweb.model.repository;

import com.mv.cidaweb.model.beans.Pessoa;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface PessoaRepository extends JpaRepository<Pessoa, UUID> {
    Optional<Pessoa> findByNome(String nome);

    Optional<Pessoa> findByLogin(String login);
}
