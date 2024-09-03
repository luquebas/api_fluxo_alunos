package com.api_controle_acesso.services;
import java.time.DayOfWeek;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.api_controle_acesso.DTOs.UsuarioDTO.UsuarioPostDTO;
import com.api_controle_acesso.DTOs.UsuarioDTO.UsuarioReturnDTO;
import com.api_controle_acesso.exceptions.ValidacaoException;
import com.api_controle_acesso.models.Usuario;
import com.api_controle_acesso.models.enums.DiaSemana;
import com.api_controle_acesso.models.enums.Role;
import com.api_controle_acesso.repositories.UsuarioRepository;

@Service
public class UsuarioService {
    
    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private CursoService cursoService;

    @Autowired
    private PasswordEncoder passwordEncoder; 

    public Usuario criarUsuario(UsuarioPostDTO dadosUsuario) {
        if (usuarioRepository.verificarCpfExistente(dadosUsuario.cpf()).isPresent())
            throw new ValidacaoException("CPF já existente");
        
        var usuario = new Usuario(dadosUsuario);

        var curso = cursoService.visualizarCurso(dadosUsuario.curso().getId());
        usuario.setCurso(curso);
        usuario.setRole(Role.ROLE_USER);
        usuario.setSenha(passwordEncoder.encode((dadosUsuario.senha())));

        return usuarioRepository.save(usuario);
    }

    public Page<UsuarioReturnDTO> visualizarUsuarios(Pageable pageable) {
        var page = usuarioRepository.findAll(pageable).map(UsuarioReturnDTO::new);
        return page;
    }

    public Usuario visualizarUsuario(Long id) {
        return usuarioRepository.getReferenceById(id);
    }

    public void deleteUsuario(Long id) {
        var usuario = usuarioRepository.getReferenceById(id);
        try {
            usuarioRepository.delete(usuario);      
        } catch (Exception e) {
            throw new RuntimeException("Não foi Possível deletar esse Usuário!");
        }
    }

    public Usuario findUsuarioByEmail(String email) {
        var usuario = usuarioRepository.findByEmail(email);
        return usuario;
    }

    public void updateUsuario(Usuario usuario) {
        usuarioRepository.save(usuario);
    }

    public DiaSemana comparaDiaSemana(DayOfWeek dayOfWeek) {
    switch (dayOfWeek) {

        case MONDAY: 
            return DiaSemana.SEGUNDA_FEIRA;
        case TUESDAY: 
            return DiaSemana.TERCA_FEIRA;
        case WEDNESDAY: 
            return DiaSemana.QUARTA_FEIRA;
        case THURSDAY: 
            return DiaSemana.QUINTA_FEIRA;
        case FRIDAY: 
            return DiaSemana.SEXTA_FEIRA;
        case SATURDAY: 
            return DiaSemana.SABADO;
        case SUNDAY: 
            return DiaSemana.DOMINGO;
        default: 
            throw new IllegalArgumentException("Dia da semana desconhecido: " + dayOfWeek);
        }
    }
}
