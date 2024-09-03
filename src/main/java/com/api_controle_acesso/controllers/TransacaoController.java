package com.api_controle_acesso.controllers;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;
import com.api_controle_acesso.DTOs.TransacaoDTO.TransacaoPostDTO;
import com.api_controle_acesso.DTOs.TransacaoDTO.TransacaoReturnDTO;
import com.api_controle_acesso.services.TransacaoService;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/transacao")
public class TransacaoController {
    
    @Autowired 
    private TransacaoService transacaoService;

    Logger logger = LoggerFactory.getLogger(TransacaoController.class);

    @PostMapping
    @Transactional
    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    public ResponseEntity<Object> criarTransacao(@RequestBody @Valid TransacaoPostDTO transacaoPostDTO, UriComponentsBuilder uriComponentsBuilder) {
        
        var transacao = transacaoService.criarTransacao(transacaoPostDTO);
        var uri = uriComponentsBuilder.path("transacao/{id}").buildAndExpand(transacao.getId()).toUri();
        
        return ResponseEntity.created(uri).body(new TransacaoReturnDTO(transacao));
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    public ResponseEntity<Page<TransacaoReturnDTO>> visualizarTransacoes(@PageableDefault(size = 10, sort = {"usuario_id"}) Pageable pageable) {

        return ResponseEntity.ok().body(transacaoService.visualizarTransacoes(pageable));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    public ResponseEntity<Object> visualizarTransacao(@PathVariable Long id) {
        
        var transacao = transacaoService.visualizarTransacao(id);
        return ResponseEntity.ok().body(new TransacaoReturnDTO(transacao));
    }

    @DeleteMapping("/{id}")
    @Transactional
    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    public ResponseEntity<Object> deleteTransacao(@PathVariable Long id) {
        
        transacaoService.excluirTransacao(id);
        return ResponseEntity.noContent().build();
    }

}
