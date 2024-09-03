package com.api_controle_acesso.controllers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;
import com.api_controle_acesso.DTOs.AuthDTO.LoginDTO;
import com.api_controle_acesso.DTOs.AuthDTO.TokenDTO;
import com.api_controle_acesso.models.Usuario;
import com.api_controle_acesso.services.JWTService;
import com.api_controle_acesso.services.UsuarioService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;

@RestController
public class autenticacaoController {
    
    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JWTService jwtService;

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    HttpServletRequest request;

    @PostMapping("/login")
    public ResponseEntity<Object> login(@RequestBody @Valid LoginDTO loginDto) {

        var authToken = new UsernamePasswordAuthenticationToken(loginDto.email(), loginDto.senha());
        var usuario = usuarioService.findUsuarioByEmail(loginDto.email());

        var authentication = authenticationManager.authenticate(authToken);

        var jwtToken = jwtService.gerarToken((Usuario) authentication.getPrincipal());

        return ResponseEntity.ok(new TokenDTO(usuario.getId(), usuario.getNome(), usuario.getEmail(), jwtToken, usuario.getRole()));
    }

    @GetMapping("/validarToken")
    public ResponseEntity<Object> validarToken(@RequestHeader(name = "Authorization", defaultValue = "") String auth) throws Exception {

        jwtService.tokenValido(auth);
        
        return ResponseEntity.ok().build();
    }
}