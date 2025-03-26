package com.mv.cidaweb.model.repository;

import com.mv.cidaweb.model.beans.Pessoa;
import com.mv.cidaweb.model.beans.Script;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Optional;

@Repository
public interface ScriptRepository extends JpaRepository<Script, Long> {
    Optional<Script> findByTitulo(String titulo);
    ArrayList<Script> findScriptByAutor(Pessoa pessoa);
}
