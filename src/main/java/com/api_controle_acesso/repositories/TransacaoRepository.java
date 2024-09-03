package com.api_controle_acesso.repositories;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.api_controle_acesso.models.Transacao;

@Repository
public interface TransacaoRepository extends JpaRepository<Transacao, Long> {

    
}
