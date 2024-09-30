package com.api_controle_acesso.repositories;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.api_controle_acesso.models.Curso;
import com.api_controle_acesso.models.Usuario;
import com.api_controle_acesso.models.enums.Role;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    
    @Query("""
            select u from Usuario u
            where u.cpf = :cpf
            """)
    Optional<Boolean> verificarCpfExistente(String cpf);

    Usuario findByEmail(String email);

    List<Usuario> findByCursoAndRole(Curso cursoId, Role role);
}
