package com.api_controle_acesso.exceptions;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import jakarta.persistence.EntityNotFoundException;

@RestControllerAdvice
public class TratadorExceptions {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Object> tratadorException400(MethodArgumentNotValidException e) {
        var errors = e.getFieldErrors();
        return ResponseEntity.badRequest().body(errors.stream().map(ErroValidacaoDados::new).toList());
    }

    @ExceptionHandler(ValidacaoException.class)
    public ResponseEntity<Object> tratadorExceptionRegraNegocio(ValidacaoException e) {
        return ResponseEntity.badRequest().body("{\"error\": \"" + e.getMessage() + "\"}");
    }

    @ExceptionHandler(LoginException.class)
    public ResponseEntity<Object> tratadorExceptionLogin(ValidacaoException e) {
        return ResponseEntity.status(401).body("{\"error\": \"" + e.getMessage() + "\"}");
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<Object> tratadorException404(EntityNotFoundException e) {
        return ResponseEntity.notFound().build();
    }

    @ExceptionHandler(AuthenticationException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ResponseEntity<Object> tratadorException401(AuthenticationException e) {
        return ResponseEntity.status(401).body("{\"error\": \"Falha na Autenticação\", \"status\": \"" + HttpStatus.UNAUTHORIZED + "\"}");
    }

    @ExceptionHandler(AccessDeniedException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ResponseEntity<Object> tratadorException403(AccessDeniedException e) {
        return ResponseEntity.status(403).body("{\"error\": \"Acesso Negado\", \"status\": \"" + HttpStatus.FORBIDDEN + "\"}");
    }

    private record ErroValidacaoDados(String field, String message) {
        public ErroValidacaoDados(FieldError error) {
            this(error.getField(), error.getDefaultMessage());
        }
    }
    
}
