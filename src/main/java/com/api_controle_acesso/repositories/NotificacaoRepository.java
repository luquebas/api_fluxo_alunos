package com.api_controle_acesso.repositories;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.api_controle_acesso.models.Notificacao;

@Repository
public interface NotificacaoRepository extends JpaRepository<Notificacao, Long> {
    
    @Query("select n from Notificacao n where n.usuario.id = :usuario_id")
    List<Notificacao> findByUsuarioId(@Param("usuario_id") Long idUsuario);
}
