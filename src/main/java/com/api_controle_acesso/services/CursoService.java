package com.api_controle_acesso.services;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import com.api_controle_acesso.DTOs.CursoDTO.CursoPostDTO;
import com.api_controle_acesso.DTOs.CursoDTO.CursoReturnGetDTO;
import com.api_controle_acesso.exceptions.ValidacaoException;
import com.api_controle_acesso.models.Curso;
import com.api_controle_acesso.repositories.CursoRepository;

@Service
public class CursoService {
    
    @Autowired
    private CursoRepository cursoRepository;

    public Curso criarCurso(CursoPostDTO cursoPostDTO) {
        if (cursoRepository.verificarNomeExistente(cursoPostDTO.nome()).isPresent())
            throw new ValidacaoException("Já existe um Curso com esse nome!");
        
        var curso = new Curso(cursoPostDTO);

        return cursoRepository.save(curso);
    }

    public Page<CursoReturnGetDTO> visualizarCursos(Pageable pageable) {
        var page = cursoRepository.findAll(pageable).map(CursoReturnGetDTO::new);
        return page;
    }

    public Curso visualizarCurso(Long id) {
        return cursoRepository.getReferenceById(id);
    }

    public void deleteCurso(Long id) {
        var curso = cursoRepository.getReferenceById(id);
        try {
            cursoRepository.delete(curso);
        } catch (Exception e) {
            throw new RuntimeException("Não foi Possível excluir o Curso!");
        }
    }
}
