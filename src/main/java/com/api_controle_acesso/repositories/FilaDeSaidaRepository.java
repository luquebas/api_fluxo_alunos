package com.api_controle_acesso.repositories;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import com.api_controle_acesso.models.FilaDeSaida;

import jakarta.transaction.Transactional;

public interface FilaDeSaidaRepository extends JpaRepository<FilaDeSaida, Long> {
    List<FilaDeSaida> findByStatus(FilaDeSaida.StatusFila status);
    FilaDeSaida findTopByOrderByHoraSolicitacaoAsc();

    @Query("""
            select f.usuario.id from FilaDeSaida f
            where f.status = 'EM_ESPERA' and f.autorizado = true
            """)
    List<Long> verificarAguardando();

    @Transactional
    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query("""
        UPDATE FilaDeSaida f SET f.status = 'RETORNOU' WHERE f.usuario.id = :id
            """)
    void removerDaLista(Long id);
    Optional<FilaDeSaida> findById(Long id);
    Optional<FilaDeSaida> findByUsuarioIdAndStatus(Long usuarioId, FilaDeSaida.StatusFila status);
}
