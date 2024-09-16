package com.api_controle_acesso.websocket;
import org.springframework.web.socket.server.HandshakeInterceptor;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.socket.WebSocketHandler;
import java.util.Map;

public class JwtHandshakeInterceptor implements HandshakeInterceptor {

    @Override
    public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response, 
                                   WebSocketHandler wsHandler, Map<String, Object> attributes) throws Exception {
        // Extrair e validar o token JWT aqui
        String token = request.getHeaders().getFirst("Authorization");
        if (token != null && token.startsWith("Bearer ")) {
            token = token.substring(7); // Remove "Bearer " prefix
            // Validar o token JWT (aqui você deve implementar sua lógica de validação de token)
            if (validateToken(token)) {
                return true;
            }
        }
        response.setStatusCode(HttpStatus.FORBIDDEN);
        return false;
    }

    @Override
    public void afterHandshake(ServerHttpRequest request, ServerHttpResponse response, 
                               WebSocketHandler wsHandler, Exception ex) {
        // Não necessário para esse exemplo
    }

    private boolean validateToken(String token) {
        // Implementar a lógica para validar o token JWT
        return true; // Substitua com a validação real
    }
}