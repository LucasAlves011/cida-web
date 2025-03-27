package com.mv.cidaweb.model.repository;

import com.mv.cidaweb.model.beans.Comentario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ComentarioRepository extends JpaRepository<Comentario, Long> {
    Optional<Comentario> findComentarioById(Long id);
}
