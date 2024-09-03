package com.api_controle_acesso.controllers;
import java.io.IOException;
import java.util.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.util.UriComponentsBuilder;
import com.api_controle_acesso.DTOs.UsuarioDTO.UsuarioPostDTO;
import com.api_controle_acesso.DTOs.UsuarioDTO.UsuarioPutDTO;
import com.api_controle_acesso.DTOs.UsuarioDTO.UsuarioReturnDTO;
import com.api_controle_acesso.models.enums.Role;
import com.api_controle_acesso.services.CursoService;
import com.api_controle_acesso.services.UsuarioService;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;

@RestController
@RequestMapping("usuario")
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private CursoService cursoService;

    Logger logger = LoggerFactory.getLogger(UsuarioController.class);
    
    @PostMapping
    @Transactional
    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    public ResponseEntity<Object> criarUsuario(@RequestBody @Valid UsuarioPostDTO dadosUsuario, UriComponentsBuilder uriComponentsBuilder, Authentication authentication) {

        var usuario = usuarioService.criarUsuario(dadosUsuario);
        var uri = uriComponentsBuilder.path("usuario/{id}").buildAndExpand(usuario.getId()).toUri();

        return ResponseEntity.created(uri).body(new UsuarioReturnDTO(usuario));
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    @GetMapping
    public ResponseEntity<Page<UsuarioReturnDTO>> getUsuarios(@PageableDefault(size = 10, sort = {"nome"}) Pageable pageable) {

        return ResponseEntity.ok(usuarioService.visualizarUsuarios(pageable));
    }

    @GetMapping("/imagem/{id}")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_USER')")
    public ResponseEntity<Object> getUsuarioImage(@PathVariable Long id) {
        var usuario = usuarioService.visualizarUsuario(id);
        var image = usuario.getFoto();

        try {
            var decodedImage = Base64.getDecoder().decode(image);

            if (decodedImage != null) {
                MediaType mediaType = MediaType.IMAGE_JPEG;

                return ResponseEntity.ok().contentType(mediaType).body(decodedImage);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build(); 
        }
    }


    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_USER')")
    @GetMapping("/{id}")
    public ResponseEntity<Object> getUsuario(@PathVariable Long id) {
        var usuario = usuarioService.visualizarUsuario(id);
        return ResponseEntity.ok(new UsuarioReturnDTO(usuario));
    }

    @PutMapping("/upload/{id}")
    @Transactional
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_USER')")
    public ResponseEntity<Object> uploadUsuarioImage(@PathVariable Long id, @RequestParam("foto") MultipartFile image) {
        var usuario = usuarioService.visualizarUsuario(id);

        if (usuario == null) {
            return ResponseEntity.notFound().build();
        }

        if (image != null && !image.isEmpty()) {
            try {
                byte[] byteImage = image.getBytes();
                usuario.setFoto(Base64.getEncoder().encodeToString(byteImage));
            } catch (IOException e) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro ao fazer upload do arquivo: " + e.getMessage());
            }
        }

        return ResponseEntity.ok().body(new UsuarioReturnDTO(usuario));
    }

    @PutMapping
    @Transactional
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_USER')")
    public ResponseEntity<Object> updateUsuario(@Valid @RequestBody UsuarioPutDTO usuarioPutDTO) {
        var usuario = usuarioService.visualizarUsuario(usuarioPutDTO.id());
        
        if (usuarioPutDTO.curso_id() != null) {
            var curso = cursoService.visualizarCurso(usuarioPutDTO.curso_id());
            usuario.setCurso(curso);
        }
        
        usuario.update(usuarioPutDTO);

        return ResponseEntity.ok().body(new UsuarioReturnDTO(usuario));
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    @PutMapping("/{id}")
    @Transactional
    public ResponseEntity<Object> updateRole(@PathVariable Long id) {

        var usuario = usuarioService.visualizarUsuario(id);
        usuario.setRole(Role.ROLE_ADMIN);

        return ResponseEntity.ok().build();
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_USER')")
    @DeleteMapping("/{id}")
    @Transactional
    public ResponseEntity<Object> deleteUsuario(@PathVariable Long id) {
       
        usuarioService.deleteUsuario(id);
        return ResponseEntity.noContent().build();
    }

}
