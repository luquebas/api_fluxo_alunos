package com.api_controle_acesso.services;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import com.api_controle_acesso.DTOs.TransacaoDTO.TransacaoPostDTO;
import com.api_controle_acesso.DTOs.TransacaoDTO.TransacaoReturnDTO;
import com.api_controle_acesso.models.Transacao;
import com.api_controle_acesso.repositories.TransacaoRepository;

@Service
public class TransacaoService {

    @Autowired
    private TransacaoRepository transacaoRepository;

    @Autowired
    private UsuarioService usuarioService;

    public Transacao criarTransacao(TransacaoPostDTO transacaoPostDTO) {

        var usuario = usuarioService.visualizarUsuario(transacaoPostDTO.usuario().getId());
        
        var transacao = new Transacao(transacaoPostDTO);
        transacao.setUsuario(usuario);

        return transacaoRepository.save(transacao);
    }

    public Page<TransacaoReturnDTO> visualizarTransacoes(Pageable pageable) {
        var page = transacaoRepository.findAll(pageable).map(TransacaoReturnDTO::new);
        return page;
    }

    public Transacao visualizarTransacao(Long id) {
        
        return transacaoRepository.getReferenceById(id);
    }

    public void excluirTransacao(Long id) {
        var transacao = transacaoRepository.getReferenceById(id);
        try {
            transacaoRepository.delete(transacao);
        } catch (Exception e) {
            throw new RuntimeException("Não foi Possível deletar essa transação");
        }
    }
}