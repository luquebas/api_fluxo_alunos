package com.api_controle_acesso.repositories;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import com.api_controle_acesso.models.FilaDeSaida;

public interface FilaDeSaidaRepository extends JpaRepository<FilaDeSaida, Long> {
    List<FilaDeSaida> findByStatus(FilaDeSaida.StatusFila status);
    FilaDeSaida findTopByOrderByHoraSolicitacaoAsc();
}
