package com.api_controle_acesso.repositories;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.api_controle_acesso.models.Curso;

@Repository
public interface CursoRepository extends JpaRepository<Curso, Long> {
    @Query("""
            select c from Curso c
            where c.nome = :nome
            """)
    Optional<Boolean> verificarNomeExistente(String nome);
    
} 
