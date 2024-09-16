package com.api_controle_acesso.websocket;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

@Configuration
@EnableWebSocket
public class WebSocketConfig implements WebSocketConfigurer  {

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(myWebSocketHandler(), "/ws/my-endpoint")
                .setAllowedOrigins("*")
                .addInterceptors(new JwtHandshakeInterceptor());
    }

    public FilaWebSocketHandler myWebSocketHandler() {
        return new FilaWebSocketHandler();
    }
    
}
