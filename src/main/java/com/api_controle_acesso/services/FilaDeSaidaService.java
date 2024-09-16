package com.api_controle_acesso.services;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.api_controle_acesso.models.FilaDeSaida;
import com.api_controle_acesso.models.Usuario;
import com.api_controle_acesso.repositories.FilaDeSaidaRepository;

@Service
public class FilaDeSaidaService {
    
    @Autowired
    private FilaDeSaidaRepository filaDeSaidaRepository;

    public FilaDeSaida adicionarAlunoNaFila(Usuario usuario) {
        FilaDeSaida filaDeSaida = new FilaDeSaida();
        filaDeSaida.setUsuario(usuario);
        filaDeSaida.setStatus(FilaDeSaida.StatusFila.EM_ESPERA);
        filaDeSaida.setHoraSolicitacao(LocalDateTime.now());
        return filaDeSaidaRepository.save(filaDeSaida);
    }

    public void autorizarSaida(Long id) {
        FilaDeSaida filaDeSaida = filaDeSaidaRepository.findById(id).orElseThrow();
        filaDeSaida.setStatus(FilaDeSaida.StatusFila.AUTORIZADO);
        filaDeSaida.setHoraAutorizacao(LocalDateTime.now());
        filaDeSaidaRepository.save(filaDeSaida);
    }

    public void marcarRetorno(Long id) {
        FilaDeSaida filaDeSaida = filaDeSaidaRepository.findById(id).orElseThrow();
        filaDeSaida.setStatus(FilaDeSaida.StatusFila.RETORNOU);
        filaDeSaidaRepository.save(filaDeSaida);
    }

    public FilaDeSaida getProximoAluno() {
        return filaDeSaidaRepository.findTopByOrderByHoraSolicitacaoAsc();
    }

    public List<FilaDeSaida> getAlunosPorStatus(FilaDeSaida.StatusFila status) {
        return filaDeSaidaRepository.findByStatus(status);
    }
}
