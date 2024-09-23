package com.api_controle_acesso.controllers;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.api_controle_acesso.models.FilaDeSaida;
import com.api_controle_acesso.models.Usuario;
import com.api_controle_acesso.services.FilaDeSaidaService;
import com.api_controle_acesso.services.FilaService;
import com.api_controle_acesso.services.UsuarioService;

@RestController
@RequestMapping("/fila")
public class FilaDeSaidaController {

    @Autowired
    private FilaDeSaidaService filaDeSaidaService;

    @Autowired
    private FilaService filaService;

    @Autowired
    private UsuarioService usuarioService;

    @PostMapping("/adicionar")
    public ResponseEntity<Object> adicionarAlunoNaFila(@RequestParam Long usuarioId) {
        Usuario usuario = usuarioService.visualizarUsuario(usuarioId); 
        FilaDeSaida filaDeSaida = filaDeSaidaService.adicionarAlunoNaFila(usuario);
        return ResponseEntity.ok(filaDeSaida);
    }

    @PutMapping("/autorizar/{id}")
    public ResponseEntity<Object> autorizarSaida(@PathVariable Long id) {
        filaDeSaidaService.autorizarSaida(id);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/retornar/{id}")
    public ResponseEntity<Object> marcarRetorno(@PathVariable Long id) {
        filaDeSaidaService.marcarRetorno(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/proximo")
    public ResponseEntity<FilaDeSaida> getProximoAluno() {
        FilaDeSaida filaDeSaida = filaDeSaidaService.getProximoAluno();
        return ResponseEntity.ok(filaDeSaida);
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<List<FilaDeSaida>> getAlunosPorStatus(@PathVariable FilaDeSaida.StatusFila status) {
        List<FilaDeSaida> fila = filaDeSaidaService.getAlunosPorStatus(status);
        return ResponseEntity.ok(fila);
    }

    @GetMapping("/usuarios")
    public ResponseEntity<List<Long>> getUsuariosNaFila() {
        List<Long> usuariosNaFila = filaService.getQueue();
        return ResponseEntity.ok(usuariosNaFila);
    }
}
