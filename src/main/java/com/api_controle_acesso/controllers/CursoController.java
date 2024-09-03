package com.api_controle_acesso.controllers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;
import com.api_controle_acesso.DTOs.CursoDTO.CursoPostDTO;
import com.api_controle_acesso.DTOs.CursoDTO.CursoPutDTO;
import com.api_controle_acesso.DTOs.CursoDTO.CursoReturnGetDTO;
import com.api_controle_acesso.services.CursoService;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/curso")
public class CursoController {

    @Autowired
    private CursoService cursoService;

    @PostMapping
    @Transactional
    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    public ResponseEntity<Object> criarCurso(@RequestBody @Valid CursoPostDTO cursoPostDTO, UriComponentsBuilder uriComponentsBuilder) {
        var curso = cursoService.criarCurso(cursoPostDTO);
        var uri = uriComponentsBuilder.path("curso/{id}").buildAndExpand(curso.getId()).toUri();

        return ResponseEntity.created(uri).body(new CursoReturnGetDTO(curso, true));
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    public ResponseEntity<Page<CursoReturnGetDTO>> visualizarCursos(@PageableDefault(size = 10, sort = {"nome"}) Pageable pageable) {
        return ResponseEntity.ok().body(cursoService.visualizarCursos(pageable));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    public ResponseEntity<Object> visualizarCurso(@PathVariable Long id) {
        
        var curso = cursoService.visualizarCurso(id);
        return ResponseEntity.ok().body(new CursoReturnGetDTO(curso));
    }

    @PutMapping
    @Transactional
    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    public ResponseEntity<Object> atualizarCurso(@RequestBody @Valid CursoPutDTO cursoPutDTO) {
        var curso = cursoService.visualizarCurso(cursoPutDTO.id());
        curso.update(cursoPutDTO);

        return ResponseEntity.ok().body(new CursoReturnGetDTO(curso));
    }

    @DeleteMapping("/{id}")
    @Transactional
    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    public ResponseEntity<Object> deleteCurso(@PathVariable Long id) {
        cursoService.deleteCurso(id);

        return ResponseEntity.noContent().build();
    } 
}
