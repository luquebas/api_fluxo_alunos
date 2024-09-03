package com.api_controle_acesso.controllers;
import java.util.List;
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
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;
import com.api_controle_acesso.DTOs.NotificacaoDTO.NotificacaoPostDTO;
import com.api_controle_acesso.DTOs.NotificacaoDTO.NotificacaoPutDTO;
import com.api_controle_acesso.DTOs.NotificacaoDTO.NotificacaoReturnDTO;
import com.api_controle_acesso.models.Notificacao;
import com.api_controle_acesso.services.NotificacaoService;
import com.api_controle_acesso.services.UsuarioService;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/notificacao")
public class NotificacaoController {
    
    @Autowired 
    private NotificacaoService notificacaoService;

    @Autowired 
    private UsuarioService usuarioService;

    Logger logger = LoggerFactory.getLogger(TransacaoController.class);

    @PostMapping
    @Transactional
    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    public ResponseEntity<Object> criarNotificacao(@RequestBody @Valid NotificacaoPostDTO notificacaoPostDTO, UriComponentsBuilder uriComponentsBuilder) {
        
        var transacao = notificacaoService.criarNotificacao(notificacaoPostDTO);
        var uri = uriComponentsBuilder.path("notificacao/{id}").buildAndExpand(transacao.getId()).toUri();
        
        return ResponseEntity.created(uri).body(new NotificacaoReturnDTO(transacao));
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    public ResponseEntity<Page<NotificacaoReturnDTO>> visualizarNotificacoes(@PageableDefault(size = 10, sort = {"usuario_id"}) Pageable pageable) {

        return ResponseEntity.ok().body(notificacaoService.visualizarNotificacoes(pageable));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_USER')")
    public ResponseEntity<Object> visualizarNotificacao(@PathVariable Long id) {
        
        var notificacao = notificacaoService.visualizarNotificacao(id);
        return ResponseEntity.ok().body(new NotificacaoReturnDTO(notificacao));
    }

    @DeleteMapping("/{id}")
    @Transactional
    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    public ResponseEntity<Object> deleteTransacao(@PathVariable Long id) {
        
        notificacaoService.deleteNotificacao(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping
    @Transactional
    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    public ResponseEntity<Object> atualizarNotificacao(@RequestBody @Valid NotificacaoPutDTO notificacaoPutDTO) {
        var notificacao = notificacaoService.visualizarNotificacao(notificacaoPutDTO.id());

        if (notificacaoPutDTO.usuario_id() != null) {
            var usuario = usuarioService.visualizarUsuario(notificacaoPutDTO.usuario_id());
            notificacao.setUsuario(usuario);
        }

        notificacao.update(notificacaoPutDTO);

        return ResponseEntity.ok().body(new NotificacaoReturnDTO(notificacao));
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_USER')")
    @GetMapping("/usuario/{userId}")
    public List<Notificacao> getUserNotifications(@PathVariable Long userId) {
        return notificacaoService.getUserNotifications(userId);
    }
}
