package com.api_controle_acesso.controllers;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.api_controle_acesso.models.FilaDeSaida;
import com.api_controle_acesso.models.enums.TipoSaida;
import com.api_controle_acesso.repositories.FilaDeSaidaRepository;
import com.api_controle_acesso.services.FilaWebsocketService;

@RestController
@RequestMapping("/fila")
public class FilaDeSaidaController {

    @Autowired
    private FilaWebsocketService filaWebsocketService;

    @Autowired
    private FilaDeSaidaRepository filaDeSaidaRepository;

    @GetMapping("/usuarios")
    public ResponseEntity<List<Long>> getUsuariosNaFila() {
        List<Long> usuariosNaFila = filaWebsocketService.getQueue();
        return ResponseEntity.ok(usuariosNaFila);
    }

    @GetMapping("/tiposaida/{tipo}")
    public List<FilaDeSaida> buscarFilasaida(@PathVariable TipoSaida tipoSaida) {
        return filaDeSaidaRepository.findByTipoSaida(tipoSaida);
    }
}
